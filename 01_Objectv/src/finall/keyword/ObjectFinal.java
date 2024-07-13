package finall.keyword;

public class ObjectFinal {

	public static void main(String[] args) {

		final Student stu = new Student("Rupesh1",100,1000);
		System.out.println("stu : "+stu);
		
		stu.setMarks(200);
		System.out.println("stu : "+stu);

		
		stu.setName("Rupesh2");
		System.out.println("stu : "+stu);

		
		final int []arr={10,20,30,40,50};
		printArray(arr);
		System.out.println();
		
		arr[1]=35;
		printArray(arr);
		
		final int x = 10;
		//x = 20;

	}

	
	public static void printArray(int arr[]){
		for(int i=0;i<arr.length;i++){
			System.out.print(arr[i]+" ");
		}
	}
}


class Student {
	
	String name;
	int rollno;
	int marks;
	
	public Student(String name, int rollno, int marks) {
		super();
		this.name = name;
		this.rollno = rollno;
		this.marks = marks;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRollno() {
		return rollno;
	}

	public void setRollno(int rollno) {
		this.rollno = rollno;
	}

	public int getMarks() {
		return marks;
	}

	public void setMarks(int marks) {
		this.marks = marks;
	}

	@Override
	public String toString() {
		return "Student [name=" + name + ", rollno=" + rollno + ", marks="
				+ marks + "]";
	}
}