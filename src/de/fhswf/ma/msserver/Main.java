package de.fhswf.ma.msserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
	
	private final static int SERVER_PORT = 9956;
	
	private static ServerSocket sSocket;
	
	public static void main(String[] args) {
		setup();
		startTask();
	}
	
	private static void setup() {
		try {
			sSocket = new ServerSocket(SERVER_PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void startTask() {
		new Thread() {
			public void run() {
				while(true) {
					System.out.println("Waiting for data ...");
					
					try {
						Socket dsocket = sSocket.accept();
					} catch(Exception e) {
						System.out.println("Cancelling because of error:");
						e.printStackTrace();
					}
					
				}
			}
		}.start();
	}

}
