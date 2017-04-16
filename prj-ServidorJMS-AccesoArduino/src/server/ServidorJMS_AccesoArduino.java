package server;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.jms.TextMessage;

public class ServidorJMS_AccesoArduino implements javax.jms.MessageListener{
	private static AccesoArduino AA; //AA es el objeto para acceder a Arduino que implementa el metodo enviarOrdenArduino
	String orden_solicitar_todos_los_valores = "TODOS";

	Context context = null;
	TopicConnectionFactory factory = null;
    TopicConnection connection = null;
	String factoryName = "ConnectionFactory";
	String s_Topic_Valores_Arduino = "dynamicTopics/Valores_Arduino";
	String s_Topic_Orden_Para_Arduino = "dynamicTopics/Orden_Para_Arduino";
	String s_Topic_Respuesta_De_Arduino = "dynamicTopics/Respuesta_De_Arduino";
    Topic Topic_Valores_Arduino = null;
    Topic Topic_Orden_Para_Arduino = null;
    Topic Topic_Respuesta_De_Arduino = null;
    TopicSession subs_session = null;
    TopicSession pub_session = null;
    TopicSubscriber subscriber_Orden_Para_Arduino = null;
    TopicPublisher publisher_Respuesta_De_Arduino = null;
    TopicPublisher publisher_Valores_Arduino = null;
    String username ="serverJMS";
	  
    private List ListaOrdenesParaArduino;
    
    public ServidorJMS_AccesoArduino() throws Exception {
    	try {
    		ListaOrdenesParaArduino = Collections.synchronizedList(new LinkedList());
    		
            // create the JNDI initial context
            Properties env = new Properties( );
            env.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
            env.put(Context.PROVIDER_URL, "tcp://localhost:61616");
            
            context = new InitialContext(env);

            // look up the ConnectionFactory
            factory = (TopicConnectionFactory) context.lookup(factoryName);

            // look up the topics
			//EJERCICIO:
			//Obtener con lookup los tres topics utilizados por el programa
            Topic_Valores_Arduino = (Topic) context.lookup(s_Topic_Valores_Arduino);
            Topic_Orden_Para_Arduino = (Topic) context.lookup(s_Topic_Orden_Para_Arduino);
            Topic_Respuesta_De_Arduino = (Topic) context.lookup(s_Topic_Respuesta_De_Arduino);
       
            // create the connection
            connection = factory.createTopicConnection();
            connection.setClientID(username); 
			
            // create the sessions
			//EJERCICIO:
			//Crear una sesion para publicar en topics y otra sesion para subscribirse a topics
            subs_session = connection.createTopicSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);
            pub_session = connection.createTopicSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);
			
            // create the publishers
			//EJERCICIO:
			//Crear dos publicadores, uno para el topic Topic_Respuesta_De_Arduino y otro para el topic Topic_Valores_Arduino
            publisher_Respuesta_De_Arduino = pub_session.createPublisher(Topic_Respuesta_De_Arduino);
            publisher_Valores_Arduino = pub_session.createPublisher(Topic_Valores_Arduino);
       
            // create the receiver
            //EJERCICIO:
			//Crear un subscriptor al topic Topic_Orden_Para_Arduino
            subscriber_Orden_Para_Arduino = subs_session.createDurableSubscriber(Topic_Orden_Para_Arduino, "Orden Durable");
            subscriber_Orden_Para_Arduino.setMessageListener(this);
			
            // start the connection, to enable message receipt
            connection.start();
        } catch (JMSException exception) {
            exception.printStackTrace();
        } catch (NamingException exception) {
            exception.printStackTrace();
        } 
    
      //Se crea y se lanza a ejecución el hilo encargado de enviar las ordenes a Arduino y de publicar sus respuestas en el MOM
      //Solicitará periódicamente al Arduino los valores de sus sensores para publicarlos en el MOM
      new ServerThread(3000,orden_solicitar_todos_los_valores).start(); //Solicita el valor de todos los sensores/actuadores cada 3 segundos	
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
    
	public void onMessage(Message m_orden_Para_Arduino) {
		try { 
			TextMessage tM_orden_Para_Arduino = (TextMessage) m_orden_Para_Arduino; 
			String s_orden_Para_Arduino = tM_orden_Para_Arduino.getText( );
			//EJERCICIO:
			//Añadir la orden recibida en la lista de órdenes a enviar a Arduino
			addOrdenParaArdunio(s_orden_Para_Arduino);
			
		} catch (JMSException jmse){ jmse.printStackTrace( ); }
	}

    public void send_respuesta_de_arduino(String respuesta){
		try {
			TextMessage respuesta_de_arduino=(TextMessage)pub_session.createTextMessage(respuesta);
			//EJERCICIO:
			//Publicar la respuesta de Arduino en el topic correspondiente
			publisher_Respuesta_De_Arduino.publish(respuesta_de_arduino);
			
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
    
    public void send_valores_recibidos_de_arduino(String valores){
		try {
			TextMessage valores_recibidos_de_arduino=(TextMessage)pub_session.createTextMessage(valores);
			//EJERCICIO:
			//Publicar los valores de Arduino en el topic correspondiente
			publisher_Valores_Arduino.publish(valores_recibidos_de_arduino);
				
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
    
    public void close() {
		try { 
			    subscriber_Orden_Para_Arduino.close();
			    publisher_Respuesta_De_Arduino.close();
			    publisher_Valores_Arduino.close();
				connection.close();
	    }catch (javax.jms.JMSException jmse){
				jmse.printStackTrace( ); 
		}
	}
    
    public static void main(String[] args) {

     if (args.length<2) {
 	        System.out.println("Usage: ServidorJMS_AccesoArduino <host> <port#>");
 	        System.exit(1);
 	 }
 	 AA = new AccesoArduino(args[0],Integer.parseInt(args[1])); //Se instancia el objeto AccesoArduino que implementa el metodo enviarOrdenArduino

      System.out.println("Enviará los mensajes del topic Orden_Para_Arduino al Arduino con IP: " + args[0] + " y puerto: " + args[1]);

      try {
    	  new ServidorJMS_AccesoArduino();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
   }

//----------------------------------------------------------------------------
//  inner class ServerThread
//----------------------------------------------------------------------------
  class ServerThread extends Thread {
    int periodo = 3000;
    String solicitud = "TODOS";
    
    ServerThread(int periodo, String solicitud) {
        super("ServerThread");
        this.periodo = periodo;
        this.solicitud = solicitud;
    }

    public void run() {
      String valores_Arduino;
      String orden, s_respuesta_de_Arduino;
      System.out.println("Enviando los valores del Arduino al MOM y esperando ordenes del MOM para enviar al Arduino...");

      while (true) {
    	try {
			while ((orden=getOrdenParaArduino(periodo))!=null)
			  {
			    //EJERCICIO:
				//Enviar la orden al Arduino invocando el método enviarOrdenArduino del objeto AA
			    s_respuesta_de_Arduino =  AA.enviarOrdenArduino(orden);
			    //System.out.println("Enviada orden: " + orden+ " y recibida respuesta:" + s_respuesta_de_Arduino);
			    send_respuesta_de_arduino(s_respuesta_de_Arduino);
			    sleep(100);//Tiempo mínimo entre envíos a Arduino
			  }
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		//EJERCICIO:
	    //Solicitar al Arduino todos los valores de sus sensores
		//enviandole la orden "TODOS" invocando el método enviarOrdenArduino del objeto AA
        valores_Arduino = AA.enviarOrdenArduino("TODOS") ;
        //System.out.println("Recibido: " + valores_Arduino);
    	send_valores_recibidos_de_arduino(valores_Arduino);
    	try {
			sleep(100); //Tiempo mínimo entre envíos a Arduino
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      }
    }
    
  }

}