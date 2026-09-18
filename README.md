# Practica Parcial

Diseno y desarrollo de APIs REST.

## Contenido

- `Ejercicio1/` - diseno OpenAPI de la API de biblioteca
- `Ejercicio2/` - diseno OpenAPI de la API de cursos universitarios
- `Ejercicio3/` - diseno OpenAPI de la API de reservas de hotel
- `Ejercicio4/` - implementacion del Ejercicio 1
- `Ejercicio5/` - implementacion del Ejercicio 2
- `Ejercicio6/` - implementacion del Ejercicio 3

Los archivos `.yaml` se pueden abrir en https://editor.swagger.io

## Como ejecutar

Java 21 y Maven.

```
cd Practica-Parcial/Ejercicio4/biblioteca-api
mvn spring-boot:run
```

| Ejercicio | Puerto | Ruta base |
|---|---|---|
| 4 | 8081 | /api/v1/libros |
| 5 | 8082 | /api/v1/cursos |
| 6 | 8083 | /api/v1/reservas |

Con la aplicacion levantada, la documentacion queda en `/swagger-ui.html`.

Para correr las pruebas: `mvn test`
