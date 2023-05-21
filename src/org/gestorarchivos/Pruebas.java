package org.gestorarchivos;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Pruebas {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		String archivo = "equisde.txt";
		String directorioI = "C:\\Users\\Alejandro\\Downloads\\daw\\Moved";
		String directorioF = "C:\\Users\\Alejandro\\Downloads\\wxmx";
		String tipo = "txt";

		GestorArchivos.setDirectorio(directorioF);
		// GestorArchivos.eliminarRepetidos();

		// Prueba con expresiones regulares
		String cadena = "soyUnaPatata5.exe";

		// Pattern pat = Pattern.compile(".*[(\\\\d)]");

		/*
		 * (.*) captura cualquier carácter que aparezca antes del punto ([^\\.]+)
		 * captura una secuencia de caracteres que no incluyan un punto y que aparezcan
		 * después del punto, lo cual representa la extensión. La marca $ al final de la
		 * expresión regular asegura que la coincidencia se encuentre al final del
		 * nombre del archivo.
		 */

		/*
		 * Scanner sc = new Scanner(System.in);
		 * 
		 * System.out.println("Escribe una cadena "); String cadena2 = sc.next();
		 * System.out.println(cadena2);
		 */
		String[] palabras = new String[3];
		palabras[0] = "Relacion MD1.txt";
		palabras[1] = "Relacion MD1 (1).txt";
		palabras[2] = "Relacion MD2 (1).txt";

		// Pattern pat = Pattern.compile(".*[(][(\\d][)]");
		// Matcher mat = pat.matcher(palabras[0]);

		// Pattern pat = Pattern.compile("(.*)\\\\.([^\\\\.]+)$");
		// Matcher mat = pat.matcher(palabras[0]);

		String fileName3 = "Práctica 3 - copia";

		String pattern3 = "(.*) \\- copia$";
		
		Pattern r3 = Pattern.compile(pattern3);
		Matcher m3 = r3.matcher(fileName3);
		
		if(m3.find()) {
			System.out.println("SI");
			System.out.println(m3.group(1));
		}else System.out.println("NO");
		
		
		
		/*
		
		
		String fileName = "MD (1)";
		String fileName2 = "MD (2)";

		String pattern = "(.*) \\(([0-9]+)\\)";

		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(fileName);

		Pattern r2 = Pattern.compile(pattern);
		Matcher m2 = r.matcher(fileName2);
				
		if (m.find()) {
		  System.out.println("Nombre base: " + m.group(1));
		  System.out.println("Número de repetición: " + m.group(2));
		  
		  if(m2.find()) {
			  System.out.println("Se ejecuta el find del archivo 2");
			  
			  if(m.group(1).contentEquals(m2.group(1)))
				  System.out.println("Son iguales");
			  
		  }
		  
		} else {
		  System.out.println("No se encontró ninguna repetición");
		}

		/*
		if (m.find()) {
			System.out.println("Nombre base: " + m.group(1));
			System.out.println("Número de repetición: " + m.group(2));

			if (nombrearchivo1 == nombrearchivo2) {
				System.out.println("Son repetidos, podemos eliminar uno");
			}

		} else {
			System.out.println("No se encontró ninguna repetición");
		}

		/*
		 * 
		 * if (mat.matches()) { System.out.println("SI"); } else {
		 * System.out.println("NO"); }
		 * 
		 * if (mat.find()) { System.out.println("Tipo de archivo: " + mat.group(1)); }
		 * else { System.out.println("No se pudo identificar el tipo de archivo"); }
		 * 
		 * /* String fileName = "P.r.a.c.t.i.ca 1.wxmx"; String pattern =
		 * "(.*)\\.([^\\.]+)$";
		 * 
		 * Pattern r = Pattern.compile(pattern); Matcher m = r.matcher(fileName);
		 * 
		 */

		/*
		 * Usar expresiones regulares para organizar una carpeta entera con TODOS los
		 * tipos que haya
		 */
		/*
		 * Elimine todos los archivos repetidos iterando por carpetas de un directorio
		 */

	}

}
