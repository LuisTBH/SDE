package services;

import java.util.Date;

import datamodel.DaoSensorsActuators;
import datamodel.SensorActuator;

public class HiloAccesoArduino extends Thread {
	private static AccesoArduino AA; 
	public int periodo = 3000;
	public String id_recibir_todos_los_valores = "TODOS";
	private boolean running = true;
	
	public void stopHiloAccesoArduino() {running = false;}
	
	public HiloAccesoArduino(String IP_Arduino, int port, int periodo, String id_solicitud) {
        super("HiloAccesoArduino");
        this.periodo = periodo;
        this.id_recibir_todos_los_valores = id_solicitud;
		AA = new AccesoArduino(IP_Arduino,port);
	}
	
	public void run() {
		String orden, s_respuesta_de_Arduino;
		String nombre,s_new_valor, s_fecha;
		double d_new_valor;
		int indice_igual, indice_and;
	    SensorActuator sa;
	    
	    //System.out.println("Accediendo y almacenando los valores del Arduino y esperando ordenes para enviar al Arduino...");
		while (running) {		
		    try {
			  while ((orden=DaoSensorsActuators.instance.getOrdenParaArduino(periodo))!=null)
				  {
				    //EJERCICIO:
				    //Enviar la orden al Arduino invocando el método enviarOrdenArduino del objeto AA
				    s_respuesta_de_Arduino = AA.enviarOrdenArduino(orden);
				    s_fecha = new Date().toString();
				    //System.out.println("Enviada orden: " + orden+ " y recibida respuesta: " + s_respuesta_de_Arduino);
				    sa = DaoSensorsActuators.instance.getModel().get("RespuestasArduino");
				    if(sa!=null){
				     sa.setStringValue("Enviada orden: '" + orden+  "' y recibida respuesta: " + s_respuesta_de_Arduino);
				     sa.setFecha(s_fecha);
				    };
				    sleep(200);//Tiempo mínimo entre envíos a Arduino
				  }
		    } catch (InterruptedException e) {
			  e.printStackTrace();
		    }  
		
			//EJERCICIO:
	        //Solicitar al Arduino todos los valores de sus sensores/actuadores
		    //enviandole la orden id_recibir_todos_los_valores invocando el método enviarOrdenArduino del objeto AA
		    
		    //Podemos modificar para que no de problemas cuando se cae el hilo y nos salta el error de tomcat de I/O
		    s_respuesta_de_Arduino = AA.enviarOrdenArduino(id_recibir_todos_los_valores);
		    //Formato respuesta: nombre1=valor1[&nombre2=valor2......&nombreN=valorN]
		    s_fecha = new Date().toString();
		    indice_and = 0;
		    while (indice_and != -1){
		      indice_igual = s_respuesta_de_Arduino.indexOf("=");
		      nombre = s_respuesta_de_Arduino.substring(0, indice_igual);
		      indice_and = s_respuesta_de_Arduino.indexOf("&");
		      if (indice_and != -1) {
		    	 s_new_valor = s_respuesta_de_Arduino.substring(indice_igual+1, indice_and);
		    	 s_respuesta_de_Arduino = s_respuesta_de_Arduino.substring(indice_and+1);
		      }
		      else s_new_valor = s_respuesta_de_Arduino.substring(indice_igual+1);
		      try {
		    	d_new_valor = Double.valueOf(s_new_valor).doubleValue();	  
		      }catch (Exception e) {
		    	d_new_valor = 0.0;
			  }  
		      //En la base datos local, se actualiza el valor para cada nombre/objeto.
		      //El objeto se crea si no existe en el DAO. 
		      sa = DaoSensorsActuators.instance.getModel().get(nombre);
		      if(sa==null){ //Si el sensor/actuador no existe, se crea
			    //EJERCICIO:
				//Crear (new) el nuevo objeto SensorActuator indicando en el constructor
				//los parametros correspondientes: nombre,s_fecha,d_new_valor,s_new_valor,""
		 		sa = new SensorActuator(nombre, s_fecha, d_new_valor, s_new_valor ,"");
		 		DaoSensorsActuators.instance.getModel().put(nombre, sa);
		      }
		      else { //Si el sensor/actuador ya existe, se actualizan sus valores:
		    	sa.setStringValue(s_new_valor);
		    	sa.setDoubleValue(d_new_valor);
		    	sa.setFecha(s_fecha);
		      }
		    };

		    try {
				sleep(200); //Tiempo mínimo entre envíos a Arduino
			} catch (InterruptedException e) {
				e.printStackTrace();
			} 
		 } 
      }
}
