package services;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBElement;

import datamodel.DaoSensorsActuators;
import datamodel.SensorActuator;

public class SensorResource {

	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	String id;

	public SensorResource(UriInfo uriInfo, Request request, String id) {
		this.uriInfo = uriInfo;
		this.request = request;
		this.id = id;
	}

	// Application integration
	@GET
	@Produces({ MediaType.TEXT_XML, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public SensorActuator getSensor() {
		// EJERCICIO:
		// Obtener de la base de datos el objeto SensorActuador con
		// identificador id
		SensorActuator sa = DaoSensorsActuators.instance.getModel().get(id);
		return sa;
	}

	@GET
	@Path("wait")
	@Produces({ MediaType.TEXT_XML, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public SensorActuator getSensorWaited(@DefaultValue("0.0") @QueryParam("value") double oldValue,
			@DefaultValue("0.0") @QueryParam("error") double error,
			@DefaultValue("500") @QueryParam("delay") int minDelay) {
		try {
			Thread.sleep(minDelay); // Tiempo minimo de espera antes de
									// responder
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// EJERCICIO:
		// Obtener de la base de datos el objeto SensorActuador con
		// identificador id
		SensorActuator sa = DaoSensorsActuators.instance.getModel().get(id);
		if (sa == null)
			throw new RuntimeException("Get: Sensor/actuator " + id + " not found");
		// EJERCICIO:
		// Invocar el metodo correspondiente del objeto sa para
		// esperar a que el valor actual difiera del anterior (oldValue) al
		// menos la cantidad "error"
		sa.waitForValue(oldValue, error);
		return sa;
	}

	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String newValor(@FormParam("new_value") String new_value, @Context HttpServletResponse servletResponse)
			throws IOException {
		double doubleValue = 0.0;
		try {
			doubleValue = Float.parseFloat(new_value);
		} catch (NumberFormatException e) {
			return "valor incorrecto";
		}

		// EJERCICIO:
		// Obtener de la base de datos el objeto SensorActuador con
		// identificador id
		SensorActuator sa = DaoSensorsActuators.instance.getModel().get(id);
		if (sa == null)
			return "No existe el sensor/actuador con identificador: " + id;
		// EJERCICIO:
		// Actualizar el sensor con su nuevo valor doubleValue invocando el
		// metodo correspondiente
		sa.setDoubleValue(doubleValue);
		return "Valor actualizado";
	}

	@POST
	@Path("SendToArduino")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String newOrden(@FormParam("new_value") String new_value, @Context HttpServletResponse servletResponse)
			throws IOException {
		// EJERCICIO:
		// Insertar la nueva orden (id+"="+new_value) en la lista ordenada de la
		// base de datos invocando el metodo correspondiente
		DaoSensorsActuators.instance.addOrdenParaArdunio(id+"="+new_value);
		
		return "Orden encolada para enviar a Arduino";
	}

	@PUT
	  @Consumes(MediaType.APPLICATION_XML)
	  public Response putSensor(JAXBElement<SensorActuator> sensorActuator) {
		// Si el sensor existe se sobreescribe.
		// Put es idempotente: el resultado es el mismo independientemente del número de veces que se haga la misma llamada.  		  
	    SensorActuator sa_recibido = sensorActuator.getValue();
	    String id = sa_recibido.getId(); 
		Response res;
		//EJERCICIO:
		//Obtener de la base de datos el objeto SensorActuador con identificador id
		SensorActuator sa = DaoSensorsActuators.instance.getModel().get(id);
		if(sa==null){ //Creamos un nuevo sensor y lo insertamos en el DAO
		 //EJERCICIO:
		 //Crear un nuevo objeto sa (new) de clase SensorActuator indicando los argumentos necesarios para el constructor 
		 
	     sa = new SensorActuator(id, sa_recibido.getFecha(), sa_recibido.getDoubleValue(), sa_recibido.getStringValue() ,sa_recibido.getDescription());
	     DaoSensorsActuators.instance.getModel().put(id, sa); //Se inserta el nuevo objeto con identificador id en la base de datos		  
		 res = Response.created(uriInfo.getAbsolutePath()).build();
	    } else { // Ya existe el sensor. Se sobreescribe:
	       sa.setFecha(sa_recibido.getFecha());
	       sa.setDoubleValue(sa_recibido.getDoubleValue());
	       sa.setStringValue(sa_recibido.getStringValue());
	       sa.setDescription(sa_recibido.getDescription());	  
		   res = Response.noContent().build();	  
		}		
	    return res;
	  }

	@DELETE
	public void deleteSensor() {
		if (DaoSensorsActuators.instance.getModel().containsKey(id))
			DaoSensorsActuators.instance.getModel().remove(id);
	}

}
