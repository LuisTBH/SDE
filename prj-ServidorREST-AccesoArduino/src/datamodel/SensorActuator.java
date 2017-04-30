package datamodel;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SensorActuator {
	private String id;
	private String s_fecha;
	private double d_value = 0.0;
	private String s_value = "";
	private String description;
	
	public SensorActuator(){
	    
	}  
	public SensorActuator(String idnew, String s_fecha, double d_value, String s_value, String description){
		this.id = idnew;
		this.s_fecha = s_fecha;
		this.d_value = d_value;
		this.s_value = s_value;
		this.description = description;	
	}
		  
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFecha() {
		return s_fecha;
	}
	public void setFecha(String s_fecha) {
		this.s_fecha = s_fecha;
	}
	public synchronized double getDoubleValue() {
		return d_value;
	}
	public synchronized void setDoubleValue(double d_value) {
		this.d_value = d_value;
		notifyAll();
	}
	public synchronized String getStringValue() {
		return s_value;
	}
	public synchronized void setStringValue(String s_value) {
		this.s_value = s_value;
		notifyAll();
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public synchronized void waitForValue(double oldValue, double error) {
		while (Math.abs(d_value-oldValue) < error) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
