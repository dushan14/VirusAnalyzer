/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virusanalyzer.database;

import com.ibatis.common.jdbc.ScriptRunner;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.util.Scanner;
import virusanalyzer.VirusDetails;

/**
 *
 * @author Dushan
 */
public class DBClass {

    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/";

    //  Database credentials
    public static String USER;
    public static String PASS;
    
    static String DB_NAME = "VirusData140323F"; //databaseWithMyIndex

    private static void createDatabase() {
        Connection conn = null;
        Statement stmt = null;
        try {

            Class.forName("com.mysql.jdbc.Driver");//STEP 2: Register JDBC driver

            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS); //STEP 3: Open a connection

            System.out.println("Creating database...");
            stmt = conn.createStatement();//STEP 4: Execute a query
            String sql = "CREATE DATABASE " + DB_NAME;
            stmt.executeUpdate(sql);

            System.out.println("Database created successfully...");

        } catch (SQLException | ClassNotFoundException se) {

        } finally {

            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
            }
        }
        System.out.println();
    }

    private static void insertData() throws ClassNotFoundException,
            SQLException {

        String aSQLScriptFilePath = "vx.sql";

     
        Class.forName("com.mysql.jdbc.Driver");
        com.mysql.jdbc.Connection con = (com.mysql.jdbc.Connection) DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/" + DB_NAME, USER, PASS);
        com.mysql.jdbc.Statement stmt = null;

        try {
           
            ScriptRunner sr = new ScriptRunner(con, false, false);

            
            Reader reader = new BufferedReader(
                    new FileReader(aSQLScriptFilePath));

       
            sr.runScript(reader);

        } catch (Exception e) {
            System.err.println("Failed to Execute" + aSQLScriptFilePath
                    + " The error is " + e.getMessage());
        }
    }

    public static VirusDetails readFromDatabase(String md5) throws UnsupportedEncodingException {
        Connection conn = null;
        Statement stmt = null;
        VirusDetails vd =  vd = new VirusDetails();
        
        
        try {
            
            Class.forName("com.mysql.jdbc.Driver");

            
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            stmt = conn.createStatement();
            String sqls;
            sqls = "use " + DB_NAME;
            stmt.executeQuery(sqls);
            System.out.println("Selected..");

         
            System.out.println("Creating statement...");
            stmt = conn.createStatement();
            String sql;
            
//            String testVirus="045c989eb036bdbc44e72f27608965ed";
            
            
            
            sql = "SELECT * FROM vx WHERE vxMD5='" +md5+ "'";
            ResultSet rs = stmt.executeQuery(sql);

            
           
           
            while (rs.next()) {
                vd.tID = rs.getInt(1);
                vd.vxCount = rs.getInt(2);
                vd.vxTotal = rs.getInt(3);
                vd.vxVirusName = rs.getString(4);

                vd.vxMD5 = rs.getString(5);
                vd.vxSHA1 = rs.getString(6);
                vd.vxSHA256 = rs.getString(7);
                vd.vxDate = rs.getString(8);
                vd.vxEngine = rs.getString(9);
                vd.vxAwareness = rs.getInt(10);
                vd.vxType = rs.getString(11);
                vd.vxTypeImage = rs.getString(12);
                vd.vxTimeStamp = rs.getString(13);
            }

            rs.close();
            stmt.close();
            conn.close();
            return vd;
        } catch (SQLException | ClassNotFoundException se) {

        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
            }
        }
        return vd;

    }

    public static boolean setDatabase(String username, String password) throws FileNotFoundException, IOException, ClassNotFoundException, SQLException {

        USER = username;
        PASS = password;

        boolean isCreated;

        new FileOutputStream("config.txt", true).close(); //create is not exist

        Scanner in = new Scanner(new FileReader("config.txt"));
        int value = 0;

        if (in.hasNext()) {
            value = in.nextInt();
        }
        System.out.println(value);
        if (value == 1) {
//            dont create or insert into database
//            already created
            System.out.println("not creating db");
            isCreated = false;
        } else {
            System.out.println("creating database");
            DBClass.createDatabase();
            DBClass.insertData();
            PrintWriter writer = new PrintWriter("config.txt", "UTF-8");
            writer.println("1");
            isCreated = true;
            writer.close();
        }
        return isCreated;
    }

    public static void forceSetupDatabase(String username, String password) throws FileNotFoundException, IOException, ClassNotFoundException, SQLException {

        USER = username;
        PASS = password;
        new FileOutputStream("config.txt", true).close(); //create is not exist
        System.out.println("creating database");
        DBClass.createDatabase();
        DBClass.insertData();
        PrintWriter writer = new PrintWriter("config.txt", "UTF-8");
        writer.println("1");

    }

}
