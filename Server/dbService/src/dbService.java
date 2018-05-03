package com.company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public enum dbService {
    INSTANCE,TESTINSTANCE("Test database connected");

    private  String url;
    private  String user;
    private  String password;
    public ResultSet rs;

    private  Connection con;
    public  Statement stmt;

     dbService(){
        try{
            this.url="jdbc:mysql://localhost:3306/weather";
            this.user="root";
            this.password="root";
            this.con= DriverManager.getConnection(url, user, password);
            this.stmt= con.createStatement();
        } catch (SQLException sqlEx) {
                System.out.println("Can't connect to Data Base");
                sqlEx.printStackTrace();
        }
    }
    dbService(String MOTD){
        try{
            this.url="jdbc:mysql://localhost:3306/test";
            this.user="root";
            this.password="root";
            this.con= DriverManager.getConnection(url, user, password);
            this.stmt= con.createStatement();
        } catch (SQLException sqlEx) {
            System.out.println("Can't connect to Data Base");
            sqlEx.printStackTrace();
        }
        System.out.println(MOTD);
    }

    public void close () {
            //close connection ,stmt  here
            try { con.close(); } catch(SQLException se) { /*can't do anything */ }
            try { stmt.close(); } catch(SQLException se) { /*can't do anything */ }
    }

    public ArrayList<String> select(String inQuery) {
        ArrayList<String> formStr =new ArrayList<>(50);
        try {
            rs=stmt.executeQuery(inQuery);
            while (rs.next()) {
                int id = rs.getInt(1);
                String city = rs.getString(2);
                float windSpeed = rs.getFloat(3);
                int temperature = rs.getInt(4);
                int pressure = rs.getInt(5);
                int humidity = rs.getInt(6);
                String date = rs.getString(7);
                String time = rs.getString(8);
                formStr.add(String.format("id: %d, city: %s, windSpeed: %.1f, temperature: %d, pressure: %d, humidity: %d, date: %s, time: %s %n", id, city, windSpeed, temperature, pressure,humidity, date, time));
            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
        return formStr;
    }

    public void insert(String city, float windSpeed, int temperature, int pressure,int humidity, String date, String time) {

        try {
            String inQuery = "INSERT INTO weather ( id , city, windSpeed, temperature, pressure,humidity, date, time) VALUES (null, '" + city + "', '" + windSpeed + "', '"+temperature + "', '" + pressure +"', '" +humidity + "', '"+ date + "', '" + time +"');";
            stmt.executeUpdate(inQuery);
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }

    }

    public void delete() {
        try {
            String inQuery = "DELETE FROM weather;";
            stmt.executeUpdate(inQuery);
            inQuery="ALTER TABLE weather AUTO_INCREMENT=0;";
            stmt.executeUpdate(inQuery);
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }

    }

}
