package simple;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.jms.TextMessage;


public class Receiver implements javax.jms.MessageListener{

    Context context = null;
    TopicConnectionFactory factory = null;
    TopicConnection connection = null;
    String factoryName = "ConnectionFactory";
    String destName = "dynamicTopics/Chat";
    Topic dest = null;
    TopicSession session = null;
    TopicSubscriber subscriber = null;
    String username ="Paco";
    int count =0;
    
	public Receiver() throws Exception {
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

            // create the receiver
            subscriber = session.createDurableSubscriber(dest,"ChatDurable");
            //subscriber = session.createSubscriber(dest);
            subscriber.setMessageListener(this);
            // start the connection, to enable message receipt
            connection.start();
        } catch (JMSException exception) {
            exception.printStackTrace();
        } catch (NamingException exception) {
            exception.printStackTrace();
        }
	}
	public void close() {
		try { 
			    subscriber.close( ); 
			    session.unsubscribe("ChatDurable");
				connection.close( );
	    }catch (javax.jms.JMSException jmse){
				jmse.printStackTrace( ); 
		}
	}
	public void onMessage(Message message) {
		try { 
			TextMessage textMessage = (TextMessage) message; 
			String text = textMessage.getText( ); 
		    System.out.println("Recibido: "+text+"("+textMessage.getStringProperty("conversacion")+")"+"("+connection.getClientID()+")");
		} catch (JMSException jmse){ jmse.printStackTrace( ); }
		count++;
	}
	
    public static void main(String[] args) {

    	try {
			Receiver r=new Receiver();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    } 
}
