package de.fhswf.ma.msserver;

public class DataManager {

	private static DataManager instance;
	public static DataManager getInstance() {
		if(instance == null)
			instance = new DataManager();
		return instance;
	}
	
	private DataManager() {
		
	}
	
}
