
import java.sql.Connection;
import java.util.Vector;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author lejla
 */
public class DataHandler {
    // DB details

    private static final String dbURL = "jdbc:ucanaccess://firstGUIdb.accdb";
    private static java.sql.Connection con;
    private static java.sql.Statement stm;
    private static java.sql.ResultSet rs;
    private static java.sql.ResultSetMetaData rsMeta;
    private static int columnCount;

    public static Vector<String> getTables() {
        Vector<String> tableNames = new Vector<String>();
        
        tableNames.add("Members");
        tableNames.add("boats");
        tableNames.add("rented");
        
        
        return tableNames;
    }

    public static void addMember(String ID, String name, String lname, String status) {
        String msAccDB = "firstGUIdb.accdb";
        String dbURL ="jdbc:ucanaccess://" + msAccDB;
        String query = "INSERT INTO Members VALUES ('" + ID + "','" + name + "','" + lname + "','" + status + "');";
        System.out.println(query);
        // Step 1: Loading or registering Oracle JDBC driver class
        // Step 2: Opening database connection
        // Step 2.A: Create and get connection using DriverManager class
        try ( Connection con = java.sql.DriverManager.getConnection(dbURL, "", "")) {
            stm = con.createStatement();
            stm.executeUpdate(query); // execute query on the database                       
        } catch (java.sql.SQLException sqlex) {
            System.err.println(sqlex);
            sqlex.printStackTrace();
        } catch (Exception ex) {
            System.err.println(ex);
            ex.printStackTrace();
        }
    }
    public static void delete(String id){
        String query="DELETE FROM Members WHERE memberID="+id;
        System.out.println(query);
        try ( Connection con = java.sql.DriverManager.getConnection(dbURL, "", "")) {
            stm = con.createStatement();
            stm.executeUpdate(query); // execute query on the database                       
        } catch (java.sql.SQLException sqlex) {
            System.err.println(sqlex);
            sqlex.printStackTrace();
        } catch (Exception ex) {
            System.err.println(ex);
            ex.printStackTrace();
        }
    }
    public static void  allocateRental(String id1){
        String query="INSERT INTO rented SELECT * FROM boats WHERE boatID="+id1;
        System.out.println(query);
        try ( Connection con = java.sql.DriverManager.getConnection(dbURL, "", "")) {
            stm = con.createStatement();
            stm.executeUpdate(query); // execute query on the database                       
        } catch (java.sql.SQLException sqlex) {
            System.err.println(sqlex);
            sqlex.printStackTrace();
        } catch (Exception ex) {
            System.err.println(ex);
            ex.printStackTrace();
        }
    }
    public static void removeRecord(String id2){
        String query= "DELETE FROM rented WHERE boatID="+id2;
        System.out.println(query);
        try ( Connection con = java.sql.DriverManager.getConnection(dbURL, "", "")) {
            stm = con.createStatement();
            stm.executeUpdate(query); // execute query on the database                       
        } catch (java.sql.SQLException sqlex) {
            System.err.println(sqlex);
            sqlex.printStackTrace();
        } catch (Exception ex) {
            System.err.println(ex);
            ex.printStackTrace();
        }
    
        }
        
    
    public static void searchRecords(String table) {
        String sqlQuery = "SELECT * FROM " + table;
        try {
            con = java.sql.DriverManager.getConnection(dbURL, "", "");
            stm = con.createStatement(
                    java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE,
                    java.sql.ResultSet.CONCUR_UPDATABLE);
            rs = stm.executeQuery(sqlQuery);
            rsMeta = rs.getMetaData();
            columnCount = rsMeta.getColumnCount();
        } catch (java.sql.SQLException sqlex) {
            System.err.println("Check your SQL " + sqlex);
            sqlex.printStackTrace();
        } catch (Exception ex) {
            System.err.println(ex);
            ex.printStackTrace();
        }
    }

    public static Object[] getTitles(String table) {
        Object[] columnNames = new Object[columnCount];
        try {
            for (int col = columnCount; col > 0; col--) {
                columnNames[col - 1]
                        = rsMeta.getColumnName(col);
            }
        } catch (java.sql.SQLException sqlex) {
            System.err.println(sqlex);
            sqlex.printStackTrace();
        } finally {
            try {
                if (null != con) {
                    // cleanup resources, once after processing
                    rs.close();
                    stm.close();
                    // and then finally close connection
                    con.close();
                }
            } catch (java.sql.SQLException sqlex) {
                System.err.println(sqlex.getMessage());
            }

        }
        return columnNames;
    }

    public static Object[][] getRows(String table) {
        searchRecords(table);
        Object[][] content;
        try {
            // determine the number of rows
            rs.last();
            int number = rs.getRow();
            content = new Object[number][columnCount];
            rs.beforeFirst();

            int i = 0;
            while (rs.next()) {
                // each row is an array of objects
                for (int col = 1; col <= columnCount; col++) {
                    content[i][col - 1] = rs.getObject(col);
                }
                i++;
            }
            return content;
        } catch (java.sql.SQLException sqlex) {
            System.err.println(sqlex);
        }
        return null;
    }
}
