package ritesh;

public class P_10 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	    int i=0,j=0,c=0;
	    String st = "Zoho Corp";
	    char str[]=st.toCharArray();
	    while(str[i]!='\0'){
	        j=i;
	        while((str[j]!=' ')&&(str[j]!='\0')){
	            j++;
	            
	        }
	        if(j-- ==' ')c++;
	        i=j+1;
	    }
	    
	    System.out.println(c);
	}

}
