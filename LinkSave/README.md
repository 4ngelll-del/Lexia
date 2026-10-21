# LinkSave para Android

Descargador de videos mediante enlaces, con interfaz en español. Android 10 o superior.

## Descargar e instalar

**[Descargar LinkSave para Android ARM64](https://github.com/4ngelll-del/Lexia/raw/refs/heads/main/LinkSave/downloads/LinkSave-arm64-v8a.apk)** — opción recomendada para la mayoría de teléfonos actuales.

Otros procesadores:

- [Android ARM de 32 bits](https://github.com/4ngelll-del/Lexia/raw/refs/heads/main/LinkSave/downloads/LinkSave-armeabi-v7a.apk)
- [Android x86_64](https://github.com/4ngelll-del/Lexia/raw/refs/heads/main/LinkSave/downloads/LinkSave-x86_64.apk) — algunos emuladores y dispositivos Intel.

1. Abre la descarga en un teléfono Android.
2. Si Android lo solicita, permite a tu navegador o gestor de archivos instalar esta aplicación.
3. Instala y abre LinkSave. Pega un enlace y pulsa **Descargar video**, o utiliza **Compartir → LinkSave** desde otra app.
4. Los videos se guardan en **Descargas/LinkSave**.

Un APK no se instala en iPhone. Puedes descargarlo desde una computadora y transferirlo a Android.

## Funciones y límites

- Motor yt-dlp en el teléfono, con FFmpeg, sin servidor propio ni claves API.
- Recibe enlaces pegados o compartidos, permite limitar la calidad y muestra el progreso.
- Descargas en segundo plano, cancelación, historial local y actualización manual del motor.
- Instagram, Facebook, Threads, X y otros sitios compatibles con la versión instalada de yt-dlp. No se garantiza cada enlace; contenido privado, DRM, autenticación y restricciones del sitio pueden impedir las descargas.

**Versión inicial con firma de desarrollo.** Compilación, revisión Android Lint y firma del APK verificadas. La prueba de ejecución en emulador no se completó; no se ha probado en un teléfono físico. Consulta [PRUEBAS.md](PRUEBAS.md).

## Código fuente

El proyecto completo está en [proyecto/](proyecto/), incluyendo las instrucciones para compilar y la licencia GPL-3.0. [Descargar el código fuente en ZIP](downloads/LinkSave-codigo-fuente.zip).

Los APK se distribuyen por procesador para que cada archivo sea menor de 100 MiB. Las sumas de comprobación están en [SHA256SUMS.txt](SHA256SUMS.txt).
