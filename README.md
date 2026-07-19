# EmployeeInovativeIdea

[![CI/CD Pipeline](https://github.com/sailendrahota/EmployeeInovativeIdea/actions/workflows/ci-cd.yaml/badge.svg)](https://github.com/sailendrahota/EmployeeInovativeIdea/actions)
[![GitOps Managed](https://img.shields.io/badge/GitOps-ArgoCD-blue?style=flat&logo=argo)](https://argoproj.github.io/argo-cd/)
[![Infrastructure](https://img.shields.io/badge/IaC-Terraform-623CE4?style=flat&logo=terraform)](https://www.terraform.io/)
[![Network Security](https://img.shields.io/badge/Security-Zero_Trust-red?style=flat&logo=kalilinux)](https://www.tigera.io/project-calico/)

*A Full-Stack Employee Registration and Tax Deduction Platform.*
*A Full-Stack Employee Registration and Tax Deduction Platform.*

This repository contains both the application source code (Spring Boot & Frontend) and the complete Infrastructure-as-Code (IaC) required to run it locally in a production-simulated Kubernetes environment.

The infrastructure is built on a modern, **Cloud-Native GitOps Architecture**, featuring zero-trust networking, automated TLS, auto-scaling, and a full observability stack.

---

## 🏗️ Architecture Stack

*   **Application:** Spring Boot (Backend), React/Angular (Frontend), MySQL (Database)
*   **Container Orchestration:** Local Kubernetes via Minikube
*   **GitOps & CI/CD:** ArgoCD (Continuous Delivery) & GitHub Actions (Continuous Integration)
*   **Networking & Security:** NGINX Ingress, Calico CNI, Terraform (Zero-Trust Network Policies)
*   **Secret Management:** Bitnami Sealed Secrets (Asymmetric Encryption)
*   **Observability:** Prometheus (Metrics), Loki (Logs), Grafana (Dashboards)

---

## 📋 Prerequisites

Before running the automated cluster rebuild, ensure the following tools are installed on your local machine and **added to your Windows system `PATH` environment variable**:

1.  **[Docker Desktop](https://www.docker.com/products/docker-desktop/)** (Must be running in the background)
2.  **[Minikube](https://minikube.sigs.k8s.io/docs/start/)** (Local Kubernetes engine)
3.  **[kubectl](https://kubernetes.io/docs/tasks/tools/)** (Kubernetes command-line tool)
4.  **[Helm](https://helm.sh/docs/intro/install/)** (Kubernetes package manager)
5.  **[Terraform](https://developer.hashicorp.com/terraform/downloads)** (Infrastructure as Code CLI)
6.  **[kubeseal](https://github.com/bitnami-labs/sealed-secrets)** (Bitnami CLI for encrypting secrets)

### Local DNS Setup
To access the local Ingress routes via your browser, map the local domains in your Windows `hosts` file (`C:\Windows\System32\drivers\etc\hosts`). Add the following line (requires Administrator privileges):
```text
127.0.0.1 db.hr-portal.local argocd.hr-portal.local
