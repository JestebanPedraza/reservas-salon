# Sistema de Reservas de Salones

Desarrollo de un sistema integral para la gestión de reservas de salones físicos distribuidos en varias sucursales, con lógica de aprobación premium e indicadores estadísticos.

## 🚀 Tecnologías Principales
- **Backend**: Java 25 LTS, Spring Boot 4.0.6
- **Base de Datos**: PostgreSQL 16
- **Seguridad**: Spring Security + JWT (JSON Web Tokens)
- **Documentación**: SpringDoc OpenAPI (SwaggerUI)
- **Contenedores**: Docker & Docker Compose
- **Calidad**: JUnit 5, Mockito, SLF4J (Logging estructurado)

---

## 🛠️ Requisitos Previos
- **Java 21+** (instalado localmente para desarrollo)
- **Maven 3.9+**
- **Docker & Docker Compose** (recomendado para despliegue)
- **Postman** (opcional, para pruebas de API)

---

## 🏃 Cómo levantar el proyecto

### 🟢 Opción A: Despliegue Automático (Docker) - RECOMENDADO
Esta opción es la más rápida ya que configura la base de datos, el microservicio de notificaciones y la API en un entorno aislado y listo para usar.

1.  **Clonar el repositorio**:
    ```bash
    git clone https://github.com/JestebanPedraza/reservas-salon.git
    cd reservas
    ```
2.  **Asegurar que Docker esté ejecutándose** (Docker Desktop en Windows/Mac o Docker Engine en Linux).
3.  **Construir y levantar los contenedores**:
    ```powershell
    docker compose up --build -d
    ```
    *   `--build`: Fuerza la construcción de las imágenes (necesario la primera vez o si cambias el código).
    *   `-d`: Ejecuta los contenedores en segundo plano (detachable mode).
4.  **Verificar el estado**:
    ```powershell
    docker ps
    ```
    Deberías ver 3 contenedores corriendo: `api-reservas`, `microservicio-notificaciones` y `postgres-reservas`.
5.  **Acceso**:
    *   API: `http://localhost:8080`
    *   Swagger UI: `http://localhost:8080/swagger-ui/index.html`
6. **NOTA**:
    Usuario administrador: 
    ```
    email: admin@mail.com
    password: admin
    ```
    Con esas credenciale speudes empezar a crear gestores!

---

### 🟡 Opción B: Levantamiento Manual (Desarrollo)
Usa esta opción si deseas realizar cambios en el código y verlos reflejados inmediatamente sin reconstruir imágenes de Docker.

**Requisitos Previos:**
*   PostgreSQL instalado y corriendo en el puerto `5432`.
*   Crear una base de datos llamada `reservas_db`.

**Pasos:**
1.  **Configurar variables de entorno**: 
    Crea un archivo `.env` o ajusta el `src/main/resources/application.yml` con tus credenciales de base de datos local:
    ```yaml
    spring:
      datasource:
        url: jdbc:postgresql://localhost:5432/reservas_db
        username: tu_usuario
        password: tu_password
    ```
2.  **Levantar el Microservicio de Notificaciones**:
    Este servicio es necesario para que el registro de reservas no falle.
    ```bash
    cd microservicio-notificaciones
    pnpm install
    pnpm run start:dev
    ```
3.  **Compilar y ejecutar la API**:
    Abre una nueva terminal en la raíz (`reservas`):
    ```powershell
    ./mvnw clean spring-boot:run
    ```
4.  **Ejecutar Pruebas Unitarias** (Opcional):
    Para asegurar que todo esté correcto antes de empezar:
    ```powershell
    ./mvnw test
    ```

---

## 📖 Documentación de la API
Una vez levantado el proyecto, puedes acceder a la documentación interactiva:
- **Swagger UI**: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- **OpenAPI JSON**: `http://localhost:8080/v3/api-docs`

---

## ⚙️ Características Destacadas
- **Seguridad**: Autenticación basada en JWT con cierre de sesión (blacklist de tokens).
- **Lógica Premium**: Las reservas que superan los $500,000 requieren aprobación manual por parte de un ADMIN.
- **Indicadores (Sección 6)**:
    - Top 10 Clientes con más reservas.
    - Top 3 Sucursales con mayor facturación en el mes.
    - Ganancias calculadas por periodo (Hoy, Semana, Mes, Año).
- **Notificaciones**: Integración con microservicio externo para alertar sobre aprobaciones/rechazos.
- **Robustez**: Logs estructurados con Trace ID y manejo global de excepciones.

---

## 👥 Roles del Sistema
- **ADMIN**: Gestión total de sucursales, salones y aprobación de reservas premium.
- **GESTOR**: Registro de reservas, búsqueda de clientes y finalización de eventos.

---
