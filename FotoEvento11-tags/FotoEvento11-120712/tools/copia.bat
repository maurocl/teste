@echo off
rem -----------------------------------------------
rem Copia PC --> Android
rem -----------------------------------------------

echo copiando arquivos ...
adb push C:/workspace-temp/FotoEvento11/config/molduras /mnt/sdcard/Pictures/fotoevento/molduras/
adb push C:/workspace-temp/FotoEvento11/config/telainicial /mnt/sdcard/Pictures/fotoevento/telainicial
adb push C:/workspace-temp/FotoEvento11/config /mnt/sdcard/Pictures
adb push C:/workspace-temp/FotoEvento11/config/config.txt /mnt/sdcard/Pictures



