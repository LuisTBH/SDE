package client;

import java.io.*;
import interfaz.InterfazAccesoArduino;

public class ClienteRPCArduino {
  private static InterfazAccesoArduino AA; //Se define la variable AA de tipo interfaz de acceso a arduino 
  
  public static void main(String[] args) {

    if (args.length<2) {
        System.out.println("Usage: ClienteRPCArduino <host> <port#>");
        System.exit(1);
    }
	
	//EJERCICIO:
	//Instanciar la variable AA creando un objeto de clase AccesoArduinoStub
    AA = new AccesoArduinoStub(args[0], Integer.parseInt(args[1]));
	  
    BufferedReader stdIn = new BufferedReader( new InputStreamReader(System.in));
    PrintWriter stdOut = new PrintWriter(System.out);
    String input,output,fin;
    
    try {
    	input="";
    	fin="fin";
    	while(!input.equals(fin)) {
    		stdOut.println("Escriba orden para enviar a Arduino...");
    		stdOut.flush();
        	//EJERCICIO: Leer de teclado
    		input = stdIn.readLine();
		    //EJERCICIO: Enviar la orden al Arduino invocando el método correspondiente del objeto AA 
    		output = AA.enviarOrdenArduino(input);
    	    //EJERCICIO: Imprimir por pantalla el resultado obtenido
    		stdOut.println(output);
    		stdOut.flush();
        }  	
    } catch (IOException e) {
    	System.err.println("I/O failed for connection to host: "+args[0]);
    }
  }
}