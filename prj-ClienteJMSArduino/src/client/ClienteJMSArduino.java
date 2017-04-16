package client;

import java.io.*;

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

public class ClienteJMSArduino implements javax.jms.MessageListener{

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
    TopicSubscriber subscriber_Valores_Arduino = null;
    TopicPublisher publisher_Orden_Para_Arduino = null;
    TopicSubscriber subscriber_Respuesta_De_Arduino = null;
    String username ="clienteJMS";
	 
    public ClienteJMSArduino() throws Exception {
    	try {
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
			
            // create the publisher
			//EJERCICIO:
			//Crear un publisher para el topic Topic_Orden_Para_Arduino
            publisher_Orden_Para_Arduino = pub_session.createPublisher(Topic_Orden_Para_Arduino);
       
            // create the receivers
			//EJERCICIO:
			//Crear dos subscribers, uno para el topic Topic_Respuesta_De_Arduino y otro para el topic Topic_Valores_Arduino
            subscriber_Respuesta_De_Arduino = subs_session.createDurableSubscriber(Topic_Respuesta_De_Arduino, "Respuesta Durable");
            subscriber_Valores_Arduino = subs_session.createDurableSubscriber(Topic_Valores_Arduino, "Valores Durable");         
            subscriber_Respuesta_De_Arduino.setMessageListener(this);
            subscriber_Valores_Arduino.setMessageListener(this);
			
            // start the connection, to enable message receipt
            connection.start();
        } catch (JMSException exception) {
            exception.printStackTrace();
        } catch (NamingException exception) {
            exception.printStackTrace();
        } 
    }
    
	public void onMessage(Message men) {
		try { 
			TextMessage tM_men = (TextMessage) men; 
			String s_men = tM_men.getText( );
			//Visaliza por consola los valores/mensajes recibidos para los topics subscritos:
			System.out.println(s_men);
		} catch (JMSException jmse){ jmse.printStackTrace( ); }
	}

    public void send_orden_para_arduino(String orden){
		try {
			TextMessage orden_para_arduino=(TextMessage)pub_session.createTextMessage(orden);
			//EJERCICIO:
			//Publicar la orden para el Arduino en el topic correspondiente
			publisher_Orden_Para_Arduino.publish(orden_para_arduino);
			
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
     
    public void close() {
		try { 
			    publisher_Orden_Para_Arduino.close();
			    subscriber_Respuesta_De_Arduino.close();
			    subscriber_Valores_Arduino.close();
				connection.close();
	    }catch (javax.jms.JMSException jmse){
				jmse.printStackTrace( ); 
		}
	}
    
    public static void main(String[] args) {
      ClienteJMSArduino ClientArduino = null;
      try {
    	  ClientArduino = new ClienteJMSArduino();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
      
      BufferedReader stdIn = new BufferedReader( new InputStreamReader(System.in));
	  PrintWriter stdOut = new PrintWriter(System.out);
	  String input,fin;
	    
	    try {    	
	    	input="";
	    	fin="fin";
	    	while(!input.equals(fin)) {
	    		stdOut.println("Escriba orden para enviar a Arduino...");
	    		stdOut.flush();
				//EJERCICIO: Leer de teclado
	    		input = stdIn.readLine();
		        //EJERCICIO: Enviar la orden al Arduino invocando el método send_orden_para_arduino de ClientArduino
	    		ClientArduino.send_orden_para_arduino(input);
    	    
	        }  	
	    } catch (IOException e) {
	    	System.err.println("I/O failed for connection to host: "+args[0]);
	    }
    }

}