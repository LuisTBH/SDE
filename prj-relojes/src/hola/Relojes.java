package hola;

import java.io.*;

class Reloj extends Thread {
  int cuenta=0;
  
  public Reloj(String nombre, int cuenta) {
       super(nombre); this.cuenta=cuenta;
    }
  public void start() {
      System.out.println(getName() + ": " +
        "Faltan " + cuenta + " seg. para la alarma");
      super.start();
    }
  public void run() {
    for (int i = 1; i <= cuenta; i++) {
      //EJERCICIO: Provoque un retraso de 1000 milisegundos 
	  //-----------------------
    	
    	try {
			sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			int contador = cuenta - i;
			System.out.println(contador);
    }
    System.out.println(getName() + ": Riiinnnng!!!");
  }
}

public class Relojes {
	public static void main(String[] args){
		//EJERCICIO: Cree dos instancias de la clase Reloj
		//-----------------------
        //-----------------------
		Reloj r1 = new Reloj("Reloj 1", 30);
		r1.start();
		
		Reloj r2 = new Reloj("Reloj 2", 10);
		r2.start();
		
	}
}