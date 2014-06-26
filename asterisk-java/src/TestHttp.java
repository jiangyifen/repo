import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;


@SuppressWarnings("unused")
public class TestHttp {
	
	public static void login() throws IOException{
		// String name = URLEncoder.encode("中文abc123", "utf-8");
		String url = "http://192.168.35.12:8088/ec/rawman?action=login&username=manager&&secret=123456";

		URL u = new URL(url);
		InputStream in = u.openStream();
		
		InputStreamReader inr = new InputStreamReader(in);
		BufferedReader br = new BufferedReader(inr);

		String s;
		do {
			s = br.readLine();
			if (s != null)
				System.out.println(new String(s.getBytes("")));
		} while (s != null);
	}
	
	public static void originate() throws IOException{
		// String name = URLEncoder.encode("中文abc123", "utf-8");
		String url = "http://192.168.35.12:8088/ec/rawman?action=originate&username=manager&&secret=123456";

		URL u = new URL(url);
		InputStream in = u.openStream();
		
		InputStreamReader inr = new InputStreamReader(in);
		BufferedReader br = new BufferedReader(inr);

		String s;
		do {
			s = br.readLine();
			if (s != null)
				System.out.println(new String(s.getBytes("")));
		} while (s != null);
	}
	
	public static void command() throws IOException{
		// String name = URLEncoder.encode("中文abc123", "utf-8");
		String url = "http://192.168.6.118:8088/ec/rawman?action=command&username=manager&&secret=123456";

		URL u = new URL(url);
		InputStream in = u.openStream();
		
		InputStreamReader inr = new InputStreamReader(in);
		BufferedReader br = new BufferedReader(inr);

		String s;
		do {
			s = br.readLine();
			if (s != null)
				System.out.println(new String(s.getBytes("")));
		} while (s != null);
	}

	public static void main(String[] args){
		
	}
}
