package app.linksave;
import android.app.*;import android.os.*;import android.content.*;import android.graphics.Color;import android.graphics.Typeface;import android.graphics.drawable.GradientDrawable;import android.net.Uri;import android.view.*;import android.widget.*;import org.json.*;
public class MainActivity extends Activity {
 EditText link; TextView status; ProgressBar progress; Button download,update,cancel; Spinner quality; Handler handler=new Handler();
 final Runnable refresh=new Runnable(){public void run(){status.setText(DownloadService.status);progress.setIndeterminate(DownloadService.busy&&DownloadService.progress<0);progress.setProgress(Math.max(0,DownloadService.progress));download.setEnabled(!DownloadService.busy);update.setEnabled(!DownloadService.busy);cancel.setVisibility(DownloadService.busy?View.VISIBLE:View.GONE);handler.postDelayed(this,500);}};
 int dp(int n){return (int)(getResources().getDisplayMetrics().density*n);}
 TextView text(String s,int size,int color){TextView v=new TextView(this);v.setText(s);v.setTextSize(size);v.setTextColor(color);v.setPadding(0,dp(8),0,dp(8));return v;}
 Button button(String s,LinearLayout box){Button b=new Button(this);b.setText(s);b.setAllCaps(false);box.addView(b,new LinearLayout.LayoutParams(-1,dp(54)));return b;}
 @Override public void onCreate(Bundle saved){super.onCreate(saved);
 ScrollView scroll=new ScrollView(this);LinearLayout box=new LinearLayout(this);box.setOrientation(LinearLayout.VERTICAL);box.setPadding(dp(24),dp(28),dp(24),dp(32));box.setBackgroundColor(Color.rgb(244,247,246));scroll.addView(box);setContentView(scroll);
 if(Build.VERSION.SDK_INT>=35)scroll.setOnApplyWindowInsetsListener((v,insets)->{android.graphics.Insets i=insets.getInsets(WindowInsets.Type.systemBars());v.setPadding(i.left,i.top,i.right,i.bottom);return insets;});
 TextView title=text("LinkSave ↓",34,0xff124c43);title.setTypeface(null,Typeface.BOLD);box.addView(title);
 box.addView(text("Tus videos, contigo.",20,0xff203f39));box.addView(text("Pega un enlace o envíalo desde Compartir en tu app favorita.",16,0xff526661));
 box.addView(text("Instagram · Facebook · Threads · X\nY muchos otros sitios compatibles",14,0xff147d70));
 link=new EditText(this);link.setHint("https://…");link.setInputType(17);link.setSingleLine(false);link.setMinLines(2);box.addView(link,new LinearLayout.LayoutParams(-1,-2));
 button("Pegar enlace",box).setOnClickListener(v->{ClipboardManager cm=(ClipboardManager)getSystemService(CLIPBOARD_SERVICE);if(cm.hasPrimaryClip())link.setText(cm.getPrimaryClip().getItemAt(0).coerceToText(this));});
 box.addView(text("Calidad máxima",14,0xff526661));quality=new Spinner(this);quality.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,new String[]{"Mejor disponible","1080p","720p","480p"}));box.addView(quality);
 download=button("↓  Descargar video",box);download.setTextColor(Color.WHITE);download.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xff147d70));download.setOnClickListener(v->{try{String url=Links.extract(link.getText().toString());startJob(url,false);}catch(Exception e){Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();}});
 progress=new ProgressBar(this,null,android.R.attr.progressBarStyleHorizontal);progress.setMax(100);box.addView(progress,new LinearLayout.LayoutParams(-1,dp(18)));status=text("Listo para descargar",15,0xff203f39);status.setTextIsSelectable(true);box.addView(status);
 cancel=button("Cancelar",box);cancel.setOnClickListener(v->DownloadService.cancel());
 button("Ver videos descargados",box).setOnClickListener(v->history());update=button("Actualizar motor de descarga",box);update.setOnClickListener(v->startJob("",true));
 box.addView(text("Se guardan en Descargas/LinkSave.\n\nLa compatibilidad varía por sitio. Los contenidos privados, con inicio de sesión o con DRM pueden no descargarse. Si un enlace falla, prueba actualizar el motor.",13,0xff526661));
 if(saved!=null)link.setText(saved.getString("link",""));else accept(getIntent());
 if(Build.VERSION.SDK_INT>=33&&checkSelfPermission("android.permission.POST_NOTIFICATIONS")!=android.content.pm.PackageManager.PERMISSION_GRANTED)requestPermissions(new String[]{"android.permission.POST_NOTIFICATIONS"},1);
 }
 void startJob(String url,boolean updating){if(DownloadService.busy)return;Intent i=new Intent(this,DownloadService.class);i.putExtra("url",url);i.putExtra("update",updating);i.putExtra("height",new int[]{0,1080,720,480}[quality.getSelectedItemPosition()]);startForegroundService(i);}
 void accept(Intent i){if(Intent.ACTION_SEND.equals(i.getAction())){String s=i.getStringExtra(Intent.EXTRA_TEXT);try{link.setText(Links.extract(s));}catch(Exception e){link.setText(s);}}}
 @Override protected void onNewIntent(Intent i){super.onNewIntent(i);setIntent(i);accept(i);}
 @Override protected void onSaveInstanceState(Bundle b){b.putString("link",link.getText().toString());super.onSaveInstanceState(b);}
 @Override protected void onResume(){super.onResume();handler.post(refresh);}
 @Override protected void onPause(){handler.removeCallbacks(refresh);super.onPause();}
 void history(){try{JSONArray a=new JSONArray(getSharedPreferences("data",0).getString("history","[]"));if(a.length()==0){Toast.makeText(this,"Aquí aparecerán tus videos descargados.",Toast.LENGTH_SHORT).show();return;}String[] names=new String[a.length()];for(int n=0;n<a.length();n++)names[n]=a.getJSONObject(n).getString("name");new AlertDialog.Builder(this).setTitle("Tus descargas").setItems(names,(d,n)->{try{JSONObject o=a.getJSONObject(n);Intent i=new Intent(Intent.ACTION_VIEW).setDataAndType(Uri.parse(o.getString("uri")),o.optString("mime","video/*")).addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);startActivity(i);}catch(Exception e){Toast.makeText(this,"No se pudo abrir. Busca el archivo en Descargas/LinkSave.",Toast.LENGTH_LONG).show();}}).setNegativeButton("Cerrar",null).show();}catch(Exception e){Toast.makeText(this,"No se pudo leer el historial.",Toast.LENGTH_SHORT).show();}}
}
