//Bullet object that is shot by the players

package Player;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Circle;

public class Bullet extends Animation {

	// center x position of the bullet
	public float flt_XPos;
	// center y position of the bullet
	public float flt_YPos;

	// x speed
	float flt_dx;
	// y speed
	float flt_dy;

	// dimensions of the bullet image
	public int int_width;
	public int int_height;

	//set a default radius of the ball
	public int int_radius;

	//initialization method
	public Bullet(Image[] img_frames, int int_duration, float flt_x, float flt_y, float flt_dx, float flt_dy) {
		super(img_frames, int_duration);

		// set the x position
		flt_XPos = flt_x;
		// set the y position
		flt_YPos = flt_y;
		// set the dx speed
		this.flt_dx = flt_dx;
		// set the dy speed
		this.flt_dy = flt_dy;

		// set the local bullet width variable from its image
		int_width = img_frames[0].getWidth();
		// set the local bullet height variable from its image
		int_height = img_frames[0].getHeight();
		
		//the radius is the width divided by 2
		int_radius = int_width/2;
	}

	//initialization method
	public Bullet(Image[] img_frames, int int_duration) {
		super(img_frames, int_duration);
	}

	//takes no input, returns a circle matching the bounds of the bullet
	public Circle getBounds() {
		//create a circle bounds object
		return new Circle(flt_XPos, flt_YPos, int_radius);
	}

}
