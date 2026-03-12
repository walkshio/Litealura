# Challenge LiterAlura - Catálogo de Libros 

### Descripción
LiterAlura es una aplicación de gestión de libros y autores que consume la API de Gutendex. El proyecto se enfoca en la persistencia de datos en una base de datos relacional y el uso de Spring Data JPA para consultas avanzadas.

### 🚀 Funcionalidades
- Buscar libros por título a través de la API Gutendex.
- Persistencia de libros y autores en base de datos MySQL.
- Listado de libros y autores registrados.
- Filtro de autores vivos en un año determinado.
- Estadísticas de libros por idioma (Español, Inglés, etc.).

### 🛠️ Tecnologías utilizadas
- **Java 21**
- **Spring Boot 3.2.3**
- **Spring Data JPA**: Para el manejo de la base de datos.
- **MySQL**: Motor de base de datos relacional.
- **Jackson**: Para la conversión de JSON a objetos Java (POJOs/Records).
- **Maven**: Gestión de dependencias.

### ⚙️ Configuración
Es necesario configurar el archivo `application.properties` con las credenciales locales de MySQL y haber creado previamente la base de datos `literalura`.


## 📸 Demostración
![Menú Principal]

<img width="508" height="212" alt="image" src="https://github.com/user-attachments/assets/d81de225-a39c-42b0-beec-d3f4b0fa252d" />


*Ejemplo de consulta en la base de datos:*

<img width="574" height="548" alt="image" src="https://github.com/user-attachments/assets/6f8a2958-4f08-4284-84de-be28bab756af" />

