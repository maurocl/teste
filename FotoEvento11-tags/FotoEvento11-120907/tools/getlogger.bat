@echo off
rem ------------------------------------------------------
rem   getlogger.bat
rem
rem   Copia o arquivo de log de execu��o do Android --> PC
rem ------------------------------------------------------

adb pull /mnt/sdcard/Pictures/logger2.log .

type logger2.log
