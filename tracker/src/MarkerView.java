/**
 * 
 */

/**
 * @author Analysis
 *
 */
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
 



import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

import com.gisfaces.model.Coordinate;
import com.gisfaces.model.GraphicsModel;
import com.gisfaces.model.MarkerGraphic;
 
@ManagedBean
public class MarkerView implements Serializable {
    
    private MapModel simpleModel;
    
    private String background;
	private double latitude;
	private double longitude;
	private int zoom;
	private double opacity;
	private boolean visible;
	

	public MarkerView()
	{
		super();
		reset();
	}

	public void reset()
	{
		this.background = "streets";
		this.latitude = 33.5940;
		this.longitude = 73.0451;
		this.zoom = 10;
		this.opacity = 1.0;
		this.visible = true;
		
		

		
	}
    @PostConstruct
    public void init() {
        simpleModel = new DefaultMapModel();
//        MarkerGraphic marker = new MarkerGraphic();
//		marker.setCoordinate(new Coordinate(33.59409, 73.045141));
//		marker.setImage("http://localhost:8080/tracker/images/harris2.png");
//		marker.setHeight(45);
//		marker.setWidth(15);
//		
//		marker.getAttributes().put("Date", "28 Jun 2016");
//		marker.getAttributes().put("Time", "1040hrs");
//		marker.getAttributes().put("Speed", "0.19kmph");
//		marker.getAttributes().put("Course", "112");
//		marker.setDraggable(false);     
        //Shared coordinates
        LatLng coord1 = new LatLng(33.59409, 73.045141);
//        LatLng coord2 = new LatLng(36.883707, 30.689216);
//        LatLng coord3 = new LatLng(36.879703, 30.706707);
//        LatLng coord4 = new LatLng(36.885233, 30.702323);
          
        //Basic marker
        simpleModel.addOverlay(new Marker(coord1, "SPR 1"));
//        simpleModel.addOverlay(new Marker(coord2, "Ataturk Parki"));
//        simpleModel.addOverlay(new Marker(coord3, "Karaalioglu Parki"));
//        simpleModel.addOverlay(new Marker(coord4, "Kaleici"));
    }
  
    public MapModel getSimpleModel() {
        return simpleModel;
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
		return zoom;
	}

	public void setZoom(int zoom)
	{
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

}
