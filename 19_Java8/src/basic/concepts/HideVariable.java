package basic.concepts;
public class HideVariable {

    private String message = "this is instance variable";

	public static void main(String[] args) {
		
		HideVariable variable = new HideVariable();
		variable.printLocalVariable();

		variable.printInstanceVariable(); 
		
		variable.printInstanceVariableWithoutThis();
	}
	
	

    HideVariable() {
        String message = "constructor local variable";
        System.out.println(message);
    }

    public void printLocalVariable() {
        String message = "method local variable";
        System.out.println(message);
    }

    public void printInstanceVariable() {
        String message = "method local variable";
        System.out.println(this.message);
    }
    
    public void printInstanceVariableWithoutThis() {
        System.out.println(message);
    }
}