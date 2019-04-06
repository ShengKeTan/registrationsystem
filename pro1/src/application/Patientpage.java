package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Patientpage {
	private final StringProperty catid;
	private final StringProperty patname;
	private final StringProperty time;
	private final StringProperty type;
	
	public Patientpage(String catid, String patname, String time, String type) {
		this.catid = new SimpleStringProperty(catid);
		this.patname = new SimpleStringProperty(patname);
		this.time = new SimpleStringProperty(time);
		this.type = new SimpleStringProperty(type);
	}
	
	
	public String getCatid() {
		return catid.get();
	}
	public String getPatname() {
		return patname.get();
	}
	public String getTime() {
		return time.get();
	}
	public String getType() {
		return type.get();
	}

}
