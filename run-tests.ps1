$ErrorActionPreference = "Stop"

$classesDir = "out\test-classes"

New-Item -ItemType Directory -Force $classesDir | Out-Null

$sourceFiles = @()
$sourceFiles += Get-ChildItem -Recurse -Filter *.java src | Select-Object -ExpandProperty FullName
$sourceFiles += Get-ChildItem -Recurse -Filter *.java test | Select-Object -ExpandProperty FullName

javac -Xlint:all -encoding UTF-8 -d $classesDir $sourceFiles
java -cp $classesDir tests.TestRunner
