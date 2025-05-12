# Sistema de Gestión de Usuarios con Patrón Abstract Factory

Este proyecto implementa un sistema de gestión de usuarios utilizando el patrón Abstract Factory, con un backend en Spring Boot y un frontend en React. El sistema permite crear y gestionar usuarios con diferentes roles (admin y guest), cada uno con sus propios campos específicos.

## Tecnologías Utilizadas

### Backend
- Java 17
- Spring Boot
- Firebase (Firestore)
- Maven

### Frontend
- React
- Vite
- Axios
- Tailwind CSS

## Requisitos Previos

- Java 17 o superior
- Node.js 16 o superior
- npm o yarn
- Cuenta de Firebase con un proyecto configurado
- Archivo de configuración de Firebase (`serviceAccountKey.json`)

## Configuración del Proyecto

### Backend

1. Ubica el archivo `serviceAccountKey.json` de Firebase en la carpeta `back/app/src/main/resources/`

2. Navega a la carpeta del backend:
```bash
cd back/app
```

3. Instala las dependencias de Maven:
```bash
./mvnw clean install
```

4. Inicia el servidor backend:
```bash
./mvnw spring-boot:run
```

El backend estará disponible en `http://localhost:8080`

### Frontend

1. Navega a la carpeta del frontend:
```bash
cd front/intefazfront
```

2. Instala las dependencias:
```bash
npm install
```

3. Inicia el servidor de desarrollo:
```bash
npm run dev
```

El frontend estará disponible en `http://localhost:5173` o `http://localhost:5174` (dependiendo de la disponibilidad del puerto)

## Estructura del Proyecto

```
├── back/
│   └── app/
│       ├── src/
│       │   ├── main/
│       │   │   ├── java/
│       │   │   │   └── com/
│       │   │   │       └── example/
│       │   │   │           └── app/
│       │   │   │               ├── config/
│       │   │   │               ├── controller/
│       │   │   │               ├── factory/
│       │   │   │               ├── model/
│       │   │   │               └── service/
│       │   │   └── resources/
│       │   └── test/
│       └── pom.xml
└── front/
    └── intefazfront/
        ├── src/
        │   ├── components/
        │   ├── pages/
        │   └── App.jsx
        ├── package.json
        └── vite.config.js
```

## Características

- Creación de usuarios con roles específicos (admin/guest)
- Gestión de usuarios (editar, eliminar)
- Formularios dinámicos según el rol del usuario
- Integración con Firebase para persistencia de datos
- Interfaz de usuario moderna con Tailwind CSS

## API Endpoints

### Usuarios
- `GET /users` - Obtener todos los usuarios
- `POST /users` - Crear un nuevo usuario
- `PUT /users/{id}` - Actualizar un usuario existente
- `DELETE /users/{id}` - Eliminar un usuario
- `GET /users/{id}` - Obtener un usuario específico

### Formularios
- `GET /form/{role}` - Obtener campos del formulario según el rol
- `POST /form/login` - Autenticación de usuario

## Contribuir

1. Haz un Fork del proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para más detalles. 