package com.company;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class dbServiceTest {
    dbService db = dbService.TESTINSTANCE;

    @Test
    @DisplayName("1.Testing select method from dbService")
    public void select() {
        ArrayList<String> al;
        try {
            db.stmt.executeUpdate("DELETE FROM weather");
            db.stmt.executeUpdate("INSERT INTO weather SET id = null , city = 'Moscow', windSpeed = 2, pressure = 760, humidity = 15, date= '2018-05-01', time= '00:02:20';");
            al = db.select("select * from weather");
            assertEquals(1, al.size());
            db.stmt.executeUpdate("DELETE FROM weather");
            al = db.select("select * from weather");
            assertEquals(0, al.size());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("2.Testing insert method from dbService")
    public void insert() {
        try {
            db.stmt.executeUpdate("DELETE FROM weather;");
            db.insert("Moscow", 1.2f, 15, 760, 88, "2018-04-20", "12:20:23");
            db.rs = db.stmt.executeQuery("SELECT * FROM weather;");
            assertTrue(db.rs.next());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("3.Testing delete method from dbService")
    public void delete() {
        db.delete();
        try {
            db.rs = db.stmt.executeQuery("select * from weather");
            assertFalse(db.rs.next());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}