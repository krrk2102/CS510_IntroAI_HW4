package cs380.othello;

import java.util.List;

public class OthelloAlphaBetaPruningPlayer_ShangqiWu extends OthelloPlayer {
	
	int depth_bound;
	int player_number;
	
	public OthelloAlphaBetaPruningPlayer_ShangqiWu(int _depth_bound, int _player_number) {
		if (_depth_bound > 0) {
			depth_bound = _depth_bound;
		} else throw new IllegalArgumentException("Depth boundary for OthelloMinimaxPruningPlayer constructor should be a positive integer.\n");
		if (_player_number == 0 || _player_number == 1) {
			player_number = _player_number;
		} else throw new IllegalArgumentException("Player number for OthelloMinimaxPruningPlayer constructor should be either 0 or 1.\n");
	}
	
	/*
	 * Almost the same with normal minimax, alpha-beta is inserted. 
	 */
	public OthelloMove getMove(OthelloState state) {
		long StartTime = System.currentTimeMillis();
		OthelloMove minimaxPruning = MinimaxPruning(state);
		long EndTime = System.currentTimeMillis();
		System.out.println("Time duration for this step of a OthelloMinimaxPruningPlayer with a depth boundary " + depth_bound + " is: " + (EndTime-StartTime) + " ms.");
		return minimaxPruning;
	}
	
	/*
	 * This entry does the same job as normal minimax
	 */
	private OthelloMove MinimaxPruning(OthelloState _pruning_state) {
		int bestMinimaxIndex = 0;
		Integer bestMinimaxValue = new Integer(Integer.MIN_VALUE);
		List<OthelloMove> minimaxPruningMove = _pruning_state.generateMoves();
		if (!minimaxPruningMove.isEmpty()) {
			for (int i = 0; i < minimaxPruningMove.size(); i++) {
				Integer FromMin = MinPruning(_pruning_state.applyMoveCloning(minimaxPruningMove.get(i)), Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
				if (bestMinimaxValue < FromMin) {
					bestMinimaxValue = FromMin;
					bestMinimaxIndex = i;
				}
			}
		} else return null;
		return minimaxPruningMove.get(bestMinimaxIndex);
	}

	/*
	 * Followings are as max-Value and min-Value functions
	 */
	private Integer MaxPruning(OthelloState _max_state, Integer _max_alpha, Integer _max_beta, int _max_depth) {
		_max_depth++;
		Integer MaxValuePruning = new Integer(Integer.MIN_VALUE);
		if (_max_depth < depth_bound && !_max_state.gameOver()) {
			List<OthelloMove> MaxPruningMove = _max_state.generateMoves();
			// For each in successor states, v = MAX(v, min-Value(state, alpha, beta))
			for (int i = 0; i < MaxPruningMove.size(); i++) {
				Integer FromMinPruning = MinPruning(_max_state.applyMoveCloning(MaxPruningMove.get(i)), _max_alpha, _max_beta, _max_depth);
				if (FromMinPruning > MaxValuePruning) {
					MaxValuePruning = FromMinPruning;
				}
				// If v >= beta, return v
				if (FromMinPruning >= _max_beta) {
					return MaxValuePruning;
				}
				// alpha = MAX(alpha, v)
				if (FromMinPruning > _max_alpha) {
					_max_alpha = FromMinPruning;
				}
			}
		} else {
			// If terminal-test(state), return utility value
			MaxValuePruning = _max_state.score();
			if (player_number == 1) {
				MaxValuePruning = -MaxValuePruning;
			}
		}
		return MaxValuePruning;
	}
	
	private Integer MinPruning(OthelloState _min_state, Integer _min_alpha, Integer _min_beta, int _min_depth) {
		_min_depth++;
		Integer MinValuePruning = new Integer(Integer.MAX_VALUE);
		if (_min_depth < depth_bound && !_min_state.gameOver()) {
			List<OthelloMove> MinPruningMove = _min_state.generateMoves();
			for (int i = 0; i < MinPruningMove.size(); i++) {
				Integer FromMaxPruning = MaxPruning(_min_state.applyMoveCloning(MinPruningMove.get(i)), _min_alpha, _min_beta, _min_depth);
				if (FromMaxPruning < MinValuePruning) {
					MinValuePruning = FromMaxPruning;
				}
				if (FromMaxPruning <= _min_alpha) {
					return MinValuePruning;
				}
				if (FromMaxPruning < _min_beta) {
					_min_beta = FromMaxPruning;
				}
			}
		} else {
			MinValuePruning = _min_state.score();
			if (player_number == 1) {
				MinValuePruning = -MinValuePruning;
			}
		}
		return MinValuePruning;
	}

}
