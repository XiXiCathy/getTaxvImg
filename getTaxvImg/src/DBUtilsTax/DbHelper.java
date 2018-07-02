package DBUtilsTax;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;



public class DbHelper {
	private Connection connection;
	private PreparedStatement ps;
	private ResultSet rs;
	private String sourceName;
	private IConnectionProvider connectionProvider;
	CallableStatement cst;

	public DbHelper(IConnectionProvider connectionProvider, String sourceName) {
		this.connectionProvider = connectionProvider;
		this.sourceName = sourceName;
	}

	public Connection getConnection() throws SQLException {
		if (sourceName == null)
			throw new SQLException("source name is null.");
		int Times = 0;
		while (connection == null || connection.isClosed()) {
			try {
				closeConnection();
				connection = connectionProvider.getConnection(sourceName);
				break;
			} catch (Exception sqle) {
				sqle.printStackTrace();
			} finally {
				if (Times > 5) {
					throw new SQLException("Attempt to connect 6 failed,no more attempts to connect.");
				}
				++Times;
			}
		}

		return connection;
	}

	public Object findObject(String sql, Object... objs) throws SQLException {
		try {
			getConnection();
			ps = connection.prepareStatement(sql);
			if (objs != null) {
				for (int i = 0; i < objs.length; i++)
					ps.setObject(i + 1, objs[i]);
			}
			rs = ps.executeQuery();
			if (rs.next())
				return rs.getObject(1);
			else
				return null;
		} finally {
			close();
		}
	}

	public ResultSet findById(String sql, Object id) throws Exception {
		getConnection();
		ps = connection.prepareStatement(sql,
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		if (id != null) {
			ps.setObject(1, id);
		}
		return ps.executeQuery();
	}

	public ResultSet query(String sql, Object... objs) throws Exception {
		getConnection();
		ps = connection.prepareStatement(sql,
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		if (objs != null) {
			for (int i = 0; i < objs.length; i++)
				ps.setObject(i + 1, objs[i]);
		}
		return ps.executeQuery();
	}

	public int insertAndReturnKey(String sql, Object... objs)
			throws SQLException {
		int countRow = 0;
		int key = 0;
		try {
			getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement(sql,
					Statement.RETURN_GENERATED_KEYS);
			if (objs != null) {
				for (int i = 0; i < objs.length; i++)
					ps.setObject(i + 1, objs[i]);
			}
			countRow = ps.executeUpdate();
			if (countRow > 0) {
				ResultSet rs = ps.getGeneratedKeys();
				if (rs.next())
					key = rs.getInt(1);
			}
			connection.commit();
		} catch (SQLException e) {
			countRow = 0;
			connection.rollback();
			closeConnection();
			throw e;
		} finally {
			if (connection != null) {
				connection.setAutoCommit(true);
			}
			close();
		}
		//return key;
		return countRow;
	}


	/**
	 * @param sql 閹绘帒鍙唌ysql鐠囶厼褰�
	 * @param betch_data 闂囷拷鐟曚焦褰冮崗銉ф畱閺佺増宓侀敍灞剧槨娑擄拷娑擃亜鍘撶槐鐘虫Ц娑擄拷娑擃亜绶熼幓鎺戝弳閻ㄥ嫯顢�
	 * @return
	 * @throws SQLException
	 */
	public int insertBatch(String sql, ArrayList<ArrayList<String>> betch_data)
			throws SQLException {
		try {
			getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement(sql);
			
			final int data_count = betch_data.size();
			
			int num = 0;
			for(int k = 0;k < data_count;k++)
			{
				num += 1;
				
				ArrayList<String> cand_data = betch_data.get(k);
				
				for (int m = 0;m < cand_data.size();m++)
				{
					ps.setString(m+1, cand_data.get(m));
				}
				
				ps.addBatch();
				
				
				if (num % 100 == 0)
				{
					ps.executeBatch();
					num = 0;
					
				}
			}
			
			ps.executeBatch();
			
			connection.commit();
			
		} catch (SQLException e) {
			connection.rollback();
			closeConnection();
			throw e;
		} finally {
			if (connection != null) {
				connection.setAutoCommit(true);
			}
			close();
		}
		return betch_data.size();
	}
	
	public int insertBatch(String sql, List<ArrayList<String>> betch_data,String dataID,String type)
			throws SQLException {
		try {
			getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement(sql);
			
			final int data_count = betch_data.size();
			String updateDate = "";
			int num = 0;
			for(int k = 0;k < data_count;k++){
				num += 1;
				
				ArrayList<String> cand_data = betch_data.get(k);
				
					
				String para1 = cand_data.get(0);
				String para2 = cand_data.get(1);
				Double para3 = null;
				try {
					para3 = Double.parseDouble(cand_data.get(2));

				} catch (Exception e) {
					para3 = 0d;
				}
				ps.setString(1,dataID);
				ps.setString(2,updateDate);
				ps.setString(3, para1);
				ps.setString(4,para2);
				ps.setString(5, type);
				ps.setDouble(6, para3);
				
				
				ps.addBatch();
				
				
				if (num % 100 == 0){
					ps.executeBatch();
					ps.clearBatch();
					num = 0;	
				}
			}
			
			ps.executeBatch();
			
			connection.commit();
			
			
		} catch (SQLException e) {
			connection.rollback();
			closeConnection();
			throw e;
		} finally {
			if (connection != null) {
				connection.setAutoCommit(true);
			}
			close();
		}
		return betch_data.size();
	}
	

	public int updatePrepareSQL(String sql, Object... objs) throws SQLException {
		int countRow = 0;
		try {
			getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement(sql);
			if (objs != null) {
				for (int i = 0; i < objs.length; i++)
					ps.setObject(i + 1, objs[i]);
			}
			//System.out.println("line:183");
			countRow = ps.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			countRow = 0;
			connection.rollback();
			closeConnection();
			throw e;
		} finally {
			if (connection != null) {
				connection.setAutoCommit(true);
			}
			close();
		}
		return countRow;
	}

	/**
	 * @param table_name  閺傛澘缂搈ysql鐞涖劌鎮曢敍灞筋洤閺嬫粏銆冪�涙ê婀崚娆忓灩闂勩倕甯張澶庛��
	 * @param column_name_list 閺傛澘缂搈ysql鐞涖劎娈戦崥鍕灙閸氾拷
	 * @return
	 * @throws SQLException
	 */
	public boolean createTableByColumnNameList(String table_name,
											
			ArrayList<String> column_name_list) throws SQLException {
		if (column_name_list.size() <= 0) {
			System.out.println("column name size is 0");
			return false;
		}

		String create_sql = "create table " + table_name + "(";
		for (int k = 0; k < column_name_list.size(); k++) {
			if (0 == k) {
				create_sql += "`" + column_name_list.get(k) + "` text";
			} else {
				create_sql += ",`" + column_name_list.get(k) + "` text";
			}
		}
		create_sql += ")";
		System.out.println("create sql:" + create_sql);

		try {
			System.out.println("create table1");
			getConnection();
			System.out.println("create table1");
			connection.setAutoCommit(false);
			System.out.println("get connection finish");
			String drop_sql = "drop table if exists " + table_name;
			ps = connection.prepareStatement(drop_sql);
			ps.executeUpdate();

			ps = connection.prepareStatement(create_sql);
			ps.executeUpdate();

			connection.commit();

			return true;
		} catch (SQLException e) {
			System.out.println(e.toString());
			closeConnection();
		}

		return false;

	}

	public void closeConnection() {
		try {
			if (connection != null) {
				connection.close();
			}
			connection = null;
		} catch (Exception e) {
		}
	}

	public void close() {
		try {
			super.finalize();
			if (rs != null) {
				rs.close();
				rs = null;
			}
			if (ps != null) {
				ps.close();
				ps = null;
			}
			if (connection != null) {
				connection.close();
				connection = null;
			}
		} catch (Throwable te) {
		}
	}
	
	public void callProcedure(String sql, Object... objs)
			throws SQLException {
		try {
			getConnection();
			connection.setAutoCommit(false);
			cst = connection.prepareCall(sql);
			if (objs != null) {
				for (int i = 0; i < objs.length; i++)
					cst.setObject(i + 1, objs[i]);
			}
//			cst.registerOutParameter(1, Types.INTEGER);//濞夈劌鍞給ut閸欏倹鏆熼惃鍕閸拷
			int count;
			
			ResultSet re = cst.executeQuery();
			/*while(re.next()){
				//count = re.getInt(1);
				System.out.println(re.getString(1));
				System.out.println(re.getString(2));
				System.out.println(re.getString(3));
				System.out.println(re.getString(4));
				System.out.println(re.getString(5));
			}*/
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			connection.rollback();
			closeConnection();
			throw e;
		} finally {
			if (connection != null) {
				connection.setAutoCommit(true);
			}
			close();
		}
	}

}
