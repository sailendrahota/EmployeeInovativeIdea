# 1. The Provider
provider "aws" {
  access_key                  = "test"
  secret_key                  = "test"
  region                      = "us-east-1"

  skip_credentials_validation = true
  skip_requesting_account_id  = true
  skip_metadata_api_check     = true

  # THE FIX: Tell AWS not to use subdomains on localhost!
  s3_use_path_style           = true

  endpoints {
    s3  = "http://localhost:4566"
    ssm = "http://localhost:4566"
  }
}

# 2. An S3 Bucket (Where your Spring Boot app would save user uploads/images)
resource "aws_s3_bucket" "my_app_bucket" {
  bucket = "my-spring-boot-assets-bucket"
}

# 3. A Secure Parameter (How DevOps securely passes DB passwords to Java)
resource "aws_ssm_parameter" "db_password" {
  name  = "/myapp/database/password"
  type  = "SecureString"
  value = "SuperSecretPassword123!"
}