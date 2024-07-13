package object.pool.fixed.no;

import java.util.TimerTask;


public class NoOfConnectionChecker extends TimerTask {

	JDBCConnectionPool pool;
	public NoOfConnectionChecker(JDBCConnectionPool pool) {
		this.pool = pool;
	}

	@Override
	public void run() {
		pool.getNoOFObjects();
	}

}
