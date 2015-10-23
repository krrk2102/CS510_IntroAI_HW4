package cs380.othello;

import java.util.List;

public class OthelloMinimaxPlayer_ShangqiWu extends OthelloPlayer {
	
	int depth_bound;
	int player_number;
	
	public OthelloMinimaxPlayer_ShangqiWu(int _depth_bound, int _player_number) {
		if (_depth_bound > 0) {
			depth_bound = _depth_bound;
		} else throw new IllegalArgumentException("Depth boundary for OthelloMinimaxPlayer constructor should be a positive integer.\n");
		if (_player_number == 0 || _player_number == 1) {
			player_number = _player_number;
		} else throw new IllegalArgumentException("Player number for OthelloMinimaxPlayer constructor should be either 0 or 1.\n");
	}
	
	/*
	 * Inherited method, return search result of minimax algorithm under the required depth boundary. Also displays running time of the search.
	 */
	public OthelloMove getMove(OthelloState state) {
		long StartTime = System.currentTimeMillis();
		OthelloMove minimax = minimaxMove(state);
		long EndTime = System.currentTimeMillis();
		System.out.println("Time duration for this step of a OthelloMinimaxPlayer with depth boundary " + depth_bound + " is: " + (EndTime-StartTime) +" ms.");
		return  minimax;
	}
	
	/*
	 * Entry of minimax search, selecting max value from min-Value
	 */
	public OthelloMove minimaxMove(OthelloState _state) {
		int minimaxIndex = 0;
		Integer minimaxValue = new Integer(Integer.MIN_VALUE);
		List<OthelloMove> minimaxMove = _state.generateMoves();
		if (!minimaxMove.isEmpty()) {
			for (int i = 0; i < minimaxMove.size(); i++) {
				Integer FromMin = MinValue(_state.applyMoveCloning(minimaxMove.get(i)), 0);
				if (minimaxValue < FromMin) {
					minimaxValue = FromMin;
					minimaxIndex = i;
				}
			}
		} else return null;
		return minimaxMove.get(minimaxIndex);
	}
	
	/*
	 * Following are min-Value & max-Value functions. Terminal state is reaching the depth boundary or game over. 
	 */
	private Integer MinValue(OthelloState _min_state, int _min_depth) {
		_min_depth++;
		Integer minValue = new Integer(Integer.MAX_VALUE);
		if (_min_depth < depth_bound && !_min_state.gameOver()) {
			// For each action in successors states, v = MIN(v, max-Value(state)
			List<OthelloMove> minMove = _min_state.generateMoves();
			for (int i = 0; i < minMove.size(); i++) {
				Integer fromMaxValue = MaxValue(_min_state.applyMoveCloning(minMove.get(i)), _min_depth);
				if (minValue > fromMaxValue) {
					minValue = fromMaxValue;
				}
			}
		} else {
			// If terminal-test(state), return utility value.
			minValue = _min_state.score();
			if (player_number == 1) {
				minValue = -minValue;
			}
		}
		return minValue;
	}
	
	private Integer MaxValue(OthelloState _max_state, int _max_depth) {
		_max_depth++;
		Integer maxValue = new Integer(Integer.MIN_VALUE);
		if (_max_depth < depth_bound && !_max_state.gameOver()) {
			List<OthelloMove> maxMove = _max_state.generateMoves();
			for (int i = 0; i < maxMove.size(); i++) {
				Integer fromMinValue = MinValue(_max_state.applyMoveCloning(maxMove.get(i)), _max_depth);
				if (maxValue < fromMinValue) {
					maxValue = fromMinValue;
				}
			}
		} else {
			maxValue = _max_state.score();
			if (player_number == 1) {
				maxValue = -maxValue;
			}
		}
		return maxValue;
	}
}
