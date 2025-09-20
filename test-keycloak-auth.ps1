# Keycloak Authentication Test Script
# This script tests the authentication and authorization flow

Write-Host "🔐 Testing Keycloak Authentication..." -ForegroundColor Green

# Test 1: Public endpoint (should work without authentication)
Write-Host "`n1. Testing public endpoint..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/auth-test/public" -Method Get
    Write-Host "✅ Public endpoint works: $($response.message)" -ForegroundColor Green
} catch {
    Write-Host "❌ Public endpoint failed: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 2: Get access token for admin user
Write-Host "`n2. Getting access token for admin user..." -ForegroundColor Yellow
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
    Write-Host "✅ Admin token obtained successfully" -ForegroundColor Green
} catch {
    Write-Host "❌ Failed to get admin token: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

# Test 3: Test authenticated endpoint with admin token
Write-Host "`n3. Testing authenticated endpoint with admin token..." -ForegroundColor Yellow
try {
    $headers = @{
        "Authorization" = "Bearer $accessToken"
        "Content-Type" = "application/json"
    }
    
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/auth-test/authenticated" -Method Get -Headers $headers
    Write-Host "✅ Authenticated endpoint works" -ForegroundColor Green
    Write-Host "   User: $($response.user)" -ForegroundColor Cyan
    Write-Host "   Authorities: $($response.authorities -join ', ')" -ForegroundColor Cyan
} catch {
    Write-Host "❌ Authenticated endpoint failed: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 4: Test admin-only endpoint
Write-Host "`n4. Testing admin-only endpoint..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/auth-test/admin-only" -Method Get -Headers $headers
    Write-Host "✅ Admin-only endpoint works" -ForegroundColor Green
    Write-Host "   Message: $($response.message)" -ForegroundColor Cyan
} catch {
    Write-Host "❌ Admin-only endpoint failed: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 5: Test manager-only endpoint (admin should have access)
Write-Host "`n5. Testing manager-only endpoint with admin..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/auth-test/manager-only" -Method Get -Headers $headers
    Write-Host "✅ Manager-only endpoint works for admin" -ForegroundColor Green
    Write-Host "   Message: $($response.message)" -ForegroundColor Cyan
} catch {
    Write-Host "❌ Manager-only endpoint failed: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 6: Get access token for regular user
Write-Host "`n6. Getting access token for regular user..." -ForegroundColor Yellow
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
    Write-Host "✅ User token obtained successfully" -ForegroundColor Green
} catch {
    Write-Host "❌ Failed to get user token: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

# Test 7: Test admin-only endpoint with regular user (should fail)
Write-Host "`n7. Testing admin-only endpoint with regular user (should fail)..." -ForegroundColor Yellow
try {
    $userHeaders = @{
        "Authorization" = "Bearer $userToken"
        "Content-Type" = "application/json"
    }
    
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/auth-test/admin-only" -Method Get -Headers $userHeaders
    Write-Host "❌ Admin-only endpoint should have failed for regular user!" -ForegroundColor Red
} catch {
    Write-Host "✅ Admin-only endpoint correctly rejected regular user" -ForegroundColor Green
}

# Test 8: Test cashier-only endpoint with regular user (should fail)
Write-Host "`n8. Testing cashier-only endpoint with regular user (should fail)..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/auth-test/cashier-only" -Method Get -Headers $userHeaders
    Write-Host "❌ Cashier-only endpoint should have failed for regular user!" -ForegroundColor Red
} catch {
    Write-Host "✅ Cashier-only endpoint correctly rejected regular user" -ForegroundColor Green
}

# Test 9: Test authenticated endpoint with regular user (should work)
Write-Host "`n9. Testing authenticated endpoint with regular user..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/auth-test/authenticated" -Method Get -Headers $userHeaders
    Write-Host "✅ Authenticated endpoint works for regular user" -ForegroundColor Green
    Write-Host "   User: $($response.user)" -ForegroundColor Cyan
    Write-Host "   Authorities: $($response.authorities -join ', ')" -ForegroundColor Cyan
} catch {
    Write-Host "❌ Authenticated endpoint failed for regular user: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`nKeycloak authentication testing completed!" -ForegroundColor Green
