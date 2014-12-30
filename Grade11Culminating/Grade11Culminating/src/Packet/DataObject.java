package Packet;

import java.awt.Point;

import HelperClasses.Globals;


//this is a stripped down player class that contains only the
//variables that are vital for multiplayer operation
public class DataObject {
	// x position of the object
	public float flt_XPos;
	// y position of the object
	public float flt_YPos;
	// array of arrays of bullet locations
	// array of bullet locations
	public Point[] ary_bulletLocations;
	// IP addresses
	public String str_localAddress;
	public String str_externalAddress;
	//uid made up of both ip addresses combined
	public String str_UID;
	
	//get and set the width and height of player
	public int int_width = Globals.img_player[0].getWidth();
	public int int_height = Globals.img_player[0].getHeight();

	// takes x and y position and array of bullet locations as input, returns a
	// ClientDataObject
	public DataObject(String str_localAddress, String str_externalAddress, float flt_XPos, float flt_YPos, Point[] ary_bulletLocations) {
		//set the local ip address
		this.str_localAddress = str_localAddress;
		//set the external ip address
		this.str_externalAddress = str_externalAddress;
		//set the uid
		this.str_UID = str_localAddress.concat(str_externalAddress);
		
		//set the x position
		this.flt_XPos = flt_XPos;
		//set the y position
		this.flt_YPos = flt_YPos;
		//set the array of bullet locations
		this.ary_bulletLocations = ary_bulletLocations;
	}

	//setter function for the data object
	public void set(DataObject dataObject) {
		//set the local ip address
		this.str_localAddress = dataObject.str_localAddress;
		//set the external ip address
		this.str_externalAddress = dataObject.str_externalAddress;
		//set the uid
		this.str_UID = str_localAddress.concat(str_externalAddress);

		//set the x position
		this.flt_XPos = dataObject.flt_XPos;
		//set the y position
		this.flt_YPos = dataObject.flt_YPos;
		//set the array of bullet locations
		this.ary_bulletLocations = dataObject.ary_bulletLocations;
	}
}
