/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



/**
 *
 * @author Azeem Irshad
 */
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import net.sf.marineapi.nmea.io.SentenceReader;
import net.sf.marineapi.nmea.parser.SentenceFactory;
import net.sf.marineapi.nmea.sentence.RMCSentence;
import net.sf.marineapi.nmea.sentence.Sentence;
import net.sf.marineapi.nmea.util.DataStatus;

/**
 * This version of the TwoWaySerialComm example makes use of the 
 * SerialPortEventListener to avoid polling.
 *
 */
@ManagedBean
@ViewScoped
public class HarrisSerialListener implements Serializable
{
	private SerialPort serialPort;
    private SQLiteDBHelper helper ;
    private List<String> serialPorts;
    private String selectedSerialPort;
    public HarrisSerialListener()
    {
        super();
        helper = new SQLiteDBHelper();
        helper.createPositionTable();
        serialPorts = new ArrayList<>();
        for (int i=1; i<33;i++){
        	serialPorts.add("COM"+i);
        }
    }
    
    
    
    
    void disconnect(){
        serialPort.removeEventListener();
        serialPort.close();
        System.out.println("Serial Port closed........");
    }
    
    public String connect( )
    {
    	try{
    	
	    	System.out.println("Connecting to port...." + selectedSerialPort);
	    	if(selectedSerialPort!=null && !selectedSerialPort.equals("")){
		        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(selectedSerialPort);
		        if ( portIdentifier.isCurrentlyOwned() )
		        {
		            System.out.println("Error: Port is currently in use");
		            addMessage(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: Port is currently in use", "Error: Port is currently in use"));
		            
		            
		        }
		        else
		        {
		            CommPort commPort = portIdentifier.open(this.getClass().getName(),2000);
		            
		            if ( commPort instanceof SerialPort )
		            {
		                serialPort = (SerialPort) commPort;
		                serialPort.setSerialPortParams(9600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
		                
		                InputStream in = serialPort.getInputStream();
		                OutputStream out = serialPort.getOutputStream();
		                               
		//                (new Thread(new SerialWriter(out))).start();
		                
		                serialPort.addEventListener(new SerialReader(in));
		                serialPort.notifyOnDataAvailable(true);
		                return "tracker";
		
		            }
		            else
		            {
		                System.out.println("Error: Only serial ports are handled by this programme.");
		                addMessage(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: Only serial ports are handled by this programme.", "Error: Only serial ports are handled by this programme."));
		            }
		        }    
	    	}
    		
    	}catch(Exception ex){
    		System.out.println("Exception occurred...." + ex.getClass() + ex.getMessage());
    		 ex.printStackTrace();
             addMessage(new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getClass() + " "+ ex.getMessage(),ex.getClass()+ " " + ex.getMessage()));
    	}
    	return "";
    	
    }
    public void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    /**
     * Handles the input coming from the serial port. A new line character
     * is treated as the end of a block in this example. 
     */
    public class SerialReader implements SerialPortEventListener 
    {
        private InputStream in;
        private byte[] buffer = new byte[1024];
        
        public SerialReader ( InputStream in )
        {
            this.in = in;
        }
        
        public void serialEvent(SerialPortEvent arg0) {
            int data;
          
            try
            {
                int len = 0;
                while ( ( data = in.read()) > -1 )
                {
//                    System.out.println(data);
//                    if ( data ==  '\n' ||data ==  (byte)0x4E ) {
//                    if (  data == (byte)0x4E) 
                    if ( data ==  '\n' )
                    {
                        break;
                    }
                    buffer[len++] = (byte) data;
                }
                System.out.println("Data Received :: " +len);
                System.out.println("Data Received :: " +len+":"+new String(buffer,0,len));
                decodeData(buffer, len);
            }
            catch ( IOException e )
            {
                e.printStackTrace();
                System.exit(-1);
            }             
        }
        public void decodeData(byte[] buffer, int len)
        {
            System.out.println("************** Parsing data *************");
            String id=null;
            int idStart = 0, idEnd =0, nmeaStart = 0;
            String nmea=null;
            for(int i=0 ; i<len;i++)
            {
//                System.out.println(buffer[i]);
                if(buffer[i] == (byte)7&& idStart == 0)
                {
                   idStart = i+1;
                }
                 if(buffer[i] == (byte)1 && idEnd == 0)
                {
                   idEnd = i;
                }
                 if(buffer[i] == (byte)1 && buffer[i+1] == (byte)3 
                         && buffer[i+2] == (byte)1 && buffer[i+3] == (byte)3&& nmeaStart == 0 )
                 {
                     nmeaStart = i+4;
                 }
            }
            System.out.println("Device ID :: " + idStart + ":" + idEnd );
            System.out.println("NMEA :: " + nmeaStart );
            id = new String(buffer, idStart,idEnd-idStart);
            nmea = new String (buffer, nmeaStart, len-nmeaStart-4);
            System.out.println("Device ID :: " + idStart + ":" + idEnd + ":" + id);
            System.out.println("NMEA :: " + nmeaStart + ":"  + nmea);
            String nmeaFormatted = "$GP" + nmea.substring(nmea.indexOf("RMC"), nmea.length());
            System.out.println("NMEA formatted :: " + nmeaFormatted);
            SentenceFactory sf = SentenceFactory.getInstance();
            Sentence sentence = sf.createParser(nmeaFormatted);
            System.out.println("Sentence Type :: " + sentence.getSentenceId());
            RMCSentence sentence1 = (RMCSentence) sentence;
//            try{
            System.out.println("Sentence Status :: " + sentence1.getStatus());
            if(sentence1.getStatus().name().equals(DataStatus.ACTIVE.name())){
                System.out.println("************** Parsing data *************");

    //             System.out.println("Sentence Course :: " + sentence1.getCourse());
                System.out.println("Sentence Date :: " + sentence1.getDate());
                System.out.println("Sentence Time :: " + sentence1.getTime());
                System.out.println("Sentence Position :: " + sentence1.getPosition());
                System.out.println("Sentence Speed :: " + sentence1.getSpeed());

                System.out.println("Sentence Course :: " + sentence1.getCourse());
               helper.insertPosition(id, sentence1);
            }
//            }catch(Exception e)
//            {
//                e.printStackTrace();
//            }
            
        }

    }

    /** */
    public static class SerialWriter implements Runnable 
    {
        OutputStream out;
        
        public SerialWriter ( OutputStream out )
        {
            this.out = out;
        }
        
        public void run ()
        {
            try
            {                
                int c = 0;
                while ( ( c = System.in.read()) > -1 )
                {
                    this.out.write(c);
                }                
            }
            catch ( IOException e )
            {
                e.printStackTrace();
                System.exit(-1);
            }            
        }
    }

	public List<String> getSerialPorts() {
		return serialPorts;
	}




	public void setSerialPorts(List<String> serialPorts) {
		this.serialPorts = serialPorts;
	}




	public String getSelectedSerialPort() {
		return selectedSerialPort;
	}




	public void setSelectedSerialPort(String selectedSerialPort) {
		this.selectedSerialPort = selectedSerialPort;
	}
    

    
    

}