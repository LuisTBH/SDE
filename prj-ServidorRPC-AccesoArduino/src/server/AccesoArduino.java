package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import interfaz.InterfazAccesoArduino;

public class AccesoArduino implements InterfazAccesoArduino {
	private String host = "localhost";
	private int port = 80;
	private Socket m_Socket = null;
	private PrintWriter pr = null;
	private BufferedReader br = null;

	public AccesoArduino(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public synchronized String enviarOrdenArduino(String input) {
		String output = "error";
		try {
			// EJERCICIO: crear el socket especificando host y port

			m_Socket = new Socket(host, port);
			// EJERCICIO: crear el BufferedReader asociado
			br = new BufferedReader(new InputStreamReader(m_Socket.getInputStream()));
			// EJERCICIO: crear el PrintWriter asociado
			pr = new PrintWriter(m_Socket.getOutputStream());
			// EJERCICIO: enviar la cadena input por la conexión
			pr.println(input);
			pr.flush();
			// EJERCICIO: leer la respuesta y asignarla a la variable output
			output = br.readLine();
			m_Socket.close();
			br.close();
			pr.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("I/O failed in reading/writing socket");
			e.printStackTrace();
		}
		return output;
	}
}
