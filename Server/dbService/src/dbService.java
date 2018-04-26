package com.company;

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

    private  Connection con;
    public  Statement stmt;

     dbService(){
        try{
            this.con= DriverManager.getConnection(url, user, password);
            this.stmt= con.createStatement();
        } catch (SQLException sqlEx) {
                System.out.println("Can't connect to Data Base");
                sqlEx.printStackTrace();
        }
    }

    public void close () {
            //close connection ,stmt  here
            try { con.close(); } catch(SQLException se) { /*can't do anything */ }
            try { stmt.close(); } catch(SQLException se) { /*can't do anything */ }
    }

    public String select() {
        try {
            String buf;
                int id = rs.getInt(1);
                String city = rs.getString(2);
                float windSpeed = rs.getFloat(3);
                int temperature = rs.getInt(4);
                int pressure = rs.getInt(5);
                String date = rs.getString(6);
                String time = rs.getString(7);
                buf = String.format("id: %d, city: %s, windSpeed: %.1f, temperature: %d, pressure: %d, date: %s, time: %s %n", id, city, windSpeed, temperature, pressure, date, time);
                return buf;
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
        return "";
    }

    public void insert(String city, float windSpeed, int temperature, int pressure, String date, String time) {

        try {
            String inQuery = "INSERT INTO weather ( id , city, windSpeed, temperature, pressure, date, time) VALUES (null, '" + city + "', '" + windSpeed + "', '"+temperature + "', '" + pressure +"', '" + date + "', '" + time +"');";
            stmt.executeUpdate(inQuery);
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }

    }

    public void delete() {
        try {
            String inQuery = "DELETE FROM weather;";
            stmt.executeUpdate(inQuery);
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }

    }

}
