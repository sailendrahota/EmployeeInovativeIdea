Write-Host "Starting Minikube Cluster Rebuild Process..." -ForegroundColor Cyan

Write-Host "Deleting old cluster state..."
minikube delete

Write-Host "Starting fresh Minikube cluster (4 CPUs, 6.5GB RAM) with Calico..."
minikube start --cpus=4 --memory=6692 --cni=calico

Write-Host "Enabling required addons (Ingress & Metrics Server)..."
minikube addons enable ingress
minikube addons enable metrics-server

Write-Host "Installing ArgoCD..."
kubectl create namespace argocd
kubectl apply -n argocd --server-side=true -f https://raw.githubusercontent.com/argoproj/argo-cd/stable/manifests/install.yaml

Write-Host "Configuring Helm Repositories..."
# Group ALL repos together to optimize network calls
helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
helm repo add grafana https://grafana.github.io/helm-charts
helm repo add bitnami https://charts.bitnami.com/bitnami
helm repo update

Write-Host "Installing Monitoring Stack (Prometheus, Grafana, Loki)..."
helm install prometheus prometheus-community/kube-prometheus-stack -n monitoring --create-namespace
helm install loki grafana/loki-stack -n monitoring

Write-Host "Installing Bitnami Sealed Secrets..."
helm install sealed-secrets bitnami/sealed-secrets -n kube-system

Write-Host "Waiting 30 seconds for all CRDs and Webhooks to stabilize..." -ForegroundColor Yellow
Start-Sleep -Seconds 30

Write-Host "Applying Network Policies via Terraform..."
# Tells Terraform to execute inside the ops/k8-security directory
terraform -chdir=ops/k8-security/ init
terraform -chdir=ops/k8-security/ apply -auto-approve

Write-Host "Applying local Kubernetes manifests from k8s/ folder..."
kubectl apply -f k8s/

Write-Host "=======================================================" -ForegroundColor Cyan
Write-Host "              CLUSTER REBUILD COMPLETE!                " -ForegroundColor Green
Write-Host "=======================================================" -ForegroundColor Cyan
Write-Host "To get your ArgoCD password, run this command:" -ForegroundColor Yellow
Write-Host "kubectl -n argocd get secret argocd-initial-admin-secret -o jsonpath='{.data.password}' | % { [System.Text.Encoding]::UTF8.GetString([System.Convert]::FromBase64String($_)) }`n"

Write-Host "IMPORTANT NEXT STEP:" -ForegroundColor Red
Write-Host "Because the cluster is brand new, the Sealed Secrets master key has changed."
Write-Host "You MUST re-encrypt your raw-secret.yaml file to fix the MySQL Pods. Run:" -ForegroundColor Yellow
Write-Host "1. kubeseal --fetch-cert --controller-name sealed-secrets --controller-namespace kube-system > my-new-cert.pem"
Write-Host "2. kubeseal --cert my-new-cert.pem --format yaml < raw-secret.yaml > k8s/secret.yaml"
Write-Host "3. kubectl apply -f k8s/secret.yaml"