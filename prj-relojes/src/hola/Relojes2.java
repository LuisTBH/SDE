package hola;

import java.io.*;

class Reloj2 implements Runnable {
	int cuenta = 0;
	Thread hilo_reloj;

	public Reloj2(String nombre, int cuenta) {
		this.cuenta = cuenta;
		hilo_reloj = new Thread(this, nombre);
	}

	public void start() {
		System.out.println(hilo_reloj.getName() + ": " + "Faltan " + cuenta + " seg. para la alarma");
		hilo_reloj.start();

	}

	public void run() {
		for (int i = 1; i <= cuenta; i++) {
			// EJERCICIO: Provoque un retraso de 1000 milisegundos
			// -----------------
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			int contador = cuenta - i;
			System.out.println(contador);
		}
		System.out.println(hilo_reloj.getName() + ": Riiinnnng!!!");
	}
}

public class Relojes2 {
	public static void main(String[] args) {
		// EJERCICIO: Cree dos instancias de la clase Reloj2
		// -----------------------
		// -----------------------
		Reloj2 r1 = new Reloj2("Reloj 1", 20);
		r1.start();
		
		Reloj2 r2 = new Reloj2("Reloj 2", 10);
		r2.start();
	}
}