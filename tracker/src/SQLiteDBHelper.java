/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import net.sf.marineapi.nmea.sentence.RMCSentence;


/**
 *
 * @author Azeem Irshad
 */
public class SQLiteDBHelper {
    private static final String jdbcDriveClass  = "org.sqlite.JDBC";
    private static final String DB_FILE = Environment.getProperty("filePath");
//  private static final String DB_FILE = "C:\\data.db";
  private static final String databaseName  = "jdbc:sqlite:" + DB_FILE;
    private static final String TABLE_POSITION = "POSITION";
    private static final String KEY_ID = "id";
    private static final String KEY_DEVICE_ID = "DEVICE";
    private static final String KEY_DATE = "DATE";
    private static final String KEY_TIME = "TIME";
    private static final String KEY_POSITION = "POSITION";
    private static final String KEY_LONGITUDE = "LONGITUDE";
    private static final String KEY_LATITUDE = "LATITUDE";
    private static final String KEY_SPEED = "SPEED";
    private static final String KEY_COURSE = "COURSE";

    public Connection getDBConnection() throws Exception{
        
        Connection c = null;
        
        Class.forName(jdbcDriveClass);
        c = DriverManager.getConnection(databaseName);
        return c;
    }
    public void createDB() throws Exception{
        Connection c = null;
        try {
            Class.forName(jdbcDriveClass);
            c = DriverManager.getConnection(databaseName);
          } catch ( Exception e ) {
            e.printStackTrace();
            throw new Exception(e);
          }
    }
    public void createPositionTable(){
         String CREATE_POSITION_TABLE = "CREATE TABLE " +TABLE_POSITION +  "( " +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_DEVICE_ID + " TEXT, "+
                KEY_DATE + " TEXT, "+
                KEY_TIME + " TEXT, "+
                KEY_LONGITUDE + " REAL, "+
                KEY_LATITUDE + " REAL, "+
                KEY_SPEED + " REAL, "+
                KEY_COURSE + " REAL )";
         String CHECK_TABLE_EXISTS = "SELECT name FROM sqlite_master WHERE type='table' AND name='" + TABLE_POSITION + "'";
         Statement stmt = null;
         Connection c = null;
         boolean tableExists = false;
         try{
            c = this.getDBConnection();
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(CHECK_TABLE_EXISTS);
            while ( rs.next() ) {
            	tableExists = true;
            	System.out.println("Table Position Exists......");
               
             }
//            stmt.executeUpdate("DROP TABLE if exists " + TABLE_POSITION);
            if(!tableExists)
            	stmt.executeUpdate(CREATE_POSITION_TABLE);
            stmt.close();
            c.close();
             
         }catch(Exception e)
         {
             e.printStackTrace();
         }
    }
    public void insertPosition(String deviceId, RMCSentence sentence){
        Statement stmt = null;
        Connection c = null;
        StringBuffer buffer  = new StringBuffer();
        buffer.append("INSERT INTO POSITION (" );
//        buffer.append( KEY_ID + " ," );
        buffer.append( KEY_DEVICE_ID + " ," );
        buffer.append( KEY_DATE + " ," );
        buffer.append( KEY_TIME + " ," );
        buffer.append( KEY_LONGITUDE + " ," );
        buffer.append( KEY_LATITUDE + " ," );
        buffer.append( KEY_SPEED + " ," );
        buffer.append( KEY_COURSE + " )" ); 
        buffer.append( "VALUES (");
        buffer.append("'"+ deviceId + "' ,");
        buffer.append("'"+ sentence.getDate()+ "' ,");
        buffer.append("'"+ sentence.getTime()  + "' ,");
        buffer.append( sentence.getPosition().getLongitude()+ " ,");
        buffer.append( sentence.getPosition().getLatitude()+ " ,");
        buffer.append( sentence.getSpeed()+ " ,");
        buffer.append( sentence.getCourse()+ " )");
        System.out.println("Query " + buffer.toString());
                    
        try{
            c = this.getDBConnection();
            stmt = c.createStatement();
            
            stmt.executeUpdate(buffer.toString());
            stmt.close();
            c.close();
             
        }catch(Exception e)
        {
             e.printStackTrace();
        }
    }
    public List<Radio> getRadios(){
    	Statement stmt = null;
        Connection c = null;
        Radio radio = null;
        List<Radio> radios = new ArrayList<>();
        int i =0 ;
        try{
            c = this.getDBConnection();
            stmt = c.createStatement();
            
            ResultSet rs = stmt.executeQuery( "SELECT distinct " + KEY_DEVICE_ID + " FROM POSITION ;" );
            while ( rs.next() ) {
            	
              radio = new Radio(rs.getString(KEY_DEVICE_ID), "radio" + i%7);
              radios.add(radio);
              i++;
            }
            rs.close();
            stmt.close();
            c.close();
             
        }catch(Exception e)
        {
             e.printStackTrace();
        }
        return radios;
    }
    
    public PositionData getRadioPosition(String deviceId){
    	Statement stmt = null;
        Connection c = null;
        PositionData radio = null;
        
        int i =0 ;
        try{
            c = this.getDBConnection();
            stmt = c.createStatement();
            
            ResultSet rs = stmt.executeQuery( "SELECT * FROM POSITION WHERE " + KEY_DEVICE_ID + " = '" + deviceId + "'  ORDER BY id desc LIMIT 1;" );
            while ( rs.next() ) {
            	radio = new PositionData();
            	 radio.setId(rs.getInt(KEY_ID));
            	 radio.setRadioId(rs.getString(KEY_DEVICE_ID));
                 radio.setDate(rs.getString(KEY_DATE));
                 radio.setTime(rs.getString(KEY_TIME));
                 radio.setLongitude(rs.getDouble(KEY_LONGITUDE));
                 radio.setLatitude(rs.getDouble(KEY_LATITUDE));
                 radio.setSpeed(rs.getFloat(KEY_SPEED));
                 radio.setCourse(rs.getFloat(KEY_COURSE));
                 
            }
            rs.close();
            stmt.close();
            c.close();
             
        }catch(Exception e)
        {
             e.printStackTrace();
        }
        return radio;
    }
    
    public void getAllPositions(){
         Statement stmt = null;
        Connection c = null;
        try{
            c = this.getDBConnection();
            stmt = c.createStatement();
            
            ResultSet rs = stmt.executeQuery( "SELECT * FROM POSITION order by ID;" );
            while ( rs.next() ) {
               int id = rs.getInt(KEY_ID);
               String  deviceId = rs.getString(KEY_DEVICE_ID);
               String  date = rs.getString(KEY_DATE);
               String  time = rs.getString(KEY_TIME);
               String  position = rs.getString(KEY_POSITION);
               float speed  = rs.getFloat(KEY_SPEED);
               float course  = rs.getFloat(KEY_COURSE);
               
               System.out.println( "ID = " + id );
               System.out.println( "Device  = " + deviceId );
               System.out.println( "DATE TIME = " + date + " " + time );
               System.out.println( "POSITION = " + position );
               System.out.println( "SPEED = " + speed );
               System.out.println( "COURSE = " + course );
               System.out.println();
            }
            rs.close();
            stmt.close();
            c.close();
             
        }catch(Exception e)
        {
             e.printStackTrace();
        }
    }
    
}
