//contains variables that stay constant during hte lifetime of the program

package HelperClasses;

public class Constants {
	
	//player speed constant
	static public final double dbl_kPLAYERMOVESPEED = .2;
	
	//frame rate constant
	static public final int int_kFRAMERATE = 60;
	
	//collision layer on map
	static public final int int_kCOLLISIONLAYER = 0;
	
	//spawn layer on map
	static public final int int_kSPAWNLAYER = 1;
	
	//is the game full screen?
	static public final boolean bool_fullscreen = true;
	
	//Server side Port
	static public final int SERVER_PORT = 1234;
	
	/*client side port *must be different than server port or local client
	will not be able to bind to the port*/
	static public final int CLIENT_PORT = 1235;
}
