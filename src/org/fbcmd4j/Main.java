package org.fbcmd4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.fbcmd4j.utils.Utils;

import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.Post;
import facebook4j.ResponseList;

public class Main {
	static final Logger logger = LogManager.getLogger(Main.class);
	private static final String CONFIG_DIR = "config";
	private static final String CONFIG_FILE = "fbcmd4j.properties";

	public static void main(String[] args) {
		logger.info("Iniciando app");
		Facebook fb =  null;
		Properties props = null;

		try {
			props = Utils.loadConfigFile(CONFIG_DIR, CONFIG_FILE);
		} catch (IOException ex) {
			logger.error(ex);
		}
		
		int option = 1;
		try {
			Scanner scan = new Scanner(System.in);
			while(true) {
				fb = Utils.configFacebook(props);
				System.out.println("Cliente de Facebook en linea de comando \n\n"
								+  "Opciones: \n"
								+  "(1) NewsFeed \n"
								+  "(2) Wall \n"
								+  "(3) Publicar Estado \n"
								+  "(4) Publicar Link \n"
								+  "(5) Salir \n"
								+  "\nPor favor ingrese una opcion:");
				try {
					option = scan.nextInt();
					scan.nextLine();
					switch (option) {
					case 0:
						Utils.configTokens(CONFIG_DIR, CONFIG_FILE, props, scan);
						props = Utils.loadConfigFile(CONFIG_DIR, CONFIG_FILE);
						break;
					case 1:
						System.out.println("Mostrando NewsFeed...");
						ResponseList<Post> newsFeed = fb.getFeed();
						for (Post p : newsFeed) {
							Utils.printPost(p);
						}
						Utils.askToSaveFile("NewsFeed", newsFeed, scan);
						break;
					case 2:
						System.out.println("Mostrando Wall...");
						ResponseList<Post> wall = fb.getPosts();
						for (Post p : wall) {
							Utils.printPost(p);
						}		
						Utils.askToSaveFile("Wall", wall, scan);
						break;
					case 3:
						System.out.println("Escribe tu estado: ");
						String estado = scan.nextLine();
						Utils.postStatus(estado, fb);
						break;
					case 4:
						System.out.println("Ingresa el link: ");
						String link = scan.nextLine();
						Utils.postLink(link, fb);
						break;

					case 5:
						System.out.println("Gracias por usar el cliente!");
						System.exit(0);
						break;

					default:
						break;
					}
				} catch (InputMismatchException ex) {
					System.out.println("Ocurrio un errror, favor de revisar log.");
					logger.error("Opcion invalida. %s. \n", ex.getClass());
				} catch (FacebookException ex) {
					System.out.println("Ocurrio un errror, favor de revisar log.");
					logger.error(ex.getErrorMessage());
				} catch (Exception ex) {
					System.out.println("Ocurrio un errror, favor de revisar log.");
					logger.error(ex);
				}
				System.out.println();
			}
		} catch (Exception e) {
			logger.error(e);
		}
			

	}
	
	

}