@echo off
set project=D:\JobHub\src\main\resources\com\DataBase
set conn=-u root -proot -h 127.0.0.1 -P 3306

cls

mysql %conn% < "%project%\SQL\4.CheckUserExistFunction.sql" > "%project%\LOG\Create-functions-sp.log" 2>&1
mysql %conn% < "%project%\SQL\6.ChangePassword_SP.sql" > "%project%\LOG\Create-functions-sp.log" 2>&1


if %errorlevel% neq 0 (
    echo Error creating database! Check the log for details.
) else (
    echo Database created successfully.
)

