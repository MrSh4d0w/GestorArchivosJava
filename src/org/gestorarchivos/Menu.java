package org.gestorarchivos;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Menu {

	private static Scanner sc;

	public static void eleccionmenu(int decisionMenu) throws FileNotFoundException {
		Scanner sc = new Scanner(System.in);
		/* Variables necesarias para el funcionamiento del programa */
		String nombreBorrar = ""; /* Opcion 1 y 2 */
		String nombreQuerido = ""; /* Opcion 2 */
		//String directorio = null; /* Necesario para utilizar el programa */
		//String decisionComprimir = null;

		System.out.println("");

		switch (decisionMenu) {
		case 1: /* Eliminar primera parte del nombre de un archivo/os */
			System.out.println("Eliminaremos la primera parte del nombre un archivo o archivos");
			GestorArchivos.comprobarDirectorio();

			System.out.println("¿Que parte quieres eliminar?");
			nombreBorrar = sc.nextLine();
			GestorArchivos.setCriterio(nombreBorrar);

			GestorArchivos.deleteName();

			break;

		case 2: /* Sustituir primera parte del nombre de un archivo/os */
			System.out.println("Sustituiremos la primera parte del nombre un archivo o archivos");
			GestorArchivos.comprobarDirectorio();

			System.out.println("¿Que parte quieres eliminar?");
			nombreBorrar = sc.nextLine();
			GestorArchivos.setCriterio(nombreBorrar);
			System.out.println(GestorArchivos.getCriterio());
			
			System.out.println("¿Que quieres poner en su lugar?");
			nombreQuerido = sc.nextLine();
			System.out.println(nombreQuerido);

			GestorArchivos.replaceName(nombreQuerido); /* Usará el tipo indicado */

			break;

		case 3: /*
				 * Crear una carpeta para un tipo de archivo y mover todos los archivos que
				 * cumplan ese tipo a ella
				 */
			System.out.println(
					"Creamos una carpeta para un tipo de archivo y moveremos todos los archivos que cumplan ese tipo a ella");
			GestorArchivos.comprobarDirectorio();

			System.out.println("¿Que tipo quieres? (java,txt,docx...)");
			String tipo = "";
			tipo = sc.next();

			/*
			 * Decision compresion
			 * System.out.println("¿Te gustaría además hacer una copia comprimida?");
			 * System.out.println("Escriba 'si', o 'no' "); decisionComprimir = sc.next();
			 */

			/*
			 * //Comprobacion de respuesta while (!decisionComprimir.contentEquals("si") &&
			 * !decisionComprimir.contentEquals("no")) { System.out.println("Respuesta " +
			 * decisionComprimir + " incorrecta. Escriba si o no."); decisionComprimir =
			 * sc.next();
			 * 
			 * }
			 * 
			 * if(decisionComprimir == "si") { try {
			 * Compress.zipFolder(GestorArchivos.getDirectorio() +
			 * "\\" + tipo, GestorArchivos.getDirectorio() + "\\" + tipo + ".zip"); } catch
			 * (Exception e) { // TODO Auto-generated catch block e.printStackTrace(); } }
			 */

			GestorArchivos.moveType(tipo);

			Menu.introMenu();

			break;

		case 4: /* Creacion paquetes java */
			System.out.println("Crea paquetes de java a partir de los archivos java encontrados");
			System.out.println(
					"Este programa procura organizar archivos java sueltos y asignarle sus respectivos paquetes");
			GestorArchivos.comprobarDirectorio();

			//String lineaLeida = GestorArchivos.crearPaquetesJava();

			// System.out.println(lineaLeida);

			break;

		case 5: /*
				 * Organiza por todos los tipos
				 * 
				 */
			System.out.println(
					"Organiza todos los archivos por carpetas con el nombre del tipo que sean (pdf: todos los pdf, docx: todos los docx...)");
			GestorArchivos.comprobarDirectorio();

			/* Una vez comprobado el directorio procedemos a mover todos los tipos */
			GestorArchivos.eliminarRepetidos();
			GestorArchivos.organizarPorTodosTipos();
			Menu.introMenu();

			break;

		case 6: /* Eliminamos los archivos repetidos que haya del directorio */
			System.out.println("Elimina todos los archivos repetidos que haya en la carpeta");

			GestorArchivos.comprobarDirectorio();
			GestorArchivos.eliminarRepetidos();

			Menu.introMenu();

			break;

		case 7: /* Exit */
			System.out.println("¡Adios! Nos vemos pronto ;)");
			System.out.println("\tAutor: MrSh4d0w");
			System.exit(0);

			break;

		default: /* Opcion incorrecta */
			System.out.println("Opcion no correcta, por favor escoja otra: ");
			/*
			 * decisionMenu = sc.nextInt(); eleccionmenu(decisionMenu);
			 */
			break;
		}

	}

	public static void introMenu() throws FileNotFoundException {

		sc = new Scanner(System.in);

		/* Introduccion al programa */
		System.out.println("");
		System.out.println("");
		System.out.println("¡Escoja una opcion!");

		System.out.println("\t\t\tAutor:MrSh4d0w");
		System.out.println("1. Eliminar la primera parte del nombre de un archivo (tu delimitas cual es esta) ");
		System.out.println(
				"2. Sustituir la primera parte del nombre de un archivo (tu delimitas cual es esta) por el nombre que quieras");
		System.out.println(
				"3. Crear una carpeta para un tipo de archivo y mover todos los archivos que cumplan ese tipo a ella");
		System.out.println("4. Crea paquetes de java a partir de los archivos java encontrados para su organizacion");
		System.out.println(
				"5. Organiza todos los archivos por carpetas con el nombre del tipo que sean (pdf: todos los pdf, docx: todos los docx...)");
		System.out.println("6. Elimina todos los archivos repetidos que haya en la carpeta");
		System.out.println("7. Exit");

		int decisionMenu = 0;

		do {
			try {
				decisionMenu = sc.nextInt();
			} catch (java.util.InputMismatchException e) {
				System.out.print("Error: valor no válido");
				decisionMenu = 0;
				sc = new Scanner(System.in);
			}

			eleccionmenu(decisionMenu);

		} while (decisionMenu != -1);

	}

	public static void main(String[] args) throws FileNotFoundException {
		System.out.println("Hola! Bienvenido al gestor de archivos.");

		try {
			introMenu();
		} catch (FileNotFoundException e) {
			e.getMessage();
		}

	}

}

/* ARREGLAR PONER OPCION MAL COMO STRING */
