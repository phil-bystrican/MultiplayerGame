//singleton that contains variables that are global

package HelperClasses;

import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.Image;

import Player.Player;

public class Globals {
	//singleton variable
	private static Globals instance = null;
	
	//is this machine the server?
	public static boolean isServer = true;

	//server ip
	public static String serverIP = "0.0.0.0";

	//player image
	public static Image[] img_player;
	
	//remote player image
	public static Image[] img_remotePlayer;
	
	public static boolean isLAN = true;
	
	//hashmap of remote players
	public static Map<String, Player> plr_networkedPlayers = new HashMap<String, Player>();

	//reference to local player
	public static Player plr_localPlayer;
	
	protected Globals() {
		//used to prevent initialization
	}

	//singleton accessor/initializer
	public static Globals getInstance() {
		if (instance == null) {
			instance = new Globals();
			img_player = new Image[1];
			img_remotePlayer = new Image[1];
		}
		//return the singleton
		return instance;
	}
}
