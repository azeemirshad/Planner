import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * 
 */

/**
 * @author Analysis
 *
 */
public class PositionData implements Serializable{
	private int id;
	private String radioId;
	private double speed;
	private double longitude;
	private double latitude;
	private float course;
	private String date;
	private String time;
	
	public PositionData(){
		super();
	}
	public PositionData(String radioId, double speed, float course, String date,
			String time) {
		super();
		this.radioId = radioId;
		this.speed = speed * 1.852;
		this.course = course;
		this.date = date;
		this.time = time;
	}
	
	public String getDateTime(){
		String newDate  = null;
		SimpleDateFormat informatter = new SimpleDateFormat("ddMMyy HHmmss");
		informatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		SimpleDateFormat outformatter = new SimpleDateFormat("dd/MM/yy HH:mm a");
//		outformatter.setTimeZone(TimeZone.getTimeZone(""));
		String dateInString =this.getDate() + " " + this.getTime();		
			
		try {

			Date date = informatter.parse(dateInString);
			System.out.println(date);
			newDate = outformatter.format(date);
			System.out.println(newDate);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return newDate;
	} 
	
	public String getRadioId() {
		return radioId;
	}
	public void setRadioId(String radioId) {
		this.radioId = radioId;
	}
	public double getSpeed() {
		return speed;
	}
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	public float getCourse() {
		return course;
	}
	public void setCourse(float course) {
		this.course = course;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	

}
