package services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import datamodel.DaoSensorsActuators;
import datamodel.SensorActuator;


//Entrada del programa para saber que se ha recibido
@Path("/sensors")
public class SensorsResource {

	  @Context
	  UriInfo uriInfo;
	  @Context
	  Request request;

	  @GET
	  @Produces({MediaType.TEXT_XML, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	  public List<SensorActuator> getAllSensors() {
	    List<SensorActuator> sensors = new ArrayList<SensorActuator>();
	    //Obtengo todos los valores de la Base de Datos
	    sensors.addAll(DaoSensorsActuators.instance.getModel().values());
	    return sensors; 
	  }

	  // get the total number of records
	  @GET
	  @Path("count")
	  @Produces(MediaType.TEXT_PLAIN)
	  public String getCount() {
	    int count = DaoSensorsActuators.instance.getModel().size();
	    return String.valueOf(count);
	  }
	  	  
	  @POST
	  @Produces(MediaType.TEXT_PLAIN)
	  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	  public String newSensor(@FormParam("id") String id,
	      @FormParam("description") String description,
	      @FormParam("value") String value,
	      @Context HttpServletResponse servletResponse) throws IOException {
  
		Response res;    
		double doubleValue = 0;
		try {
			doubleValue = Float.parseFloat(value);
		} catch (NumberFormatException e) {
			res = Response.notAcceptable(null).build();
			return res.toString();
		}
		
		//Si existe el id de sensor no se crea nada.
	    SensorActuator sa = DaoSensorsActuators.instance.getModel().get(id);
	    if(sa!=null) return "Ya existe el identificador";
		  
		// Creamos un nuevo sensor y los insertamos en el datastore
		String s_fecha = new Date().toString();
		sa = new SensorActuator(id,s_fecha,doubleValue,"",description);
		DaoSensorsActuators.instance.getModel().put(id, sa);	
	
		return "Nuevo sensor: " + id + " creado";
	  }
	 
	  // Tunnel
	  @Path("{sensor}")
	  public SensorResource tunnelToSensor(@PathParam("sensor") String id) { //Id vale lo que pongamos detras de /sensor/ del Path inicial
	    return new SensorResource(uriInfo, request, id);
	  }
}
