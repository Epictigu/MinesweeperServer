package de.fhswf.ma.msserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 *
 * @author Dominik Müller, Timo Röder
 * @version 1.0
 */
public class Main {
	
	private final static int SERVER_PORT = 9956;
	
	private static ServerSocket sSocket;
	
	public static void main(String[] args) {
		setup();
		startTask();
	}
	
	private static void setup() {
		DataManager.getInstance().loadData();
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
						Socket dSocket = sSocket.accept();
						new Thread(){
							public void run() {
								try {
									dataWork(dSocket);
								} catch (IOException e) {
									System.out.println("Cancelling because of error: " + e.getMessage());
								}
							}
						}.start();
						
					} catch(Exception e) {
						System.out.println("Cancelling because of error:");
						e.printStackTrace();
					}
					
				}
			}
		}.start();
	}
	
	private static List<String> readLines(Socket dSocket) throws IOException {
		List<String> lineList = new ArrayList<String>();
		BufferedReader lineReader = new BufferedReader(new InputStreamReader(dSocket.getInputStream()));
		lineList.add(lineReader.readLine());
		while(lineReader.ready()) {
			lineList.add(lineReader.readLine());
		}
		
		return lineList;
	}
	
	private static void dataWork(Socket dSocket) throws IOException{
		List<String> lineList = readLines(dSocket);
		System.out.println("Data received: " + lineList);
		
		if(lineList.isEmpty()) {
			System.out.println("Data empty. Cancel Workthrough.");
			return;
		}
		
		if(lineList.get(0).equals("highscore_info")) {
			sendHighscore(dSocket);
		} else if(lineList.get(0).equals("highscore_add")) {
			try {
				Difficulty difficulty = Difficulty.valueOf(lineList.get(1));
				if(difficulty == null) {
					System.out.println("Wrong data. Cancel Workthrough.");
					return;
				}
				String name = lineList.get(4);
				if(name.length() > 16)
					name = name.substring(0, 16);
				if(name.length() == 0)
					return;
				DataManager.getInstance().addHighscore(new Highscore(name, Integer.parseInt(lineList.get(2)), Integer.parseInt(lineList.get(3))), difficulty);
				System.out.println("Highscore-Daten verarbeitet.");
			} catch(NumberFormatException e) {
				System.out.println("Wrong data. Cancel Workthrough.");
			}
		}
	}
	
	private static void sendHighscore(Socket dSocket) throws IOException {
		System.out.println("Sending Highscore Info ...");
		
		OutputStreamWriter sStream = new OutputStreamWriter(dSocket.getOutputStream());
		PrintWriter sWriter = new PrintWriter(sStream);
		
		for(Difficulty difficulty : Difficulty.values()) {
			sWriter.println("difficulty: " + difficulty);
			Highscore[] highscores = DataManager.getInstance().getHighscoreByDifficulty(difficulty);
			for(Highscore highscore : highscores) {
				if(highscore == null)
					break;
				sWriter.println(highscore.getPoints());
				sWriter.println(highscore.getTime());
				sWriter.println(highscore.getName());
			}
		}
		sWriter.flush();
	}

}
