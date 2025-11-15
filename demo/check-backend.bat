@echo off
echo ========================================
echo 后端启动诊断工具
echo ========================================
echo.

echo [1/5] 检查 Java 版本...
java -version
if %errorlevel% neq 0 (
    echo ❌ Java 未安装或未配置环境变量
    pause
    exit /b 1
)
echo ✅ Java 已安装
echo.

echo [2/5] 检查 Maven...
call mvnw.cmd --version
if %errorlevel% neq 0 (
    echo ❌ Maven wrapper 有问题
    pause
    exit /b 1
)
echo ✅ Maven 正常
echo.

echo [3/5] 检查 MySQL 连接...
mysql -u root -p123456 -e "SELECT 1" 2>nul
if %errorlevel% neq 0 (
    echo ⚠️ 无法连接 MySQL，请确保:
    echo    1. MySQL 服务已启动
    echo    2. 用户名密码正确
    echo    3. 数据库 programe1db 已创建
) else (
    echo ✅ MySQL 连接正常
)
echo.

echo [4/5] 检查数据库...
mysql -u root -p123456 -e "USE programe1db; SELECT 1;" 2>nul
if %errorlevel% neq 0 (
    echo ⚠️ 数据库 programe1db 不存在
    echo 正在创建数据库...
    mysql -u root -p123456 -e "CREATE DATABASE IF NOT EXISTS programe1db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
    if %errorlevel% equ 0 (
        echo ✅ 数据库创建成功
    )
) else (
    echo ✅ 数据库存在
)
echo.

echo [5/5] 编译项目...
call mvnw.cmd clean compile
if %errorlevel% neq 0 (
    echo ❌ 项目编译失败
    pause
    exit /b 1
)
echo ✅ 项目编译成功
echo.

echo ========================================
echo 诊断完成! 现在可以启动后端:
echo    mvnw.cmd spring-boot:run
echo ========================================
pause
