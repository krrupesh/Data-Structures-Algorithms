package serionlabs;
class Foo1{
	public static void X(){
		Y();
	}

	public static void Y() {
		System.out.println("Y");		
	}
}

class Bar extends Foo1{
	public static void Y() {
		System.out.println("Z");		
	}
}

class FooTest{
	public static void main(String[] args) {
		Bar.X();
	}
}