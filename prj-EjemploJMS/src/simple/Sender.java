package simple;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.jms.TopicPublisher;
import javax.jms.TextMessage;


public class Sender {

    Context context = null;
    TopicConnectionFactory factory = null;
    TopicConnection connection = null;
    String factoryName = "ConnectionFactory";
    String destName = "dynamicTopics/Chat";
    Topic dest = null;
    TopicSession session = null;
    TopicPublisher publisher = null;
    String username ="Pepe";
    
	public Sender() throws Exception {
        try {
            // create the JNDI initial context
            Properties env = new Properties( );
            env.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
            env.put(Context.PROVIDER_URL, "tcp://localhost:61616");

            context = new InitialContext(env);

            // look up the ConnectionFactory
            factory = (TopicConnectionFactory) context.lookup(factoryName);

            // look up the Destination
            dest = (Topic) context.lookup(destName);

            // create the connection
            connection = factory.createTopicConnection();
            connection.setClientID(username);  
            // create the session
            session = connection.createTopicSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);

            // create the publisher
            publisher = session.createPublisher(dest);
            
            //connection.start();
        } catch (JMSException exception) {
            exception.printStackTrace();
        } catch (NamingException exception) {
            exception.printStackTrace();
        }
	}
	public void send(String text){
		try {
			System.out.println(text + "("+ connection.getClientID()+")");
			TextMessage m=(TextMessage)session.createTextMessage(text+"_");
			//publisher.publish(m);
			m.setStringProperty("conversacion", "particular");
			publisher.publish(m,
					javax.jms.DeliveryMode.PERSISTENT, 
					javax.jms.Message.DEFAULT_PRIORITY, 
					60000);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		try { 
			    publisher.close( ); 
				connection.close( );  
	    }catch (javax.jms.JMSException jmse){
				jmse.printStackTrace( ); 
		}
	}
	
    public static void main(String[] args) {

    	Sender s=null;
    	try {
			s=new Sender();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        for (int i=0;i<10;i++) {
        	s.send("Mensaje_("+i+")");
        }
        s.close();
        System.exit(0);
    }
    
}
