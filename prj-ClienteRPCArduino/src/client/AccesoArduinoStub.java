package client;

import java.net.*;
import java.io.*;

import interfaz.*;

public class AccesoArduinoStub implements InterfazAccesoArduino {

  private Socket m_Socket = null;
  private PrintWriter pr = null;
  private BufferedReader br = null;
  private String host = "localhost";
  private int port;
  private String output = "Error";
  private boolean connected  = false;
  
  public AccesoArduinoStub(String host, int port) {
    this.host= host; this.port =port; //host y port del servidorRPC
  }

  public String enviarOrdenArduino(String input)
  {
    connect();
    if (m_Socket != null && pr != null && br != null) {
  	try {
	         //Se envia la cadena "input" al servidorRPC
             pr.println(input); 
             pr.flush();
			 //Se lee la respuesta del servidorRPC y se asigna a la variable output
             output= br.readLine();
      } catch (IOException e) {
        System.err.println("I/O failed in reading/writing socket");
      }
    }
    disconnect();
    return output;
  }

  private synchronized void connect()
  { //Conecta con el servidorRPC
	//EJERCICIO: Implemente el método connect
	  try {
		m_Socket = new Socket(host, port);
		br = new BufferedReader(new InputStreamReader(m_Socket.getInputStream()));
		pr = new PrintWriter(m_Socket.getOutputStream());
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
  }

  private synchronized void disconnect(){ 
	//EJERCICIO: Implemente el método disconnect
	 try {
		m_Socket.close();
		br.close();
		pr.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	 
  }
}
