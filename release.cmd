@echo off

cd target

set /p version="Enter version number: "

call "C:\Program Files\7-Zip\7z.exe" a clusterbrake.tar lib clusterbrake.properties clusterbrake*.jar
call "C:\Program Files\7-Zip\7z.exe" a clusterbrake-%version%-release.tar.gz clusterbrake.tar
