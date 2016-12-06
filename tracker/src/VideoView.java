import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;

/**
 * 
 */

/**
 * @author Analysis
 *
 */
@ManagedBean
@SessionScoped
public class VideoView implements Serializable{
	private String videoUrl ;
	private boolean playing;
	private Integer volume;
	
	
	public VideoView() {
		playing = false;
		volume = 50;
		this.videoUrl = "";
	}

	public String showVideo(){
		System.out.println("******************* Show video *****************");
		playing = true;
		return "video";
	}
	public String pauseVideo(){
		System.out.println("******************* Pause video *****************");
		this.videoUrl = "";
		playing = false;
		return "video";
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public boolean isPlaying() {
		return playing;
	}

	public void setPlaying(boolean playing) {
		this.playing = playing;
	}

	public Integer getVolume() {
		return volume;
	}

	public void setVolume(Integer volume) {
		this.volume = volume;
	}
	

}
