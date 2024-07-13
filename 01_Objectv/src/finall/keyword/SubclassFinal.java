package finall.keyword;

public class SubclassFinal extends FinalTest{


	public static void main(String[] args) {

		SubclassFinal obj  = new SubclassFinal();
		obj.method3();
		
	}
	
	public void method3(){
		System.out.println("SubclassFinal.method3()");
		method1();
		method2();
	}
	
	public void method2(){
		System.out.println("SubclassFinal.method2()");
	}
}
