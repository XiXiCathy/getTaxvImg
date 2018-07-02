package DBUtilsTax;

import java.sql.*;
public class JdbcProvider implements IConnectionProvider {
	private String DBDriver;
    private String DBUrl;
    private String username;
    private String password;
    private String DBUrlFinal;
    
    public JdbcProvider(String DBDriver,String DBUrl,String username,String password) throws ClassNotFoundException {
        this.DBDriver = DBDriver;
        this.DBUrl = DBUrl;
        this.username = username;
        this.password = password;
        Class.forName(DBDriver);
    }

	@Override
	public Connection getConnection(String sourceName) throws SQLException {
		//DBUrl = DBUrl + sourceName;
		DBUrlFinal = DBUrl + sourceName;
		System.out.println(DBUrlFinal);
		return DriverManager.getConnection(DBUrlFinal,username,password);
	}
	
	public String getDBUrl() {
        return DBUrl;
    }
    public void setDBUrl(String dBUrl) {
        DBUrl = dBUrl;
    }
    public String getDBDriver() {
        return DBDriver;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
}
