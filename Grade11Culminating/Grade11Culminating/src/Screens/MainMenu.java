//the main menu

package Screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.newdawn.slick.openal.SoundStore;

import HelperClasses.AudioManager;
import HelperClasses.Globals;

@SuppressWarnings("serial")
public class MainMenu extends JPanel implements ActionListener {

	//parent frame
	JFrame frm_parent;

	//height and width of the screen
	int int_screenWidth, int_screenHeight;

	//images to display various game aspects
	BufferedImage img_backgroundImage, img_headerImage, img_instructions;

	//is the game ready to be started?
	boolean bool_ready = false;

	// constructor, takes screen width and height as well as the parent frame as
	// input,
	// outputs a created MainMenu
	public MainMenu(int int_width, int int_height, JFrame frm_parent) {
		
		// set the parent frame
		this.frm_parent = frm_parent;
		// set screen width
		int_screenWidth = int_width;
		// set screen height
		int_screenHeight = int_height;

		// load the background image from a file
		try {
			// load the header image
			img_headerImage = ImageIO.read(new File(
					"./resources/Images/Header.png"));
			// load the background image
			img_backgroundImage = ImageIO.read(new File(
					"./resources/Images/low_contrast_linen.png"));
			// load the instructions image
			img_instructions = ImageIO.read(new File(
					"./resources/Images/Instructions.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		// set the size of the parent jframe
		frm_parent.setSize(int_width, int_height);

		// let the program know to exit if the window is closed
		frm_parent.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//set the panel to be semi-transparent
		this.setOpaque(false);
		//remove all layouts from the panel
		this.setLayout(null);
		//set the size of the panel
		this.setSize(int_width, int_height);
		//set the location of the panel on screen
		this.setLocation(0, 0);

		//calculate the button width
		int int_buttonWidth = int_width / 4;
		//set the button height
		int int_buttonHeight = 75;

		//calculate the x offset for the buttons
		int int_xOffest = int_width / 2 - ((int_buttonWidth + 10) * 3) / 2;
		//calculate the y offset for the buttons
		int int_yOffset = int_height - int_buttonHeight - 40;

		// add a multiplayer server button to the button layout
		this.add(createNewButton("Multiplayer (Server)", "MultiStartServer", int_xOffest,
				int_yOffset, int_buttonWidth, int_buttonHeight));
		// add a multiplayer client button to the button layout
		this.add(createNewButton("Multiplayer (Client)", "MultiStartClient",
				int_xOffest + (int_buttonWidth + 10) * 1, int_yOffset,
				int_buttonWidth, int_buttonHeight));
		// add an exit button to the button layout
		this.add(createNewButton("Exit", "Exit", int_xOffest
				+ (int_buttonWidth + 10) * 2, int_yOffset, int_buttonWidth,
				int_buttonHeight));

		// display the menu
		displayMenu();
	}

	// takes title and action command as input, creates a transparent button
	// with white text and size 16 font, returns the created button
	public JButton createNewButton(String str_title, String str_actionCommand,
			int x, int y, int w, int h) {
		// create a new game button
		JButton btn_newButton = new JButton(str_title);
		//dont paint the boarder of the button
		btn_newButton.setBorderPainted(false);
		//set the color of the button
		btn_newButton.setBackground(new Color(255, 255, 255, 64));
		//set the size of the button
		btn_newButton.setBounds(x, y, w, h);

		//set the button font to a basic sans-serif font sized 20
		btn_newButton.setFont(new Font("sansserif", Font.PLAIN, 20));

		// add the button to an action listener so we know when it is pressed
		btn_newButton.addActionListener(this);

		//add an action listener to the buttons for roll-over events
		btn_newButton.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent evt) {
				//get the button that was pressed
				JButton button = (JButton) evt.getComponent();
				//change the background color and its alpha value
				button.setBackground(new Color(255, 255, 255, 128));
				//repaint the panel
				repaint();
			}
			//on mouse click
			public void mouseClicked(MouseEvent evt) {
				//get the button that was pressed
				JButton button = (JButton) evt.getComponent();
				//change the background color and its alpha value
				button.setBackground(new Color(255, 255, 255, 192));
				//repaint the panel
				repaint();
			}
			//on mouse button release
			public void mouseReleased(MouseEvent evt) {
				//get the button that was pressed
				JButton button = (JButton) evt.getComponent();
				//change the background color and its alpha value
				button.setBackground(new Color(255, 255, 255, 64));
				//repaint the panel
				repaint();
			}

			public void mouseExited(MouseEvent evt) {
				//get the button that was pressed
				JButton button = (JButton) evt.getComponent();
				//change the background color and its alpha value
				button.setBackground(new Color(255, 255, 255, 64));
				//repaint the panel
				repaint();
			}
		});

		// set the command used in the callback method
		btn_newButton.setActionCommand(str_actionCommand);

		// return the newly setup button
		return btn_newButton;
	}

	public void paintComponent(Graphics g) {
		// draw super components
		super.paintComponent(g);

		// calculate the number of tiles that will fit across the screen
		int int_xRepeats = int_screenWidth / img_backgroundImage.getWidth() + 1;
		// calculate the number of tiles that will fit down the screen
		int int_yRepeats = int_screenHeight / img_backgroundImage.getHeight()
				+ 1;

		// draw each tile so the screen is completely covered
		for (int x = 0; x < int_xRepeats; x++) {
			for (int y = 0; y < int_yRepeats; y++) {
				// draw the background tile
				g.drawImage(img_backgroundImage, img_backgroundImage.getWidth()
						* x, img_backgroundImage.getHeight() * y, null);
			}
		}

		// draw the title header
		g.drawImage(img_headerImage,
				(int_screenWidth - img_headerImage.getWidth()) / 2, 0, null);
		// draw the instructions
		g.drawImage(img_instructions,
				(int_screenWidth - img_instructions.getWidth()) / 2,
				img_headerImage.getHeight(), null);

	}

	// button callbacks
	public void actionPerformed(ActionEvent e) {
		Globals.getInstance();
		// check to see which button was pressed
		// multiplayer server button
		if ("MultiStartServer".equals(e.getActionCommand())) {
			// remove the menu from view and resume the game
			Globals.isServer = true;
			try {
				// set the server address as the local address
				Globals.serverIP = InetAddress.getLocalHost().getHostAddress();
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//remove the menu from view
			dismissMenu();
		}
		// multiplayer client button
		else if ("MultiStartClient".equals(e.getActionCommand())) {
			// remove the menu from view and resume the game
			Globals.isServer = false;
			//get the server's ip from the client
			Globals.serverIP = createInputDialog();
			if (bool_ready)
				//remove the menu from view
				dismissMenu();

			System.out.println(Globals.serverIP);
		}
		// exit button
		else if ("Exit".equals(e.getActionCommand())) {
			// quit the program
			System.exit(0);
		}

	}

	// Takes the title of the message and the message as input and displays a
	// message box with that information, returns the server IP
	String createInputDialog() {

		//create a dialogue box telling the user to enter an ip
		String str_serverIP = JOptionPane.showInputDialog(null, "Server IP Address: ",
				"Enter the Server IP Address", 1);
		//if something was entered continue
		if (str_serverIP != null) {
			//the game is ready to start
			bool_ready = true;
			//return the entered ip address
			return str_serverIP;
		} 
		//if nothing was entered
		else {
			//let the user know they need to enter an ip address
			JOptionPane.showMessageDialog(null,"Please Enter an IP Address", "Error!", 1);
			//the game is not ready to start
			bool_ready = false;
			//return nothing
			return "";
		}
	}

	// adds this panel to the parent menu and displays it
	void displayMenu() {
		// add the menu panel to the frame
		frm_parent.add(this);
		// show the main menu to the user
		frm_parent.setVisible(true);
		// repaint the frame
		this.repaint();
		// validate the frame
		this.validate();
	}

	// takes no arguments, removes the menu from view, returns nothing
	void dismissMenu() {
		synchronized (frm_parent) {
			// tell the parent frame to stop waiting
			frm_parent.notify();
		}
		// remove the pause menu from the frame
		frm_parent.removeAll();

		// hide the menu
		frm_parent.setVisible(false);
		// disable the frame
		frm_parent.setEnabled(false);
		// dispose of the frame's resources
		frm_parent.dispose();
	}

}
