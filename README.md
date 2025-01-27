# Franquicias App

Este proyecto implementa un sistema reactivo para gestionar franquicias y sucursales utilizando **Spring WebFlux** y **PostgreSQL** con R2DBC.

## Tecnologías Usadas // Prerrequisitos
- **Java 17**
- **Spring Boot 3**
- **Spring WebFlux**
- **Spring Data R2DBC**
- **PostgreSQL**

## Clonacion del proyecto

1. Clonar el Repositorio
2. git clone https://github.com/santiqc/backend-nequi.git
3. cd franquicias-app


## Configuración de Base de Datos 
1. CREATE DATABASE franquicias;
2. Ejecutar script de inicialización: desde el properties descomentar la linea #spring.sql.init.mode=always para que se cree

## Compilación y Ejecución
Usando Maven

# Compilar el proyecto
mvn clean package

# Ejecutar la aplicación
java -jar target/franquicias-app-0.0.1-SNAPSHOT.jar
