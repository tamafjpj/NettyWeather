import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



public enum dbService {
    INSTANCE;

    private final String url = "jdbc:mysql://localhost:3306/weather";
    private  final String user = "root";
    private  final String password = "root";
    public ResultSet rs;
    public int testVar;

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

    public void close () {
            //close connection ,stmt and resultset here
            try { con.close(); } catch(SQLException se) { /*can't do anything */ }
            try { stmt.close(); } catch(SQLException se) { /*can't do anything */ }
            try { rs.close(); } catch(SQLException se) { /*can't do anything */ }
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
            } else if ("select * from weather order by id desc limit 1;".equals(inQuery)) {
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
        }
    }

    public void insert (String city, int windSpeed, int temperature, int pressure, String date, String time) {
        System.out.println("INSERT INTO weather ( id , city, windSpeed, temperature, pressure, date, time) VALUES (null, '" + city + ", '" + windSpeed + "', '"+temperature + "', '" + pressure +"', '" + date + "', '" + time +"');");
        try {
            String inQuery = "INSERT INTO weather ( id , city, windSpeed, temperature, pressure, date, time) VALUES (null, '" + city + "', '" + windSpeed + "', '"+temperature + "', '" + pressure +"', '" + date + "', '" + time +"');";
            stmt.executeUpdate(inQuery);
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }

    }
    public static void main(String[] args) {
       dbService db = dbService.INSTANCE;
       
    }
}
