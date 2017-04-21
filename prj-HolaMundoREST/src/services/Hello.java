package services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/hello")
public class Hello {

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String plainTextHello() {
		return "Hello SDI. Plain text.";
	}

	@GET
	@Produces(MediaType.TEXT_XML)
	public String xmlHello() {
		return "<?xml version=\"1.0\"?>" + "<hello> Hello SDI. XML." + "</hello>";
	}

	@GET
	@Produces(MediaType.TEXT_HTML)
	public String htmlHello() {
		return "<html> " + "<title>" + "Hello SDI" + "</title>" + "<body><h1>" + "Hello SDI. HTML." + "</body></h1>"
				+ "</html> ";
	}

}
