package hola;

import java.io.*;

public class EntradaSalida{

  public static void main(String args[]) throws IOException{
    int j;
    byte[] buffer = new byte[80];
    String filename, filename2;
    float f1 = (float) 3.1416;
    float f2 = 0;

    try {
	    //-----------------------------------
        //E/S con InputStream y OutputStream
		//-----------------------------------
        System.out.println("Teclee una cadena");
        j = System.in.read(buffer);
        System.out.print("La cadena: ");
        System.out.write(buffer,0,j);
        //Convertimos cadena de bytes a cadena de caracteres (2 bytes)
        String tira = new String(buffer,0,j-2);
        System.out.println("Otra vez la cadena: " + tira);

		//-----------------------------------
        //E/S con BufferedReader y PrintWriter
        //Conveniente con cadenas de caracteres (1 caracter = 2 bytes)
		//-----------------------------------
        BufferedReader stdIn = new BufferedReader( new InputStreamReader(System.in));
        PrintWriter stdOut = new PrintWriter(System.out);
        System.out.println("Teclee un entero");
        //Se lee un entero por teclado y se imprime en pantalla
        stdOut.write(stdIn.readLine());
        stdOut.flush();
        System.out.println("\nTeclee un nombre para un fichero");
        //EJERCICIO: Lea de teclado una cadena para el nombre del fichero 
        //     	     y almacénela en la variable filename 
        //........................
        
        filename = stdIn.readLine();
		
		//-----------------------------------
	    //E/S con ficheros y floats en formato numérico
        //-----------------------------------
        DataOutputStream fout1 = new DataOutputStream(new FileOutputStream(filename));
        DataInputStream fin1 = new DataInputStream(new FileInputStream(filename));
        fout1.writeFloat(f1); //Escribe un float en el fichero filename (en formato binario) 
        fout1.flush();
        f2= fin1.readFloat();  //Lee el float que se ha escrito en el fichero filename 
        System.out.println("Escrito y leido el float: " +f2+ " del fichero: " +filename+"\n");
		fout1.close();
        fin1.close();
		
	    //-----------------------------------
        //E/S con ficheros y floats en formato de texto
		//-----------------------------------
        filename2=filename + ".txt";
        System.out.println("Fichero: "+filename2);
        PrintWriter fout2 = new PrintWriter(new FileOutputStream(filename2));
        BufferedReader fin2 = new BufferedReader(new InputStreamReader(new FileInputStream(filename2)));
        fout2.println(f1);//fout2.println(new Float(f1).toString());
		fout2.flush();
        f2=Float.valueOf(fin2.readLine()).floatValue();
        System.out.println("Escrito y leido el float: " +f2+ " del fichero: " +filename2);     
        fout2.close();
        fin2.close();

	 } catch (IOException e) {
	       System.out.println("Error en E/S");
	       System.exit(1);
	 }
   }
}
