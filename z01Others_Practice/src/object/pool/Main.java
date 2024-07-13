package object.pool;

import java.sql.Connection;

public class Main {
	
  public static void main(String args[]) {
    // Do something...

    // Create the ConnectionPool:
    JDBCConnectionPool pool = new JDBCConnectionPool("org.mariadb.jdbc.Driver", "jdbc:mariadb://localhost/mca","root", "root");

    // Get a connection:
    Connection con1 = pool.getConnection();
    System.out.println("con1 : "+con1);
    
    Connection con2= pool.getConnection();
    System.out.println("con2 : "+con2);

    // Return the connection:
    pool.releaseConnection(con1);
    pool.releaseConnection(con2);
    
    Connection con3 = pool.getConnection();
    System.out.println("con3 : "+con3);

    Connection con4 = pool.getConnection();
    System.out.println("con4 : "+con4);


 
  }
}