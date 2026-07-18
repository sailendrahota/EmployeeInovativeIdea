$errors = $null
[System.Management.Automation.Language.Parser]::ParseFile(".\rebuild-cluster.ps1", [ref]$null, [ref]$errors)
if ($errors) { $errors } else { Write-Host "Syntax is 100% clean. No parsing errors!" -ForegroundColor Green }