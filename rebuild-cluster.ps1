Write-Host "Starting Minikube Cluster Rebuild Process..." -ForegroundColor Cyan

Write-Host "Deleting old cluster state..."
minikube delete

Write-Host "Starting fresh Minikube cluster (4 CPUs, 8GB RAM)..."
minikube start --cpus=4 --memory=5120

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

Write-Host "Applying local Kubernetes manifests from k8s/ folder..."
kubectl apply -f k8s/

Write-Host "Cluster rebuild complete!" -ForegroundColor Green
Write-Host "To get your ArgoCD password, run this command in your terminal:" -ForegroundColor Yellow
Write-Host "kubectl -n argocd get secret argocd-initial-admin-secret"
