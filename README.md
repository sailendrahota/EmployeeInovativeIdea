# EmployeeInovativeIdea

[![CI/CD Pipeline](https://github.com/sailendrahota/EmployeeInovativeIdea/actions/workflows/ci-cd.yaml/badge.svg)](https://github.com/sailendrahota/EmployeeInovativeIdea/actions)
[![GitOps Managed](https://img.shields.io/badge/GitOps-ArgoCD-blue?style=flat&logo=argo)](https://argoproj.github.io/argo-cd/)
[![Infrastructure](https://img.shields.io/badge/IaC-Terraform-623CE4?style=flat&logo=terraform)](https://www.terraform.io/)
[![Event Streaming](https://img.shields.io/badge/Streaming-Kafka-black?style=flat&logo=apachekafka)](https://kafka.apache.org/)
[![Network Security](https://img.shields.io/badge/Security-Zero_Trust-red?style=flat&logo=kalilinux)](https://www.tigera.io/project-calico/)

*A Full-Stack, Event-Driven Employee Registration/Order Management and Tax Deduction Platform.*

This repository contains both the application source code (Spring Boot Microservices & (react.js)Frontend) and the complete Infrastructure-as-Code (IaC) required to run it locally in a production-simulated Kubernetes environment.

The infrastructure is built on a modern, **Cloud-Native GitOps Architecture**, featuring an asynchronous event-driven message pipeline, zero-trust networking, automated TLS certificate provisioning, and a full observability stack.

---

## 🏗️ Architecture Stack

*   **Application Services:** Spring Boot (Modular Microservices), Apache Camel (Integration Routes), React/Angular (Frontend)
*   **Data & Event Streaming:** MySQL (Database), Apache Kafka (Message Broker for decoupled processing)
*   **Container Orchestration:** Local Kubernetes via Minikube
*   **GitOps & CI/CD:** Argo CD (Continuous Delivery) & GitHub Actions (Continuous Integration)
*   **Networking & Security:** NGINX Ingress, Jetstack `cert-manager` (Automated TLS/HTTPS), Calico CNI, Terraform (Zero-Trust Network Policies)
*   **Secret Management:** Bitnami Sealed Secrets (Asymmetric Encryption), Kubernetes ConfigMaps & Secrets
*   **Observability & Testing:** Prometheus (Metrics), Loki (Logs), Grafana (Dashboards), k6 (Load Testing)

---

## ✨ Key Features

*   **Event-Driven Architecture:** Decoupled synchronous web requests from backend database persistence using Kafka and Apache Camel routes. Benchmarked to safely ingest and process **12,000+ requests under 235ms p(95) latency** with zero data loss.
*   **Automated TLS / HTTPS:** Integrated Jetstack `cert-manager` to automatically provision, manage, and inject TLS certificates into the NGINX Ingress controller for secure end-to-end encryption.
*   **Microservices Extraction:** Implemented the Strangler Fig pattern to decouple high-throughput REST APIs from backend background consumers, allowing independent pod scaling.
*   **GitOps & IaC:** Entire cluster state is declaratively managed by Argo CD and provisioned via Terraform, ensuring zero-configuration drifts and automated disaster recovery.

---

## 📋 Prerequisites

Before running the automated cluster rebuild, ensure the following tools are installed on your local machine and **added to your Windows system `PATH` environment variable**:

1.  **[Docker Desktop](https://www.docker.com/products/docker-desktop/)** (Must be running in the background)
2.  **[Minikube](https://minikube.sigs.k8s.io/docs/start/)** (Local Kubernetes engine)
3.  **[kubectl](https://kubernetes.io/docs/tasks/tools/)** (Kubernetes command-line tool)
4.  **[Helm](https://helm.sh/docs/intro/install/)** (Kubernetes package manager)
5.  **[Terraform](https://developer.hashicorp.com/terraform/downloads)** (Infrastructure as Code CLI)
6.  **[kubeseal](https://github.com/bitnami-labs/sealed-secrets)** (Bitnami CLI for encrypting secrets)
7.  **[k6](https://k6.io/docs/get-started/installation/)** (Performance and load testing CLI)

### Local DNS Setup
To access the local Ingress routes via your browser over secure HTTPS, map the local domains in your Windows `hosts` file (`C:\Windows\System32\drivers\etc\hosts`). Add the following line (requires Administrator privileges):
```text
127.0.0.1 db.hr-portal.local argocd.hr-portal.local api.hr-portal.local grafana.hr-portal.local