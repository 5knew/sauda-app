# Keycloak Setup Script
Write-Host "Setting up Keycloak for Sauda-DB..." -ForegroundColor Green

# Wait for Keycloak to be ready
Write-Host "Waiting for Keycloak to be ready..." -ForegroundColor Yellow
$maxAttempts = 30
$attempt = 0

do {
    $attempt++
    try {
        $response = Invoke-WebRequest -Uri "http://localhost:8081/realms/master" -UseBasicParsing -TimeoutSec 5
        if ($response.StatusCode -eq 200) {
            Write-Host "Keycloak is ready!" -ForegroundColor Green
            break
        }
    }
    catch {
        Write-Host "Attempt $attempt/$maxAttempts - Keycloak not ready yet..." -ForegroundColor Yellow
        Start-Sleep -Seconds 10
    }
} while ($attempt -lt $maxAttempts)

if ($attempt -eq $maxAttempts) {
    Write-Host "Keycloak failed to start within expected time" -ForegroundColor Red
    exit 1
}

# Get admin token
Write-Host "Getting admin token..." -ForegroundColor Yellow
$tokenBody = "username=admin&password=admin123&grant_type=password&client_id=admin-cli"

$tokenResponse = Invoke-RestMethod -Uri "http://localhost:8081/realms/master/protocol/openid-connect/token" -Method Post -Body $tokenBody -ContentType "application/x-www-form-urlencoded"
$adminToken = $tokenResponse.access_token

Write-Host "Admin token obtained!" -ForegroundColor Green

# Create realm
Write-Host "Creating sauda-realm..." -ForegroundColor Yellow
$realmData = Get-Content "keycloak-setup.json" -Raw
$headers = @{
    "Authorization" = "Bearer $adminToken"
    "Content-Type" = "application/json"
}

try {
    Invoke-RestMethod -Uri "http://localhost:8081/admin/realms" -Method Post -Body $realmData -Headers $headers
    Write-Host "Realm created successfully!" -ForegroundColor Green
}
catch {
    Write-Host "Error creating realm: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "Keycloak setup completed!" -ForegroundColor Green
Write-Host "Admin Console: http://localhost:8081/admin" -ForegroundColor Cyan
Write-Host "Username: admin" -ForegroundColor Cyan
Write-Host "Password: admin123" -ForegroundColor Cyan
Write-Host "Realm: sauda-realm" -ForegroundColor Cyan
