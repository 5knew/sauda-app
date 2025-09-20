# Simple Keycloak Authentication Test
Write-Host "Testing Keycloak Authentication..." -ForegroundColor Green

# Test 1: Public endpoint
Write-Host "1. Testing public endpoint..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/auth-test/public" -Method Get
    Write-Host "SUCCESS: Public endpoint works" -ForegroundColor Green
    Write-Host "Message: $($response.message)" -ForegroundColor Cyan
} catch {
    Write-Host "ERROR: Public endpoint failed" -ForegroundColor Red
}

# Test 2: Get admin token
Write-Host "2. Getting admin token..." -ForegroundColor Yellow
try {
    $tokenBody = @{
        username = "admin"
        password = "admin123"
        grant_type = "password"
        client_id = "sauda-app"
        client_secret = "sauda-secret"
    }
    
    $tokenResponse = Invoke-RestMethod -Uri "http://localhost:8081/realms/sauda-realm/protocol/openid-connect/token" -Method Post -Body $tokenBody -ContentType "application/x-www-form-urlencoded"
    $accessToken = $tokenResponse.access_token
    Write-Host "SUCCESS: Admin token obtained" -ForegroundColor Green
} catch {
    Write-Host "ERROR: Failed to get admin token" -ForegroundColor Red
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

# Test 3: Test authenticated endpoint
Write-Host "3. Testing authenticated endpoint..." -ForegroundColor Yellow
try {
    $headers = @{
        "Authorization" = "Bearer $accessToken"
        "Content-Type" = "application/json"
    }
    
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/auth-test/authenticated" -Method Get -Headers $headers
    Write-Host "SUCCESS: Authenticated endpoint works" -ForegroundColor Green
    Write-Host "User: $($response.user)" -ForegroundColor Cyan
    Write-Host "Authorities: $($response.authorities -join ', ')" -ForegroundColor Cyan
} catch {
    Write-Host "ERROR: Authenticated endpoint failed" -ForegroundColor Red
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 4: Test admin-only endpoint
Write-Host "4. Testing admin-only endpoint..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/auth-test/admin-only" -Method Get -Headers $headers
    Write-Host "SUCCESS: Admin-only endpoint works" -ForegroundColor Green
    Write-Host "Message: $($response.message)" -ForegroundColor Cyan
} catch {
    Write-Host "ERROR: Admin-only endpoint failed" -ForegroundColor Red
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "Testing completed!" -ForegroundColor Green
