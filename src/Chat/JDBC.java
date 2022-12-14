package Chat;
import java.sql.*;

public class JDBC {
	static Connection conn = null;
	
	public static Connection connection() {	
		try {
			final String url = "jdbc:mysql://localhost/talk";
		    final String user = "root";
		    final String passwd = "2413ys";
			
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			conn = DriverManager.getConnection(url, user, passwd);
		
		} catch (ClassNotFoundException cvfe) {
			System.out.println("DB Driver Loading Fail..");
		} catch (SQLException sqle) {
			System.out.println("DB Connection failed: " + sqle.toString());
		} catch (Exception e) {
			System.out.println("Unkown error");
			e.printStackTrace();
		}
		
		return conn;
	}
}