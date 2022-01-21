package de.fhswf.ma.msserver;

import java.util.HashMap;
import java.util.Map;

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
		for(Highscore hc : highscoreA) {
			System.out.println(hc);
		}
	}
	
	
	
}
