# Spring Boot Batch

[![N|Solid](https://i.imgur.com/r3lfak5.png)](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)

[![Build Status](https://i.imgur.com/NifAxX7.jpg[/img])](http://www.google.com)

Este es un proyecto Spring-Boot-Batch el cual procesa un CVS a una Base de Datos:

  - Lee un CSV
  - Base de Datos en memoria
  - Perfiles

Para poder compilarlo de Jekins usamos el comando

```
mvn clean install -DskipTests=true
```

Para poder correr el proyecto compilado
```
mvn springboot:run -Dspring-boot.run.profiles=desa
```
o podemos usar en la jar de la carpeta Target
```
java -jar -Dspring.profiles.actives=desa spring-boot-batch-example-0.0.1-SNAPSHOT
```
