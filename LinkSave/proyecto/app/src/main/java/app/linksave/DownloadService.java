package app.linksave;
import android.app.*;import android.os.*;import android.content.*;import android.net.Uri;import android.provider.MediaStore;import android.webkit.MimeTypeMap;import java.io.*;import java.util.*;import org.json.*;import com.yausername.youtubedl_android.*;import com.yausername.ffmpeg.FFmpeg;import kotlin.Unit;
public class DownloadService extends Service {
 public static volatile boolean busy=false;public static volatile int progress=0;public static volatile String status="Listo para descargar";private static volatile boolean canceled=false;private static volatile boolean running=false;
 static final String PROCESS="linksave-download";NotificationManager notifications;long lastNotice;
 public static void cancel(){if(!busy)return;canceled=true;status="Cancelando…";if(running)new Thread(()->YoutubeDL.getInstance().destroyProcessById(PROCESS)).start();}
 @Override public IBinder onBind(Intent i){return null;}
 Notification notification(String message){PendingIntent p=PendingIntent.getActivity(this,0,new Intent(this,MainActivity.class),PendingIntent.FLAG_IMMUTABLE);return new Notification.Builder(this,"downloads").setSmallIcon(app.linksave.R.drawable.ic_download).setContentTitle("LinkSave").setContentText(message).setContentIntent(p).setOngoing(busy).setProgress(100,Math.max(0,progress),progress<0&&busy).build();}
 void report(String s,int p){status=s;progress=p;if(System.currentTimeMillis()-lastNotice>1000){notifications.notify(1,notification(s));lastNotice=System.currentTimeMillis();}}
 @Override public int onStartCommand(Intent intent,int flags,int startId){
 notifications=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);notifications.createNotificationChannel(new NotificationChannel("downloads","Descargas de videos",NotificationManager.IMPORTANCE_LOW));
 if(busy)return START_NOT_STICKY;busy=true;canceled=false;progress=-1;status="Preparando motor…";startForeground(1,notification(status));
 if(intent==null){busy=false;stopForeground(STOP_FOREGROUND_REMOVE);stopSelf();return START_NOT_STICKY;}
 final String url=intent.getStringExtra("url");final boolean update=intent.getBooleanExtra("update",false);final int height=intent.getIntExtra("height",0);
 new Thread(()->runJob(url,update,height),"LinkSave-worker").start();return START_NOT_STICKY;
 }
 void checkCanceled() throws InterruptedException{if(canceled)throw new InterruptedException("Cancelado");}
 void runJob(String url,boolean update,int height){File folder=null;PowerManager.WakeLock lock=((PowerManager)getSystemService(POWER_SERVICE)).newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"LinkSave:download");lock.acquire(6*60*60*1000L);
 try{
  YoutubeDL.getInstance().init(this);FFmpeg.getInstance().init(this);checkCanceled();
  if(update){report("Actualizando motor…",-1);YoutubeDL.getInstance().updateYoutubeDL(this,YoutubeDL.UpdateChannel._STABLE);checkCanceled();report("Motor actualizado. Ya puedes descargar.",100);}
  else{
   String valid=Links.extract(url);folder=new File(getCacheDir(),"job-"+UUID.randomUUID());if(!folder.mkdirs())throw new IOException("No se pudo preparar el almacenamiento.");
   YoutubeDLRequest req=new YoutubeDLRequest(valid);req.addOption("--no-playlist");req.addOption("--no-mtime");req.addOption("--restrict-filenames");req.addOption("--socket-timeout","25");req.addOption("--retries","2");req.addOption("--max-filesize","2G");req.addOption("--merge-output-format","mp4");
   String cap=height>0?"[height<="+height+"]":"";
   req.addOption("-f","bv*"+cap+"[ext=mp4]+ba[ext=m4a]/b"+cap+"[ext=mp4]/bv*"+cap+"+ba/b"+cap);
   req.addOption("-o",new File(folder,"%(title).100s [%(id)s].%(ext)s").getAbsolutePath());
   report("Conectando con el sitio…",-1);checkCanceled();running=true;
   try{YoutubeDL.getInstance().execute(req,PROCESS,(p,eta,line)->{if(canceled)YoutubeDL.getInstance().destroyProcessById(PROCESS);else report(p>=0?"Descargando · "+Math.round(p)+"% · "+eta+" s restantes":"Procesando video…",p>=0?Math.round(p):-1);return Unit.INSTANCE;});}finally{running=false;}
   checkCanceled();File[] files=folder.listFiles(f->f.isFile()&&!f.getName().endsWith(".part")&&!f.getName().endsWith(".ytdl"));if(files==null||files.length==0)throw new IOException("El sitio no devolvió ningún video.");
   report("Guardando en Descargas/LinkSave…",-1);for(File f:files){checkCanceled();save(f);}report("✓ Video guardado en Descargas/LinkSave",100);
  }
 }catch(Exception e){if(canceled)report("Descarga cancelada",0);else{String reason=e.getMessage()==null?e.getClass().getSimpleName():e.getMessage();if(reason.length()>1000)reason=reason.substring(reason.length()-1000);report("No se pudo completar. Prueba actualizar el motor; el enlace puede requerir acceso o no ser compatible.\n\nDetalle: "+reason,0);}}
 finally{if(folder!=null)delete(folder);if(lock.isHeld())lock.release();busy=false;stopForeground(STOP_FOREGROUND_REMOVE);notifications.notify(1,notification(status));stopSelf();}
 }
 void save(File file)throws Exception{
  String name=file.getName();String ext=name.contains(".")?name.substring(name.lastIndexOf('.')+1):"mp4";String mime=MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext);if(mime==null)mime="video/mp4";
  ContentValues values=new ContentValues();values.put(MediaStore.Downloads.DISPLAY_NAME,name);values.put(MediaStore.Downloads.MIME_TYPE,mime);values.put(MediaStore.Downloads.RELATIVE_PATH,"Download/LinkSave");values.put(MediaStore.Downloads.IS_PENDING,1);
  Uri uri=getContentResolver().insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI,values);if(uri==null)throw new IOException("No se pudo crear el archivo de destino.");
  try{try(InputStream in=new FileInputStream(file);OutputStream out=getContentResolver().openOutputStream(uri)){if(out==null)throw new IOException("No se pudo abrir el destino.");byte[] buffer=new byte[65536];int n;while((n=in.read(buffer))!=-1){checkCanceled();out.write(buffer,0,n);}}
   values.clear();values.put(MediaStore.Downloads.IS_PENDING,0);getContentResolver().update(uri,values,null,null);
  }catch(Exception e){getContentResolver().delete(uri,null,null);throw e;}
  android.content.SharedPreferences prefs=getSharedPreferences("data",0);JSONArray old=new JSONArray(prefs.getString("history","[]"));JSONArray next=new JSONArray();JSONObject row=new JSONObject();row.put("name",name);row.put("uri",uri.toString());row.put("mime",mime);next.put(row);for(int i=0;i<old.length()&&i<99;i++)next.put(old.get(i));prefs.edit().putString("history",next.toString()).apply();
 }
 void delete(File f){if(f.isDirectory()){File[] children=f.listFiles();if(children!=null)for(File c:children)delete(c);}f.delete();}
 @Override public void onTimeout(int startId,int fgsType){cancel();stopSelf();}
}
