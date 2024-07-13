package newhiresetB.print.words.inreverseorder;

public class PrintWordsInReverseOrder {

	public static void main(String[] args) {

		String str = "This    is a    dcmd;lck ldm c;lkd c   book";
		System.out.println(str+" "+str.length());

		String reverse = printWordsInReverseOrder(str);
		
		System.out.println(reverse+" "+reverse.length());
	}

	public static String printWordsInReverseOrder(String str){
		
		if((str == null) ||(str.length()==0)){
			return null;
		}
		
		String strArray[] = str.split(" ");
		StringBuffer sb1 = new StringBuffer();

		for(int i=0;i<strArray.length;i++){
			StringBuffer sb = new StringBuffer(strArray[i]);
			if (i == strArray.length - 1) {
				sb1.append(sb.reverse());
			}else{
				sb1.append(sb.reverse()+" ");
			}
		}
				
		return sb1.toString();
	}
	
}
