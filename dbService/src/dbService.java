import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



public class dbService {
    private final String url = "jdbc:mysql://localhost:3306/weather";
    private  final String user = "root";
    private  final String password = "root";
    public ResultSet rs;

    private  Connection con;
    private  Statement stmt;
    private dbService(){
        try{
            this.con= DriverManager.getConnection(url, user, password);
            this.stmt= con.createStatement();
        } catch (SQLException sqlEx) {
                sqlEx.printStackTrace();
        }
    }


    public void select(String inQuery) {
        try {
            inQuery=inQuery.toLowerCase();
            System.out.println(inQuery);
            this.rs = stmt.executeQuery(inQuery);
            if ("select * from weather".equals(inQuery)) {
                while (rs.next()) {
                    int id = rs.getInt(1);
                    String city = rs.getString(2);
                    int windSpeed = rs.getInt(3);
                    int temperature = rs.getInt(4);
                    int pressure = rs.getInt(5);
                    String date = rs.getString(6);
                    String time = rs.getString(7);
                    System.out.printf("id: %d, city: %s, windSpeed: %d, temperature: %d, pressure: %d, date: %s, time: %s %n", id, city, windSpeed, temperature, pressure, date, time);
                }
            }


        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        } finally {
            //close connection ,stmt and resultset here
            try { con.close(); } catch(SQLException se) { /*can't do anything */ }
            try { stmt.close(); } catch(SQLException se) { /*can't do anything */ }
            try { rs.close(); } catch(SQLException se) { /*can't do anything */ }
        }
    }

    public void insert (String query) {

    }
    public static void main(String[] args) {
        dbService db = new dbService();
        db.select("SELECT * FROM weather");
    }
}
