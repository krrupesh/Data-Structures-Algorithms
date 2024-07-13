package object.pool.fixed.no;

import java.sql.Connection;
import java.util.Enumeration;
import java.util.Hashtable;

// ObjectPool Class

public abstract class ObjectPool {
	
  private long expirationTime;
  public static final int MAX_NO_OF_CONNECTIONS = 10;

  /*
   * what is locked and unlocked
   */
  public Hashtable<Connection, Long> locked, unlocked;

  public ObjectPool() {
    expirationTime = 10000; // 30 seconds,  change this and test 
    locked = new Hashtable<Connection, Long>();
    unlocked = new Hashtable<Connection, Long>();
  }

  protected abstract Connection create();

  public abstract boolean isConnectionAvailable(Connection con);

  public abstract void expire(Connection con);

  public synchronized Connection getConnection() {

    long now = System.currentTimeMillis();
    Connection con;
    
    if((locked.size() + unlocked.size()) <= MAX_NO_OF_CONNECTIONS){
    	 if (unlocked.size() > 0) {
    	      Enumeration<Connection> e = unlocked.keys();
    	      while (e.hasMoreElements()) {
    	        con = e.nextElement();
    	        if ((now - unlocked.get(con)) > expirationTime) {
    	          // object has expired
    	          unlocked.remove(con);
    	          expire(con); // close Connection
    	          con = null;
    	        }else{
    	          if(isConnectionAvailable(con)) { // true if Connection is open
    	            unlocked.remove(con);
    	            locked.put(con, now);
    	            return (con);
    	          }else {
    	            // object failed validation
    	            unlocked.remove(con);
    	            expire(con);
    	            con = null;
    	          }
    	        }
    	      }
    	    }

    	 	//System.out.println("No objects available, creating a new one !!");
    	    con = create();
    	    locked.put(con, now);
    	    
    	    return (con);
    }else{
    	System.out.println("Max Connection Reached !!");
    	return null;
    }  
  }

  public synchronized void releaseConnection(Connection t) {
    locked.remove(t);
    unlocked.put(t, System.currentTimeMillis());
  }
}