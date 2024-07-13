package object.pool.fixed.no;

import java.sql.Connection;

public class MyThread extends Thread {

	
	JDBCConnectionPool pool; 
	int sleepDuration;
	int sleepBefore;
	
	public MyThread(JDBCConnectionPool pool, int sleepDuration, int sleepBefore) {
		this.pool = pool;
		this.sleepDuration = sleepDuration;
		this.sleepBefore =  sleepBefore;
	}

	public void run(){
		
		try {
			Thread.sleep(sleepBefore);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Connection con = pool.getConnection();
		
		try {
			Thread.sleep(sleepDuration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		pool.releaseConnection(con);
		
	}
}
