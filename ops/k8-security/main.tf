terraform {
  required_providers {
    kubernetes = {
      source  = "hashicorp/kubernetes"
      version = "~> 2.0"
    }
  }
}

provider "kubernetes" {
  config_path    = "~/.kube/config"
  config_context = "minikube"
}

# ---------------------------------------------------------
# NETWORK POLICY: Zero-Trust Backend Lock-down
# ---------------------------------------------------------
resource "kubernetes_network_policy" "backend_lockdown" {
  metadata {
    name      = "backend-zero-trust-policy"
    namespace = "default"
  }

  spec {
    # 1. We are locking down the Spring Boot Backend
    pod_selector {
      match_labels = {
        app = "spring-boot"
      }
    }

    policy_types = ["Ingress"]

    ingress {
      # ALLOW RULE 1: The Frontend Application
      from {
        pod_selector {
          match_labels = {
            app = "zele-frontend"
          }
        }
      }

      # ALLOW RULE 2: Prometheus (Scraping Metrics)
      # Allows any pod living in the "monitoring" namespace to connect
      from {
        namespace_selector {
          match_labels = {
            "kubernetes.io/metadata.name" = "monitoring"
          }
        }
      }

      # ALLOW RULE 3: Swagger / External API Traffic
      # Allows the Minikube Ingress Controller to route browser traffic in
      from {
        namespace_selector {
          match_labels = {
            "kubernetes.io/metadata.name" = "ingress-nginx"
          }
        }
      }

      # The only port all of the above are allowed to talk to
      ports {
        port     = "8080"
        protocol = "TCP"
      }
    }
  }
}
# ---------------------------------------------------------
# RBAC: Create a Read-Only Role for Junior Developers
# ---------------------------------------------------------
resource "kubernetes_role" "junior_dev_role" {
  metadata {
    name      = "junior-dev-readonly"
    namespace = "default"
  }

  # What is this role allowed to do?
  rule {
    api_groups = [""]
    resources  = ["pods", "services", "configmaps"]
    verbs      = ["get", "list", "watch"] # Notice: NO "delete" or "create" verbs!
  }
}
# ---------------------------------------------------------
# NETWORK POLICY: Zero-Trust Database Lock-down
# ---------------------------------------------------------
resource "kubernetes_network_policy" "mysql_isolation" {
  metadata {
    name      = "mysql-zero-trust-policy"
    namespace = "default"
  }

  spec {
    # 1. Target the MySQL Database
    pod_selector {
      match_labels = {
        app = "mysql"
      }
    }

    policy_types = ["Ingress"]

    ingress {
      # ALLOW RULE 1: The Spring Boot Backend
      from {
        pod_selector {
          match_labels = {
            app = "spring-boot" # Matches your backend label perfectly
          }
        }
      }

      # ALLOW RULE 2: The Adminer GUI
      from {
        pod_selector {
          match_labels = {
            app = "adminer"
          }
        }
      }

      # Lock to MySQL port only
      ports {
        port     = "3306"
        protocol = "TCP"
      }
    }
  }
}