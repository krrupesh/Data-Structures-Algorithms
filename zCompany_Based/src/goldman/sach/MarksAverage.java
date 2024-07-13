package goldman.sach;

import java.util.HashMap;
import java.util.*;

public class MarksAverage {

	public static void main(String[] args) {

		String [][]marksDetails = {{"Rupesh1","100"},
								   {"Rupesh2","200"},
								   {"Rupesh3","100"},
								   {"Rupesh1","100"},
								   {"Rupesh1","400"},
								   {"Rupesh2","400"}};
		
		printMarks(marksDetails);
	}
	
	public static void printMarks(String [][]marksDetails){
		
		String name;
		int marks;
		Map<String,Marks> marksMap = new HashMap<String,Marks>();
		float maxAverageMarks = 0.0F;
		
		
		for(int i =0;i<marksDetails.length;i++){
			name = marksDetails[i][0];
			marks = Integer.parseInt(marksDetails[i][1]);
			
			if(marksMap.containsKey(name)){
				marksMap.get(name).totalMarks = marksMap.get(name).totalMarks + marks;
				marksMap.get(name).count = marksMap.get(name).count + 1;
			}else{
				marksMap.put(name, new Marks(name,marks));
			}
		}
		
		for(Map.Entry<String, Marks> mark : marksMap.entrySet()){
			System.out.println(mark.getKey()+"  ,"+mark.getValue().getAverageMarks());
			
			if(maxAverageMarks < mark.getValue().getAverageMarks() ){
				maxAverageMarks = mark.getValue().getAverageMarks();
			}
		}
		
		
		System.out.println("maxAverageMarks : "+maxAverageMarks);
		
		
		/*OptionalDouble average = Arrays.stream(marksDetails)
                .mapToDouble(l -> Double.parseDouble(l[1])).average();*/
		
	}
	
	
	
	
	static class Marks{
		String name;
		int totalMarks;
		int count = 1;
		float averageMarks;
		
		public Marks(String name, int totalMarks) {
			super();
			this.name = name;
			this.totalMarks = totalMarks;
		}



		public float getAverageMarks() {
			return totalMarks/count;
		}
	
	}

}
