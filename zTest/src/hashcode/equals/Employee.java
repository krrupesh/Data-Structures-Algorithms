package hashcode.equals;

public class Employee {

	int age;
	String name;
	
	public Employee(int age, String name) {
		super();
		this.age = age;
		this.name = name;
	}

	
	/*
	 
	emp1 hashcode.equals.Employee@a
	emp2 hashcode.equals.Employee@a
	false
	true
	{hashcode.equals.Employee@a=200}
	 
	 */
	
	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	/*@Override
	public int hashCode(){
	
		return 10;
	}*/
	
	@Override
	public boolean equals(Object obj){
		
		Employee emp = (Employee)obj;
		
		if((emp.age == this.age) && (emp.name == this.name)){
			return true;
		}else{
			return false;
		}
		
	}
	
}
