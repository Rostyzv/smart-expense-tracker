# Smart Expense Tracker (Android)

Esta es la aplicación móvil desarrollada en Kotlin y Jetpack Compose. Permite gestionar gastos diarios con una interfaz moderna y reactiva.
Para que funcione, necesitas tener corriendo el backend:
[Smart Expense Backend](https://github.com/Rostyzv/smart-expense-backend)

Requisitos:
- Android Studio Ladybug o superior.
- Tener el backend ejecutándose en la misma red local.

Configuración de Red:
Para conectar la app con el servidor Python:
- Localiza tu IP local (ej. `192.168.1.XX`).
- Actualiza la `BASE_URL` en tu código de Retrofit:
   `private const val BASE_URL = "http://192.168.1.XX:5000/"`
