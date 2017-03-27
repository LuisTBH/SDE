package server;

import interfaz.InterfazAccesoArduino;

import java.net.*;

import server.AccesoArduino;

import java.io.*;

public class ServidorMultihiloRPC_AccesoArduino {
	private static InterfazAccesoArduino AA; //Se define la variable AA de tipo interfaz de acceso a arduino 
	private static ServerSocket serverSocket = null;

    public static void main(String[] args) {

     if (args.length<2) {
 	        System.out.println("Usage: ServidorRPC_AccesoArduino <host> <port#>");
 	        System.exit(1);
 	 }
	 //EJERCICIO:
	 //Instanciar la variable AA creando un objeto de clase AccesoArduino
     AA = new AccesoArduino(args[0], Integer.parseInt(args[1]));    	
 	 InetAddress miIP = null;	
 	 try
 	 {
      InetAddress localhost = InetAddress.getLocalHost();
 	  InetAddress[] direcciones = InetAddress.getAllByName(localhost.getCanonicalHostName());
 	  if (direcciones.length > 1)
 	   {
 		System.out.println("Se dispone de las siguientes IP:");
 		for (int i=0;i<direcciones.length;i++)
 	    System.out.println(i+": " + direcciones[i].toString());
 		System.out.println("¿Por cuál se recibirán las conexiones de los clientes? ");
 		BufferedReader stdIn = new BufferedReader( new InputStreamReader(System.in));
        String seleccion = stdIn.readLine();
 		miIP = direcciones[Integer.valueOf(seleccion).intValue()];
 	   }
 	  else miIP =  InetAddress.getLocalHost(); // miIP = direcciones[0];
 	  }catch (UnknownHostException e){
 		  System.out.println("Error al obtener las direcciones IP locales. " + e.toString());
 		  System.exit(1);
 	  }catch (IOException e){		  
 	  }
 	  
 	  try {
          serverSocket = new ServerSocket(4000,0,miIP);
      } catch (IOException e) {
        System.out.println(miIP.getHostAddress() + ": could not listen on port 4000, " + e.toString());
        System.exit(1);
      }
      System.out.println("ServidorMultihiloRPC_AccesoArduino escuchando por el puerto 4000 y la IP: " + miIP.getHostAddress());
      System.out.println("Enviará los mensajes de los clientes al puerto " + args[1] + " del arduino con IP: " + args[0]);

      boolean listening = true;
      while (listening) {
    	try {
			//EJERCICIO: aceptar una nueva conexión 
    		Socket clientSocket = null;
    		clientSocket = serverSocket.accept();
    	    //EJERCICIO: y crear un hilo MultiServerThread para que la gestione
    		new MultiServerThread(clientSocket, AA).start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     }

     try {
          serverSocket.close();
     } catch (IOException e) {
          System.err.println("Could not close server socket." + e.getMessage());
     }
   }
}

//----------------------------------------------------------------------------
//  class EchoMultiServerThread
//----------------------------------------------------------------------------

class MultiServerThread extends Thread {
	private static InterfazAccesoArduino AA;
    private Socket clientSocket = null;
    private BufferedReader br = null;
    private PrintWriter pr = null;
    private String inputline = new String();
    String output;

    MultiServerThread(Socket socket, InterfazAccesoArduino AA) {
        super("EchoMultiServerThread");
        clientSocket = socket;
        MultiServerThread.AA = AA;
        try {
		    //EJERCICIO: crear el BufferedReader asociado 
        	
        	BufferedReader br = new BufferedReader( new InputStreamReader(clientSocket.getInputStream()));
    	    //EJERCICIO: crear el PrintWriter asociado	
        	
        	PrintWriter pr = new PrintWriter(clientSocket.getOutputStream());
        } catch (IOException e) {
            System.err.println("Error sending/receiving" + e.getMessage());
            e.printStackTrace();
        }
    }

    public void run() {
       try {
            while ((inputline = br.readLine()) != null) {
			 //EJERCICIO: Enviar la orden al Arduino invocando el método correspondiente del objeto AA 
            inputline = br.readLine();
            //pr.println(inputline);
            //pr.flush();
            output = AA.enviarOrdenArduino(inputline);
             //EJERCICIO: Devolver la respuesta por el socket  
            pr.println(output);
            pr.flush();
            }
            pr.close();
            br.close();
            clientSocket.close();
        } catch (IOException e) {
            System.err.println("Error sending/receiving: " + e.getMessage());
            e.printStackTrace();
        }
    }
}