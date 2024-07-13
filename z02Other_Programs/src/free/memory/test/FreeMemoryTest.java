package free.memory.test;

import java.util.ArrayList;
import java.util.List;

public class FreeMemoryTest {

	public static void main(String[] args) {

		Runtime runtime = Runtime.getRuntime();
		System.out.println(runtime.freeMemory());
		
		List list = new ArrayList<>();
		
		for(int i=0;i<100000000;i++){
			list.add("Rupesh1Rupesh1Rupesh1Rupesh1Rupesh1Rupesh1Rupesh1Rupesh1Rupesh1Rupesh1Rupesh1Rupesh1");	
		}
		
		System.out.println(runtime.freeMemory());
	}

}
