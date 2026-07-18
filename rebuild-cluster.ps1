Write-Host "Starting Minikube Cluster Rebuild Process..." -ForegroundColor Cyan

Write-Host "Deleting old cluster state..."
minikube delete

Write-Host "Starting fresh Minikube cluster (4 CPUs, 8GB RAM) with Calico..."
minikube start --cpus=4 --memory=6692 --cni=calico

Write-Host "Enabling required addons (Ingress & Metrics Server)..."
minikube addons enable ingress
minikube addons enable metrics-server

Write-Host "Installing ArgoCD..."
kubectl create namespace argocd
kubectl apply -n argocd --server-side=true -f https://raw.githubusercontent.com/argoproj/argo-cd/stable/manifests/install.yaml

Write-Host "Installing Prometheus & Grafana..."
helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
helm repo update
helm install prometheus prometheus-community/kube-prometheus-stack -n monitoring --create-namespace

Write-Host "Installing Bitnami Sealed Secrets..."
helm repo add bitnami https://charts.bitnami.com/bitnami
helm install sealed-secrets bitnami/sealed-secrets -n kube-system

Write-Host "Waiting 30 seconds for all CRDs and Webhooks to stabilize..." -ForegroundColor Yellow
Start-Sleep -Seconds 30

Write-Host "Applying Network Policies via Terraform..."
# Tells Terraform to execute inside the ops/k8-security directory
terraform -chdir=ops/k8-security/ init
terraform -chdir=ops/k8-security/ apply -auto-approve

Write-Host "Applying local Kubernetes manifests from k8s/ folder..."
kubectl apply -f k8s/

Write-Host "Cluster rebuild complete!" -ForegroundColor Green
Write-Host "To get your ArgoCD password, run this command in your terminal:" -ForegroundColor Yellow
Write-Host "kubectl -n argocd get secret argocd-initial-admin-secret -o jsonpath='{.data.password}' | % { [System.Text.Encoding]::UTF8.GetString([System.Convert]::FromBase64String($_)) }"