//player object for both remote and local players

package Player;

import java.awt.Point;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

import HelperClasses.Collision;

public class Player extends Animation {

	// x position of the player
	public float flt_XPos = 33;
	// y position of the player
	public float flt_YPos = 33;
	// width of the player
	public int int_width;
	// height of the player
	public int int_height;

	// can the player move right?
	public boolean bool_canMoveRight = true;
	// can the player move left?
	public boolean bool_canMoveLeft = true;
	// can the player move up?
	public boolean bool_canMoveUp = true;
	// can the player move down?
	public boolean bool_canMoveDown = true;

	// create and array to hold bullets
	public Bullet[] ary_bulletArray;
	// the number of bullets remaining in the player's magazine
	private int int_magazine = 5;

	// Create an array of images for the bullet image(s) as the constructor
	// takes an array as a parameter (this is a slick image not an awt image)
	public Image[] img_bullet;

	//the number of spawn points on a map
	private int int_numOfSpawns = 4;
	
	//array to hold the spawn points 
	public Point[] pnt_spawnPoints = new Point[int_numOfSpawns];
	
	// IP addresses
	public String str_localAddress;
	//only used in non-lan mode
	public String str_externalAddress;
	//made up of a combination of the ip addresses
	public String str_UID;

	//initialization method, takes users internal and external IPs as strings, and array of images for animation
	public Player(String str_localAddress, String str_externalAddress, Image[] img_player) {
		super(img_player, 1);
		// set the player's unique identifier
		this.str_localAddress = str_localAddress;
		this.str_externalAddress = str_externalAddress;

		this.str_UID = str_localAddress + str_externalAddress;

		// set the local player width variable from its image
		int_width = img_player[0].getWidth();
		// set the local player height variable from its image
		int_height = img_player[0].getHeight();

		// give the bullet image array 1 place for an image
		img_bullet = new Image[1];

		// make the array hold the same number of bullets as the magazine
		ary_bulletArray = new Bullet[int_magazine];

		try {
			// load the bullet image into the first spot in the array
			img_bullet[0] = new Image("./resources/Images/Bullet.png");
		} catch (SlickException e) {
			// couldnt load the image
			e.printStackTrace();
		}
	}
	
	//takes no arguments, moves the player to a spawn location, returns nothing
	public void reSpawn()
	{
		//create a random spawn point
		Point pnt_newSpawn = pnt_spawnPoints[(int )(Math.random() * int_numOfSpawns)];
		//set the new spawn x
		this.flt_XPos = pnt_newSpawn.x;
		//set the new spawn y
		this.flt_YPos = pnt_newSpawn.y;
	}

	// takes the speed to move the player at as input,
	// moves the player right if possible, returns nothing
	public void moveRight(double dbl_speed) {
		// check if the player can move right
		if (bool_canMoveRight) {
			// move the player right
			flt_XPos += dbl_speed;
		}
	}

	// takes the speed to move the player at as input,
	// moves the player left if possible, returns nothing
	public void moveLeft(double dbl_speed) {
		// check if the player can move left
		if (bool_canMoveLeft) {
			// move the player left
			flt_XPos -= dbl_speed;
		}
	}

	// takes the speed to move the player at as input,
	// moves the player up if possible, returns nothing
	public void moveUp(double dbl_speed) {
		// check if the player can move up
		if (bool_canMoveUp) {
			// move the player up
			flt_YPos -= dbl_speed;
		}
	}

	// takes the speed to move the player at as input,
	// moves the player down if possible, returns nothing
	public void moveDown(double dbl_speed) {
		// check if the player can move down
		if (bool_canMoveDown) {
			// move the player down
			flt_YPos += dbl_speed;
		}
	}

	/*takes the update delta, and the walls collision array as input, moves the bullet in it's direction of travel,
	checks if bullets collide with the walls and destroys it if it does*/
	public void updateBullets(int int_delta, boolean bool_collision[][]) {
		// loop through the bullets in the player's bullet array
		for (int i = 0; i < ary_bulletArray.length; i++) {

			Bullet blt_bullet = ary_bulletArray[i];
			// check is the bullet exists
			if (blt_bullet != null) {
				// update the x position of the bullet (move it left or right)
				blt_bullet.flt_XPos += blt_bullet.flt_dx * (int_delta / 1.5);
				// update the y position of the bullet (move it up or down)
				blt_bullet.flt_YPos += blt_bullet.flt_dy * (int_delta / 1.5);

				//check to see if the bullet collided with a wall
				if (Collision.checkCollision((int) blt_bullet.flt_XPos - blt_bullet.int_radius, (int) blt_bullet.flt_YPos - blt_bullet.int_radius, blt_bullet.int_width, blt_bullet.int_height, bool_collision)) {
					blt_bullet = null;
					//add a bullet back to the magazine
					int_magazine++;
				}
				
				//put the bullet back in the array
				ary_bulletArray[i] = blt_bullet;
			}
		}
	}

	// takes no input returns the number of bullets in the magazine
	public int getNumBulletsInMagazine() {
		// return the number of bullets in the magazine
		return int_magazine;
	}

	// takes x,y of where to shoot and the centers of the x and y axes as input,
	// calculates the slope
	// and creates a bullet that travels towards the x,y form the player, and
	// adds it to the bullet array
	public void shoot(float x, float y, float flt_screenXCenter, float flt_screenYCenter) {
		//Calculate the divisor used for slope calculations
		float divisor = (float) Math.sqrt(Math.pow(x - (flt_XPos + flt_screenXCenter), 2) + Math.pow(y - (flt_YPos + flt_screenYCenter), 2));
		//calculate the horizontal slope of the bullet (dx)
		float slopeX = (x - (flt_XPos + flt_screenXCenter)) / divisor;
		//calculate the vertical slope of the bullet (dy)
		float slopeY = (y - (flt_YPos + flt_screenYCenter)) / divisor;

		// if the user has a bullet remaining in his magazine
		if (int_magazine > 0) {
			// loop through the bullets
			for (int i = 0; i < ary_bulletArray.length; i++) {
				// look for the 'empty' bullet in the array
				if (ary_bulletArray[i] == null) {
					// create a bullet that starts in the center of the player,
					// set its dx and dy to the calculated slopes
					ary_bulletArray[i] = new Bullet(img_bullet, 1, flt_XPos + (int_width / 2), flt_YPos + (int_height / 2), slopeX, slopeY);
					// take 1 bullet out of the magazine
					int_magazine--;

					//break the loop as a bullet has been shot
					break;
				}
			}
		}
	}

	//takes no input, returns a rectangle matching the bounds of the player
	public Rectangle getBounds() {
		//create a rectangle bounds object
		return new Rectangle(flt_XPos, flt_YPos, int_width, int_height);
	}

}
