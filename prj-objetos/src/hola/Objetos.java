package hola;

import java.io.*;

class Punto {
	public int x = 0;
	public int y = 0;

	public Punto(int x, int y) {
		this.x = x;
		this.y = y;
	}
}

class Rectangulo {
	protected Punto origen;
	protected int ancho = 0;
	protected int alto = 0;
	private static String descripcionClase = "Rectangulo definido mediante un punto de origen y un ancho y un alto que puede moverse";

	public Rectangulo(int origenx, int origeny, int ancho, int alto) {
		origen = new Punto(origenx, origeny);
		this.ancho = ancho;
		this.alto = alto;
	}

	public Rectangulo(Punto p, int ancho, int alto) {
		origen = p;
		this.ancho = ancho;
		this.alto = alto;
	}

	public Rectangulo() {
		origen = new Punto(0, 0);
		this.ancho = 0;
		this.alto = 0;
	}

	public int ancho() {
		return ancho;
	}

	public int alto() {
		return alto;
	}

	public int area() {
		return (ancho * alto);
	}

	public void mover(int arriba, int derecha) {
		origen.x += arriba;
		origen.y += derecha;
	}

	public String toString() {
		return "(Origen: {" + Integer.toString(origen.x) + "," + Integer.toString(origen.y) + "}, Final: {"
				+ Integer.toString(origen.x + ancho) + "," + Integer.toString(origen.y + alto) + "})";
	}

	public static String descripcion() {
		return descripcionClase;
	}
}

class RectanguloColor extends Rectangulo {
	int color;

	public RectanguloColor(Punto p, int ancho, int alto, int color) {
		super(p, ancho, alto);
		this.color = color;
	}
	
/*	public RectanguloColor(int origenx, int origeny, int ancho, int alto, int color) {
		origen = new Punto(origenx, origeny);
		this.ancho = ancho;
		this.alto = alto;
		this.color = color;
	}*/

	public String toString() {
		// EJERCICIO: Sobrecargue este método para que incluya en el String
		// también el color
		// COMPLETADO!!!!!
		// .....................
		return super.toString() + ", Color: " + Integer.toString(color) + "";
		//return "(Origen: {" + Integer.toString(origen.x) + "," + Integer.toString(origen.y) + "}, Final: {"
		//+ Integer.toString(origen.x + ancho) + "," + Integer.toString(origen.y + alto) + "}, Color: " + color + ")";
	}
}
public class Objetos {
	static Rectangulo R;
	static RectanguloColor RC;

	public static void main(String args[]) throws IOException {
		if (args.length < 4) {
			System.out.println("Uso: Objetos origen-x origen-y ancho alto");
			System.exit(1);
		}

		int[] i = new int[4];
		int j = 0;
		for (j = 0; j < i.length; j++)
			i[j] = Integer.parseInt(args[j]);

		R = new Rectangulo(i[0], i[1], i[2], i[3]);

		// EJERCICIO: Cree una instancia de rectángulo color RC que añada a R el
		// atributo de color.
		// .....................
		// .....................
		Punto puntoColor = new Punto(i[0],i[1]);
		int numeroColor = Integer.parseInt(args[4]);
		RC = new RectanguloColor(puntoColor, i[2], i[3],numeroColor);		
		

		System.out.println("Nombre de la clase de R: " + R.getClass().getName());
		System.out.println("Descripción de la clase de R: " + Rectangulo.descripcion());
		System.out.println("Area de R: " + R.area());
		System.out.println("R: " + R.toString());
		System.out.println("RC: " + RC.toString());

		// EJERCICIO: Invoque el método mover(10,10) sobre R
		// .....................
		
		R.mover(10, 10);
		// EJERCICIO: Invoque el método toString sobre R y RC y visualicelos por
		// pantalla el resultado
		// .....................
		// .....................
		System.out.println("Despues de moverse R");
		System.out.println("R: " + R.toString());
		System.out.println("RC: " + RC.toString());

		// PREGUNTA: Se ha "movido" R? y RC? Debería haberse "movido" RC?
		// Explique convenientemente este aspecto.
		
		//No deberia haberse movido RC porque son dos instancias de objetos distintas, aunque la clase
		//RectanguloColor extienda de Rectangulo no quiere decir que sean el mismo objeto. Sino que uno hereda
		//los atributos y métodos de la clase padre, pudiendo tener otros constructores y añadiendo nuevos métodos
		//y atributos.
	}
}
