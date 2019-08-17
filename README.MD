# Spring Boot Batch

[![N|Solid](https://i.imgur.com/r3lfak5.png)](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)

Este es un proyecto Spring-Boot-Batch el cual procesa un CVS a una Base de Datos:

  - Lee un CSV
  - Base de Datos en memoria
  - Perfiles

Para poder correr el JAR usamos
```
mvn springboot:run -Dspring-boot.run.profiles=desa
```
o podemos usar en la jar de la carpeta Target
```
java -jar -Dspring.profiles.actives=desa spring-boot-batch-example-0.0.1-SNAPSHOT
```