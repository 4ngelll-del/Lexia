# Validación

- Compilación Android con SDK 35, Java 21, AGP 8.9.1 y Gradle 8.11.1.
- Revisión Android Lint completada: 0 errores; 1 aviso sobre reglas de copia de seguridad. La copia de seguridad está deshabilitada con allowBackup=false.
- Firma del APK verificada con apksigner (APK Signature Scheme v2).
- Pruebas de extracción y validación de enlaces: 9 casos correctos, incluyendo Instagram, Facebook, Threads y X, texto compartido, enlaces inválidos y rechazo de credenciales incrustadas.
- El reel proporcionado en la conversación se descargó con yt-dlp en Linux antes de desarrollar la app. Esto no demuestra el funcionamiento de cada plataforma desde Android.

La prueba de ejecución en emulador no se completó: el entorno sin aceleración no terminó de arrancar de forma estable. No se ha probado en un teléfono físico. La compatibilidad de cada servicio depende de yt-dlp, de la región, del enlace y de las restricciones del sitio.
