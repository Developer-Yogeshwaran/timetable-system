# Azure deployment instructions

Prerequisites
- `az` (Azure CLI) installed and accessible on PATH. Install: https://aka.ms/installazurecliwindows
- You must be logged in: `az login`
- A packaged JAR at `target/timetable-0.0.1-SNAPSHOT.jar` (run `mvnw -DskipTests package`)

Quick steps
1. Open PowerShell in the repository root (D:\timetable).
2. Run the deploy script (edit parameters if you want different names):

```powershell
cd D:\timetable
.\deploy\deploy-to-azure.ps1 -ResourceGroup "timetable-rg" -Location "eastus" -AppServicePlan "timetable-plan" -AppName "timetable-app-yogeshwaran"
```

What the script does
- Creates the resource group (if missing).
- Creates a Linux App Service plan.
- Creates a Web App configured for Java 17.
- Deploys the runnable JAR using `az webapp deploy`.

Afterwards
- Visit `https://<AppName>.azurewebsites.net`.
- To tail logs: `az webapp log tail --name <AppName> --resource-group <ResourceGroup>`

If you prefer container-based deployment (ACR + Web App for Containers) tell me and I can provide that script instead.
