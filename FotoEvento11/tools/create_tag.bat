@echo off
@rem
@rem create_tag.bat
@rem
rem copy -rHEAD https://svn2.sliksvn.com/maurolopes_maurocl/FotoEvento11 https://svn2.sliksvn.com/maurolopes_maurocl/FotoEvento11-tags/FotoEvento11-120907


set HOSTNAME=https://svn2.sliksvn.com
set ACCOUNT=maurolopes_maurocl
set DIR=

copy -rHEAD %HOSTNAME%/%ACCOUNT%/%DIR% %HOSTNAME%/%ACCOUNT%/FotoEvento11-tags/FotoEvento11-120907