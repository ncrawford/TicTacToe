package TicTacToe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.InvalidParameterException;

public class TicTacToe
{
	private int turn = 0; // current turn (will go up to max_turns)
	private int current_player = 0; // 0: x; 1: y
	private int x_moves = 0, y_moves = 0;  // stored as binary values
	
	/*
	 * Possible winning combinations
	 * These will serve as bit masks
	 * There are 8 combinations in total
	 * 
	 * 111  000  000  100  010  001  100  001
	 * 000  111  000  100  010  001  010  010
	 * 000  000  111  100  010  001  001  100
	 * 
	 * 448  56   7    292  146  73   273  84
	*/
	private final static int winning_moves[] = {448, 56, 7, 292, 146, 73, 273, 84}, num_winning_moves = 8;
	private final static int size = 9, max_turns = 9, min_win_turns = 5; // need at least 5 turns for a possible winner
	
	// returns X or Y depending on whose turn it currently is
	public char currentPlayer()
	{
		return current_player == 0 ? 'X' : 'Y';
	}
	
	// returns true or false depending if the given move is valid
	// to be valid, must be between 1 and 9 (inclusively) and not already played
	public boolean isValidMove(int move)
	{
		int compare = (int) Math.pow(2, move-1);
		return (move >= 1 && move <= size && (x_moves & compare) == 0 && (y_moves & compare) == 0) ? true : false;
	}
	
	// check for winner
	// returns 't' if tie, 'n' if no winner, or 0/1 for the binary value of the winner
	public char findWinner()
	{
		if (turn < min_win_turns)
			return 'n'; // no winner yet
		
		// loop through possible winning bit masks
		for (int i=0; i<num_winning_moves; ++i)
			if ( current_player == 0 && (x_moves & winning_moves[i]) == winning_moves[i] )
				return (char) current_player;
			else if ( current_player == 1 && (y_moves & winning_moves[i]) == winning_moves[i] )
				return (char) current_player;
		
		// if no winner was found, it is a tie if we've played max_turns, or not finished otherwise
		return turn == max_turns ? 't' : 'n';
	}
	
	// play the turn!
	// takes an integer value between 1 and 9
	// throws InvalidParameterException if invalid move is given
	// to see if move will be valid, call isValidMove()
	public void playTurn(int move)
	{
		if (!isValidMove(move))
			throw new InvalidParameterException();
		
		// the move represents a binary position (not a directy binary number)
		// we can obtain this through 2^(move-1)
		move = (int) Math.pow(2, move-1);
		
		// bitwise or operation to assign the move to a player
		if ( current_player == 0 )
			x_moves |= move;
		else
			y_moves |= move;
		
		++turn; // a turn has occured; increment
		
		char winner = findWinner(); // check for winner each time
		
		// if there is a winner, call the endGame function
		if ( winner != 'n' )
		{
			endGame(winner);
			return;
		}
		
		// no winner, set the current_player to the opposite player
		current_player ^= 1; // xor with 1; same as inverting
	}
	
	public void endGame(char winner)
	{
		if ( winner == 't' )
		{
			// tie
			System.out.println("Tie");
		}
		else
		{
			// current_player won
			if ( current_player == 0 )
				System.out.println("X won");
			else
				System.out.println("O won");
		}
	}
	
	public String toString()
	{
		String string = "";
		
		// the following two lines are for debugging purposes
		//string += "X moves: " + Integer.toBinaryString(x_moves) + "\n";
		//string += "Y moves: " + Integer.toBinaryString(y_moves) + "\n";
		
		for (int i=1; i<=size; ++i)
		{
			int compare = (int) Math.pow(2, i-1);
			if ( (x_moves & compare) == compare )
				string += "X";
			else if ( (y_moves & compare) == compare )
				string += "Y";
			else
				string += "-";
			
			if ( i % 3 == 0 )
				string += "\n";
		}
		
		return string;
	}
	
	public static void main (String[] args) throws IOException
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		TicTacToe game = new TicTacToe();
		char winner;
		do
		{
			int move;
			do
			{
				System.out.print("Player " + game.currentPlayer() + " move (1-9): ");
				String s_move = reader.readLine();
				move = Integer.parseInt(s_move);
			} while ( !game.isValidMove(move) );
			game.playTurn(move);
			System.out.println(game.toString());
		} while ( (winner = game.findWinner()) == 'n' );
		
		if ( winner == 't' )
			System.out.println("Tie game!");
		else
			System.out.println("Player " + game.currentPlayer() + " wins!");
		
		System.out.println(game.toString());
	}
}

