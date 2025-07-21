# ARQUITECTURA

## Frontend
Se crea Frontend usando React; se usa routes para manejar rutas y axios para la conexión con el Backend. La arquitecta que se usa es Component-Based + Service-Oriented, se definen los componentes principales, en los cuales se agregan formularios, botones, eventos y estados. Adicional, se crea una clase .Service donde se definen los metodos de la API y se da la conexión entre el Frontend y el Backend.

## Backend 
Se crea el backend utilizando arquitectura limpia, en la cual se crea un dominio donde se definie el modelo del negocio y las interfaces con sus comportamientos (lo cual no deberia moficarse), se crea tambien la capa de infraestructura donde se colocan las implementaciones técnicas y se crea la capa de aplicacion de casos de uso donde se implemnta la logica especifica del sistema y por ultimo la capa del controlador donde se definieron los endpoints.

## Base de datos
Se crea la base de datos utilizando un servicio en DynamoBD de AWS.

## Pruebas
Se crean pruebas unitarias que validen los useCases donde se encuentra la lógica del negocio y pruebas de integración para probar los principales flujos que son:

# INSTALACIÓN

## Prerequisitos
- JAVA 17 + Spring Boot.
- DOCKER.
- NPM.

## Correr proyecto
### Backend
Para correr el Backend se deben seguir los siguientes pasos:

1. Ejecutar ¨mvn install¨ en consola.
2. Encender Docker y ejecutar en consola ¨docker build -t guama-transactions .¨
3. Ejecutar el siguiente commando en consola, con los datos enviados en el correo. ¨docker run -p 8080:8080 -e AWS_ACCESS_KEY_ID=tu-access-ke8 \
           -e AWS_SECRET_ACCESS_KEY=tu-secret-key \
           -e AWS_REGION=us-east-2 \guama-transactions¨
4. Una vez ejecutado, el backend comenzaria a correr y se puede acceder a através de: *http://localhost:8080/api/transactions*

Adjunto Collection para probar los servicios de la API: https://bold-rocket-312324.postman.co/workspace/ServiciosAndes~37bbb871-b2c5-4b8e-908d-292c7b858fd5/collection/8122221-c694fb37-3b73-45fa-b379-195202857be9?action=share&creator=8122221

### Frontend
Para correr el Frontend se deben seguir los siguientes pasos:

1. Desde la consola navegar con cd hasta la carpeta del front llamada ¨guama'frontend¨.
2. Una vez en la carpeta ejecutar ¨npm install¨.
3. Luego ejecutar ¨npm start¨.
4. El frontend comenzara a correr y se abrira el navegador o se puede acceder desde: *://localhost:3000*

