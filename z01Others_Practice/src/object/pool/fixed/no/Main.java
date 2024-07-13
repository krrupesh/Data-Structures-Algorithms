package object.pool.fixed.no;

import java.sql.Connection;
import java.util.Timer;

public class Main {
	
  public static final int ONE_SECOND = 1000;	
	
  public static void main(String args[]) {
	  
    // Create the ConnectionPool:
    JDBCConnectionPool pool = new JDBCConnectionPool("org.mariadb.jdbc.Driver", "jdbc:mariadb://localhost/mca","root", "root");
	
    Timer timer = new Timer();
	NoOfConnectionChecker noc = new NoOfConnectionChecker(pool); 
	timer.scheduleAtFixedRate(noc, 0, 2000);

	createThread(pool,1*ONE_SECOND,0);
	createThread(pool,4*ONE_SECOND,1*ONE_SECOND);
	createThread(pool,10*ONE_SECOND,1*ONE_SECOND);
	createThread(pool,12*ONE_SECOND,1*ONE_SECOND);
	createThread(pool,13*ONE_SECOND,1*ONE_SECOND);
	createThread(pool,15*ONE_SECOND,1*ONE_SECOND);
	createThread(pool,17*ONE_SECOND,1*ONE_SECOND);
	createThread(pool,20*ONE_SECOND,1*ONE_SECOND);
	createThread(pool,30*ONE_SECOND,1*ONE_SECOND);

  }

	private static void createThread(JDBCConnectionPool pool, int sleepDuration, int sleepBefore) {
		MyThread mythread = new MyThread(pool,sleepDuration,sleepBefore);
		mythread.start();
	}
}