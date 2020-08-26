/*
 * TODO
 * 
 * choose who starts the game
 * How to play
 * 
 * setPlayScreen() 
 * better UI for the love of god
 * 
 */

import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.stream.IntStream;
import java.util.Scanner;
import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.util.ArrayList;
import java.util.Arrays;

public class mathstest extends JFrame{

	static JFrame gameFrame;
	static JFrame rulesFrame;
	static JFrame winFrame;
	static JFrame pFixFrame;
	static JPanel activeWindow;
	static JPanel statDisp;
	static JPanel diceBox;
	static JPanel subRollReq;

	
	public static void main(String[] args) {
		getPlayers();
		
	}// end method main
	
	static class values{
		static ArrayList<String> playerNames = new ArrayList<String>();
		static ArrayList<Integer> playerPoints = new ArrayList<Integer>();
		static int total = 0;
		static int rowVals[] = new int[7];
		static int activePN = 0;
	}//end class values
	
	static class playerFns {
		
		
		public final static void changePlayer(JLabel dispTotal, JLabel[] localPNs) {
		//determine next active player
		do {
			if (values.activePN < values.playerNames.size() - 1) {
			values.activePN = values.activePN + 1;
			}
			else { 
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
		localPNs[values.activePN].setBorder(BorderFactory.createLineBorder(Color.green, 10));
		
		}// end changePlayer()
		
		public final static void checkWin() {
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
	}
	
	public static void getPlayers() {
		//instantiate components
		JFrame initFrame = new JFrame();
		JLabel welcome = new JLabel ("Welcome to the virtual dice game assistant!");
		JLabel playerNameReq = new JLabel("Enter Player Names: ");
		JTextField playerNameInput[] = new JTextField[8]; 
	    JButton submit = new JButton("Submit");
	    JButton rules = new JButton("How to play");
	     
	     //make functions
	    ActionListener submitAction = new ActionListener() {
	    	 public void actionPerformed(ActionEvent e) {
	    		 //add the player names from the playerNameInput text field array, stop when finds an empty field
	    		
	    		 for (int i = 0; i < playerNameInput.length; i++) {
	    			 if (!playerNameInput[i].getText().contentEquals(" ")) { 
	    				 values.playerNames.add(playerNameInput[i].getText());
	    				 values.playerPoints.add(30);
	    			 }
	    		 }//add player names from text field to array in values and give them starting points
	    		 
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
	     initFrame.setLayout(new GridLayout(11,1));
	     
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
	     
	     initFrame.setSize(1200,1600);
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
		rulesFrame.setSize(1200,1200);
		rulesFrame.show();
	}
	
	public static void setPlayScreen() {
		//Initialize frame contents	
		gameFrame = new JFrame("Virtual Dice Game Assisstant");
		activeWindow = new JPanel();
		activeWindow.setLayout(new CardLayout());
		diceBox = new JPanel();	
		statDisp = new JPanel();
		JLabel localPNs[] = new JLabel[values.playerNames.size()];
		JLabel localPPs[] = new JLabel[values.playerPoints.size()];
		activeWindow.setBorder(BorderFactory.createLineBorder(Color.black, 10));
		statDisp.setBorder(BorderFactory.createLineBorder(Color.black, 10));
		JButton dice[][] = new JButton[7][7];
		JButton next = new JButton("Next");
		JLabel dispTotal = new JLabel("Current dice total is: " + String.valueOf(values.total));	
		subRollReq = new JPanel();
		JLabel subRollReqTxt = new JLabel ("How many " + "did you roll?");
		String[] diceOpts = {"0", "1", "2", "3", "4", "5", "6"};
		JComboBox<String> diceMultiplyer = new JComboBox<String>(diceOpts);
		JButton submitDiceMult = new JButton("Submit");
		JButton changePoints = new JButton("Change points");
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
					dice[j][row].setBorder(BorderFactory.createLineBorder(Color.red, 10));
					}
					button.setBorder(BorderFactory.createLineBorder(Color.blue, 10));
						
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
			
			for (int j = 1; j <=6; j++) { //initialize dice buttons
				for (int i = 1; i <= 6; i++ ) {
					String iStr = Integer.toString(i);
					Icon d1 = new ImageIcon("res\\d" + iStr + ".PNG");
					dice[i][j] = new JButton(d1);
					dice[i][j].setName(Integer.toString(j));
					dice[i][j].setActionCommand(iStr);
					dice[i][j].addActionListener(addDice);
					diceBox.add(dice[i][j]);
					}
				}//end dice init for loop
			
			//populate player names and scores
			for (int i = 0; i < values.playerNames.size(); i++) {
				localPNs[i] = new JLabel(values.playerNames.get(i));
				localPPs[i] = new JLabel(Integer.toString(values.playerPoints.get(i)));
				statDisp.add(localPNs[i]);
				statDisp.add(localPPs[i]);
			}
			
			//Assemble components
			gameFrame.setSize(1200,1600);
			gameFrame.setLayout(new GridLayout(2,1));
			gameFrame.add(activeWindow);
			
			diceBox.setLayout(new GridLayout(6,6));
			diceBox.setSize(gameFrame.getWidth()/2, gameFrame.getHeight());
			//gameFrame.add(diceBox);
	
			submitDiceMult.addActionListener(subSub);
			subRollReq.setLayout(new GridLayout(1, 3));
			subRollReq.add(subRollReqTxt);
			subRollReq.add(diceMultiplyer);
			subRollReq.add(submitDiceMult);
			//gameFrame.add(subRollReq);
			
			next.addActionListener(clearDice);
			statDisp.add(next);
			statDisp.add(dispTotal);
			changePoints.addActionListener(changePointsAction);
			statDisp.add(changePoints);
			localPNs[values.activePN].setBorder(BorderFactory.createLineBorder(Color.green, 10));
			statDisp.setLayout(new GridLayout (values.playerNames.size() + 2 ,2));
			//statDisp.setSize(gameFrame.getWidth()/2, gameFrame.getHeight());	
			gameFrame.add(statDisp);
			
			
			
			//gameFrame.pack();
			gameFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
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
		winFrame.setSize(1200,1600);
		winFrame.setLayout(new GridLayout(3,1));
		
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
		pFixFrame.setLayout(new GridLayout(10, 2));
		JTextField pNames[] = new JTextField[values.playerNames.size()];
		JTextField pPoints[] = new JTextField[values.playerPoints.size()];
		JLabel pFixDia = new JLabel("Who's turn is it?");
		String[] playerOpts = new String[values.playerNames.size()];
		JComboBox<String> activeP;
		JButton pFixSubmit = new JButton("Continue");
		JLabel errAP0 = new JLabel("The active player cannot have 0 points");
		
		for(int i = 0; i < values.playerNames.size(); i++) {
			playerOpts[i] = values.playerNames.get(i);
			pNames[i] = new JTextField(values.playerNames.get(i));
			pFixFrame.add(pNames[i]);
			pPoints[i] = new JTextField (String.valueOf(values.playerPoints.get(i)));
			pFixFrame.add(pPoints[i]);
		}
		activeP = new JComboBox<String>(playerOpts);
		//make functions
		ActionListener pFixSubmitAction= new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//set active player
				String activePChoice = (String) activeP.getSelectedItem();
				values.activePN = values.playerNames.indexOf(activePChoice);
				for (int i = 0; i < pNames.length; i++) {
					//set pNames to values.playerNames
					values.playerNames.set(i, pNames[i].getText());
					//set pPoints to values.playerPoints
					values.playerPoints.set(i, Integer.parseInt(pPoints[i].getText()));
					}
					if (values.playerPoints.get(values.activePN) <= 0) {
						pFixFrame.add(errAP0);
						return;
					}
					//change frame
					gameFrame.setVisible(false);
					pFixFrame.setVisible(false);
					setPlayScreen();
			}
		};
		
		//Assemble components
		pFixFrame.add(pFixDia);
		pFixFrame.add(activeP);
		pFixSubmit.addActionListener(pFixSubmitAction);
		pFixFrame.add(pFixSubmit);
		
		pFixFrame.setSize(1200, 1600);
		pFixFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		pFixFrame.show();
	}// end setPointFix()

}//end class mathstest