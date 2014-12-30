package Screens;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.*;
import javax.swing.*;

@SuppressWarnings("serial")
public abstract class BasicMenu extends JPanel implements ActionListener {

	// parent frame
	public JFrame jfrm_parent;

	// jpanel for the top of the view (ex for the title)
	JPanel jpnl_Panel = new JPanel();

	// background image
	BufferedImage bfrd_img_backgroundImage = null;

	// label for the title at the top of the frame
	JLabel lbl_title;


	// constructor, takes screen width and height as well as the parent frame as
	// input,
	// outputs a created BasicMenu
	public BasicMenu(int int_width, int int_height, String str_title, JFrame frm_parent) {
		// set the parent frame
		jfrm_parent = frm_parent;


		// set the size of the jframe
		frm_parent.setSize(int_width, int_height);
		// let the program know to exit if the window is closed
		frm_parent.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// set the title string
		lbl_title = new JLabel(str_title);

		// set the path to the font
		File str_fontFile = new File("./resources/Fonts/gota.ttf");

		// font variable that will hold the loaded font
		Font font;
		try {
			// load the font from a file
			font = Font.createFont(Font.TRUETYPE_FONT, str_fontFile);
			// make the font bigger
			font = font.deriveFont(75.0f);

		} catch (FontFormatException e1) {
			// the font couldn't be loaded load a default
			font = new Font("serif", Font.PLAIN, 80);
		} catch (IOException e1) {
			// the font couldn't be loaded load a default
			font = new Font("serif", Font.PLAIN, 80);
		}

		// setup various aspects of the jpanel
		setupJPanel(int_width, int_height);

		try {
			// load the background image from a file
			bfrd_img_backgroundImage = ImageIO.read(new File("./resources/Images/low_contrast_linen.png"));

		} catch (IOException e) {
			// the image was not found
			System.out.println("no load");
		}
	}

	// takes width and height as input, adds the button layout panel to 'this'
	// panel
	// returns nothing
	void setupJPanel(int int_width, int int_height) {
		// make the panel visible
		setVisible(true);

		// set the size of the panel
		setSize(int_width, int_height);
		// put the panel in the top corner of the window
		setLocation(0, 0);

		// make the background of the button layout panel transparent
		jpnl_Panel.setOpaque(false);

		// set the bounds of the button layout so the buttons dont take up the
		// whole screen
		jpnl_Panel.setLayout(null);
		jpnl_Panel.setBounds(0, 0, int_width, int_height);

		// add the jpanel to the main jpanel
		jfrm_parent.add(jpnl_Panel);
	}


	// button callback
	public abstract void actionPerformed(ActionEvent e);	
}
