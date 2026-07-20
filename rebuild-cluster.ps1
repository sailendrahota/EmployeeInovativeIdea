Write-Host "1/10: Starting Minikube Cluster Rebuild Process..." -ForegroundColor Cyan

Write-Host "2/10: Deleting old cluster state..." -ForegroundColor Yellow
minikube delete

Write-Host "3/10: Starting fresh Minikube cluster (4 CPUs, 6.5GB RAM) with Calico..." -ForegroundColor Green
minikube start --cpus=4 --memory=6692 --cni=calico

Write-Host "4/10: Restoring Sealed Secrets Master Key..." -ForegroundColor Green
# CRITICAL: Apply the backup before installing the Helm chart!
kubectl apply -f master-key-backup.yaml

Write-Host "5/10: Enabling required addons..." -ForegroundColor Yellow
minikube addons enable ingress
minikube addons enable metrics-server

Write-Host "6/10: Configuring Helm Repositories..." -ForegroundColor Cyan
helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
helm repo add grafana https://grafana.github.io/helm-charts
helm repo add bitnami https://charts.bitnami.com/bitnami
helm repo add jetstack https://charts.jetstack.io
helm repo update

Write-Host "7/10: Installing Platform Foundations..." -ForegroundColor Magenta
helm upgrade --install prometheus prometheus-community/kube-prometheus-stack -n monitoring --create-namespace
helm upgrade --install loki grafana/loki-stack -n monitoring --create-namespace --set grafana.datasource.isDefault=false
helm upgrade --install sealed-secrets bitnami/sealed-secrets -n kube-system --create-namespace
kubectl apply -f kafka-dev.yaml     # helm upgrade --install kafka bitnami/kafka --namespace event-streaming --create-namespace -f k8s/kafka-values.yml
helm upgrade --install cert-manager jetstack/cert-manager -n cert-manager --create-namespace --set crds.enabled=true

Write-Host "8/10: Installing ArgoCD..." -ForegroundColor Cyan
kubectl create namespace argocd --dry-run=client -o yaml | kubectl apply -f -
kubectl apply -n argocd --server-side=true -f https://raw.githubusercontent.com/argoproj/argo-cd/stable/manifests/install.yaml

Write-Host "9/10: Waiting 45 seconds for Webhooks & CRDs to initialize..." -ForegroundColor Yellow
Start-Sleep -Seconds 45

Write-Host "10/10: Applying Network & GitOps Policies..." -ForegroundColor Green
terraform -chdir=ops/k8-security/ init
terraform -chdir=ops/k8-security/ apply -auto-approve
kubectl apply -f argocd-app.yaml

Write-Host "=======================================================" -ForegroundColor Cyan
Write-Host "             CLUSTER REBUILD COMPLETE!                 " -ForegroundColor Green
Write-Host "=======================================================" -ForegroundColor Cyan

$ArgoPassword = kubectl -n argocd get secret argocd-initial-admin-secret -o go-template="{{.data.password | base64decode}}"
Write-Host "Your ArgoCD Password is: " -NoNewline
Write-Host $ArgoPassword -ForegroundColor Green

Write-Host "`nIMPORTANT NEXT STEP" -ForegroundColor Red
Write-Host "Because the cluster is brand new, the Sealed Secrets master key has changed."
Write-Host "You MUST re-encrypt your raw-secret.yaml file to fix the MySQL Pods. Run:" -ForegroundColor Yellow
Write-Host "  1. kubeseal --fetch-cert --controller-name sealed-secrets --controller-namespace kube-system > my-new-cert.pem"
Write-Host "  2. kubeseal --cert my-new-cert.pem --format yaml < raw-secret.yaml > k8s/secret.yaml"
Write-Host "  3. git add k8s/secret.yaml && git commit -m `"Rotate sealed secrets`" && git push"

Write-Host "`nFinal Routing Steps:" -ForegroundColor Cyan
Write-Host "  1. Open a NEW PowerShell window and run: minikube tunnel"
Write-Host "  2. Access your Database GUI at: https://db.hr-portal.local"
Write-Host "  3. Access ArgoCD at: https://argocd.hr-portal.local"