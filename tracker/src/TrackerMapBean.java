

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;

import com.gisfaces.event.MapClickEvent;
import com.gisfaces.event.MapExtentEvent;
import com.gisfaces.event.MapGeoLocationEvent;
import com.gisfaces.event.MapGraphicDragEvent;
import com.gisfaces.event.MapGraphicSelectEvent;
import com.gisfaces.event.MapGraphicViewEvent;
import com.gisfaces.model.Background;
import com.gisfaces.model.CircleGraphic;
import com.gisfaces.model.Coordinate;
import com.gisfaces.model.Graphic;
import com.gisfaces.model.GraphicsModel;
import com.gisfaces.model.LineStyle;
import com.gisfaces.model.MarkerGraphic;
import com.gisfaces.model.MeasurementUnit;
import com.gisfaces.model.PolygonGraphic;
import com.gisfaces.model.PolylineGraphic;
import com.gisfaces.model.TextGraphic;
import com.gisfaces.util.GISUtilities;
import com.gisfaces.util.StringUtilities;

@ManagedBean
@SessionScoped
public class TrackerMapBean
{
	private String background;
	private double latitude;
	private double longitude;
	private int zoom;
	private double opacity;
	private boolean visible;
	private String where;
	private boolean airports;
	private boolean warnings;
	private boolean radar;
	private boolean windSpeed;
	private String geoip;
	private GraphicsModel geoipGraphicsModel;
	private GraphicsModel graphicsModel;
	private GraphicsModel starbucksGraphicsModel;

	public TrackerMapBean()
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
		this.where = "MAGNITUDE >= 2";
		this.airports = true;
		this.warnings = false;
		this.radar = false;
		this.windSpeed = false;
		this.geoip = null;
		this.geoipGraphicsModel = new GraphicsModel();
		this.geoipGraphicsModel.setName("Geocoded IP Addresses");

		this.buildGraphicsModel();
		this.buildStarbucksGraphicsModel();
	}

	public List<SelectItem> getBackgrounds()
	{
		return Background.getSelectItems();
	}

	public void doMapClickListener(AjaxBehaviorEvent event)
	{
		MapClickEvent e = (MapClickEvent) event;

		String summary = "Map Click Event";
		String detail = String.format("Latitude='%s', Longitude='%s'", e.getLatitude(), e.getLongitude());

		System.out.println(String.format("%s: %s", summary, detail));
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void doMapExtentListener(AjaxBehaviorEvent event)
	{
		MapExtentEvent e = (MapExtentEvent) event;

		this.latitude = e.getLatitude();
		this.longitude = e.getLongitude();
		this.zoom = e.getZoom();

		String summary = "Map Extent Update Event";
		String detail = String.format("Latitude='%s', Longitude='%s', Zoom='%s'", e.getLatitude(), e.getLongitude(), e.getZoom());

		System.out.println(String.format("%s: %s", summary, detail));
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void doMapGraphicViewListener(AjaxBehaviorEvent event)
	{
		MapGraphicViewEvent e = (MapGraphicViewEvent) event;

		String summary = "Map Graphic View Event";
		String detail = String.format("Service ID='%s', Layer ID='%s', Attributes='%s'", e.getServiceId(), e.getLayerId(), e.getAttributesJson());

		System.out.println(String.format("%s: %s", summary, detail));
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void doMapGraphicSelectListener(AjaxBehaviorEvent event)
	{
		MapGraphicSelectEvent e = (MapGraphicSelectEvent) event;

		String summary = "Map Graphic Select Event";
		String detail = String.format("Service ID='%s', Layer ID='%s', Attributes='%s'", e.getServiceId(), e.getLayerId(), e.getAttributesJson());

		System.out.println(String.format("%s: %s", summary, detail));
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void doMapGraphicDragListener(AjaxBehaviorEvent event)
	{
		MapGraphicDragEvent e = (MapGraphicDragEvent) event;

		String summary = "Map Graphic Drag Event";
		String detail = String.format("Latitude='%s', Longitude='%s', Attributes='%s'", e.getLatitude(), e.getLongitude(), e.getAttributesJson());

		System.out.println(String.format("%s: %s", summary, detail));
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void doMapGeoLocationListener(AjaxBehaviorEvent event)
	{
		MapGeoLocationEvent e = (MapGeoLocationEvent) event;

		this.latitude = e.getLatitude();
		this.longitude = e.getLongitude();

		String summary = "Map Geo-Location Event";
		String detail = String.format("Latitude='%s', Longitude='%s', Heading='%s', Speed='%s', Altitude='%s', Timestamp='%s'", e.getLatitude(), e.getLongitude(), e.getHeading(), e.getSpeed(), e.getAltitude(), e.getTimestamp());

		System.out.println(String.format("%s: %s", summary, detail));
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void geoIpActionListener(ActionEvent event)
	{
		System.out.println("Map bean geocode ip action listener fired.");

		try
		{
			// Build the url to the geocode service and request json format.
			String url = String.format("http://freegeoip.net/json/%s", this.geoip);

			// Get the result.
			String json = GISUtilities.getUrlConnectionOutput(url);

			// Parse the json result.
			Map<String, Object> attribs = StringUtilities.toMap(json);

			if (attribs.containsKey("latitude") && attribs.containsKey("longitude"))
			{
				double latitude = Double.valueOf(attribs.get("latitude").toString());
				double longitude = Double.valueOf(attribs.get("longitude").toString());

				// Create a new marker.
				MarkerGraphic marker = new MarkerGraphic();
				marker.setCoordinate(new Coordinate(latitude, longitude));
				marker.setAttributes(attribs);

				// Add the new marker to the graphics model.
				this.getGeoipGraphicsModel().getGraphics().add(marker);

				// Refresh the graphics model.
				this.getGeoipGraphicsModel().setRefresh(true);

				// Zoom to the coordinates.
				this.latitude = latitude;
				this.longitude = longitude;
				this.zoom = 10;
			}
		}
		catch (IOException e)
		{
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Error during geocode.", e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}

	public void resetActionListener(ActionEvent event)
	{
		System.out.println("Map bean reset action listener fired.");
		this.reset();
	}

	private void buildGraphicsModel()
	{
		MarkerGraphic marker = new MarkerGraphic();
		marker.setCoordinate(new Coordinate(33.59409, 73.045141));
		marker.setImage("http://localhost:8080/tracker/images/harris2.png");
		marker.setHeight(45);
		marker.setWidth(15);
		
		marker.getAttributes().put("Date", "28 Jun 2016");
		marker.getAttributes().put("Time", "1040hrs");
		marker.getAttributes().put("Speed", "0.19kmph");
		marker.getAttributes().put("Course", "112");
		marker.setDraggable(false);

//		PolylineGraphic polyline = new PolylineGraphic();
//		polyline.getCoordinates().add(new Coordinate(30.324073168632953, -81.6584550476046));
//		polyline.getCoordinates().add(new Coordinate(30.320813258514306, -81.65879837035853));
//		polyline.setStyle(LineStyle.SOLID);
//		polyline.setColor("#0000FF");
//		polyline.setOpacity(0.5);
//		polyline.setWidth(5);
//		polyline.getAttributes().put("Location", "The Main Street Bridge");
//		polyline.getAttributes().put("Polyline Setting", "Value");
//		polyline.getAttributes().put("setColor()", polyline.getColor());
//		polyline.getAttributes().put("setOpacity()", polyline.getOpacity());
//		polyline.getAttributes().put("setWidth()", polyline.getWidth());
//
//		PolygonGraphic polygon = new PolygonGraphic();
//		polygon.getCoordinates().add(new Coordinate(30.32462882430922, -81.66111579894871));
//		polygon.getCoordinates().add(new Coordinate(30.325202998529473, -81.66095486640785));
//		polygon.getCoordinates().add(new Coordinate(30.325453041089542, -81.65989271163804));
//		polygon.getCoordinates().add(new Coordinate(30.324897390089276, -81.65879837036005));
//		polygon.getCoordinates().add(new Coordinate(30.324212082847886, -81.65900221824515));
//		polygon.getCoordinates().add(new Coordinate(30.32462882430922, -81.66111579894871));
//		polygon.setFillColor("#FF0000");
//		polygon.setFillOpacity(0.25);
//		polygon.setOutlineStyle(LineStyle.SHORTDASHDOT);
//		polygon.setOutlineColor("#FF0000");
//		polygon.getAttributes().put("Location", "The Jacksonville Landing");
//		polygon.getAttributes().put("Polylgon Setting", "Value");
//		polygon.getAttributes().put("setFillColor()", polygon.getFillColor());
//		polygon.getAttributes().put("setFillOpacity()", polygon.getFillOpacity());
//		polygon.getAttributes().put("setOutlineColor()", polygon.getOutlineColor());
//		polygon.getAttributes().put("setOutlineOpacity()", polygon.getOutlineOpacity());
//		polygon.getAttributes().put("setOutlineStyle()", "LineStyle.SHORTDASHDOT");
//
//		TextGraphic text = new TextGraphic();
//		text.setCoordinate(new Coordinate(30.322628449125027, -81.66060081481795));
//		text.setText("Jacksonville");
//		text.setSize("24px");
//		text.setFamily("Verdana");
//		text.setBold(false);
//		text.setColor("#000000");
//		text.setOpacity(0.75);
//		text.getAttributes().put("Location", "Downtown Jacksonville");
//
//		CircleGraphic circle = new CircleGraphic();
//		circle.setCoordinate(new Coordinate(30.32003530938606, -81.65982833862175));
//		circle.setFillColor("#00FF00");
//		circle.setFillOpacity(0.25);
//		circle.setOutlineStyle(LineStyle.SOLID);
//		circle.setOutlineColor("#00FF00");
//		circle.getAttributes().put("Location", "Friendship Park Fountain");
//		circle.setRadius(50);
//		circle.setRadiusUnit(MeasurementUnit.METERS);
//
		this.graphicsModel = new GraphicsModel();
		this.graphicsModel.setName("SPR 1");
		this.graphicsModel.getGraphics().add(marker);
//		this.graphicsModel.getGraphics().add(polyline);
//		this.graphicsModel.getGraphics().add(polygon);
//		this.graphicsModel.getGraphics().add(text);
//		this.graphicsModel.getGraphics().add(circle);
	}

	private void buildStarbucksGraphicsModel()
	{
		this.starbucksGraphicsModel = new GraphicsModel();
		this.starbucksGraphicsModel.setName("Starbucks");

		List<Graphic> graphics = this.starbucksGraphicsModel.getGraphics();
		graphics.add(buildStarbucksMarker(30.304353, -81.655535, "1980 San Marco Blvd, Jacksonville, FL 32207"));
		graphics.add(buildStarbucksMarker(30.312096, -81.680833, "1650 Margaret St, Jacksonville, FL 32204"));
		graphics.add(buildStarbucksMarker(30.2432613, -81.5986099, "7153 Philips Hwy, Jacksonville, FL 32256"));
		graphics.add(buildStarbucksMarker(30.278487, -81.720025, "4495 Roosevelt Blvd, Jacksonville, FL 32210"));
		graphics.add(buildStarbucksMarker(30.292561, -81.601978, "5960 Beach Blvd, Jacksonville, FL 32216"));
		graphics.add(buildStarbucksMarker(30.220144, -81.551926, "8221 Southside Blvd, Jacksonville, FL 32256"));
		graphics.add(buildStarbucksMarker(30.204192, -81.617150, "9661 San Jose Blvd, Jacksonville, FL 32257"));
		graphics.add(buildStarbucksMarker(30.260889, -81.645445, "1500 University Blvd W, Jacksonville, FL 32217"));
		graphics.add(buildStarbucksMarker(30.429896, -81.662830, "1044 Dunn Ave, Jacksonville, FL 32218"));
		graphics.add(buildStarbucksMarker(30.316828, -81.558313, "9301 Atlantic Blvd #101, Jacksonville, FL 32225"));
		graphics.add(buildStarbucksMarker(30.25813, -81.5262888, "10281 Midtown Pkwy #203, Jacksonville, FL 32246"));
		graphics.add(buildStarbucksMarker(30.190558, -81.551744, "9940 Southside Blvd, Jacksonville, FL 32256"));
	}

	private MarkerGraphic buildStarbucksMarker(double latitude, double longitude, String address)
	{
		MarkerGraphic marker = new MarkerGraphic();
		marker.setCoordinate(new Coordinate(latitude, longitude));
		marker.getAttributes().put("Address", address);
		marker.setImage("http://www.brandinsightblog.com/wp-content/uploads/2008/04/starbucks-logo.png");
		marker.setHeight(32);
		marker.setWidth(32);
		marker.setDraggable(true);

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

	public String getWhere()
	{
		return where;
	}

	public void setWhere(String where)
	{
		this.where = where;
	}

	public boolean isAirports()
	{
		return airports;
	}

	public void setAirports(boolean airports)
	{
		this.airports = airports;
	}

	public boolean isWarnings()
	{
		return warnings;
	}

	public void setWarnings(boolean warnings)
	{
		this.warnings = warnings;
	}

	public boolean isRadar()
	{
		return radar;
	}

	public void setRadar(boolean radar)
	{
		this.radar = radar;
	}

	public boolean isWindSpeed()
	{
		return windSpeed;
	}

	public void setWindSpeed(boolean windSpeed)
	{
		this.windSpeed = windSpeed;
	}

	public GraphicsModel getGraphicsModel()
	{
		return graphicsModel;
	}

	public void setGraphicsModel(GraphicsModel graphicsModel)
	{
		this.graphicsModel = graphicsModel;
	}

	public GraphicsModel getStarbucksGraphicsModel()
	{
		return starbucksGraphicsModel;
	}

	public void setStarbucksGraphicsModel(GraphicsModel starbucksGraphicsModel)
	{
		this.starbucksGraphicsModel = starbucksGraphicsModel;
	}

	public String getGeoip()
	{
		return geoip;
	}

	public void setGeoip(String geoip)
	{
		this.geoip = geoip;
	}

	public GraphicsModel getGeoipGraphicsModel()
	{
		return geoipGraphicsModel;
	}

	public void setGeoipGraphicsModel(GraphicsModel geoipGraphicsModel)
	{
		this.geoipGraphicsModel = geoipGraphicsModel;
	}
}
