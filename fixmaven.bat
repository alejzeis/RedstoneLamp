:: This simple batch file is to fix eclipse dependency errors, it downloads
:: The maven dependencies and adds them to the classpath for eclipse

mvn eclipse:eclipse
echo "Finished refresh of depencies!"
pause
