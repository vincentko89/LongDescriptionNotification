package com.magnetstreet.longdescriptionnotification;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LongDescriptionNotification {

    public static void main(String[] args) throws ClassNotFoundException, SQLException
    {	
    	setupDB();
		File file = new File("N:\\_communications\\Long Descriptions\\MASTER LISTS\\WEDDINGS\\MASTER LIST (WEDDINGS).xlsx");

		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	 
		System.out.println("File modification time : " + sdf.format(file.lastModified()));
		System.out.println("DB modification time : " + getModificationTime());
		
		if(!sdf.format(file.lastModified()).equals(getModificationTime())) sendEmail();

		
    }
    
    public static void setupDB() throws ClassNotFoundException, SQLException {
        Connection c = null;
        Statement stmt = null;

        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:LongDescriptionNotification.db");

        System.out.println("Opened database successfully");
        createTable(c);
        //insertEntry(c);
    }
    
    public static void createTable(Connection c) throws SQLException {
        Statement stmt = null;

    	stmt = c.createStatement();
        String sql = "CREATE TABLE IF NOT EXISTS notificationTime " +
                "(id INT PRIMARY KEY     NOT NULL," +
                " modification_time           TEXT    NOT NULL)"; 
        stmt.executeUpdate(sql);
        stmt.close();
        c.close();

        System.out.println("Table created successfully");
    }
    
    public static void insertEntry(Connection c) throws SQLException {
        
    	Statement stmt = null;
        stmt = c.createStatement();
        String sql = "INSERT INTO notificationTime (ID,modification_time) " +
                     "VALUES (1, '0');"; 
        stmt.executeUpdate(sql);    	
        System.out.println("Records created successfully");
    }
    
/*    public static void updateModificationTime() throws SQLException, ClassNotFoundException {
    	
    	Connection c = null;
        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:LongDescriptionNotification.db");
        c.setAutoCommit(false);
        
        Statement stmt = null;
        String sql = "UPDATE notificationTime set modification_time = " + getNow() + ";";
        
        stmt = c.createStatement();

        stmt.executeUpdate(sql);
        c.commit();
        System.out.println("Records updated successfully");
    }*/
    
    public static String getModificationTime() throws ClassNotFoundException, SQLException {
    	Connection c = null;
    	Statement stmt = null;
    	String  modificationTime=null;
    	
        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:LongDescriptionNotification.db");   
        stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery( "SELECT * FROM notificationTime LIMIT 1;" );
        while ( rs.next() ) {
            modificationTime = rs.getString("modification_time");
        }  
        rs.close();
        stmt.close();
        c.close();
        return modificationTime;
    }
    
    public static String getNow() {
    	Calendar cal = Calendar.getInstance();
    	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    	return sdf.format(cal.getTime());
    }
    
    public static void sendEmail() {
        String from = Email.getUSER_NAME();
        String pass = Email.getPASSWORD();
        String[] to = {Email.getRECIPIENT()}; // list of recipient email addresses
        String subject = "Long Description Excel file is modified";
        String body = "Long Description Excel file is modified.";

        Email.sendFromGMail(from, pass, to, subject, body);   	    
    }
}
