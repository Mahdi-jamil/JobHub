@echo off
set project=D:\JobHub\src\main\resources\com\DataBase
set conn=-u root -proot -h 127.0.0.1 -P 3306

cls

mysql %conn% < "%project%\SQL\1.Create-jobhub-Tables.sql" > "%project%\LOG\Create-tables-FK-indexes.log" 2>&1
mysql %conn% < "%project%\SQL\5.Add-Foreign-Keys.sql" > "%project%\LOG\Create-tables-FK-indexes.log" 2>&1
mysql %conn% < "%project%\SQL\2.Create-jobhub-indexes.sql" > "%project%\LOG\Create-tables-FK-indexes.log" 2>&1


if %errorlevel% neq 0 (
    echo Error creating database! Check the log for details.
) else (
    echo Database created successfully.
)

