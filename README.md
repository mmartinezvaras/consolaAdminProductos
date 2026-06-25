# Aplicación de Gestion Venta y Reventa de Cascos

## Tecnologías Utilizadas
- **Frontend**: Angular, PrimeNG, SCSS, TypeScript estricto, Formularios reactivos, HttpClient
- **Backend**: Spring Boot, Spring Data JPA, Bean Validation, MySQL Driver, Spring Boot Test

## Estructura del Proyecto
```
Cascos/
├── backend/
│   ├── config/
│   ├── controller/
│   ├── dto/
│   ├── entity/
│   ├── enums/
│   ├── exception/
│   ├── mapper/
│   ├── repository/
│   ├── service/
│   └── service/impl/
├── database/
│   └── seed.sql
└── frontend/
```

## Requisitos
- Node.js >= 20.20.0
- npm >= 11.12.1
- Java >= 25.0.2
- Maven >= 3.9.13

## Comandos Básicos
```sh
# Compilar y ejecutar el backend
cd backend && mvn clean install spring-boot:run

# Compilar y ejecutar el frontend
cd ../frontend && npm install && ng serve