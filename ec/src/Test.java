import java.io.IOException;

import org.json.JSONException;

public class Test {

	public static void main(String[] args) throws JSONException, IOException {
		String s = "123,";
		System.out.println(s.substring(0, s.length()-1));
	}

}