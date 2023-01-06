package nim;
import java.util.Scanner;

/*
 * Nim Game - Main Class - Ian Korovinsky - A5
 * This is a simple number game which is played with three piles of counters 
 * The player can play either against the computer or against another player
 * Each turn, the player (or the computer) can take from any pile (as long as that pile still has counters in it). They can take an amount of counters ranging from one to the all of the counters in the chosen pile
 * The person who takes the last counter loses
 * DEFINITION: Nim Sum - the sum of all of the unpaired multiples of the three piles. It can help determine the amount of counters that a player should take (excluding special cases). If a player is in a losing position, the Nim Sum cannot provide any guidance.
 * How to find the Nim Sum: Each pile value is converted into binary (also known as powers of two) and the multiples are paired up. If a multiple does not have a pair from a different pile, its value is added to the Nim Sum.   
 */

public class Main {
	
	public static int pileACounters = 0;
	public static int pileBCounters = 0;
	public static int pileCCounters = 0;
	public static int totalPileCounters = 0;
	public static boolean playerOneTurn = true;
	public static String name1;
	public static String name2;
	public static String pile;
	public static Scanner console = new Scanner (System.in);
	public static int amountRemoved = 0;
	public static int turns = 0;
	public static boolean computerPlay = false;
	
	//Starts the game
	public static void main(String[] args) {
		beginning();
	}
	
	//Introduces the user to the game. Allows the user(s) to select their name(s) and the game mode that they would like to play
	public static void beginning() {
		Scanner modeSelector = new Scanner (System.in);
		System.out.println("Hello! Welcome to the Nim Game! How would you like to play? \n\nA: against a computer \nB: against another player");
		String mode = modeSelector.next();
		Scanner name = new Scanner (System.in);
		//If option A is chosen, the necessary text is printed, one name is asked for, and the player versus computer gamemode is started
		if (mode.equals("A")) {
			System.out.println("Starting game against computer...\n");
			System.out.print("Player, what is your name: ");
			name1 = name.nextLine();
			computerPlay = true;
			choosePileValues();
			playerVsComputerMove();
		}
		//If option B is chosen, necessary text is printed, two names are asked for, and the player versus player gamemode is started
		else if (mode.equals("B")) {
			System.out.println("Starting game against another player...\n");
			System.out.print("Player 1, enter your name: ");
			name1 = name.nextLine();
			System.out.print("Player 2, enter your name: ");
			name2 = name.nextLine();
			choosePileValues();
			System.out.println("\n" + name1 + " will make the first move.");
			playerVsPlayer();
		}
		//If none of the options are chosen, the user is asked again
		else {
			System.out.println("You picked none of the options! Please try again!");
			beginning();
		}
	}
	
	/* 
	 * Used when two players are playing against each other
	 * Allows the player whose turn it is to pick a pile, while ensuring that the pile which is picked still has counters in it
	 * If there are no counters left, it triggers the win message
	 */
	public static void playerVsPlayer() {
		//Illustrates the remaining counters in the console
		System.out.println("\nA: " + drawCounters(pileACounters) + "\nB: " + drawCounters(pileBCounters) + "\nC: " + drawCounters(pileCCounters) + "\n");
		turnCalculator();
		//If there are counters left, it triggers pile choosing for the necessary player (depending on whose turn it is)
		if (totalPileCounters > 0) {
			//Allows the player to select a pile that is not empty
			if (playerOneTurn) {
				System.out.print(name1 + ", choose a pile: ");
			}
			else {
				System.out.print(name2 + ", choose a pile: ");
			}
			pile = console.next();
			if (pile.equals("A")) {
				if (pileACounters > 0) {
					pileAChoose();
				}
				else {
					System.out.println("Nice try, but pile A is empty. Please try again.");
					playerVsPlayer();
				}
			}
			else if (pile.equals("B")) {
				if (pileBCounters > 0) {
					pileBChoose();
				}
				else {
					System.out.println("Nice try, but pile B is empty. Please try again.");
					playerVsPlayer();
				}
			}
			else if (pile.equals("C")) {
				if (pileCCounters > 0) {
					pileCChoose();
				}
				else {
					System.out.println("Nice try, but pile C is empty. Please try again.");
					playerVsPlayer();
				}
			}
			//If none of the piles are chosen, the user is asked again
			else {
				System.out.println("That was not one of the options. Please try again.");
				playerVsPlayer();
			}
		}
		//Triggers the game-end message if there are no counters left
		else {
			endingPlayer();
		}
	}
	
	//Allows the user to determine whether they will go first or whether the computer will go first
	public static void playerVsComputerMove() {
		Scanner input = new Scanner (System.in);
		System.out.println("\nWould you like to go first, or would you like the computer to go first? \n\nA: you go first \nB: computer goes first");
		String turn = input.nextLine();
		//If option A is chosen then player versus computer gameplay is started.
		if (turn.equals("A")) {
			playerVsComputer();
		}
		//If option B is chosen then player versus computer gameplay is started, except the amount of turns that have occurred is set back to negative one to allow for the computer to be able to go first
		else if (turn.equals("B")) {
			turns = -1;
			playerVsComputer();
		}
		//If none of the options are chosen then the user is asked again
		else {
			System.out.println("You have not chosen one of the available options. Please try again.");
			playerVsComputerMove();
		}	
	}
	
	/*
	 * Used for player against computer play
	 * Allows the user to choose a pile (if the pile still has counters left in it)
	 * Triggers the move of the computer if it is the turn of the computer
	 * Triggers the victory/loss message if there are no counters left
	 */
	public static void playerVsComputer() {
		//Illustrates the remaining counters
		System.out.println("\nA: " + drawCounters(pileACounters) + "\nB: " + drawCounters(pileBCounters) + "\nC: " + drawCounters(pileCCounters) + "\n");
		turnCalculator();
		//Continues gameplay if there are counters left
		if (totalPileCounters > 0) {
			//Triggers the turn of the player if it is their turn
			if (playerOneTurn) {
				//Allows for the player to choose a pile, while ensuring that the pile that they choose is not empty
				//If the pile that they choose is empty or if they do not choose any of the available options, they are asked again
				System.out.print(name1 + ", choose a pile: ");
				pile = console.next();
				if (pile.equals("A")) {
					if (pileACounters > 0) {
						pileAChoose();
					}
					else {
						System.out.println("Nice try, but pile A is empty. Please try again.");
						playerVsComputer();
					}
				}
				else if (pile.equals("B")) {
					if (pileBCounters > 0) {
						pileBChoose();
					}
					else {
						System.out.println("Nice try, but pile B is empty. Please try again.");
						playerVsComputer();
					}
				}
				else if (pile.equals("C")) {
					if (pileCCounters > 0) {
						pileCChoose();
					}
					else {
						System.out.println("Nice try, but pile C is empty. Please try again.");
						playerVsComputer();
					}
				}
				else {
					System.out.println("That was not one of the options. Please try again.");
					playerVsComputer();
				}
			}
			//Triggers the move of the computer if it is its turn
			else {
				computerMove();
			}
		}
		//Triggers game-end message if there are no counters left
		else {
			endingComputer();
		}
	}
	
	/*
	* Allows the user to choose how many counters they would like to remove from pile A (as long as the move is according to the game rules)
	* If the move is not according to the game rules, it prevents the move
	*/
	public static void pileAChoose() {
		System.out.print("How many to remove from pile A: ");
		amountRemoved = console.nextInt();
		//Makes sure that the user does not remove too many or too little counters
		//If the player does not make a move that follows the rules, the user is asked again
		if (amountRemoved <= 0) {
			System.out.println("You must choose at least 1. Please try again.");
			pileAChoose();
		}
		else if (amountRemoved > pileACounters) {
			System.out.println("Pile A doesn't have that many. Please try again.");
			pileAChoose();
		}
		//If the player does make a move that follows the rules, the removal of the counters is triggered
		else {
			System.out.println("You take " + amountRemoved + " from pile A.");
			pileARemove();
		}
	}
	
	/*
	* Removes the selected number of counters from pile A
	* Updates the number of counters left (in total)
	* Updates the number of turns that have passed
	*/
	public static void pileARemove() {
		//The amount of turns that has passed is increased by one
		turns++;
		//The amount of counters that was selected to be removed is removed from the pile and from the total amount of counters remaining
		pileACounters -= amountRemoved;
		totalPileCounters -= amountRemoved;
		//Triggers necessary gamemode depending on whether or not the user initially chose to play against the computer or against another player
		if (computerPlay) {
			playerVsComputer();
		}
		else {
			playerVsPlayer();
		}
	}
	
	/*
	* Allows the user to choose how many counters they would like to remove from pile B (as long as the move is according to the game rules)
	* If the move is not according to the game rules, it prevents the move
	*/
	public static void pileBChoose() {
		System.out.print("How many to remove from pile B: ");
		amountRemoved = console.nextInt();
		if (amountRemoved <= 0) {
			System.out.println("You must choose at least 1. Please try again.");
			pileBChoose();
		}
		else if (amountRemoved > pileBCounters) {
			System.out.println("Pile B doesn't have that many. Please try again.");
			pileBChoose();
		}
		else {
			System.out.println("You take " + amountRemoved + " from pile B.");
			pileBRemove();
		}
	}
	
	/*
	* Removes the selected number of counters from pile B
	* Updates the number of counters left (in total)
	* Updates the number of turns that have passed
	*/
	public static void pileBRemove() {
		turns++;
		pileBCounters -= amountRemoved;
		totalPileCounters -= amountRemoved;
		if (computerPlay) {
			playerVsComputer();
		}
		else {
			playerVsPlayer();
		}
	}
	
	/*
	* Allows the user to choose how many counters they would like to remove from pile C (as long as the move is according to the game rules)
	* If the move is not according to the game rules, it prevents the move
	*/
	public static void pileCChoose() {
		System.out.print("How many to remove from pile C: ");
		amountRemoved = console.nextInt();
		if (amountRemoved <= 0) {
			System.out.println("You must choose at least 1. Please try again.");
			pileCChoose();
		}
		else if (amountRemoved > pileCCounters) {
			System.out.println("Pile C doesn't have that many. Please try again.");
			pileCChoose();
		}
		else {
			System.out.println("You take " + amountRemoved + " from pile C.");
			pileCRemove();
		}
	}
	
	/*
	* Removes the selected number of counters from pile C
	* Updates the number of counters left (in total)
	* Updates the number of turns that have passed
	*/
	public static void pileCRemove() {
		pileCCounters -= amountRemoved;
		totalPileCounters -= amountRemoved;
		turns++;
		if (computerPlay) {
			playerVsComputer();
		}
		else {
			playerVsPlayer();
		}
	}
	
	/*
	 * Checks whether if it possible for the computer to make a move according to one of the special cases. 
	 * If so, it triggers that move.
	 * If not, it triggers a second method which continues to evaluate what the best move for the computer is 
	 */
	public static void computerMove() {
		//Checks if the computer can make a move that falls under special case one
		if ((pileACounters == 0 && pileBCounters == 1 && pileCCounters != 0) || (pileACounters == 0 && pileCCounters == 1 && pileBCounters != 0) || (pileBCounters == 0 && pileACounters == 1 && pileCCounters != 0) || (pileBCounters == 0 && pileCCounters == 1 && pileACounters != 0) || (pileCCounters == 0 && pileACounters == 1 && pileBCounters != 0) ||  (pileCCounters == 0 && pileBCounters == 1 && pileACounters != 0)) {
			twoPilesOnePieceLeft();
		}
		//Checks if the computer can make a move that falls under special case two
		else if ((pileACounters + pileBCounters == 0 && pileCCounters > 1)|| (pileACounters + pileCCounters == 0 && pileBCounters > 1) || (pileBCounters + pileCCounters == 0 && pileACounters > 1)) {
			onePileLeft();
		}
		//Checks if the computer can make a move that falls under special case three
		else if ((pileACounters == 1 && pileBCounters == 1 && pileCCounters > 1) || (pileACounters == 1 && pileBCounters > 1 && pileCCounters == 1) || (pileACounters > 1 && pileBCounters == 1 && pileCCounters == 1)) {
			twoOnesThreePiles();
		}
		//Triggers the evaluation of a computer move that does not fall under any of the special cases
		else {
			computerNormalMove();
		}
	}
	
	
	//Determines the computer's move for special case one: when there are two piles left and at least one of them has one piece left
	public static void twoPilesOnePieceLeft() {
		//Checks which two piles have counters in them, and which one of the piles can potentially have more than one counter in it
		//Triggers the removal of counters from the necessary pile
		if (pileBCounters == 1 && pileCCounters != 0) {
			amountRemoved = pileCCounters;
			System.out.println("Computer takes " + amountRemoved + " from pile C.");
			pileCRemove();
		}
		else if (pileCCounters == 1 && pileBCounters != 0){
			amountRemoved = pileBCounters;
			System.out.println("Computer takes " + amountRemoved + " from pile B.");
			pileBRemove();
		}
		else if (pileACounters == 1 && pileCCounters != 0){
			amountRemoved = pileCCounters;
			System.out.println("Computer takes " + amountRemoved + " from pile C.");
			pileCRemove();
		}
		else if (pileCCounters == 1 && pileACounters != 0){
			amountRemoved = pileACounters;
			System.out.println("Computer takes " + amountRemoved + " from pile A.");
			pileARemove();
		}
		else if (pileACounters == 1 && pileBCounters != 0){
			amountRemoved = pileBCounters;
			System.out.println("Computer takes " + amountRemoved + " from pile B.");
			pileBRemove();
		}
		else {
			amountRemoved = pileACounters;
			System.out.println("Computer takes " + amountRemoved + " from pile A.");
			pileARemove();
		}
	}
	
	//Determines the computer's move for special case two: when there are two piles, two of them have one counter left, and one of them has more than one counter left
	public static void twoOnesThreePiles() {
		//Checks which pile has more than one counter in it
		//Triggers the removal of counters from the necessary pile
		if (pileACounters > 1) {
			amountRemoved = pileACounters-1;
			System.out.println("Computer takes " + amountRemoved + " from pile A.");
			pileARemove();
		}
		else if (pileBCounters > 1) {
			amountRemoved = pileBCounters-1;
			System.out.println("Computer takes " + amountRemoved + " from pile B.");
			pileBRemove();
		}
		else {
			amountRemoved = pileCCounters-1;
			System.out.println("Computer takes " + amountRemoved + " from pile C.");
			pileCRemove();
		}
	}
	
	
	//Determines the computer's move for special case three: when there is one pile left and it has more than one counter left in it
	public static void onePileLeft() {
		//Checks which pile has counters left in it (and the pile has to have more than one counter left in it)
		//Triggers the removal of counters from the necessary pile
		if (pileACounters > 1) {
			amountRemoved = pileACounters-1;
			System.out.println("Computer takes " + amountRemoved + " from pile A.");
			pileARemove();
		}
		else if (pileBCounters > 1) {
			amountRemoved = pileBCounters-1;
			System.out.println("Computer takes " + amountRemoved + " from pile B.");
			pileBRemove();
		}
		else {
			amountRemoved = pileCCounters-1;
			System.out.println("Computer takes " + amountRemoved + " from pile C.");
			pileCRemove();
		}
	}
	
	/*
	 * Calculates a move for the computer that will make the Nim Sum zero (if possible)
	 * If that is not possible, it will trigger another method which will calculate a stalling move for the computer
	 */
	public static void computerNormalMove() {
		int maybeTaken = 0;
		boolean takeFromA = false;
		boolean takeFromB = false;
		amountRemoved = 0;
		//Runs through each possible move for each pile and determines which moves are ideal (moves that will make the Nim Sum equal to zero), and which moves are not
		//If a move that makes the Nim Sum is discovered, it replaces the previous potential move
		for (int i = 1; i <= pileACounters; i++) {
			maybeTaken = i;
			//The XOR operator is used to check if the removal of a certain amount of counters from a set pile will make the Nim Sum equal to zero
			if ((pileACounters-maybeTaken ^ pileBCounters ^ pileCCounters) == 0) {
				//Indicates that the move should be made on pile A
				takeFromA = true;
				amountRemoved = maybeTaken;
			}
		}
		for (int i = 1; i <= pileBCounters; i++) {
			maybeTaken = i;
			if ((pileACounters ^ pileBCounters-maybeTaken ^ pileCCounters) == 0) {
				//Indicates that the move should be made on pile B and not pile A
				takeFromA = false;
				takeFromB = true;
				amountRemoved = maybeTaken;
			}
		}
		for (int i = 1; i <= pileCCounters; i++) {
			maybeTaken = i;
			if ((pileACounters ^ pileBCounters ^ pileCCounters-maybeTaken) == 0) {
				//Indicates that the move should be made on pile C and not on pile B or on pile A
				takeFromA = false;
				takeFromB = false;
				amountRemoved = maybeTaken;
			}
		}
		//If a move that makes the Nim Sum equal to zero is found, this triggers its execution 
		if (amountRemoved > 0) {
			if (takeFromA) {
				System.out.println ("Computer takes " + amountRemoved + " from pile A.");
				pileARemove();
			}
			else if (takeFromB) {
				System.out.println ("Computer takes " + amountRemoved + " from pile B.");
				pileBRemove();
			}
			else {
				System.out.println ("Computer takes " + amountRemoved + " from pile C.");
				pileCRemove();
			}
		}
		//If no move that makes the Nim Sum equal to zero is found, this triggers the evaluation of a stalling move
		else {
			noOptimalMovePossible();
		}
	}
	

	/*
	* Calculates a stalling move for the computer
	* This is done when no other option is possible
	*/
	public static void noOptimalMovePossible() {
		//Triggers the removal of one counter from either pile A or pile B (pile chosen depends on which piles it is possible to remove a counter from)
		if (pileACounters != 0) {
			amountRemoved = 1;
			System.out.println ("Computer takes 1 from pile A.");
			pileARemove();
		}
		else {
			amountRemoved = 1;
			System.out.println ("Computer takes 1 from pile B.");
			pileBRemove();
		}
	}
	
	//Allows the user to choose how many counters there will be in each pile
	public static void choosePileValues() {
		System.out.print("\nHow many counters would you like there to be in pile A: ");
		pileACounters = console.nextInt();
		System.out.print("How many counters would you like there to be in pile B: ");
		pileBCounters = console.nextInt();
		System.out.print("How many counters would you like there to be in pile C: ");
		pileCCounters = console.nextInt();
		//Calculates how many counters there are in total
		totalPileCounters = pileACounters + pileBCounters + pileCCounters;
	}
	
	//Determines who should go depending on how many turns have passed
	public static void turnCalculator() {
		//If an even number of turns has passed, it sets that the next turn should be given to the first player
		if (turns %2 == 0) {
			playerOneTurn = true;
		}
		//If an odd number of turns has passed, it sets that the next turn should be given to the computer or to the second player
		else {
			playerOneTurn = false;
		}
	}
	
	//Illustrates the number of counters remaining for a chosen pile using asterisks (in the console, while the game is ongoing)
	public static String drawCounters(int counters) {
		String pieces = ("");
		//For each counter, an asterisk is added to a string
		for (int i = 0; i < counters; i++) {
			pieces = pieces + "*";
		}
		//The string which contains all of the asterisks for a chosen pile is returned
		return pieces;
	}
	
	//Displays the victory message for the player against player gamemode
	public static void endingPlayer() {
		//Prints the victory message depending on whose turn it should have been after the last counter was taken
		if (playerOneTurn) {
			System.out.println (name1 + ", there are no counters left, so you WIN!");
		}
		else {
			System.out.println (name2 + ", there are no counters left, so you WIN!");
		}
	}
	
	//Displays the victory message for the player against computer gamemode
	public static void endingComputer() {
		//Prints the victory message depending on whether it should have been the player's turn or the computer's turn after the last counter was taken
		if (playerOneTurn) {
			System.out.println (name1 + ", there are no counters left, so you WIN!");
		}
		else {
			System.out.println (name1 + ", you took the last counter, so you LOSE!");
		
		}
	}
}