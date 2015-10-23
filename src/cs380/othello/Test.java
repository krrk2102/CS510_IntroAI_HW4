package cs380.othello;

import java.util.Scanner;

/**
 * CS 510 Introduction to Artificial Intelligence
 * Homework 4
 * Shangqi Wu
 * Implementation for minimax algorithm
 * Implementation for extra credit part 1 & 2: minimax algorithm with alpha-beta pruning, minimax algorithm tournament
 * 
 * original @author santi
 */
public class Test {
    
    
    public static void main(String args[]) {
    	
    	Scanner user_input = new Scanner(System.in);
        // Create the game state with the initial position for an 8x8 board:
        OthelloState state = setState(user_input);
        OthelloPlayer[] players = new OthelloPlayer[2];
        players = setPlayers(user_input);
        user_input.close();
        
        int counter = 0;
        do{
        	counter++;
            // Display the current state in the console:
        	System.out.println("\nStep number: " + counter);
            System.out.println("Current state, " + OthelloState.PLAYER_NAMES[state.nextPlayerToMove] + " to move:");
            System.out.print(state);
            
            // Get the move from the player:
            OthelloMove move = players[state.nextPlayerToMove].getMove(state);            
            System.out.println(move);
            state = state.applyMoveCloning(move);
        }while(!state.gameOver());

        // Show the result of the game:
        System.out.println("\nFinal state with score: " + state.score());
        System.out.println(state);
        System.out.println("Total step number is: " + counter);
        if (state.score() > 0) {
        	System.out.println("Player 1 wins.");
        } else if (state.score() == 0) {
        	System.out.println("2 Plyer tie.");
        } else {
        	System.out.println("Player 2 wins.");
        }
    }
    
    private static OthelloState setState(Scanner state_input) {
    	int board_size = 2;
    	while (board_size <= 2) {
    		System.out.println("Please enter a number as board size you preferred:\n(The number must be greater than 2, a size of 8 is recommended)");
    		board_size = state_input.nextInt();
    	}
    	OthelloState initialState = new OthelloState(board_size);
    	return initialState;
    }
    
    private static OthelloPlayer[] setPlayers(Scanner input_players) {
    	OthelloPlayer[] _players = new OthelloPlayer[2];
    	int player_type = -1;
        while (player_type < 1 || player_type > 4) {
        	System.out.println("Please select which player you expect Player 1 to be:\nPress 1 for Random Player\nPress 2 for Minimax Player\nPress 3 for Minimax Player with Alpha-Beta Pruning\nPress 4 for Minimax Player with Tournament");
        	player_type = input_players.nextInt();
        }
        _players[0] = setOnePlayer(input_players, player_type, 0);
        player_type = -1;
        while (player_type < 1 || player_type > 4) {
        	System.out.println("Please select which player you expect Player 2 to be:\nPress 1 for Random Player\nPress 2 for Minimax Player\nPress 3 for Minimax Player with Alpha-Beta Pruning\nPress 4 for Minimax Player with Tournament");
        	player_type = input_players.nextInt();
        }
        _players[1] = setOnePlayer(input_players, player_type, 1);
    	return _players;
    }
    
    private static OthelloPlayer setOnePlayer(Scanner input_player, int _player_type, int _player_number) {
    	switch (_player_type) {
    		case 1: {
    			return new OthelloRandomPlayer();
    		}
    		case 2: {
    			int depth = -1;
    			while (depth < 0) {
    				System.out.println("You have chosen Minimax Player, please enter a number as minimax algorithm search depth:\n(Please note depth must be a positive number, 5 is recommended)");
    				depth = input_player.nextInt();
    			}
    			return new OthelloMinimaxPlayer_ShangqiWu(depth, _player_number);
    		}
    		case 3: {
    			int depth = -1;
    			while (depth < 0) {
    				System.out.println("You have chosen Minimax Player with Pruning, please enter a number as minimax algorithm search depth:\n(Please note depth must be a positive number, 6 is recommended)");
    				depth = input_player.nextInt();
    			}
    			return new OthelloAlphaBetaPruningPlayer_ShangqiWu(depth, _player_number);
    		}
    		case 4: {
    			int time_bound = 0;
    			while (time_bound <= 0) {
    				System.out.println("You have chosen Minimax Player with Tournament, please enter a number as time boundary:\n(Please note time boundary must be a positive number, which stands for ms, 100 is fine)");
    				time_bound = input_player.nextInt();
    			}
    			return new OthelloMinimaxPlayerTournament_ShangqiWu(time_bound, _player_number);
    		}
    		default: {
    			return null;
    		}
    	}
    }
    
}
