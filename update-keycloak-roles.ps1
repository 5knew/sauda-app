# Update Keycloak Roles Script
Write-Host "Updating Keycloak roles for Sauda-DB..." -ForegroundColor Green

# Get admin token
Write-Host "Getting admin token..." -ForegroundColor Yellow
$tokenBody = "username=admin&password=admin123&grant_type=password&client_id=admin-cli"
$tokenResponse = Invoke-RestMethod -Uri "http://localhost:8081/realms/master/protocol/openid-connect/token" -Method Post -Body $tokenBody -ContentType "application/x-www-form-urlencoded"
$adminToken = $tokenResponse.access_token

Write-Host "Admin token obtained!" -ForegroundColor Green

# Add new roles to existing realm
$headers = @{
    "Authorization" = "Bearer $adminToken"
    "Content-Type" = "application/json"
}

# Define new roles
$newRoles = @(
    @{ name = "CASHIER"; description = "Cashier role - can process sales and view inventory" },
    @{ name = "INVENTORY_MANAGER"; description = "Inventory Manager role - can manage stock and suppliers" },
    @{ name = "SALES_MANAGER"; description = "Sales Manager role - can view sales reports and manage customers" }
)

foreach ($role in $newRoles) {
    try {
        $roleJson = $role | ConvertTo-Json
        Invoke-RestMethod -Uri "http://localhost:8081/admin/realms/sauda-realm/roles" -Method Post -Body $roleJson -Headers $headers
        Write-Host "Role $($role.name) added successfully!" -ForegroundColor Green
    }
    catch {
        Write-Host "Role $($role.name) might already exist or error: $($_.Exception.Message)" -ForegroundColor Yellow
    }
}

# Add new users
$newUsers = @(
    @{
        username = "manager"
        email = "manager@sauda.com"
        firstName = "Store"
        lastName = "Manager"
        password = "manager123"
        roles = @("MANAGER", "USER")
    },
    @{
        username = "cashier"
        email = "cashier@sauda.com"
        firstName = "POS"
        lastName = "Cashier"
        password = "cashier123"
        roles = @("CASHIER", "USER")
    },
    @{
        username = "inventory"
        email = "inventory@sauda.com"
        firstName = "Inventory"
        lastName = "Manager"
        password = "inventory123"
        roles = @("INVENTORY_MANAGER", "USER")
    },
    @{
        username = "sales"
        email = "sales@sauda.com"
        firstName = "Sales"
        lastName = "Manager"
        password = "sales123"
        roles = @("SALES_MANAGER", "USER")
    }
)

foreach ($user in $newUsers) {
    try {
        $userData = @{
            username = $user.username
            email = $user.email
            firstName = $user.firstName
            lastName = $user.lastName
            enabled = $true
            credentials = @(
                @{
                    type = "password"
                    value = $user.password
                    temporary = $false
                }
            )
        } | ConvertTo-Json -Depth 3

        # Create user
        Invoke-RestMethod -Uri "http://localhost:8081/admin/realms/sauda-realm/users" -Method Post -Body $userData -Headers $headers
        Write-Host "User $($user.username) created successfully!" -ForegroundColor Green
    }
    catch {
        Write-Host "User $($user.username) might already exist or error: $($_.Exception.Message)" -ForegroundColor Yellow
    }
}

Write-Host "Keycloak roles update completed!" -ForegroundColor Green
Write-Host "Test users:" -ForegroundColor Cyan
Write-Host "- manager/manager123 (MANAGER role)" -ForegroundColor Cyan
Write-Host "- cashier/cashier123 (CASHIER role)" -ForegroundColor Cyan
Write-Host "- inventory/inventory123 (INVENTORY_MANAGER role)" -ForegroundColor Cyan
Write-Host "- sales/sales123 (SALES_MANAGER role)" -ForegroundColor Cyan
