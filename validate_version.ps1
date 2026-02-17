# validate_version.ps1

# 1. Read ServerVersion from manifest.json
$manifestPath = "src/main/resources/manifest.json"
if (-not (Test-Path $manifestPath)) {
    Write-Host "Erreur: Le fichier manifest.json n'a pas été trouvé à l'emplacement '$manifestPath'"
    exit 1
}
$manifest = Get-Content $manifestPath | ConvertFrom-Json
$serverVersionConstraint = $manifest.ServerVersion

# Extract the version number (remove >=, >, <, <=, =)
$requiredVersion = $serverVersionConstraint -replace '[<>= ]', ''

Write-Host "Version requise par le manifest : $requiredVersion"

# 2. Get available versions from Hytale Maven
$metadataUrl = "https://maven.hytale.com/release/com/hypixel/hytale/Server/maven-metadata.xml"
try {
    $metadataContent = Invoke-WebRequest -Uri $metadataUrl -UseBasicParsing -TimeoutSec 10
    $metadata = [xml]$metadataContent.Content
} catch {
    Write-Host "Erreur: Impossible de récupérer les versions du serveur Hytale depuis $metadataUrl"
    Write-Host $_.Exception.Message
    exit 1
}

$availableVersions = $metadata.metadata.versioning.versions.version

# 3. Check if the required version exists
if ($availableVersions -contains $requiredVersion) {
    Write-Host "Validation réussie: La version du serveur '$requiredVersion' existe."
    exit 0
} else {
    Write-Host "Erreur de validation: La version du serveur '$requiredVersion' spécifiée dans manifest.json n'a pas été trouvée dans la liste des versions disponibles."
    # Write-Host "Versions disponibles: $($availableVersions -join ', ')" # Uncomment for debugging
    exit 1
}
