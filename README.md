
# Proyecto de Control de Asistencia - Acceso Seguro (Backend)

## Intregrantes

- [Jose Corredor](https://github.com/MementoMoriReload)
- [Antonella Gonzalez](https://github.com/Antoogg)
- [Alexander Valdes](https://github.com/alexanderRaulvaldes)

## Endpoints

#### AuthController
#### Registro

```http
  POST api/v1/auth/registro
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `rut` | `string` | **Required**. RUT|
| `nombre` | `string` | **Required**. nombre|
| `email` | `string` | **Required**. email|
| `pass` | `string` | **Required**. contraseña|
| `rol` | `Rol(enum)` | rol|

#### Login
```http
  POST api/v1/auth/login
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `username` | `string` | **Required**. Rut o correo|
| `password` | `string` | **Required**. Contraseña|

#### EdificioController
#### Registrar Movimiento

```http
  POST /api/v1/edificio
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `nomPersona` | `string` | **Required**. Nombre|
| `rut` | `string` | **Required**. RUT|
| `edificioId` | `string` | **Required**. ID de Edificio|
| `tipo` | `TipoMovimiento(enum)` |  **Required**. Tipo de movimiento (Entrada o Salida)|
| `visitante` | `boolean` |  Es visitante o empleado|
#### Listar todos los Edificios

```http
  GET /api/v1/edificio
```

#### Listar todas las personas registradas

```http
  GET /api/v1/edificio/personas
```

#### Obtener las personas de un edificio

```http
  GET /api/v1/edificio/{id}
```
| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `edificioId` | `Long` | **Required**. Id del edificio |

#### Eliminar cierta persona de un edificio por su RUT

```http
  DELETE /api/v1/user/{id}/
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `edificioId` | `Long` | **Required**. Id del edificio |
| `rut` | `Long` | **Required**. RUT de la persona a eliminar|

## Documentación Swagger

[Auth-Service Swagger ](http://localhost:8081/doc/swagger-ui/index.html#/)



 [Edificio Swagger](http://localhost:8082/doc/swagger-ui/index.html#/)

## 

![Logo](https://thumb.wikimedia.org/wikipedia/commons/thumb/a/aa/Logo_DuocUC.svg/1280px-Logo_DuocUC.svg.png?utm_source=es.wikipedia.org&utm_campaign=index&utm_content=thumbnail)

