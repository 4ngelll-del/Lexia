import app.linksave.Links;
public class LinksCheck {
 public static void main(String[] args) {
  eq("https://www.instagram.com/reel/DajOpVnz__A/?l=1", Links.extract("Mira este video https://www.instagram.com/reel/DajOpVnz__A/?l=1"));
  eq("https://x.com/user/status/123",Links.extract("Compartido: https://x.com/user/status/123."));
  eq("https://www.facebook.com/share/r/ABC/",Links.extract("https://www.facebook.com/share/r/ABC/"));
  eq("https://www.threads.net/@user/post/ABC",Links.extract("https://www.threads.net/@user/post/ABC"));
  for(String bad:new String[]{"", "ftp://example.com/file", "https://", "https://user:pass@example.com/", "javascript:alert(1)"}){boolean rejected=false;try{Links.extract(bad);}catch(IllegalArgumentException e){rejected=true;}if(!rejected)throw new AssertionError(bad);}
  System.out.println("9 comprobaciones de enlaces correctas");
 }
 static void eq(String a,String b){if(!a.equals(b))throw new AssertionError(a+" != "+b);}
}
