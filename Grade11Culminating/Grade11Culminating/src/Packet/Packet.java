//basic packet class, used for encoding and decoding data for transmission

package Packet;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;
import HelperClasses.Globals;
import Player.Bullet;
import Player.Player;

import com.google.gson.Gson;

public class Packet {

	// create a gson instance (google json)
	static Gson gson = new Gson();

	// takes a player object as input, outputs the player as a json string
	public static String encodeClientPacket(Player plr_player) {
		// create an array of points to hold the bullet locations
		Point[] ary_bulletLocations = new Point[5];
		// loop thought the array of bullets
		for (int i = 0; i < plr_player.ary_bulletArray.length; i++) {
			// if the bullet is not null
			if (plr_player.ary_bulletArray[i] != null) {
				// add the bullet's x,y coordinates to the point array
				ary_bulletLocations[i] = new Point((int) plr_player.ary_bulletArray[i].flt_XPos, (int) plr_player.ary_bulletArray[i].flt_YPos);
			}
		}

		// return the player class, converted to DataObject and encoded
		// as a json string
		return gson.toJson(new DataObject(plr_player.str_localAddress, plr_player.str_externalAddress, plr_player.flt_XPos, plr_player.flt_YPos,
				ary_bulletLocations));
	}

	// takes a string (json) as input, outputs a data object which the
	// server can take an package into an array
	public static DataObject decodeClientPacket(String str_data) {
		// recreate a player object from the received basic player data
		return gson.fromJson(str_data, DataObject.class);
	}

	// takes an hashmap of DataObjects as input,
	// outputs the objects as a json string
	public static String encodeServerPacket(Map<String, DataObject> players) {
		// return the data encoded as a json string
		DataObject[] do_playerData = new DataObject[players.size()];

		int i = 0;
		//loop through each data object in the hashmap
		for (DataObject dataObject : players.values()) {
			//add the data object to the array of data objects
			do_playerData[i] = dataObject;
			i++;
		}
		//encode the array into a JSON string and return it
		return gson.toJson(do_playerData);
	}

	// takes a string (json) as input, outputs an array of players
	public static Map<String, Player> decodeServerPacket(String str_data) {

		// recreate an array of player objects from the received basic player
		// data
		DataObject[] do_playerData = gson.fromJson(str_data, DataObject[].class);
		Map<String, Player> plr_tmpPlayers = new HashMap<String, Player>();

		// loop through all the player objects in the array
		for (int i = 0; i < do_playerData.length; i++) {
			// if the bullet is not null
			if (do_playerData[i] != null) {
				// set the unique identifier for the player
				plr_tmpPlayers.put(do_playerData[i].str_UID, new Player(do_playerData[i].str_localAddress, do_playerData[i].str_externalAddress,
						Globals.img_remotePlayer));
				// set the player x position to that in the server data object
				plr_tmpPlayers.get(do_playerData[i].str_UID).flt_XPos = do_playerData[i].flt_XPos;
				// set the player y position to that in the server data object
				plr_tmpPlayers.get(do_playerData[i].str_UID).flt_YPos = do_playerData[i].flt_YPos;

				// loop through all the bullet locations in the player data
				for (int j = 0; j < do_playerData[i].ary_bulletLocations.length; j++) {
					// if the bullet is not null
					if (do_playerData[i].ary_bulletLocations[j] != null) {
						// create a new bullet (to avoid null pointer exceptions
						// related to its image)
						plr_tmpPlayers.get(do_playerData[i].str_UID).ary_bulletArray[j] = new Bullet(plr_tmpPlayers.get(do_playerData[i].str_UID).img_bullet, 1);
						// set the bullet's x position
						plr_tmpPlayers.get(do_playerData[i].str_UID).ary_bulletArray[j].flt_XPos = do_playerData[i].ary_bulletLocations[j].x;
						// set the bullet's y position
						plr_tmpPlayers.get(do_playerData[i].str_UID).ary_bulletArray[j].flt_YPos = do_playerData[i].ary_bulletLocations[j].y;
					}
				}
			}
		}

		// return the parsed array of players
		return plr_tmpPlayers;
	}
}
