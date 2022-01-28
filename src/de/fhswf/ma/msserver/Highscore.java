package de.fhswf.ma.msserver;

/**
 * 
 *
 * @author Dominik M�ller
 * @version 1.0
 */
public class Highscore {

	private String name;
	private int points;
	private int time;
	
	public Highscore(String name, int points, int time) {
		this.name = name;
		this.points = points;
		this.time = time;
	}
	
	public String getName() {
		return name;
	}
	
	public int getPoints() {
		return points;
	}
	
	public int getTime() {
		return time;
	}
	
}
