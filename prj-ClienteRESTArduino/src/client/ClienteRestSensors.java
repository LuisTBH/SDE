package client;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.client.ClientBuilder;

public class ClienteRestSensors {

	private static UriBuilder getBaseURI() {
	    return UriBuilder.fromUri("http://localhost:8080/prj-ServidorREST-AccesoArduino");
	}
	
	public static void main(String[] args) {  
		
	    Client client = ClientBuilder.newClient();
		
		WebTarget target = client.target(getBaseURI().path("valores").path("sensors").build());
		String result = target.request("application/xml").get(String.class); 
		System.out.println("All sensors:\n"+result);
		
		String sensorId = "luz";
		URI resourceUri = getBaseURI().path("valores").path("sensors").path(sensorId).path("wait").build();	    
	    SensorActuator sa;
	    String OldValue = "0.0";
		while (true) {
			target = client.target(resourceUri).queryParam("value", OldValue).queryParam("error", "0.5").queryParam("delay", "5000");
			sa = target.request("application/json").get(SensorActuator.class);
			OldValue = sa.getStringValue();
			System.out.println("luz="+OldValue);	
		}
	}	
}
