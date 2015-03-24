package jdbc;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
 
import javax.sql.DataSource;

public class DataSourceTest {
	
	public static void main(String[] args) {        
        testDataSource();       
    }
 
    private static void testDataSource() {
        DataSource ds = null;
        ds = DataSourceFactory.getMySQLDataSource();
         
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            con = ds.getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery("select * from user_ct_test_collection_01 Limit 10;");
            while(rs.next()){
                //System.out.println("Employee ID="+rs.getInt("empid")+", Name="+rs.getString("name"));
                System.out.println(rs.getInt("userId") +" " +  rs.getString("query"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
                try {
                    if(rs != null) rs.close();
                    if(stmt != null) stmt.close();
                    if(con != null) con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
    }

}
