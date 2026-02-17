# validate_version.ps1

# 1. Read ServerVersion from manifest.json
$manifestPath = "src/main/resources/manifest.json"
if (-not (Test-Path $manifestPath)) {
    Write-Host "Erreur: Le fichier manifest.json n'a pas été trouvé à l'emplacement '$manifestPath'"
    exit 1
}
$manifest = Get-Content $manifestPath | ConvertFrom-Json
$serverVersionConstraint = $manifest.ServerVersion

# 2. Get available versions and latest version from Hytale Maven
$metadataUrl = "https://maven.hytale.com/release/com/hypixel/hytale/Server/maven-metadata.xml"
try {
    $metadataContent = Invoke-WebRequest -Uri $metadataUrl -UseBasicParsing -TimeoutSec 10
    $metadata = [xml]$metadataContent.Content
} catch {
    Write-Host "Erreur: Impossible de récupérer les versions du serveur Hytale depuis $metadataUrl"
    Write-Host $_.Exception.Message
    exit 1
}
$latestVersion = $metadata.metadata.versioning.latest
$availableVersions = $metadata.metadata.versioning.versions.version

# 3. Perform validation based on constraint
if ($serverVersionConstraint.StartsWith(">=")) {
    $requiredVersion = $serverVersionConstraint -replace '>=', ''
    Write-Host "Contrainte de version : '>=' $requiredVersion"
    Write-Host "Dernière version disponible : $latestVersion"

    # Direct string comparison works for YYYY.MM.DD-hash format
    if ($latestVersion -ge $requiredVersion) {
        Write-Host "Validation réussie: La dernière version '$latestVersion' est supérieure ou égale à '$requiredVersion'."
        exit 0
    } else {
        Write-Host "Erreur de validation: La dernière version '$latestVersion' n'est pas supérieure ou égale à '$requiredVersion'."
        exit 1
    }
} else {
    # Simple existence check for exact version match
    $requiredVersion = $serverVersionConstraint -replace '[<>= ]', ''
    Write-Host "Contrainte de version : '$requiredVersion' (correspondance exacte)"
    if ($availableVersions -contains $requiredVersion) {
        Write-Host "Validation réussie: La version du serveur '$requiredVersion' existe."
        exit 0
    } else {
        Write-Host "Erreur de validation: La version du serveur '$requiredVersion' n'a pas été trouvée."
        exit 1
    }
}
