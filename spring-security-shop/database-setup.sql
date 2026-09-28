USE [master];
GO

IF DB_ID(N'security_shop') IS NULL
BEGIN
    CREATE DATABASE [security_shop];
END;
GO

USE [security_shop];
GO

IF DATABASE_PRINCIPAL_ID(N'spring_security_app') IS NULL
BEGIN
    CREATE USER [spring_security_app]
    FOR LOGIN [spring_security_app];
END;
GO

IF IS_ROLEMEMBER(N'db_owner', N'spring_security_app') <> 1
BEGIN
    ALTER ROLE [db_owner]
    ADD MEMBER [spring_security_app];
END;
GO

SELECT
    DB_NAME() AS database_name,
    USER_NAME() AS [current_user];
GO