package open;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * 1101128958
 * Adding into batch subscriber : 1104641690
 */

public class InsertIntoVM_missedcalls_table {

	static String ss = "jdbc:mysql://172.16.30.218:3306/mca_rupesh";

	
	public static void main(String[] args) {
		
		long startTime  = System.currentTimeMillis();
		
		System.out.println("Start Time : "+getCompleteDate(System.currentTimeMillis()));
		
		boolean insert = false;
		insert = true;
		
		if(insert){
			insertIntoVM_MISSEDCALLS();
		}else{
			deleteRecordsFromvm_missedcalls();
		}
		
		
		//deleteRecordsFromvm_missedcalls_mca();
				
		//insertIntovm_missedcalls_mca();
		
		System.out.println("End Time : "+getCompleteDate(System.currentTimeMillis()));
		
		System.out.println("Time taken in Minutes : "+getTimeInHHMMSSFormat((System.currentTimeMillis()-startTime)));
	}
	
	public static void deleteRecordsFromvm_missedcalls(){
		
     Connection con;
   	 Statement stmt = null; 
   		
        try {
           Class.forName("org.mariadb.jdbc.Driver");
           con = DriverManager.getConnection(ss,"root","onmobile");
           
           //String insertQuery = "insert into vm_missedcalls (SUBSCRIBER,CALLER,CALLEDTIME,PROCESSED,REASON) values (?,'2222222222',sysdate(),'N','U')";
      		
      		String deleteQuery = "delete from vm_missedcalls";
      		
      		stmt = con.createStatement();
      		stmt.execute(deleteQuery);

        } catch (SQLException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
              System.out.println(e.toString());
        } catch (ClassNotFoundException e) {
              // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println(e.toString());
        }finally{
       	 closeStatement(stmt);
        }
		
	}
	
	public static void deleteRecordsFromvm_missedcalls_mca(){
		
	     Connection con;
	   	 Statement stmt = null; 
	   		
	        try {
	           Class.forName("org.mariadb.jdbc.Driver");
	           con = DriverManager.getConnection(ss,"root","onmobile");
	           
	           //String insertQuery = "insert into vm_missedcalls (SUBSCRIBER,CALLER,CALLEDTIME,PROCESSED,REASON) values (?,'2222222222',sysdate(),'N','U')";
	      		
	      		String deleteQuery = "delete from vm_missedcalls_mca";
	      		
	      		stmt = con.createStatement();
	      		stmt.execute(deleteQuery);

	        } catch (SQLException e) {
	              // TODO Auto-generated catch block
	              e.printStackTrace();
	              System.out.println(e.toString());
	        } catch (ClassNotFoundException e) {
	              // TODO Auto-generated catch block
	            e.printStackTrace();
	            System.out.println(e.toString());
	        }finally{
	       	 closeStatement(stmt);
	        }
			
		}
	
	public static void insertIntoVM_MISSEDCALLS(){
				
         Connection con;
    	 PreparedStatement pStmt = null;  
	     String begin = "BEGIN";
         //String end = "END";// not required

    		
         try {
            Class.forName("org.mariadb.jdbc.Driver");
            con = DriverManager.getConnection(ss,"root","onmobile");
                   		
       		String insertQuery = "insert into vm_missedcalls (SUBSCRIBER,CALLER,CALLEDTIME,PROCESSED,REASON) values (?,'2222222222',sysdate(),'N','U')";
       		
			//pStmt = con.prepareStatement(insertQuery);
               
            String query;
                       
       		StringBuilder subsStart = new StringBuilder("11");
       		String subscriber;
       		
       		for(int i = 0 ;i<1;i++){
       			for(int j = 0 ;j<1;j++){
       				for(int k = 0 ;k<1;k++){
       					
       					// inserting 20000 records
       					for(int l = 0 ;l<2;l++){
       						for(int m = 0 ;m<10;m++){

       							pStmt = con.prepareStatement(insertQuery);
       			                //pStmt.execute(begin); // at each 1000 insertion

       							
       							for(int n = 0 ;n<10;n++){
       								for(int o = 0 ;o<10;o++){
       									for(int p = 0 ;p<10;p++){
       										subscriber = subsStart.append(i).append(j).append(k).append(l).append(m).append(n).append(o).append(p).toString();
       										
       										pStmt.setString(1, subscriber);
       										
       										pStmt.execute();

       										subsStart = new StringBuilder("11");
       										
       										System.out.println("Adding into batch subscriber : "+subscriber);
       									}
       								}
       							}
       							//pStmt.execute(begin); // at each 1000 insertion
       							
       							try {
									Thread.sleep(30000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
       						}
       						
       					}
       				}
       			}
       		}
	       		
	       		/*System.out.println("Before executing batch ..........................");
	    		System.out.println("Execute Batch Start Time : "+getCompleteDate(System.currentTimeMillis()));
				int arr[] = pStmt.executeBatch();   
	    		System.out.println("Execute Batch End Time : "+getCompleteDate(System.currentTimeMillis()));
	       		System.out.println("After executing batch ..........................");*/

               
         } catch (SQLException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
               System.out.println(e.toString());
         } catch (ClassNotFoundException e) {
               // TODO Auto-generated catch block
             e.printStackTrace();
             System.out.println(e.toString());
         }finally{
        	 closeStatement(pStmt);
         }
		
	}

	
	public static void insertIntovm_missedcalls_mca(){
		
        Connection con;
   	 PreparedStatement pStmt = null; 
   		
        try {
           Class.forName("org.mariadb.jdbc.Driver");
           con = DriverManager.getConnection(ss,"root","onmobile");
           
           //String insertQuery = "insert into vm_missedcalls (SUBSCRIBER,CALLER,CALLEDTIME,PROCESSED,REASON) values (?,'2222222222',sysdate(),'N','U')";
      		
      		String insertQuery = "insert into vm_missedcalls_mca (SUBSCRIBER,CALLER,CALLEDTIME,REASON,PROCESSED) values (?,'2222222222',sysdate(),'U','U')";
      		
			pStmt = con.prepareStatement(insertQuery);
              
              String query;
                           
      		
	       		StringBuilder subsStart = new StringBuilder("11");
	       		String subscriber;
	       		
	       		for(int i = 0 ;i<1;i++){
	       			for(int j = 0 ;j<1;j++){
	       				for(int k = 0 ;k<10;k++){
	       					for(int l = 0 ;l<4;l++){
	       						for(int m = 0 ;m<10;m++){
	       							for(int n = 0 ;n<10;n++){
	       								for(int o = 0 ;o<10;o++){
	       									for(int p = 0 ;p<10;p++){
	       										subscriber = subsStart.append(i).append(j).append(k).append(l).append(m).append(n).append(o).append(p).toString();
	       										
	       										pStmt.setString(1, subscriber);

	       										subsStart = new StringBuilder("11");
	       										pStmt.execute();
	       										
	       										System.out.println("Adding into batch subscriber : "+subscriber);
	       									}
	       								}
	       							}
	       						}
	       					}
	       				}
	       			}
	       		}
	       		
	       		/*System.out.println("Before executing batch ..........................");
	    		System.out.println("Execute Batch Start Time : "+getCompleteDate(System.currentTimeMillis()));
				int arr[] = pStmt.executeBatch();   
	    		System.out.println("Execute Batch End Time : "+getCompleteDate(System.currentTimeMillis()));
	       		System.out.println("After executing batch ..........................");*/

              
        } catch (SQLException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
              System.out.println(e.toString());
        } catch (ClassNotFoundException e) {
              // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println(e.toString());
        }finally{
       	 closeStatement(pStmt);
        }
		
	}
	
	public static String getTimeInHHMMSSFormat(long timeinmillis){
		String messageInterval;
		System.out.println("timeinmillis : "+timeinmillis);
		long noOfSeconds = timeinmillis/1000;
		long noOfMinutes = noOfSeconds/60;
		long noOfHours = 0;
		noOfSeconds = noOfSeconds % 60;
		
		if(noOfMinutes >= 60){
			noOfHours = noOfMinutes/60;
			noOfMinutes = noOfMinutes%60;
			
			messageInterval = ""+noOfHours+" hours "+noOfMinutes+" minutes";
		}else{
			messageInterval = ""+noOfMinutes+" minutes"; 
		}
		
		if(noOfSeconds>0){
			messageInterval = ""+noOfHours+" hours "+noOfMinutes+" minutes "+noOfSeconds+" seconds ";
		}
		
		return messageInterval;
	}
	
	public static void closeStatement(Statement stmt){
		try{
			if(stmt != null){
				stmt.close();
			}
		}catch(SQLException sqlEx){

		}
	}
	
	public static void closeResultSet(ResultSet rs){
		try{
			if(rs != null){
				rs.close();
			}
		}catch(SQLException sqlEx){
			
		}
	}
	
    public static String getCompleteDate(long millis){
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String strDate = sdf.format(new Date(millis));
				
		return strDate;
	}	
}
