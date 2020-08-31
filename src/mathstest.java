/*
 * TODO
 * 
 * choose who starts the game
 * 
 * setPlayScreen() 
 * better UI for the love of god
 * make dicebuttons the size of the icons
 * 
 * setPointFix()
 * Scale components based on amount of players
 */

import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.stream.IntStream;
import java.util.Scanner;
import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.NumberFormatter;

import java.util.ArrayList;
import java.util.Arrays;

public class mathstest extends JFrame{
	//List of all frames
	static JFrame initFrame; //Handled in getPlayers()
	static JFrame gameFrame; //Handled in setPlayScreen()
	static JFrame rulesFrame; //Handled in setInstructionScreen()
	static JFrame winFrame; //Handled in setWinScreen()
	static JFrame pFixFrame; //Handled in setPointFix()
	
	static JPanel activeWindow;
	static JPanel statDisp;
	static JPanel diceBox;
	static JPanel subRollReq;	
	
	static int stdWid = 1000;
	static int stdHi = 1000;
	
	public static void main(String[] args) {
		getPlayers();
		
	    //UI colours by component
		Color green = Color.decode("#00BB61");
		Color red = Color.decode("#FF6F00"); //FF6F00 or F90012
		Color gold = Color.decode("#FFD700");
		Font stdFont = new Font("Segoe UI", Font.BOLD, 20);
		
		UIManager.put("Frame.background", green);
		
		UIManager.put("Panel.background", green);
	    
		UIManager.put("Label.background", gold);
	    UIManager.put("Label.font", stdFont);
		
		UIManager.put("TextField.background", gold);
	    UIManager.put("TextField.font", stdFont);
	    
	    UIManager.put("FormattedTextField.background", gold);
	    UIManager.put("FormattedTextField.font", stdFont);
	    
	    UIManager.put("ComboBox.background", gold);
	    UIManager.put("ComboBox.selectionBackground", gold);
	    UIManager.put("ComboBox.font", stdFont);
		
	    UIManager.put("Button.border", 10);
		UIManager.put("Button.background", red);
	    UIManager.put("Button.foreground", Color.white);
	    UIManager.put("Button.font", stdFont);
	    SwingUtilities.updateComponentTreeUI(initFrame);
	}// end method main
	
	static class values{
		static ArrayList<String> playerNames = new ArrayList<String>();
		static ArrayList<Integer> playerPoints = new ArrayList<Integer>();
		static int total = 0;
		static int rowVals[] = new int[7];
		static int activePN = 0;
		static int partPoints = 30; 
	}//end class values
	
	static class playerFns {
		
		
		public final static void changePlayer(JLabel dispTotal, JLabel[] localPNs) {
		if(values.playerNames.size() > 1) {
			//determine next active player
			do {
				if (values.activePN < values.playerNames.size() - 1) {
					values.activePN = values.activePN + 1;
				} else { 
					values.activePN = 0;
				}
			} while (values.playerPoints.get(values.activePN) <= 0);
		
			//empty totals array
			Arrays.fill(values.rowVals, 0);
			values.total = IntStream.of(values.rowVals).sum();
			((JLabel) dispTotal).setText("Current dice total is: " + String.valueOf(values.total));
		
			//change highlight of active player
			for (int i = 0; i < values.playerNames.size(); i++) {
				localPNs[i].setBorder(BorderFactory.createEmptyBorder());
			}
			localPNs[values.activePN].setBorder(BorderFactory.createLineBorder(Color.white, 5));
			}
		}// end changePlayer()
		
		public final static void checkWin() {
			if (values.playerNames.size() >1 ) {
			int lTot = 0;
			String winner = "no one";
			for (int i = 0; i < values.playerPoints.size(); i++) {
				if (values.playerPoints.get(i) <= 0) {
					lTot++;
				}
				else {
					winner = values.playerNames.get(i);
				}
			}
			if (lTot == values.playerPoints.size() - 1) {
				gameFrame.setVisible(false);
				setWinScreen(winner);
			}
			}
			else {
				if (values.playerPoints.get(0) <= 0) {
					setWinScreen("no one");
				}
			}
			
		}
	}
	
	public static void getPlayers() {
		//instantiate components
		initFrame = new JFrame();
		JLabel welcome = new JLabel ("Welcome to the virtual dice game assistant!");
		JLabel playerNameReq = new JLabel("Enter Player Names: ");
		JTextField playerNameInput[] = new JTextField[8]; 
	    JButton submit = new JButton("Start game");
	    JButton rules = new JButton("How to play");
	   
	    JPanel changePoints = new JPanel(new GridLayout(1, 2));
	    JLabel pointsReq = new JLabel("Adjust points?");
	    NumberFormat format = NumberFormat.getInstance();
	    NumberFormatter formatter = new NumberFormatter(format);
	    formatter.setValueClass(Integer.class);
	    formatter.setMinimum(0);
	    formatter.setMaximum(99);
	    formatter.setAllowsInvalid(false);
	    // If you want the value to be committed on each keystroke instead of focus lost
	    formatter.setCommitsOnValidEdit(true);
	    JFormattedTextField startPoints = new JFormattedTextField(formatter);
	    startPoints.setValue(values.partPoints);
	    
	    JFrame error = new JFrame("Error");
	    JLabel[] eMess = new JLabel[1];
	    eMess[0] = new JLabel("Error: Please submit at least one name");
	    
	     //make functions
	     ActionListener submitAction = new ActionListener() {
	    	 public void actionPerformed(ActionEvent e) {
	    		 values.partPoints = Integer.valueOf(startPoints.getText());
	    		 //add the player names from the playerNameInput text field array, checks all fields
	    		 for (int i = 0; i < playerNameInput.length; i++) {
	    			 if (!playerNameInput[i].getText().contentEquals(" ")) { 
	    				 values.playerNames.add(playerNameInput[i].getText());
	    				 values.playerPoints.add(values.partPoints);
	    			 }
	    		 }
	    		 
	    		 if(values.playerNames.size()  == 0) {
	    			 error.add(eMess[0]);
	    			 error.setSize(stdWid, stdHi / 4);
	    			 error.show();
	    			 return;
	    		 }
	    		 
	    		 //add up amount of valid text fields, set playerNames to that length
	    		 initFrame.setVisible(false);
	    		 setPlayScreen();
	    	 }
	     };//end ActionListener Submit
	     
	     ActionListener rulesAction = new ActionListener() {
	    	 public void actionPerformed(ActionEvent f) {
	    		 setInstructionScreen();
	    	 }
	     };//end ActionListener rulesAction
	     
	     //display on frame
	     initFrame.setLayout(new GridLayout(13, 1, 2, 2));
	     
	     
	     initFrame.add(welcome);
	    
	     initFrame.add(playerNameReq);
	     for (int i = 0; i < playerNameInput.length; i++ ) {
		    	playerNameInput[i] = new JTextField(" ");
		    	initFrame.add(playerNameInput[i]);
		    }
	     
	     submit.addActionListener(submitAction);
	     initFrame.add(submit);

	     rules.addActionListener(rulesAction);
	     initFrame.add(rules);
	     
	     changePoints.add(pointsReq);
	     changePoints.add(startPoints);
	     initFrame.add(changePoints);
	     
	     initFrame.setSize(stdWid,stdHi);
	     initFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	     initFrame.show();
	}
	
	public static void setInstructionScreen() {
		//instantiate components
		rulesFrame = new JFrame("Dice game rules");
		JLabel rulesBody = new JLabel();
		
		
		//make functions
		String data = "<html>";
		try {
		      File myObj = new File("res\\rules.txt");
		      Scanner myReader = new Scanner(myObj);
		      while (myReader.hasNextLine()) {
		        data += myReader.nextLine() + "<br>";
		      }
		      myReader.close();
		    } catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
		data += "</html>";
		
		rulesBody.setText(data);
		//display on frame
		rulesFrame.add(rulesBody);
		rulesFrame.setSize(stdWid,stdHi);
		rulesFrame.show();
	}
	
	public static void setPlayScreen() {
		//Initialize frame contents	
		gameFrame = new JFrame("Virtual Dice Game Assisstant");
		gameFrame.setLayout(new GridBagLayout());
		activeWindow = new JPanel();
		activeWindow.setLayout(new CardLayout());
		diceBox = new JPanel();	
		diceBox.setLayout(new GridBagLayout());
		statDisp = new JPanel();
		JLabel localPNs[] = new JLabel[values.playerNames.size()];
		JLabel localPPs[] = new JLabel[values.playerPoints.size()];
		activeWindow.setBorder(BorderFactory.createLineBorder(Color.black, 5));
		statDisp.setBorder(BorderFactory.createLineBorder(Color.black, 5));
		JButton dice[][] = new JButton[7][7];
		JButton next = new JButton("Next");
		JLabel dispTotal = new JLabel("Current dice total is: " + String.valueOf(values.total));	
		subRollReq = new JPanel();
		JLabel subRollReqTxt = new JLabel ("How many " + "did you roll?");
		String[] diceOpts = {"0", "1", "2", "3", "4", "5", "6"};
		JComboBox<String> diceMultiplyer = new JComboBox<String>(diceOpts);
		JButton submitDiceMult = new JButton("Submit");
		JButton changePoints = new JButton("Change points");
		JPanel playFunctions = new JPanel(new GridLayout(1, 3, 5, 5));
		
		
		activeWindow.add(diceBox, "dice");
		activeWindow.add(subRollReq, "sub");
		
		
		//make functions
		ActionListener addDice = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//get used variables from e
				JButton button = (JButton)e.getSource();
				int row = Integer.parseInt(button.getName());
				String str = e.getActionCommand();
						
				//change borders on all dice
				for (int j = 1; j <= 6; j++) {
					dice[j][row].setBorder(BorderFactory.createLineBorder(Color.red, 5));
					}
					button.setBorder(BorderFactory.createLineBorder(Color.blue, 5));
						
				//change number total
				values.rowVals[row] = Integer.parseInt(str);
				values.total = IntStream.of(values.rowVals).sum();
				dispTotal.setText("Current dice total is: " + String.valueOf(values.total)); 
				}//end method actionPerformed
			};//end Action Listener addDice
				
			ActionListener clearDice = new ActionListener() {
				public void actionPerformed(ActionEvent f) {
				//change player's points
					if (values.total <= 30) {
						values.playerPoints.set(values.activePN, values.playerPoints.get(values.activePN) + (values.total - 30) );
						playerFns.checkWin();
						localPPs[values.activePN].setText(Integer.toString(values.playerPoints.get(values.activePN)));
						playerFns.changePlayer(dispTotal, localPNs);
						
					
					}else {
						//player gets < 30
						//find previous person to take points from
						CardLayout cl = (CardLayout) activeWindow.getLayout();
						cl.show(activeWindow, "sub");
					}
				
					//reset array and remove dice borders
					for (int j = 1; j <=6; j++) {
						for (int i = 1; i <= 6; i++ ) {
							dice[i][j].setBorder(BorderFactory.createEmptyBorder());
						}
					}//end for loop remove border
				}// end actionPerformed()		
			};//end ActionListener clearDice
				
			ActionListener subSub = new ActionListener(){
				public void actionPerformed(ActionEvent g) {
					//calculate who is the previous player
					int prevPN = values.activePN;
					do {
						if (prevPN == 0) {
							prevPN = values.playerNames.size() - 1;		
						}
						else {
							prevPN -= 1;
						}
					} while (values.playerPoints.get(prevPN) <= 0 );
					
					//get value in combobox, multiply it by 
					int tempVal =  Integer.valueOf(String.valueOf(diceMultiplyer.getSelectedItem()));
					values.playerPoints.set(prevPN, values.playerPoints.get(prevPN) - ((values.total - 30) * tempVal) ); //multiply this by amount of dice rolled
					playerFns.checkWin();
					localPPs[prevPN].setText(Integer.toString(values.playerPoints.get(prevPN)));
					
					playerFns.changePlayer(dispTotal, localPNs);
					
					CardLayout cl = (CardLayout) activeWindow.getLayout();
					cl.show(activeWindow, "dice");
					
				}
			};
			
			ActionListener changePointsAction = new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setPointFix();
				}
			};
			
			GridBagConstraints c = new GridBagConstraints();
			for (int j = 1; j <=6; j++) { //initialize dice buttons
				for (int i = 1; i <= 6; i++ ) {
					String iStr = Integer.toString(i);
					Icon d1 = new ImageIcon("res\\d" + iStr + ".PNG");
					dice[i][j] = new JButton(d1);
					dice[i][j].setName(Integer.toString(j));
					dice[i][j].setActionCommand(iStr);
					dice[i][j].addActionListener(addDice);
					Dimension dSize = new Dimension(d1.getIconHeight(), d1.getIconWidth());
					dice[i][j].setSize(dSize);
					c.gridx = i;
					c.gridy = j;
					c.weightx = 0.166;
					c.weighty = 0.166;
					diceBox.add(dice[i][j], c);
					}
				}//end dice init for loop
			
			//populate player names and scores
			for (int i = 0; i < values.playerNames.size(); i++) {
				localPNs[i] = new JLabel(values.playerNames.get(i), SwingConstants.CENTER);
				localPPs[i] = new JLabel(Integer.toString(values.playerPoints.get(i)), SwingConstants.CENTER);
				statDisp.add(localPNs[i]);
				statDisp.add(localPPs[i]);
			}
			
			//Assemble components
			gameFrame.setSize(stdWid,stdHi);
			
			
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 0.6;
			c.insets = new Insets(50,50,0,50); //Up, Left, Down, Right
			gameFrame.add(activeWindow, c);		
			
			diceBox.setSize(gameFrame.getWidth()/2, gameFrame.getHeight());
	
			submitDiceMult.addActionListener(subSub);
			subRollReq.setLayout(new GridLayout(1, 3));
			subRollReq.add(subRollReqTxt);
			subRollReq.add(diceMultiplyer);
			subRollReq.add(submitDiceMult);
			
			localPNs[values.activePN].setBorder(BorderFactory.createLineBorder(Color.white, 5));
			statDisp.setLayout(new GridLayout (values.playerNames.size(), 2));
			c.gridx = 0;
			c.gridy = 1;
			c.weighty = 0.2;
			c.insets = new Insets(10, 10, 10, 10);
			gameFrame.add(statDisp, c);
			
			next.addActionListener(clearDice);
			playFunctions.add(next);
			playFunctions.add(dispTotal);
			changePoints.addActionListener(changePointsAction);
			playFunctions.add(changePoints);
			c.gridx = 0;
			c.gridy = 2;
			c.weighty = 0.2;
			c.insets = new Insets(20, 0, 50, 0);
			gameFrame.add(playFunctions, c);
			
			//gameFrame.pack();
			gameFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			gameFrame.pack();
			gameFrame.show();
	}// end method setPanel
	
	public static void setWinScreen(String winner) {
		//Initialize frame contents	
		winFrame = new JFrame("A winner!");
		JLabel winTxt = new JLabel("A winner is " + winner + "!!");
		JButton replay = new JButton("Replay with same players?");
		JButton restart = new JButton("Play with new players?");
		
		//make functions
		ActionListener replayAction = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for(int i = 0; i < values.playerNames.size(); i++) {
					values.playerPoints.set(i, 30);
				}
				winFrame.setVisible(false);
				setPlayScreen();
			}
		};
		
		ActionListener restartAction = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				for(int i = 0; i < values.playerNames.size(); i++) {
					values.playerNames.clear();
					values.playerPoints.clear();
				}
				winFrame.setVisible(false);
				getPlayers();
			}
		};
		
		//Assemble components
		winFrame.setSize(stdWid,stdHi);
		winFrame.setLayout(new GridLayout(3, 1, 10, 10));
		
		winFrame.add(winTxt);
		
		replay.addActionListener(replayAction);
		winFrame.add(replay);
		
		restart.addActionListener(restartAction);
		winFrame.add(restart);
		
		winFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		winFrame.show();
	}
	
	public static void setPointFix() {
		//instantiate components
		pFixFrame = new JFrame("Fix dice points");
		pFixFrame.setLayout(new GridLayout(4,1));
		JTextField pNames[] = new JTextField[values.playerNames.size()];
		JTextField pPoints[] = new JTextField[values.playerPoints.size()];
		JLabel pFixDia = new JLabel("Who's turn is it?");
		String[] playerOpts = new String[values.playerNames.size()];
		JComboBox<String> activeP;
		JButton pFixSubmit = new JButton("Continue");
		JLabel errAP0 = new JLabel("The active player cannot have 0 points");
		
		JFrame error = new JFrame("Error");
		JLabel[] eMess = new JLabel[2];
		eMess[0] = new JLabel("Error: Active player cannot have 0 points");
		
		JPanel pNamesPan = new JPanel(new GridLayout(values.playerNames.size(), 2));
		//Populate player info arrays for combobox, names and point values
		for(int i = 0; i < values.playerNames.size(); i++) {
			playerOpts[i] = values.playerNames.get(i);
			pNames[i] = new JTextField(values.playerNames.get(i));
			pNamesPan.add(pNames[i]);
			pPoints[i] = new JTextField (String.valueOf(values.playerPoints.get(i)));
			pNamesPan.add(pPoints[i]);
		}
		activeP = new JComboBox<String>(playerOpts);
		activeP.setSelectedIndex(values.activePN);
		//make functions
		ActionListener pFixSubmitAction= new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//set active player if valid choice
				int activePChoice = values.playerNames.indexOf(activeP.getSelectedItem());
				if (Integer.parseInt(pPoints[activePChoice].getText()) <= 0 ) {
					error.add(eMess[0]);
					error.setSize(stdWid, stdHi/4);
					error.show();
					return;
				}
				values.activePN = values.playerNames.indexOf(activeP.getSelectedItem());
				for (int i = 0; i < pNames.length; i++) {
					//set pNames to values.playerNames
					values.playerNames.set(i, pNames[i].getText());
					//set pPoints to values.playerPoints
					values.playerPoints.set(i, Integer.parseInt(pPoints[i].getText()));
					}
					//change frame
					gameFrame.setVisible(false);
					pFixFrame.setVisible(false);
					setPlayScreen();
			}
		};
		
		//Assemble components
		
		pFixFrame .add(pNamesPan);
		
		pFixFrame.add(pFixDia);
		pFixFrame.add(activeP);
		pFixSubmit.addActionListener(pFixSubmitAction);
		pFixFrame.add(pFixSubmit);
		
		pFixFrame.setSize(stdWid, stdHi);
		pFixFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		pFixFrame.show();
	}// end setPointFix()

}//end class mathstest