package clearwater;

import java.util.Map;
import java.util.TreeMap;

public class Main {

	public static void main(String[] args) {

		Map<Integer, String> m = new TreeMap<Integer, String>();
		m.put(11, "audi");
		m.put(null, null);
		m.put(11, "bmw");
		m.put(null, "fer");

		System.out.println(m.size());

	}

}
