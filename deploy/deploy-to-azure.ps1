param(
    [string]$ResourceGroup = "timetable-rg",
    [string]$Location = "eastus",
    [string]$AppServicePlan = "timetable-plan",
    [string]$AppName = "timetable-app-yogeshwaran",
    [string]$Sku = "B1",
    [string]$JarPath = "target\timetable-0.0.1-SNAPSHOT.jar"
)

Write-Host "Starting Azure deployment script..."

if (-not (Get-Command az -ErrorAction SilentlyContinue)) {
    Write-Error "Azure CLI (az) is not installed or not on PATH. Install it first: https://aka.ms/installazurecliwindows"
    exit 1
}

# Ensure login
try {
    $account = az account show --output json | ConvertFrom-Json
} catch {
    Write-Host "You are not logged in. Opening browser to log in..."
    az login | Out-Null
    $account = az account show --output json | ConvertFrom-Json
}

Write-Host "Using subscription:`t$($account.name) ($($account.id))"

Write-Host "Creating resource group '$ResourceGroup' in '$Location'..."
az group create --name $ResourceGroup --location $Location --output none

Write-Host "Creating App Service plan '$AppServicePlan' (Linux, SKU $Sku)..."
az appservice plan create --name $AppServicePlan --resource-group $ResourceGroup --is-linux --sku $Sku --output none

Write-Host "Creating Web App '$AppName' with Java 17 runtime..."
az webapp create --resource-group $ResourceGroup --plan $AppServicePlan --name $AppName --runtime "JAVA|17-java17" --output none

if (-not (Test-Path $JarPath)) {
    Write-Error "Jar not found at '$JarPath'. Make sure you ran 'mvnw -DskipTests package' in the repository root."
    exit 1
}

Write-Host "Deploying '$JarPath' to Web App '$AppName'..."
az webapp deploy --resource-group $ResourceGroup --name $AppName --src-path $JarPath --type jar --output none

$url = "https://$AppName.azurewebsites.net"
Write-Host "Deployment complete. App should be reachable at: $url"
Write-Host "You can view logs with: az webapp log tail --name $AppName --resource-group $ResourceGroup"
