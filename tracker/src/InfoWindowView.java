/**
 * 
 */

/**
 * @author Analysis
 *
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
  




import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.event.map.StateChangeEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.LatLngBounds;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
  
@ManagedBean
@ViewScoped
public class InfoWindowView implements Serializable {
      
    private MapModel advancedModel;
  float i =0;
    private Marker marker;
    private String mapType;
    private String background;
	private double latitude;
	private double longitude;
	private int zoom;
	private double opacity;
	private boolean visible;
	private List<Radio> radios;
	private SQLiteDBHelper db = null;
	private String appUrl ;
	public InfoWindowView()
	{
		super();
		reset();
//		 System.out.println("Beginnging serial port search");
//	        try
//	        {
//	            
//	            (new TwoWaySerialComm()).connect("COM17");
//	        }
//	        catch ( Exception e )
//	        {
//	            // TODO Auto-generated catch block
//	            e.printStackTrace();
//	        }
	}

	public void reset()
	{
		this.background = "streets";
		this.latitude = 31.26685518639077;
		this.longitude = 73.16749577083328;
		this.zoom = 5;
		this.opacity = 1.0;
		this.visible = true;
		radios = new ArrayList<Radio>();
		db = new SQLiteDBHelper();
		mapType = "HYBRID";
		HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String url = req.getRequestURL().toString();
        this.appUrl = url.substring(0, url.length() - req.getRequestURI().length()) + req.getContextPath() + "/";

		
	}
	
    @PostConstruct
    public void init() {
    	radios = db.getRadios();
    	advancedModel = new DefaultMapModel();
    	LatLng coord = null;
    	Marker marker = null;
    	for (Radio radio : radios) {
			PositionData position = db.getRadioPosition(radio.getRadioId());
			System.out.println("Fetched new position :: " + position.getRadioId() + " :" + position.getId() );
			//coord =new LatLng(33.594066, 73.045148 );
			coord = new LatLng(position.getLatitude(), position.getLongitude());
			marker  = new Marker(coord, position.getRadioId(), position, this.appUrl + "images/" + radio.getImage() +".png");
//	        marker.setTitle("SPR 1");
	        marker.setClickable(true);
//	        marker.setData(marker1Data);
	        advancedModel.addOverlay(marker);
		}
        if(marker!=null && this.zoom == 5){
        	this.latitude = marker.getLatlng().getLat();
    		this.longitude = marker.getLatlng().getLng();
    		this.zoom = 15;
    	}
        
    	
//        LatLng coord1 ,coord2,coord3,coord4;
//        //Shared coordinates
////        if(i==0){
//         coord1 = new LatLng(33.594066, 73.045148 );
//        coord2 = new LatLng(33.604007, 73.067148);
//        coord3 = new LatLng(33.594703, 73.075707);
//        coord4 = new LatLng(33.600033, 73.071148);
////        }else{
////        	coord1 = new LatLng(35.879466, 30.667648);
////            coord2 = new LatLng(36.883707, 31.689216);
////            coord3 = new LatLng(37.879703, 30.706707);
////            coord4 = new LatLng(36.885233, 32.702323);
////            	
////        }
//        
//        //Icons and Data
//        PositionData marker1Data = new PositionData("SPR 1", 15, 192, "28 Jun 2016", "10:40 AM");
//        Marker marker  = new Marker(coord4, "Kaleici", "kaleici.png", "http://localhost:8080/tracker/images/walkie-talkie-radio-32.png");
//        marker.setTitle("SPR 1");
//        marker.setClickable(true);
//        marker.setData(marker1Data);
//        advancedModel.addOverlay(new Marker(coord1, "Konyaalti", marker1Data, "http://maps.google.com/mapfiles/ms/micons/blue-dot.png"));
//        advancedModel.addOverlay(new Marker(coord2, "Ataturk Parki", marker1Data));
//        advancedModel.addOverlay(marker);
//        advancedModel.addOverlay(new Marker(coord3, "Karaalioglu Parki", marker1Data, "http://maps.google.com/mapfiles/ms/micons/yellow-dot.png"));
    }
  
    public MapModel getAdvancedModel() {
        return advancedModel;
    }
    
    public void updatePoll() {
    	i++;
    	System.out.println("Poll updating ****************************************************" + i);
        advancedModel.getMarkers().clear();
        radios = db.getRadios();
    	advancedModel = new DefaultMapModel();
    	LatLng coord = null;
    	Marker marker = null;
    	for (Radio radio : radios) {
			PositionData position = db.getRadioPosition(radio.getRadioId());
			System.out.println("Fetched new position :: " + position.getRadioId() + " :" + position.getId() );
			coord = new LatLng(position.getLatitude(), position.getLongitude());
			marker  = new Marker(coord, position.getRadioId(), position,  this.appUrl + "images/" + radio.getImage() +".png");
//	        marker.setTitle("SPR 1");
	        marker.setClickable(true);
//	        marker.setData(marker1Data);
	        advancedModel.addOverlay(marker);
		}
    	 if(marker!=null && this.zoom == 5){
         	this.latitude = marker.getLatlng().getLat();
     		this.longitude = marker.getLatlng().getLng();
     		this.zoom = 15;
     	}
//          this.init();
        //Shared coordinates
//        LatLng coord1 ,coord2,coord3,coord4;
//        coord1 = new LatLng(33.594066, 73.045148 );
//        coord2 = new LatLng(33.604007, 73.067148+ i*0.01);
//        coord3 = new LatLng(33.594703, 73.075707+ i*0.01);
//        coord4 = new LatLng(33.600033, 73.071148+ i*0.01);
//        	
////        
////        
////        //Icons and Data
//        PositionData marker1Data = new PositionData("SPR 1", 15, 192, "28 Jun 2016", "10:40 AM");
//        Marker marker  = new Marker(coord4, "Kaleici", "kaleici.png", "http://localhost:8080/tracker/images/walkie-talkie-radio-32.png");
//        marker.setTitle("SPR 1");
//        marker.setClickable(true);
//        marker.setData(marker1Data);
//        advancedModel.addOverlay(new Marker(coord1, "Konyaalti", marker1Data, "http://maps.google.com/mapfiles/ms/micons/blue-dot.png"));
//        advancedModel.addOverlay(new Marker(coord2, "Ataturk Parki", marker1Data));
//        advancedModel.addOverlay(marker);
//        advancedModel.addOverlay(new Marker(coord3, "Karaalioglu Parki", marker1Data, "http://maps.google.com/mapfiles/ms/micons/yellow-dot.png"));
    }
  
    
    public void onStateChange(StateChangeEvent event) {
        LatLngBounds bounds = event.getBounds();
        int zoomLevel = event.getZoomLevel();
        
        addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Zoom Level", String.valueOf(zoomLevel)));
        addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Center", event.getCenter().toString()));
//        addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "NorthEast", bounds.getNorthEast().toString()));
//        addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "SouthWest", bounds.getSouthWest().toString()));
        System.out.println("Zoom changed ::: " + zoom);
        System.out.println("Center changed :: " + event.getCenter().getLat() + ":" + event.getCenter().getLng());
        this.setZoom(zoomLevel);
        this.setLatitude(event.getCenter().getLat());
        this.setLongitude(event.getCenter().getLng());
    }
    
    public void mapTypeChanged(ValueChangeEvent event){
    	System.out.println("Value Changes " + event.getNewValue());
    	this.mapType = (String) event.getNewValue();
    }
    public void handleChange(){
    	System.out.println("Value Changes " + this.mapType);
//    	this.mapType = (String) event.getNewValue();
    }
    
//    public void onMapTypeChange(Map event) {
//        LatLngBounds bounds = event.getBounds();
//        int zoomLevel = event.getZoomLevel();
//        
//        addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Zoom Level", String.valueOf(zoomLevel)));
//        addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Center", event.getCenter().toString()));
////        addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "NorthEast", bounds.getNorthEast().toString()));
////        addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "SouthWest", bounds.getSouthWest().toString()));
//        
//        this.setZoom(zoomLevel);
//        this.setLatitude(event.getCenter().getLat());
//        this.setLongitude(event.getCenter().getLng());
//    }
    
    public void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
    public void onMarkerSelect(OverlaySelectEvent event) {
        marker = (Marker) event.getOverlay();
    }
      
    public Marker getMarker() {
        return marker;
    }
    
    
    public String getBackground()
	{
		return background;
	}

	public void setBackground(String background)
	{
		this.background = background;
	}

	public double getLatitude()
	{
		return latitude;
	}

	public void setLatitude(double latitude)
	{
		this.latitude = latitude;
	}

	public double getLongitude()
	{
		return longitude;
	}

	public void setLongitude(double longitude)
	{
		this.longitude = longitude;
	}

	public int getZoom()
	{
//		System.out.println("getZoom ::: " + zoom);
		return zoom;
	}

	public void setZoom(int zoom)
	{
		System.out.println("setZoom ::: " + zoom);
		this.zoom = zoom;
	}

	public double getOpacity()
	{
		return opacity;
	}

	public void setOpacity(double opacity)
	{
		this.opacity = opacity;
	}

	public boolean isVisible()
	{
		return visible;
	}

	public void setVisible(boolean visible)
	{
		this.visible = visible;
	}

	public String getMapType() {
		return mapType;
	}

	public void setMapType(String mapType) {
		this.mapType = mapType;
	}

	public List<Radio> getRadios() {
		return radios;
	}

	public void setRadios(List<Radio> radios) {
		this.radios = radios;
	}
}
