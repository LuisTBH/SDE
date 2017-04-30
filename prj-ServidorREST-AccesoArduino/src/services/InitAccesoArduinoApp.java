package services;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.glassfish.jersey.filter.LoggingFilter;

public class InitAccesoArduinoApp extends Application{
	
	 private HiloAccesoArduino hiloAccesoArduino = null;

     @Override
        public Set<Class<?>> getClasses() {
            return new HashSet<Class<?>>() {/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

			{
                System.out.println("Getting classes from InitAccesoArduinoApp...");
                // Add resources.
                add(SensorResource.class);
                add(SensorsResource.class);
                // Add LoggingFilter.
                //add(LoggingFilter.class);
                
                // Start additional active components
                if (hiloAccesoArduino == null) {
                	//Se crea y se lanza a ejecución el hilo encargado de enviar las ordenes a Arduino
                    //Solicitará periódicamente al Arduino los valores de sus sensores/actuadores para almacenarlos en el servidor web
                	
                	//hiloAccesoArduino = new HiloAccesoArduino("192.168.4.1", 80, 1000, "TODOS");
                	
                	hiloAccesoArduino = new HiloAccesoArduino("192.168.1.118", 80, 5000, "TODOS");
                	
                	 //Solicita cada segundo el valor de TODOS los sensores/actuadores del Arduino 
                }
                if (!hiloAccesoArduino.isAlive()) {
                	System.out.println("InitAccesoArduinoApp: iniciando hilo para acceso a Arduino");
                	hiloAccesoArduino.start();
                }
            }};
        }
}