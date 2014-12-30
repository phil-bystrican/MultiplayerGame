package HelperClasses;


public class Collision {
	/*
	 * Note: passing a boolean array with tens of thousands of elements should
	 * not cause slow downs or use too much memory as java passes pointers to
	 * functions unless told to copy
	 */

	// takes the x and y positions, width, height and an array of an the object
	// and checks if an object will collide with
	// precalculated points contained in the boolean array, returns true if a
	// collision occurred and false if one did not
	static public boolean checkCollision(int int_x, int int_y, int int_width, int int_height, boolean bool_collision[][]) {
		// check if a collision occurred
		if (bool_collision[int_x][int_y] || bool_collision[int_x + int_width][int_y] || bool_collision[int_x][int_y + int_height]
				|| bool_collision[int_x + int_width][int_y + int_height]) {
			// a collision occurred
			return true;
		}

		// no collision occurred
		return false;
	}

	// takes the x and y positions, width, height and an array of an the object
	// and checks if an object's bottom will collide
	// with precalculated points contained in the boolean array, returns true if
	// a collision occurred and false if one did not
	static public boolean checkBottomCollision(int int_x, int int_y, int int_width, int int_height, boolean bool_collision[][]) {
		// check if a collision occurred on the corners or the middle
		if (bool_collision[int_x][int_y + int_height] | bool_collision[int_x + int_width][int_y + int_height]) {
			// a collision occurred
			return true;
		}

		// no collision occurred
		return false;
	}

	// takes the x and y positions, width, height and an array of an the object
	// and checks if an object's top will collide
	// with precalculated points contained in the boolean array, returns true if
	// a collision occurred and false if one did not
	static public boolean checkTopCollision(int int_x, int int_y, int int_width, int int_height, boolean bool_collision[][]) {
		// check if a collision occurred
		if (bool_collision[int_x][int_y] | bool_collision[int_x + int_width][int_y]) {
			// a collision occurred
			return true;
		}

		// no collision occurred
		return false;
	}

	// takes the x and y positions, width, height and an array of an the object
	// and checks if an object's left will collide
	// with precalculated points contained in the boolean array, returns true if
	// a collision occurred and false if one did not
	static public boolean checkLeftCollision(int int_x, int int_y, int int_width, int int_height, boolean bool_collision[][]) {
		// check if a collision occurred
		if (bool_collision[int_x][int_y] | bool_collision[int_x][int_y + int_height]) {
			// a collision occurred
			return true;
		}

		// no collision occurred
		return false;
	}

	// takes the x and y positions, width, height and an array of an the object
	// and checks if an object's right will collide
	// with precalculated points contained in the boolean array, returns true if
	// a collision occurred and false if one did not
	static public boolean checkRightCollision(int int_x, int int_y, int int_width, int int_height, boolean bool_collision[][]) {
		// check if a collision occurred
		if (bool_collision[int_x + int_width][int_y] | bool_collision[int_x + int_width][int_y + int_height]) {
			// a collision occurred
			return true;
		}

		// no collision occurred
		return false;
	}


}
