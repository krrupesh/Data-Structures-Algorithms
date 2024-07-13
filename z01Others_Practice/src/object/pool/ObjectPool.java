package object.pool;

import java.sql.Connection;
import java.util.Enumeration;
import java.util.Hashtable;

// ObjectPool Class

public abstract class ObjectPool {

	private long expirationTime;

	/*
	 * what is locked and unlocked
	 */
	private Hashtable<Connection, Long> locked, unlocked;

	public ObjectPool() {
		expirationTime = 30000; // 30 seconds
		locked = new Hashtable<Connection, Long>();
		unlocked = new Hashtable<Connection, Long>();
	}

	protected abstract Connection create();

	public abstract boolean isConnectionAvailable(Connection con);

	public abstract void expire(Connection con);

	public synchronized Connection getConnection() {

		long now = System.currentTimeMillis();
		Connection con;

		if (unlocked.size() > 0) {
			Enumeration<Connection> e = unlocked.keys();
			while (e.hasMoreElements()) {
				con = e.nextElement();
				if ((now - unlocked.get(con)) > expirationTime) {
					// object has expired
					unlocked.remove(con);
					expire(con); // close Connection
					con = null;
				} else {
					if (isConnectionAvailable(con)) { // true if Connection is
														// open
						unlocked.remove(con);
						locked.put(con, now);
						return (con);
					} else {
						// object failed validation
						unlocked.remove(con);
						expire(con);
						con = null;
					}
				}
			}
		}
		// no objects available, create a new one
		System.out.println("No objects available, creating a new one !!");
		con = create();
		locked.put(con, now);

		System.out.println("getConnection unlocked : " + unlocked);
		System.out.println("getConnection locked : " + locked);

		return (con);
	}

	public synchronized void releaseConnection(Connection t) {
		System.out.println("releaseConnection unlocked : " + unlocked);
		System.out.println("releaseConnection locked : " + locked);
		locked.remove(t);
		unlocked.put(t, System.currentTimeMillis());
	}
}