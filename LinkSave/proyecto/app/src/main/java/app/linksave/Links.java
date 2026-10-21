package app.linksave;
import java.net.URI;
import java.util.regex.*;
public final class Links {
 public static String extract(String text) {
  Matcher m=Pattern.compile("https?://[^\\s<>\"\\u0000-\\u001f]+",Pattern.CASE_INSENSITIVE).matcher(text==null?"":text);
  if(!m.find()) throw new IllegalArgumentException("Pega un enlace completo que empiece por https://");
  String link=m.group().replaceAll("[).,;!]+$", "");
  URI uri=URI.create(link);
  if(uri.getHost()==null || uri.getUserInfo()!=null) throw new IllegalArgumentException("El enlace no es válido.");
  return link;
 }
}
