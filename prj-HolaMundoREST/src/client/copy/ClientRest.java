package client.copy;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.client.ClientBuilder;

public class ClientRest {

	private static UriBuilder getBaseURI() {
		return UriBuilder.fromUri("http://localhost:8080/prj-HolaMundoREST");
	}

	public static void main(String[] args) {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(getBaseURI().path("rest").path("hello").build());
		String result = target.request(MediaType.TEXT_PLAIN).get(String.class);
		System.out.println("Result 1: " + result);
		result = target.request(MediaType.TEXT_XML).get(String.class);
		System.out.println("Result 2: " + result);
		result = target.request(MediaType.TEXT_HTML).get(String.class);
		System.out.println("Result 3: " + result);
	}
}
