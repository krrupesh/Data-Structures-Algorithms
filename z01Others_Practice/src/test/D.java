package test;

import java.util.ArrayList;
import java.util.List;

public class D extends C {
	
	public static void main(String[] args) {
		/*D d = new D();
		d.m(1, 2);
		
		Integer a = null;
		int b = 2;
		System.out.println(b);
		
		try {
		    int c = 1/0;
		} catch (Exception e) {
		    System.out.println("E");
		} finally {
		    System.out.println("F");
		}*/
		
		ArrayList<Integer> l = new ArrayList<Integer>();
		l.add(2);
		l.add(3);
		l.add(-1);
		l.add(-3);
		l.add(4);
		
		removeNegative(l);
	}
	
	static void removeNegative(ArrayList<Integer> a) {
	    int i=0;
	    while(i<a.size())
	        if (a.get(i) < 0) 
	            a.remove(i);
	        else
	            i++;
	    
    	System.out.println(i);

	}
	
	
    @Override
    public float m(float x1, float x2) {
    	System.out.println("hiii");
    	return 0;
    }
}