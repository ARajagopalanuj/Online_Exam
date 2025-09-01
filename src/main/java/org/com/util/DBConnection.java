package org.com.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {


    private static final String user="postgres";

    private static final String password="Udhayajana@2002";

    private static final String url="jdbc:postgresql://localhost:5432/onexam";



    public static Connection getDbConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("PostgreSQL Driver not found!", e);
        }

        return DriverManager.getConnection(url,user,password);
    }
}


