package object.pool.fixed.no;

import java.sql.Connection;
import java.sql.SQLException;
//The three remaining methods are abstract 
//and therefore must be implemented by the subclass
import java.sql.DriverManager;
import java.util.Date;

public class JDBCConnectionPool extends ObjectPool {

  private String dsn, usr, pwd;

  public JDBCConnectionPool(String driver, String dsn, String usr, String pwd) {
    super();
    try {
      Class.forName(driver).newInstance();
    } catch (Exception e) {
      e.printStackTrace();
    }
    this.dsn = dsn;
    this.usr = usr;
    this.pwd = pwd;
  }

  /*
   * create the Connection Object
   */
  @Override
  protected Connection create() {
    try {
      return (DriverManager.getConnection(dsn, usr, pwd));
    } catch (SQLException e) {
      e.printStackTrace();
      return (null);
    }
  }

  /*
   * close the Connection Object
   */
  @Override
  public void expire(Connection o) {
    try {
      ((Connection) o).close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /*
   * Validates if Connection Object is closed or open, if already closed then returns false else true
   */
  @Override
  public boolean isConnectionAvailable(Connection o) {
    try {
      return (!((Connection) o).isClosed());
    } catch (SQLException e) {
      e.printStackTrace();
      return (false);
    }
  }

	public void getNoOFObjects() {
		System.out.println("No of Live Objects : "+locked.size()+" "+new Date());		
		System.out.println("No of Unsed Objects : "+unlocked.size());
	}
}