package datamodel;

//import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public enum DaoSensorsActuators {
	instance;
	  private List ListaOrdenesParaArduino = Collections.synchronizedList(new LinkedList());;
	  private Map<String, SensorActuator> contentProvider = new HashMap<String, SensorActuator>();
	  
	  private DaoSensorsActuators() { 
		//Se crea un objeto para guardar las respuestas de Arduino cuando se envian las ordenes solicitadas por los clientes
		SensorActuator respuestasArduino = new SensorActuator("RespuestasArduino","",0.0,"","Objeto con la ultima respuesta de Arduino");
	    contentProvider.put("RespuestasArduino", respuestasArduino); 
	    //Los objetos sensores/actuadores se crearan dinamicamente en el HiloAccesoArduino que recibe de Arduino los valores
		//para todos los sensores/actuadores de dicho Arduino  
	  }
	  
	  public Map<String, SensorActuator> getModel(){
	    return contentProvider;
	  }

	  public void addOrdenParaArdunio(String orden) {
          synchronized (ListaOrdenesParaArduino) {
		    	 ListaOrdenesParaArduino.add(orden);
		    	 ListaOrdenesParaArduino.notifyAll();
		  }
	   }
		    
	  public String getOrdenParaArduino(int timeout) throws InterruptedException {
		  synchronized (ListaOrdenesParaArduino) {
		       if (ListaOrdenesParaArduino.isEmpty())ListaOrdenesParaArduino.wait(timeout);
		       if (ListaOrdenesParaArduino.isEmpty())return null; //Se sale por timeout, sin ninguna orden para arduino   
		       return (String) ListaOrdenesParaArduino.remove(0);
		  }
	  }
	    
}
