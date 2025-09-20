# Test with cashier user
Write-Host "Testing with cashier user..." -ForegroundColor Green

# Get cashier token
Write-Host "1. Getting cashier token..." -ForegroundColor Yellow
try {
    $tokenBody = @{
        username = "cashier"
        password = "cashier123"
        grant_type = "password"
        client_id = "sauda-app"
        client_secret = "sauda-secret"
    }
    
    $tokenResponse = Invoke-RestMethod -Uri "http://localhost:8081/realms/sauda-realm/protocol/openid-connect/token" -Method Post -Body $tokenBody -ContentType "application/x-www-form-urlencoded"
    $cashierToken = $tokenResponse.access_token
    Write-Host "SUCCESS: Cashier token obtained" -ForegroundColor Green
} catch {
    Write-Host "ERROR: Failed to get cashier token" -ForegroundColor Red
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

# Test authenticated endpoint with cashier
Write-Host "2. Testing authenticated endpoint with cashier..." -ForegroundColor Yellow
try {
    $cashierHeaders = @{
        "Authorization" = "Bearer $cashierToken"
        "Content-Type" = "application/json"
    }
    
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/auth-test/authenticated" -Method Get -Headers $cashierHeaders
    Write-Host "SUCCESS: Authenticated endpoint works for cashier" -ForegroundColor Green
    Write-Host "User: $($response.user)" -ForegroundColor Cyan
    Write-Host "Authorities: $($response.authorities -join ', ')" -ForegroundColor Cyan
} catch {
    Write-Host "ERROR: Authenticated endpoint failed for cashier" -ForegroundColor Red
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}

# Test cashier-only endpoint with cashier (should work)
Write-Host "3. Testing cashier-only endpoint with cashier..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/auth-test/cashier-only" -Method Get -Headers $cashierHeaders
    Write-Host "SUCCESS: Cashier-only endpoint works for cashier" -ForegroundColor Green
    Write-Host "Message: $($response.message)" -ForegroundColor Cyan
} catch {
    Write-Host "ERROR: Cashier-only endpoint failed for cashier" -ForegroundColor Red
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}

# Test admin-only endpoint with cashier (should fail)
Write-Host "4. Testing admin-only endpoint with cashier (should fail)..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/auth-test/admin-only" -Method Get -Headers $cashierHeaders
    Write-Host "ERROR: Admin-only endpoint should have failed for cashier!" -ForegroundColor Red
} catch {
    Write-Host "SUCCESS: Admin-only endpoint correctly rejected cashier" -ForegroundColor Green
    Write-Host "Error (expected): $($_.Exception.Message)" -ForegroundColor Cyan
}

Write-Host "Cashier testing completed!" -ForegroundColor Green
