package immutable.clas;



/*
 1. final class
 2. member variable private
 3. inside constructor deep clone reference objects
 4. inside getting deep clone reference objects
 5. should associated class members also be immutable ?
 */
public final class MyImmutable {

	private final int  id = 101;
	private final String  name ="Rupesh";
	
	
	
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	
	
}
