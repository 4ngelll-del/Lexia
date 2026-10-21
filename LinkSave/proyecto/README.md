# LinkSave para Android

App nativa en español para descargar videos a partir de enlaces. Android 10 o superior; ARM64, ARMv7 y x86_64. Sin servidor propio, cuenta ni clave API. Procesamiento en el teléfono con yt-dlp y FFmpeg.

## Instalar y usar

1. Abre el APK en tu Android. Si el sistema lo pide, permite a tu navegador o gestor de archivos instalar esta app.
2. Abre LinkSave y pega un enlace; también puedes usar Compartir → LinkSave desde otra app.
3. Selecciona la calidad máxima y pulsa Descargar video.
4. Los archivos aparecen en Descargas/LinkSave. Puedes abrirlos desde Ver videos descargados.

La descarga continúa en un servicio en primer plano y muestra una notificación si lo autorizas. Puedes cancelar desde la app. Una descarga a la vez. Las listas de reproducción están desactivadas. Tamaño máximo por flujo: 2 GiB; se necesita espacio para los archivos temporales y el video final. No se reanudan trabajos después de forzar el cierre de la app. El historial local conserva hasta 100 archivos.

## Compatibilidad

Instagram, Facebook, Threads, X y otros sitios que soporte la versión instalada de yt-dlp. No se garantiza que cada enlace funcione. Contenido privado, autenticación, restricciones regionales, límites del sitio y DRM pueden impedir las descargas. No incluye inicio de sesión ni importación de cookies. No extrae videos internos de apps sin enlace web. Para sitios que cambien, usa Actualizar motor de descarga (se conecta al distribuidor de yt-dlp de la biblioteca).

Se prioriza MP4 con audio; si no está disponible, se conserva otro contenedor compatible con la fuente. Las calidades son límites máximos, no aumento de resolución. El botón Cancelar también permite cancelar una actualización, pero espera a que la petición de actualización termine antes de limpiar el trabajo.

## Privacidad

Los enlaces se envían directamente a sus sitios de origen y servidores multimedia. No hay analítica ni backend del desarrollador. La actualización del motor usa GitHub. El historial se almacena solo en el dispositivo. No se requiere permiso para leer toda tu galería; se utiliza MediaStore para crear archivos en Descargas.

## Compilar

Requiere JDK 17 o 21 y Android SDK 35. Abre el proyecto en Android Studio o configura `ANDROID_HOME` y ejecuta `./gradlew assembleDebug`. No se incluye local.properties porque contiene una ruta específica del entorno de compilación.

Dependencias principales: Android Gradle Plugin 8.9.1, Gradle 8.11.1 y youtubedl-android 0.18.1. El APK entregado usa una firma de desarrollo; no es una publicación en Play Store.

## Fuentes y licencias

Este proyecto se distribuye bajo GPL-3.0. Se incluye su código fuente para poder modificarlo y recompilarlo.

- youtubedl-android 0.18.1 (GPL-3.0): https://github.com/yausername/youtubedl-android/tree/0.18.1 — biblioteca, runtimes y recetas de compilación nativa. Si el tag cambia, consulta https://github.com/yausername/youtubedl-android
- yt-dlp y sus dependencias: https://github.com/yt-dlp/yt-dlp
- Binarios distribuidos por el wrapper: https://github.com/xibr/ytdlp-lazy
- FFmpeg: https://ffmpeg.org/ y receta https://github.com/yausername/youtubedl-android/blob/master/BUILD_FFMPEG.md
- Dependencias AndroidX, Kotlin, Jackson, Commons IO y Commons Compress se resuelven por Maven; sus avisos están en sus respectivas distribuciones.

Consulta también las licencias de las dependencias antes de redistribuir otra compilación.
