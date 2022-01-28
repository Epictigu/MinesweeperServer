package de.fhswf.ma.msserver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * TODO Add comment here
 *
 * @author Dominik Müller, Timo Röder
 * @version 1.0
 */
public class DataManager {

	private static DataManager instance;
	public static DataManager getInstance() {
		if(instance == null)
			instance = new DataManager();
		return instance;
	}
	
	private Map<Difficulty, Highscore[]> highscoreByDifficulty;
	
	private DataManager() {
		highscoreByDifficulty = new HashMap<Difficulty, Highscore[]>();
		for(Difficulty difficulty : Difficulty.values()) {
			Highscore[] highscore = new Highscore[10];
			highscoreByDifficulty.put(difficulty, highscore);
		}
	}
	
	public Highscore[] getHighscoreByDifficulty(Difficulty difficulty) {
		return highscoreByDifficulty.get(difficulty);
	}
	
	public synchronized void addHighscore(Highscore highscore, Difficulty difficulty) {
		Highscore[] highscoreA = getHighscoreByDifficulty(difficulty);
		for(int i = 0; i < highscoreA.length; i++) {
			if(highscoreA[i] == null) {
				highscoreA[i] = highscore;
				break;
			} else if(highscoreA[i].getPoints() < highscore.getPoints()
					|| (highscoreA[i].getPoints() == highscore.getPoints() && highscoreA[i].getTime() > highscore.getTime())) {
				Highscore oldHighscore = highscoreA[i];
				highscoreA[i] = highscore;
				highscore = oldHighscore;
			}
		}
		highscoreByDifficulty.put(difficulty, highscoreA);
		saveData();
	}
	
	public void saveData() {
		File folder = new File("data");
		if(!folder.exists())
			folder.mkdir();
		
		for(Difficulty d : highscoreByDifficulty.keySet()) {
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(new File("data/" + d.toString())));
				Highscore[] highscores = highscoreByDifficulty.get(d);
				for(Highscore highscore : highscores) {
					if(highscore == null)
						continue;
					writer.write(highscore.getPoints() + ";" + highscore.getTime() + ";" + highscore.getName());
					writer.newLine();
				}
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void loadData() {
		File folder = new File("data");
		if(!folder.exists())
			return;
		
		for(Difficulty d : Difficulty.values()) {
			File f = new File("data/" + d.toString());
			if(!f.exists())
				continue;
			try {
				BufferedReader reader = new BufferedReader(new FileReader(f));
				String line;
				int index = 0;
				Highscore[] highscores = new Highscore[10];
				while((line = reader.readLine()) != null) {
					try {
						String[] parts = line.split(";");
						int points = Integer.valueOf(parts[0]);
						int time = Integer.valueOf(parts[1]);
						String name = "";
						for(int i = 2; i < parts.length; i++) {
							name += parts[i];
						}
						highscores[index++] = new Highscore(name, points, time);
					} catch(NumberFormatException ne) {
						System.err.println("[ERROR] Die Datei für " + d.toString() + " enthält ungültige Datensätze!");
					}
				}
				highscoreByDifficulty.put(d, highscores);
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	
	
}
