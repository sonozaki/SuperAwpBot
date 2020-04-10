/*

Copyright (C) <2020>  <Francisco DomÃ­nguez Lerma>

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>

*/
package superAwpBot;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;

public class SuperAwpBot implements NativeMouseListener, NativeKeyListener, NativeMouseInputListener, Runnable {
	public static String dispositivo = "";
	public static int raton = -1;
	public static String teclado = "";
	public static boolean moviendo = false;
	public static Thread hilo;
	public static Dimension screenSize;
	public static Robot robot;
	public static double altura;
	public static double ancho;
	public static BufferedImage bufferedImage;
	public static boolean evento = false;

	public static void main(String[] args) throws ParseException {
		Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
		logger.setLevel(Level.OFF);

		try {
			GlobalScreen.registerNativeHook();
		} catch (NativeHookException ex) {
			System.err.println("Error Nativehook");
			System.err.println(ex.getMessage());

			System.exit(1);
		}

		Properties configFile = new Properties();
		InputStream is = null;
		System.out.println("************************************************************");
		System.out.println("***** Bienvenido a SuperAwpBot *****");
		System.out.println("Version: 1.2 <== Ahora puedes mover la mira para cambiar el pick\n");
		System.out.println("");
		System.out.println("************************************************************");
		System.out.println("");
		System.out.println("");
		System.out.println("************************************************************");
		System.out.println("Cargando fichero de configuracion...");

		if (args.length == 0) {
			System.out.println("ERROR: Debes indicar la ruta del fichero de configuracion");
			System.exit(1);
		}

		// ABRE EL FICHERO DE CONFIG
		try {
			is = new FileInputStream(args[0]);
			configFile.load(is);
		} catch (IOException e) {
			System.out.println("Error abriendo fichero de configuracion");
			e.printStackTrace();
			System.exit(1);
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				System.out.println("Error cerrando fichero de configuracion");
				e.printStackTrace();
				System.exit(1);
			}
		}

		dispositivo = configFile.getProperty("dispositivo");
		if (dispositivo == null
				|| (!dispositivo.equalsIgnoreCase("raton") && !dispositivo.equalsIgnoreCase("teclado"))) {
			System.out.println("ERROR: Fichero de configuracion mal formado");
			System.exit(1);
		} else {
			System.out.println("Dispositivo: " + dispositivo);
			if (dispositivo.equalsIgnoreCase("raton")) {
				raton = Integer.parseInt(configFile.getProperty("key"));
				System.out.println("Tecla del raton: " + raton);
			} else {
				teclado = configFile.getProperty("key");
				System.out.println("Tecla del teclado: " + teclado);
			}
			System.out.println("");
			System.out.println("Configuracion cargada con exito!");
			System.out.println("");

			System.out.println("Arrancando server...");

		}

		GlobalScreen.addNativeMouseMotionListener(new SuperAwpBot());
		hilo = new Thread(new SuperAwpBot());
		hilo.start();

		if (dispositivo.equalsIgnoreCase("raton")) {
			GlobalScreen.addNativeMouseListener(new SuperAwpBot());
		} else {
			GlobalScreen.addNativeKeyListener(new SuperAwpBot());
		}

		System.out.println("");

		System.out.println("Servidor arrancado con exito!");
		System.out.println("************************************************************");
		System.out.println("");
		System.out.println("");
		System.out.println("************************************************************");
		System.out.println(
				"SuperAwpBot te permite disparar con AWP automaticamente mientras estas apuntando a un pixel en concreto. Es extremadamente util para pickear con AWP.\n");
		System.out.println(
				"INSTRUCCIONES: Durante el juego, pickea con AWP (con la mira puesta) donde desees, a continuacion, pulsa "
						+ ((dispositivo.equalsIgnoreCase("teclado")) ? teclado : raton) + " en el "
						+ ((dispositivo.equalsIgnoreCase("teclado")) ? "teclado" : "raton")
						+ " para que SuperAwpBot empiece a detectar cambios en la mira, en cuanto pase un enemigo, SuperAwpBot lo detectara y disparara.\n");
		System.out.println(
				"ADVERTENCIA 1: SuperAwpBot detecta CUALQUIER CAMBIO QUE PASE POR LA MIRA, si pasa un aliado de tu equipo por tu mira, disparara, al igual que con cualquier otra cosa que afecte a tu mira, como una flash, humo etc... SuperAwpBot es muy sensible, usalo con cuidado.\n");
		System.out.println(
				"ADVERTENCIA 2: Mientras SuperAwpBot esta activo (esperando un cambio en la mira) tampoco puedes moverte con el teclado, ya que lo detectara como un cambio, solo puedes mover la mira.\n");
		System.out.println(
				"ADVERTENCIA 3: SuperAwpBot puede ser detectado por CSGO, por lo que te pueden dar un buen VAC, no uses tu cuenta MAIN.\n");
		System.out.println("************************************************************");
	}

	// Metodo que compara dos imagenes
	public static boolean bufferedImagesEqual(BufferedImage img1, BufferedImage img2) {

		int noIgual = 0;
		int margen = 1;
		int margenPorPixel = 90000;

		if (img1.getWidth() == img2.getWidth() && img1.getHeight() == img2.getHeight()) {
			for (int x = 0; x < img1.getWidth(); x++) {
				for (int y = 0; y < img1.getHeight(); y++) {
					if (img1.getRGB(x, y) != img2.getRGB(x, y)) {
						if (((img1.getRGB(x, y) - img2.getRGB(x, y)) >= margenPorPixel)
								|| ((img1.getRGB(x, y) - img2.getRGB(x, y)) >= margenPorPixel)) {
							noIgual++;
						}
					}
				}
			}
		} else {
			return false;
		}
		if (noIgual >= margen)
			return false;
		else {
			return true;
		}
	}

	public void establecerImagenInicial() {
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		robot = null;
		try {
			robot = new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		altura = screenSize.getHeight() / 2;
		ancho = screenSize.getWidth() / 2;

		Rectangle area = new Rectangle();
		area.setBounds((int) ancho - 1, (int) altura, 2, 2);
		bufferedImage = robot.createScreenCapture(area);

	}

	// Evento de raton
	@Override
	public void nativeMouseReleased(NativeMouseEvent arg0) {
		if (dispositivo.equalsIgnoreCase("raton")) {
			if (arg0.getButton() == raton) {
				System.out.println("Escuchando...");

				evento = true;

			}
		}
	}

	// Evento de teclado
	@Override
	public void nativeKeyReleased(NativeKeyEvent e) {
		if (dispositivo.equalsIgnoreCase("teclado")) {
			String tecla = NativeKeyEvent.getKeyText(e.getKeyCode());

			if (tecla.equals(teclado)) {
				System.out.println("Escuchando...");

				evento = true;

			}
		}
	}
	
	@Override
	public void run() {
		
		while (true) {
			establecerImagenInicial();
					Rectangle area2 = new Rectangle();
					area2.setBounds((int) ancho - 1, (int) altura, 2, 2);
					BufferedImage bufferedImage2 = robot.createScreenCapture(area2);

					if (!bufferedImagesEqual(bufferedImage, bufferedImage2) && !moviendo && evento) {
						robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
						robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
						System.out.println("Disparando...\n");
						evento=false;
					}

					if (moviendo) {
						moviendo = false;
						establecerImagenInicial();
					}

				}

			}
	
	@Override
	public void nativeMouseMoved(NativeMouseEvent arg0) {
		moviendo = true;
	}

	@Override
	public void nativeMousePressed(NativeMouseEvent arg0) {
	}

	@Override
	public void nativeMouseClicked(NativeMouseEvent arg0) {
	}

	@Override
	public void nativeKeyPressed(NativeKeyEvent arg0) {
		
	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent arg0) {
	}

	@Override
	public void nativeMouseDragged(NativeMouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	}


