package Screens;

//slick imports

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Input;
import org.newdawn.slick.openal.SoundStore;
import org.newdawn.slick.tiled.TiledMap;
//import org.newdawn.slick.openal.SoundStore;

//Screen/menu imports
import HelperClasses.AudioManager;
import HelperClasses.Collision;
import HelperClasses.Constants;
import HelperClasses.Globals;
import Packet.Client;
import Packet.Server;
import Player.Bullet;
import Player.Player;

//awt imports
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;

//swing imports
import javax.swing.JFrame;

/**
 * @author Phil Bystrican and Qasim Iqbal
 */

public class MainWindow extends BasicGame {
	// create a 2d array of booleans that will hold which x,y positions
	// contain solid walls/objects (1 = solid, 0 = non-solid)
	public boolean bool_collision[][];

	// Create the main menu globally
	static MainMenu jpnl_mainMenu;

	// Create server class
	Server server;

	// Create client class
	Client client;

	// Create a reference to the player
	private Player plr_localPlayer;

	// Create a reference to the map used in the level
	private TiledMap map;
	// variable for the width of the screen
	private int int_screenWidth = 0;
	// variable for the height of the screen
	private int int_screenHeight = 0;

	// camera x position
	int flt_cameraX;
	// camera y position
	int flt_cameraY;

	// initialization
	public MainWindow() {
		// call to the superclass that sets the title of the window
		super("Game");

	}

	public static void main(String[] arguments) {
		//set the path to the lwjgl libs
		System.setProperty("org.lwjgl.librarypath", System.getProperty("user.dir") + "/lib/lwjglNatives");
		//allow software open gl on computers lacking capable graphics cards **May be very slow**
		System.setProperty("org.lwjgl.opengl.Display.allowSoftwareOpenGL","true");

		// Create and set up the jframe.
		JFrame frame = new JFrame();

		//check if the game should be full screen
		if (Constants.bool_fullscreen) {
			//dont allow resizing of the frame
			frame.setResizable(false);
			//check if the fram is currently displayed
			if (!frame.isDisplayable()) {
				// Can only do this when the frame is not visible
				//set the frame to lack decorations
				frame.setUndecorated(true);
			}
			//get the current screen device
			GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
			try {
				//check if full screen is supported
				if (gd.isFullScreenSupported()) {
					//set the game to full screen
					gd.setFullScreenWindow(frame);
				} else {
					// Can't run fullscreen, need to bodge around it (setSize to
					// screen size, etc)
				}
				//set the frame to visible
				frame.setVisible(true);

			} finally {
				//an error occurred
				gd.setFullScreenWindow(null);
			}
		}

		// get the default tool kit
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		// get the screen resolution
		Dimension dim_screenSize = toolkit.getScreenSize();

		// create the main menu jpanel (it is automatically added to the frame
		// and displayed)
		jpnl_mainMenu = new MainMenu(dim_screenSize.width, dim_screenSize.height, frame);

		try {
			synchronized (frame) {
				//wait for the frame to finish
				frame.wait();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// setup the game container to the screen dimensions
		setupGameContainer(dim_screenSize.width, dim_screenSize.height);

	}

	// takes screen width and height as input, sets up the game container,
	// returns nothing
	static void setupGameContainer(int int_screenWidth, int int_screenHeight) {
		try {
			// Create an AppGameContainer object that manages the game life
			// cycle (updating/ drawing)
			AppGameContainer AGC_gameContainer = new AppGameContainer(new MainWindow());

			// set the size of the window
			AGC_gameContainer.setDisplayMode(int_screenWidth, int_screenHeight, Constants.bool_fullscreen);

			// limit the frame rate to the target fps defined in constants
			AGC_gameContainer.setTargetFrameRate(Constants.int_kFRAMERATE);

			// turn vertical refresh sync on
			AGC_gameContainer.setVSync(true);

			// start the game's cycle
			AGC_gameContainer.start();
		} catch (SlickException e) {
			// an error occurred print it to console
			e.printStackTrace();
		}
	}

	// create initialize objects here, this method is called only once
	// by the AppGameContainer in the main method of this class
	@Override
	public void init(GameContainer gc_container) throws SlickException {
		//Initialize  the globals singleton
		Globals.getInstance();

		try {
			//load the player image
			Globals.img_player[0] = new Image("./resources/Images/Player.png");
			//load the remote player image
			Globals.img_remotePlayer[0] = new Image("./resources/Images/RemotePlayer.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}

		// check if this machine is the server
		if (Globals.isServer) {
			//create a new instance of server
			server = new Server();
			//start the server
			server.run();
		} 
		// create a client but dont start it yet
		client = new Client();

		// Don't update the game until it has loaded
		gc_container.pause();

		// get the default tool kit
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		// get the screen resolution
		Dimension dim_screenSize = toolkit.getScreenSize();

		// set the screen width
		int_screenWidth = dim_screenSize.width;
		// set the screen height
		int_screenHeight = dim_screenSize.height;

		try {
			// Get local IP if LAN, external IP if ONLINE.
			String str_localAddress = "";
			String str_externalAddress = "";

			if (!Globals.isLAN) {
				//create a url connection to whatismyip.com disguised as a browser
				URL whatismyip = new URL("http://automation.whatismyip.com/n09230945.asp");
				URLConnection connection = whatismyip.openConnection();
				connection.addRequestProperty("Protocol", "Http/1.1");
				connection.addRequestProperty("Connection", "keep-alive");
				connection.addRequestProperty("Keep-Alive", "1000");
				connection.addRequestProperty("User-Agent", "Web-Agent");
				//read the output of the request
				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				//set the returned output as the external ip
				str_externalAddress = in.readLine();
			}
			//get the local IP
			str_localAddress = InetAddress.getLocalHost().getHostAddress();

			// load the map from a file
			map = new TiledMap("./resources/Maps/Snow/Snow1.tmx");

			// create the player with the image from the global class
			plr_localPlayer = new Player(str_localAddress, str_externalAddress, Globals.img_player);

			// calculate all the collision positions for the current map
			calculateCollisionsForMap();

			//spawn the player
			plr_localPlayer.reSpawn();

			// set a global reference to the local player to be used
			Globals.plr_localPlayer = plr_localPlayer;

		} catch (Exception e) {
			// an error occurred print it to console
			e.printStackTrace();
		}

		// calculate the center of the screen's x axis offset by the map size
		flt_cameraX = (int)-plr_localPlayer.flt_XPos;
		// calculate the center of the screen's y axis offset by the map size
		flt_cameraY = (int)plr_localPlayer.flt_YPos;

		AudioManager.getInstance().loadGameMusic();
		AudioManager.getInstance().playMusic();

		// The game has loaded it can update now
		gc_container.resume();

		// start the client now that a UID has been set
		client.run(plr_localPlayer.str_UID);
	}

	// takes no input, calculate all the collision positions for the current
	// map, returns nothing
	void calculateCollisionsForMap() {
		// the width of the map
		int int_mapWidth = map.getWidth();
		// the height of the map
		int int_mapHeight = map.getHeight();

		// the width of each tile on the map
		int int_tileWidth = map.getTileWidth();
		// the height of each tile on the map
		int int_tileHeight = map.getTileHeight();

		// initialize the 2d array of booleans to the number of pixels in the map
		bool_collision = new boolean[int_mapWidth * int_tileWidth][int_mapHeight * int_tileHeight];

		// loop through each x axis pixel
		for (int x = 0; x < int_mapWidth * int_tileWidth; x++) {
			// loop through each y axis pixel
			for (int y = 0; y < int_mapHeight * int_tileHeight; y++) {
				// convert the screen coordinates to world coordinates, if a
				// tile exists on the collision layer
				// of the map at those coordinates, the collision boolean is set
				// to true
				bool_collision[x][y] = stringToBool("" + map.getTileId(x / int_tileWidth, y / int_tileHeight, Constants.int_kCOLLISIONLAYER));
			}
		}

		// counter for the number of spawns
		int int_spawnNumber = 0;
		// loop through each x axis tile
		for (int x = 0; x < int_mapWidth; x++) {
			// loop through each y axis tile
			for (int y = 0; y < int_mapHeight; y++) {
				// check if the value contains a spawn point
				if (map.getTileId(x, y, Constants.int_kSPAWNLAYER) > 0) {
					// add the spawn point to the array of spawn points
					plr_localPlayer.pnt_spawnPoints[int_spawnNumber] = new Point(x * int_tileWidth, y * int_tileHeight);

					// increase the spawn point counter
					int_spawnNumber++;
				}
			}
		}

	}

	// update positions/ do calculations here, this method is called
	// automatically
	// by the AppGameContainer in the main method of this class
	@Override
	public void update(GameContainer gc_container, int int_delta) throws SlickException
	// delta is the amount of time passed since last update
	{
		//if the left mouse button was pressed
		if (gc_container.getInput().isMousePressed(0)) {
			// shoot where the player clicked
			plr_localPlayer.shoot(gc_container.getInput().getAbsoluteMouseX(), gc_container.getInput().getAbsoluteMouseY(), flt_cameraX, flt_cameraY);
		}
		//if tshe escape key was pressed
		if (gc_container.getInput().isKeyPressed(Input.KEY_ESCAPE)) {
			//stop the music
			AudioManager.getInstance().stopMusic();
			//quit the game
			System.exit(0);
		}

		try {
			// check to see if the left arrow or a key was pressed
			if (gc_container.getInput().isKeyDown(Input.KEY_LEFT) || gc_container.getInput().isKeyDown(Input.KEY_A)) {
				// update the booleans that determine which way the player can
				// move
				updatePlayerMovementDirections(int_delta);
				// move the player left
				plr_localPlayer.moveLeft(Constants.dbl_kPLAYERMOVESPEED * int_delta);
			}
			// check to see if the right arrow or d key was pressed
			else if (gc_container.getInput().isKeyDown(Input.KEY_RIGHT) || gc_container.getInput().isKeyDown(Input.KEY_D)) {
				// update the booleans that determine which way the player can
				// move
				updatePlayerMovementDirections(int_delta);
				// move the player right
				plr_localPlayer.moveRight(Constants.dbl_kPLAYERMOVESPEED * int_delta);
			}

			// check to see if the up arrow or w key was pressed
			if (gc_container.getInput().isKeyDown(Input.KEY_UP) || gc_container.getInput().isKeyDown(Input.KEY_W)) {
				// update the booleans that determine which way the player can
				// move
				updatePlayerMovementDirections(int_delta);
				// move the player up
				plr_localPlayer.moveUp(Constants.dbl_kPLAYERMOVESPEED * int_delta);
			}
			// check to see if the down arrow or s key was pressed
			else if (gc_container.getInput().isKeyDown(Input.KEY_DOWN) || gc_container.getInput().isKeyDown(Input.KEY_S)) {
				// update the booleans that determine which way the player can
				// move
				updatePlayerMovementDirections(int_delta);
				// move the player down
				plr_localPlayer.moveDown(Constants.dbl_kPLAYERMOVESPEED * int_delta);
			}
		} catch (Exception e) {
		}

		// update the locations of the bullets
		plr_localPlayer.updateBullets(int_delta, bool_collision);

		// Camera Update
		// if the player is near the right half of the screen
		if (plr_localPlayer.flt_XPos + flt_cameraX < int_screenWidth / 2 - 150) {
			//update the camera's x position
			flt_cameraX += (Constants.dbl_kPLAYERMOVESPEED * int_delta) - 0.5;

		}
		// if the player is near the left half of the screen
		else if (plr_localPlayer.flt_XPos + plr_localPlayer.int_width + flt_cameraX > int_screenWidth / 2 + 150) {
			//update the camera's x position
			flt_cameraX -= (Constants.dbl_kPLAYERMOVESPEED * int_delta) - 0.5;
		}
		// if the player is near the top half of the screen
		if (plr_localPlayer.flt_YPos + flt_cameraY < int_screenHeight / 2 - 150) {
			//update the camera's y position
			flt_cameraY += (Constants.dbl_kPLAYERMOVESPEED * int_delta) - 0.5;

		}
		// if the player is near the bottom half of the screen
		else if (plr_localPlayer.flt_YPos + plr_localPlayer.int_height + flt_cameraY > int_screenHeight / 2 + 150) {
			//update the camera's y position
			flt_cameraY -= (Constants.dbl_kPLAYERMOVESPEED * int_delta) - 0.5;
		}

		// update the sound buffers
		SoundStore.get().poll(int_delta);
	}

	// takes the delta update time as input, determines which ways the player
	// can move without colliding
	// with walls and sets the player's booleans appropriately, returns nothing
	void updatePlayerMovementDirections(int int_delta) throws Exception {
		// calculate a modified delta value to help keep the player from
		// "sticking" to walls
		int int_deltaMod = (int) Math.floor(int_delta * Constants.dbl_kPLAYERMOVESPEED) + 1;

		/*
		 * Update the left player collision boolean, since the function returns
		 * if there is a collision and the canMove boolean is true if there is
		 * no collision the returned boolean must be flipped using the !
		 * (equivalent of the not gate)
		 */
		plr_localPlayer.bool_canMoveLeft = !Collision.checkLeftCollision((int) plr_localPlayer.flt_XPos - int_deltaMod, (int) plr_localPlayer.flt_YPos, plr_localPlayer.int_width, plr_localPlayer.int_height, bool_collision);

		/*
		 * Update the right player collision boolean, since the function returns
		 * if there is a collision and the canMove boolean is true if there is
		 * no collision the returned boolean must be flipped using the !
		 * (equivalent of the not gate)
		 */
		plr_localPlayer.bool_canMoveRight = !Collision.checkRightCollision((int) plr_localPlayer.flt_XPos + int_deltaMod, (int) plr_localPlayer.flt_YPos, plr_localPlayer.int_width, plr_localPlayer.int_height, bool_collision);

		/*
		 * Update the top player collision boolean, since the function returns
		 * if there is a collision and the canMove boolean is true if there is
		 * no collision the returned boolean must be flipped using the !
		 * (equivalent of the not gate)
		 */
		plr_localPlayer.bool_canMoveUp = !Collision.checkTopCollision((int) plr_localPlayer.flt_XPos, (int) plr_localPlayer.flt_YPos - int_deltaMod, plr_localPlayer.int_width, plr_localPlayer.int_height, bool_collision);

		/*
		 * Update the bottom player collision boolean, since the function
		 * returns if there is a collision and the canMove boolean is true if
		 * there is no collision the returned boolean must be flipped using the
		 * ! (equivalent of the not gate)
		 */
		plr_localPlayer.bool_canMoveDown = !Collision.checkBottomCollision((int) plr_localPlayer.flt_XPos, (int) plr_localPlayer.flt_YPos + int_deltaMod, plr_localPlayer.int_width, plr_localPlayer.int_height, bool_collision);
	}

	// draw/render objects here, this method is called automatically by the
	// AppGameContainer
	// in the main method of this class
	@Override
	public void render(GameContainer gc_container, Graphics g) throws SlickException {

		// draw the map on screen
		map.render((int) flt_cameraX, (int) flt_cameraY);

		// draw the player
		plr_localPlayer.draw(plr_localPlayer.flt_XPos + flt_cameraX, plr_localPlayer.flt_YPos + flt_cameraY);

		//set the drawing colour
		g.setColor(new Color(0, 0, 255, 128));

		// Draw nametag above them
		g.drawString(plr_localPlayer.str_UID, plr_localPlayer.flt_XPos + flt_cameraX, plr_localPlayer.flt_YPos + flt_cameraY - 20);

		// loop through the bullets in the player's bullet array
		for (Bullet blt_bullet : plr_localPlayer.ary_bulletArray) {
			// check is the bullet exists
			if (blt_bullet != null) {
				// draw the bullet if it is not null
				blt_bullet.draw(blt_bullet.flt_XPos - blt_bullet.int_radius + flt_cameraX, blt_bullet.flt_YPos - blt_bullet.int_radius + flt_cameraY);
			}
		}

		// set the drawing colour to white
		g.setColor(new Color(255, 255, 255));

		// draw the string 'bullets'
		g.drawString("Bullets", 180, 0);

		// the distance the bullets in the magazine will be from each other
		int int_spacer = 4;

		// for each bullet in the magazine draw a line on screen to represent it
		for (int i = 0; i < plr_localPlayer.getNumBulletsInMagazine(); i++) {
			// draw each bullet 20 pixels high and int_spacer # of pixels right
			// of the previous
			g.drawLine(i * int_spacer + 200, 40, i * int_spacer + 200, 20);
		}

		// Networked drawing
		for (Player plr_networkedPlayer : Globals.plr_networkedPlayers.values()) {
			try {
				//if the player is not the local player
				if (!plr_networkedPlayer.str_UID.equals(plr_localPlayer.str_UID)) {
					// draw the networked players
					plr_networkedPlayer.draw(plr_networkedPlayer.flt_XPos + flt_cameraX, plr_networkedPlayer.flt_YPos + flt_cameraY);

					// Draw name tag above them
					g.setColor(new Color(255, 0, 0, 128));
					// g.setFont(new Font());
					g.drawString(plr_networkedPlayer.str_UID, plr_networkedPlayer.flt_XPos + flt_cameraX, plr_networkedPlayer.flt_YPos + flt_cameraY - 20);

					// loop through the bullets in the player's bullet array
					for (Bullet blt_bullet : plr_networkedPlayer.ary_bulletArray) {
						// check is the bullet exists
						if (blt_bullet != null) {
							// draw the bullet if it is not null
							blt_bullet.draw(blt_bullet.flt_XPos + flt_cameraX,
									blt_bullet.flt_YPos + flt_cameraY);
							if (blt_bullet.getBounds().intersects(plr_localPlayer.getBounds())) 
							{				
								//respawn the player
								plr_localPlayer.reSpawn();

								//set the camera position
								flt_cameraX = (int) -plr_localPlayer.flt_XPos;
								flt_cameraY = (int) plr_localPlayer.flt_YPos;
							}
						}
					}
				}
			} 
			catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	// takes a string as input, checks if the string contains 0 or > 0 and
	// returns
	// the appropriate boolean (0 - false, > 0 - true)
	public boolean stringToBool(String str) {

		// check if the string is greater than 0 and if so a
		if (Integer.parseInt(str) > 0) {
			// the string contains a number > 0 so the boolean is true
			return true;
		}

		// the string contains a 0 so the boolean is false
		return false;

	}
}