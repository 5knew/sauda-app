# Test with regular user
Write-Host "Testing with regular user..." -ForegroundColor Green

# Get user token
Write-Host "1. Getting user token..." -ForegroundColor Yellow
try {
    $tokenBody = @{
        username = "user"
        password = "user123"
        grant_type = "password"
        client_id = "sauda-app"
        client_secret = "sauda-secret"
    }
    
    $tokenResponse = Invoke-RestMethod -Uri "http://localhost:8081/realms/sauda-realm/protocol/openid-connect/token" -Method Post -Body $tokenBody -ContentType "application/x-www-form-urlencoded"
    $userToken = $tokenResponse.access_token
    Write-Host "SUCCESS: User token obtained" -ForegroundColor Green
} catch {
    Write-Host "ERROR: Failed to get user token" -ForegroundColor Red
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

# Test authenticated endpoint with user
Write-Host "2. Testing authenticated endpoint with user..." -ForegroundColor Yellow
try {
    $userHeaders = @{
        "Authorization" = "Bearer $userToken"
        "Content-Type" = "application/json"
    }
    
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/auth-test/authenticated" -Method Get -Headers $userHeaders
    Write-Host "SUCCESS: Authenticated endpoint works for user" -ForegroundColor Green
    Write-Host "User: $($response.user)" -ForegroundColor Cyan
    Write-Host "Authorities: $($response.authorities -join ', ')" -ForegroundColor Cyan
} catch {
    Write-Host "ERROR: Authenticated endpoint failed for user" -ForegroundColor Red
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}

# Test admin-only endpoint with user (should fail)
Write-Host "3. Testing admin-only endpoint with user (should fail)..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/auth-test/admin-only" -Method Get -Headers $userHeaders
    Write-Host "ERROR: Admin-only endpoint should have failed for user!" -ForegroundColor Red
} catch {
    Write-Host "SUCCESS: Admin-only endpoint correctly rejected user" -ForegroundColor Green
    Write-Host "Error (expected): $($_.Exception.Message)" -ForegroundColor Cyan
}

# Test cashier-only endpoint with user (should fail)
Write-Host "4. Testing cashier-only endpoint with user (should fail)..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/auth-test/cashier-only" -Method Get -Headers $userHeaders
    Write-Host "ERROR: Cashier-only endpoint should have failed for user!" -ForegroundColor Red
} catch {
    Write-Host "SUCCESS: Cashier-only endpoint correctly rejected user" -ForegroundColor Green
    Write-Host "Error (expected): $($_.Exception.Message)" -ForegroundColor Cyan
}

Write-Host "User testing completed!" -ForegroundColor Green
