package org.gestorarchivos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import java.lang.Thread;
import java.time.*;

public class GestorArchivos<E> implements Comparable<E> {

	private static String criterio;
	private static String directorio;
	public static ArrayList<String> archivosArrayList = new ArrayList<>();
	public static ArrayList<Integer> indices = new ArrayList<>();
	public static ArrayList<String> archivosArrayListTipo = new ArrayList<>();
	public static ArrayList<String> carpetasCreadas = new ArrayList<>();
	private static String informe = "";
	private static boolean DecisionZip = false;

	/**
	 * Getter criterio
	 * 
	 * @return criterio
	 */
	public static String getCriterio() {
		return criterio;
	}

	/**
	 * Setter criterio
	 * 
	 * @param criterio
	 */
	public static void setCriterio(String criterio) {
		GestorArchivos.criterio = criterio;
	}

	/**
	 * Getter directorio
	 * 
	 * @return directorio
	 */
	public static String getDirectorio() {
		return directorio;
	}

	/**
	 * Setter directorio
	 * 
	 * @param directorio
	 */
	public static void setDirectorio(String directorio) {
		GestorArchivos.directorio = directorio;
	}

	/**
	 * Comprobará si hay un directorio ya establecido por el usuario, si es asi
	 * permitira cambiarlo, si no es asi, introducirlo
	 */
	public static void comprobarDirectorio() {
		Scanner sc = new Scanner(System.in);
		String cambiarlo = "";
		String directorioNuevo = "";

		if (getDirectorio() != null) {
			System.out.println("El directorio definido es " + getDirectorio());
			System.out.println("¿Quieres cambiarlo? Si es así, responda si");
			cambiarlo = sc.nextLine();

			if (cambiarlo.contains("si")) {
				System.out.println("Introduce el nuevo directorio");
				
				while(directorioNuevo.equals("")) {
					directorioNuevo = sc.nextLine();
				}
				
				setDirectorio(directorioNuevo);
			} else
				System.out.println("Vale, usaremos el directorio ya definido");

		} else {
			System.out.println("Introduce el directorio para la ejecucion");
			directorioNuevo = sc.nextLine();
			setDirectorio(directorioNuevo);
		}

	}

	/**
	 * File (utilizado por metodos internos DeleteSin)
	 * 
	 * @return carpeta objetivo (File)
	 */
	public static File carpeta() {
		File carpetaObj = new File(getDirectorio());
		return carpetaObj;
	}

	/**
	 * @return listado archivos carpeta
	 */
	public static String[] getListado() {
		return carpeta().list();
	}

	public String completo() {
		String cadena = "El directorio es ";
		cadena += directorio;

		return cadena;
	}

	public static void mostrarTodosArchivosLista() {
		System.out.println("Ruta busqueda archivos " + getDirectorio());

		if (getListado().length != 0) {
			System.out.println("Se han encontrado " + getListado().length + " archivos");

			/* Esperamos un poco antes de mostrar los archivos */
			try {
				Thread.sleep(400);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			for (int i = 0; i < getListado().length; i++) {
				System.out.println(getListado()[i]);
			}

		} else {
			System.out.println("No se ha encontrado ningun archivo en la ruta");

		}
	}

	/**
	 * Añade elementos a un arraylist con el criterio establecido
	 * 
	 * @return archivos añadidos (pantalla)
	 */
	public static void filtroporCriterio() {
		String criterio = getCriterio();

		for (int i = 0; i < getListado().length; i++) {
			if (getListado()[i].startsWith(criterio)) {
				// System.out.println(getListado()[i]);
				archivosArrayList.add(getListado()[i]);
			}
		}
	}

	/**
	 * Añade elementos a un arraylist (arrayListArchivosTipo) que cumpla que es del
	 * tipo especificado (ej: java, txt, etc)
	 * 
	 * @param tipo
	 */
	public static void filtroporTipo(String tipo) {

		for (int i = 0; i < getListado().length; i++) {
			if (getListado()[i].endsWith(tipo)) {
				// System.out.println(getListado()[i]);
				archivosArrayListTipo.add(getListado()[i]);
			}
		}
	}

	/**
	 * Renombrara todos los archivos quitando la primera parte que será la que
	 * obtenga de criterio y reemplazando por el nombre que queramos
	 * 
	 * @param nombreQuerido (nombre que queremos)
	 * @throws FileNotFoundException
	 * 
	 */
	public static void replaceName(String nombreQuerido) throws FileNotFoundException {
		System.out.println("Mostrando archivos que empiecen por " + getCriterio() + " :");
		filtroporCriterio();

		if (archivosArrayList.size() != 0) {
			archivosArrayList.ensureCapacity(archivosArrayList.size() * 2 + 1);
			/* Array de String con nombres finales (quitandole criterio del principio) */

			for (String archivo : archivosArrayList) {
				/* Archivo con su nombre original */
				File file1 = new File(getDirectorio(), archivo);

				archivo = archivo.replaceFirst(criterio, nombreQuerido);

				/* Archivo renombrado */
				File fileRename = new File(getDirectorio(), archivo);

				moverFile(file1, fileRename, 2);
			}

		} else
			System.out.println("No hay elementos que renombrar");
		System.out.println("\n");
		informe("\n");
		GenerarInforme(getDirectorio(), criterio);
		resetearInforme();
		resetearListas();
		Menu.introMenu(); /* Volvemos al menu */

	}

	/*
	 * HACER UN METODO DELETE uno sin parametro que elimine lo que se encuentre en
	 * el ArrayList otro que le pasese el nombre completo del archivo
	 */

	/**
	 * Renombrara todos los archivos quitando la primera parte que será la que
	 * obtenga de criterio
	 * 
	 * @throws FileNotFoundException
	 */
	public static void deleteName() throws FileNotFoundException {
		System.out.println("Mostrando archivos que empiecen por " + getCriterio() + " :");
		filtroporCriterio();

		if (archivosArrayList.size() != 0) {
			archivosArrayList.ensureCapacity(archivosArrayList.size() * 2 + 1);
			System.out.println("");

			for (String archivo : archivosArrayList) {
				System.out.println(archivo);
				/* Archivo con su nombre original */
				File file1 = new File(getDirectorio() + "\\\\" + archivo);

				archivo = archivo.replaceFirst(criterio, ""); /* Aqui eliminamos esa parte del nombre */

				/* Archivo renombrado */
				File fileRename = new File(getDirectorio() + "\\\\" + archivo);

				moverFile(file1, fileRename, 2);
			}
		} else {
			System.out.println("No hay elementos que renombrar");
			// throw new FileNotFoundException("No hay elementos que renombrar");
		}

		System.out.println("\n");
		informe("\n");
		GenerarInforme(getDirectorio(), criterio);
		resetearInforme();
		resetearListas();
		Menu.introMenu(); /* Volvemos al menu */
	}

	/**
	 * Mueve un archivo desde su ruta original a la ruta que queramos
	 * 
	 * @param directorioFinal
	 * @param directorioPrincipio
	 * @param archivo
	 * @throws FileNotFoundException si las rutas no son correctas o no se encuentra
	 *                               el archivo
	 */
	public static void mover(String directorioFinal, String directorioPrincipio, String archivo)
			throws FileNotFoundException {
		File file1 = new File(directorioPrincipio, archivo);
		File fileMoved = new File(directorioFinal, archivo);

		File directorioI = new File(directorioPrincipio);
		File directorioF = new File(directorioFinal);

		if (!directorioI.exists() || !directorioF.exists())
			if (!directorioI.isDirectory() || !directorioF.isDirectory()) {
				// throw new FileNotFoundException("Las rutas no son correctas o no han sido
				// creadas");
				System.out.println("Las rutas no son correctas o no han sido creadas. Volviendo al menu: ");
				Menu.introMenu();
			}

		boolean flag = file1.renameTo(fileMoved);

		if (flag == true) {
			System.out.println("File " + file1.getName() + " successfully moved to " + fileMoved.getAbsolutePath());
			informe("File " + file1.getName() + " successfully moved to " + fileMoved.getAbsolutePath() + "\n");
		} else if (file1.isDirectory())
			System.out.println(file1.getName() + " es un directorio (carpeta)");
		else
			System.out.println("Operation Failed with " + archivo);

		System.out.println("");
	}

	/**
	 * Creara una carpeta con el tipo que hayamos indicado y moverá todo aquellos
	 * archivos que cumplan con este a dicha carpeta
	 * 
	 * @throws FileNotFoundException
	 * 
	 */
	public static void moveType(String tipo) throws FileNotFoundException {
		filtroporTipo(tipo);

		/* Permite el añadido a carpetas ya existentes */

		if (archivosArrayListTipo.size() != 0) {
			archivosArrayList.ensureCapacity(archivosArrayList.size() * 2 + 1);

			/* Ruta con carpeta final que tendra el nombre de "tipo" */
			String fileCarpetaTipo = getDirectorio(); /* Ruta final de cada archivo renombrado */

			/*
			 * Creamos la carpeta con el nombre del tipo (si es necesario) Y pondremos la
			 * ruta que será directorio + Nombre Carpeta (tipo)
			 */
			fileCarpetaTipo = fileCarpetaTipo + "\\\\" + tipo;
			File carpeta = new File(fileCarpetaTipo);
			if (!carpeta.exists()
					|| !carpeta.isDirectory()) { /* Comprobacion de si la carpeta ya existe o hay que crearla */
				carpeta.mkdir(); /* Creacion de carpeta */

				/*
				 * System.out.println("");
				 * System.out.println("Hemos creado una carpeta del tipo " + tipo);
				 * System.out.println("");
				 */
			}

			/* Por cada archivo que sea de ese tipo lo moveremos a su directorio final */
			for (String archivo : archivosArrayListTipo) {
				try {
					mover(fileCarpetaTipo, getDirectorio(), archivo);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (DecisionZip == true) {
				try {
					Compress.zipFolder(fileCarpetaTipo, fileCarpetaTipo + ".zip");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		} else
			System.out.println("No hay elementos que mover");

		if (informe.isEmpty())
			System.out.println("No hay elementos para mover");
		else {
			System.out.println("\n");
			System.out.println("Informe con cambios generado en " + getDirectorio());
			informe("\n");
			GenerarInforme(getDirectorio(), tipo);

		}
		resetearInforme();
		resetearListas();

		System.out.println("\n\n");

	}

	public static void organizarPorTodosTipos() throws FileNotFoundException {

		/*
		 * (.*) captura cualquier carácter que aparezca antes del punto ([^\\.]+)
		 * captura una secuencia de caracteres que no incluyan un punto y que aparezcan
		 * después del punto, lo cual representa la extensión. La marca $ al final de la
		 * expresión regular asegura que la coincidencia se encuentre al final del
		 * nombre del archivo.
		 */
		String pattern = "(.*)\\.([^\\.]+)$";

		Pattern r = Pattern.compile(pattern);

		for (int i = 0; i < getListado().length; i++) {
			String fileName = getListado()[i];
			Matcher m = r.matcher(fileName);
			File file = new File(getDirectorio(), fileName);

			if (m.find() && !file.isDirectory()) {
				// System.out.println("Tipo de archivo: " + m.group(2));
				moveType(m.group(2));
			} else {
				// System.out.println("No se pudo identificar el tipo de archivo, o es un
				// directorio");
			}

		}
		System.out.println(" ");

		resetearInforme();
		resetearListas();
	}

	/* ARREGLAR REPETIDOS INDEX OUT OF BOUNDS */
	public static void eliminarRepetidos() {

		String[] repetidos = getListado().clone();
		// System.out.println("Tamaño del listado " + repetidos.length);

		String pattern0 = "(.*)\\.([^\\.]+)$";
		Pattern r0 = Pattern.compile(pattern0);

		for (int i = 0; i < repetidos.length; i++) {
			String fileName = repetidos[i];

			Matcher m0 = r0.matcher(fileName);
			File file = new File(getDirectorio(), fileName);

			if (m0.find() && !file.isDirectory()) {
				// System.out.println("Archivo: " + m.group(1));
				repetidos[i] = m0.group(1);
			} else {
				// System.out.println("No se pudo identificar el tipo de archivo, o es un
				// directorio");
			}

		}

		ArrayList<String> listaRepetidos = new ArrayList<String>();

		for (int i = 0; i < repetidos.length; i++) {
			listaRepetidos.add(repetidos[i]);
		}

		System.out.println("");

		/*
		 * Comprobamos si la cadena principal es igual y lo unico que cambia es el final
		 * (Si tiene (1)...
		 */
		/*
		 * for (int i = 0; i < listaRepetidos.size(); i++) {
		 * System.out.println(listaRepetidos.get(i)); }
		 */
		/*
		 * for (int i = 0; i < listaRepetidos.size(); i++) {
		 * System.out.println(listaRepetidos.get(i)); }
		 */
		for (int i = 0; i < listaRepetidos.size() - 1; i++) {
			String pattern = "(.*) \\(([0-9]+)\\)";

			Pattern r = Pattern.compile(pattern);
			Matcher m = r.matcher(listaRepetidos.get(i));

			Matcher m2 = r.matcher(listaRepetidos.get(i + 1));

			String pattern3 = "(.*) \\- copia$";
			Pattern r3 = Pattern.compile(pattern3);
			Matcher m3 = r3.matcher(listaRepetidos.get(i));

			//System.out.println(listaRepetidos.get(i) + " con i " + i);
			
			if (m.find()) {
				//System.out.println("Nombre base: " + m.group(1));
				//System.out.println("Número de repetición: " + m.group(2));

				String archivoPosibleRep = m.group(1);
				//System.out.println(archivoPosibleRep);

				// FUNCIONA CUANDO LOS DOS SON TIPO nombre (1) nombre (2)...
				if (m2.find() && m.group(1).contentEquals(m2.group(1))) {

					archivosArrayList.add(getListado()[i]);
					//System.out.println("NOMBRE ARCHIVO " + getListado()[i]);
					informe("Archivo " + getListado()[i] + " repetido eliminado.\n");

					File file1 = new File(getDirectorio() + "\\\\" + archivosArrayList.get(0));
					file1.delete();
					archivosArrayList.clear();

					listaRepetidos.remove(i);
					i--;

				} else if (m.group(1).contentEquals(listaRepetidos.get(i + 1))) {

					archivosArrayList.add(getListado()[i]);
					//System.out.println("NOMBRE ARCHIVO " + getListado()[i]);
					informe("Archivo " + getListado()[i] + " repetido eliminado.\n");

					File file1 = new File(getDirectorio() + "\\\\" + archivosArrayList.get(0));
					file1.delete();
					archivosArrayList.clear();

					listaRepetidos.remove(i);
					i--;

				}
				
			}

			if (m3.find()) {
				System.out.println("ARCHIVO TIPO COPIA " + listaRepetidos.get(i));
				System.out.println("SIGUIENTE A TIPO COPIA  " + listaRepetidos.get(i+1));
				System.out.println("m group 1 " + m3.group(1));
				
				if(m3.group(1).contentEquals(listaRepetidos.get(i+1))){ /* Si es del tipo "- copia" */
					//System.out.println(" TIPO COPIA ");
					archivosArrayList.add(getListado()[i]);
					System.out.println("Archivo repetido: " + getListado()[i]);
					informe("Archivo " + getListado()[i] + " repetido eliminado.\n");

					File file1 = new File(getDirectorio() + "\\\\" + archivosArrayList.get(0));
					file1.delete();
					archivosArrayList.clear();

					listaRepetidos.remove(i);
					i--;

				}
			}
			
			
		}

		System.out.println("");

		try

		{
			GenerarInforme(getDirectorio(), "borrarRepetidos");
			System.out.println("Generado informe con los cambios ");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		resetearListas();

		resetearInforme();

	}

	public static void moverFile(File file1, File fileMoved, int opcionInforme) throws FileNotFoundException {
		if (!file1.exists()) {
			// throw new FileNotFoundException("Archivo no encontrado");
			System.out.println("Archivo no encontrado. Volviendo al menu: ");
			Menu.introMenu();
		}

		boolean flag = file1.renameTo(fileMoved);

		switch (opcionInforme) {
		case 1: /* Move */
			if (flag == true) {
				System.out.println(
						"File " + file1.getName() + " successfully moved to " + fileMoved.getAbsolutePath() + "\n");
				informe("File " + file1.getName() + " successfully moved to " + fileMoved.getAbsolutePath() + "\n");
			} else if (file1.isDirectory())
				System.out.println(file1.getName() + " es un directorio (carpeta)");
			else
				System.out.println("Operation Failed with " + file1.getName());

			break;

		case 2: /* Replace name and delete name */
			if (flag == true) {
				System.out.println("File Successfully Renamed from " + file1.getName() + " to " + fileMoved.getName());
				informe("File Successfully Renamed from " + file1.getName() + " to " + fileMoved.getName() + "\n");
			} else if (file1.isDirectory())
				System.out.println(file1.getName() + " es un directorio (carpeta)");
			else
				System.out.println("Operation Failed with " + file1.getName());

			break;

		default:
			System.out.println(
					"Opcion por defecto al escribir una que no es correcta (Informe: operacion mover archivos)");
			if (flag == true) {
				System.out.println(
						"File " + file1.getName() + " successfully moved to " + fileMoved.getAbsolutePath() + "\n");
				informe("File " + file1.getName() + " successfully moved to " + fileMoved.getAbsolutePath() + "\n");
			} else if (file1.isDirectory())
				System.out.println(file1.getName() + " es un directorio (carpeta)");
			else
				System.out.println("Operation Failed with " + file1.getName());

			break;
		}

	}

	public static String crearPaquetesJava() throws FileNotFoundException {
		filtroporTipo(".java");
		String lineaLeida = null;

		for (String archivojava : archivosArrayListTipo) {

			String fileCarpeta = getDirectorio();
			String archivo = getDirectorio() + "\\\\" + archivojava;
			FileReader fileJava = new FileReader(archivo);
			BufferedReader br = new BufferedReader(fileJava);

			try { /* Podemos hacer la propia creacion ya en la carpeta */
				lineaLeida = br.readLine();
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (lineaLeida != null) {
				if (lineaLeida.contains("package ")) {
					String[] paquete = lineaLeida.split("package ");

					lineaLeida = paquete[1];

					/* Ahora vamos con la creacion del paquete */
					String separador = Pattern.quote(".");
					String[] lineasPaquetes = lineaLeida.split(separador);

					for (int i = 0; i < lineasPaquetes.length; i++) {
						/* Creamos la carpeta con el nombre de la linea */
						fileCarpeta += "\\\\" + lineasPaquetes[i];
						File carpeta = new File(fileCarpeta);

						if (!carpeta.exists() || !carpeta.isDirectory()) {
							carpeta.mkdir();
							System.out.println("Folder created " + fileCarpeta);
							informe("Carpeta creada " + fileCarpeta + "\n");
						}
					}

					mover(fileCarpeta, getDirectorio(), archivojava);
				}
			}
		}
		informe("\n");
		GenerarInforme(getDirectorio(), "paquetesJava");
		resetearListas();
		Menu.introMenu(); /* Volvemos al menu */
		return lineaLeida;
	}

	public static void comprimir(String nombreZip, String archivo) throws IOException {

		byte[] buffer = new byte[1024];

		try {
			// Creacion archivos zip
			FileOutputStream fos = new FileOutputStream(getDirectorio() + "\\" + nombreZip + ".zip");

			// Permite comprimir cada una de las entradas generadas a partir de la clase
			// ZipEntry con el nombre del archivo original
			ZipOutputStream zos = new ZipOutputStream(fos);
			ZipEntry ze = new ZipEntry(archivo);
			zos.putNextEntry(ze);

			// Ruta del archivo que queremos comprimir
			FileInputStream in = new FileInputStream(getDirectorio() + "\\" + archivo);

			int len;
			while ((len = in.read(buffer)) > 0)
				zos.write(buffer, 0, len);

			in.close();
			zos.closeEntry();

			zos.close();
			System.out.println("Hecho");

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	/**
	 * Informacion que tendra el informe (añade informacion al String cadena)
	 * 
	 * @param cadena
	 */
	public static void informe(String cadena) {
		if (informe == null)
			informe = cadena;
		else
			informe = informe + cadena;
	}

	/**
	 * Genera un archivo con el informe de las modificaciones o cambios hechos
	 * 
	 * @param directorio
	 * @param criterio   para añadirla al nombre del informe
	 * @throws FileNotFoundException (archivo no encontrado) necesaria esta
	 *                               excepcion para el uso de FileReader
	 */
	public static void GenerarInforme(String directorio, String criterio) throws FileNotFoundException {

		if (!informe.isEmpty()) {
			/* Para añadir fecha y hora al informe */
			LocalDateTime fecha = LocalDateTime.now();
			String fechaSt = "";

			fechaSt = fecha.toString();
			fechaSt = fechaSt.replaceAll(":", "-");

			/* Primero habrá que crear el archivo */
			if (criterio.contentEquals(".java"))
				criterio = "java";
			else if (criterio.contentEquals(".txt"))
				criterio = "txt";

			directorio = directorio + "\\\\" + "informe-" + criterio + "-" + fechaSt + ".txt"; /* Ruta */
			File fileInforme = new File(directorio); /* Creacion del archivo en la ruta especificada */
			PrintWriter pw = new PrintWriter(fileInforme);
			pw.print(informe);
			pw.close(); /* Una vez acabado importante cerrar el Print Writer */
			// FileReader fr = new FileReader(fileInforme); /* Permite lectura de archivos
			// */
			resetearInforme();
			resetearListas();
		}
	}

	public static void resetearInforme() {
		informe = "";
	}

	public static void resetearListas() {
		archivosArrayList.clear();
		archivosArrayListTipo.clear();

	}

	@Override
	public int compareTo(E o) {
		// TODO Auto-generated method stub
		return 0;
	}

	public static boolean getDecisionZip() {
		return DecisionZip;
	}

	public static void setDecisionZip(boolean decisionZip) {
		DecisionZip = decisionZip;
	}

}
