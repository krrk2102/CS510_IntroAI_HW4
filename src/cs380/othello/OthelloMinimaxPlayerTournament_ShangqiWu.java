package cs380.othello;

import java.util.List;

public final class OthelloMinimaxPlayerTournament_ShangqiWu extends OthelloPlayer {
	
	int time_duration;
	int player_number;
	
	public OthelloMinimaxPlayerTournament_ShangqiWu(int _time_duration, int _player_number) {
		if (_player_number == 0 || _player_number == 1) {
			player_number = _player_number;
		} else throw new IllegalArgumentException("Player number for OthelloMinimaxPlayerTournament constructor should be either 0 or 1.\n");
		if (_time_duration > 1) {
			time_duration = _time_duration;
		} else throw new IllegalArgumentException("Time boundary for OthelloMinimaxPlayerTournament constructor should be greater than 1 ms.\n");
	}
	
	/*
	 * Adding time boundary check for minimax algorithm.
	 * Running in a iterative deepening pattern, once time boundary exceeds, stop searching and return current utility value.
	 */
	public OthelloMove getMove(OthelloState state) {
		int depth_bound = 0;
		long end_time = System.currentTimeMillis() + time_duration;
		while (true) {
			depth_bound++;
			OthelloMove TournamentResult = MinimaxTournament(state, depth_bound, end_time);
			if (depth_bound == Integer.MAX_VALUE || end_time <= System.currentTimeMillis()) {
				return TournamentResult;
			}
		}
	}
	
	private OthelloMove MinimaxTournament(OthelloState _tour_state, int _depth_bound, long _time_bound) {
		int minimaxIndex = 0;
		Integer minimaxValue = new Integer(Integer.MIN_VALUE);
		List<OthelloMove> minimaxTourMove = _tour_state.generateMoves();
		if (!minimaxTourMove.isEmpty()) {
			for (int i = 0; i < minimaxTourMove.size(); i++) {
				if (System.currentTimeMillis() < _time_bound) {
					Integer FromMinTour = MinTour(_tour_state.applyMoveCloning(minimaxTourMove.get(i)), 0, _depth_bound, _time_bound);
					if (minimaxValue < FromMinTour) {
						minimaxValue = FromMinTour;
						minimaxIndex = i;
					}
				} else break;
			}
		} else return null;
		return minimaxTourMove.get(minimaxIndex);
	}
	
	private Integer MinTour(OthelloState _min_state, int _min_depth, int _min_depth_bound, long _min_time_bound) {
		_min_depth++;
		Integer minValue = new Integer(Integer.MAX_VALUE);
		if (_min_depth < _min_depth_bound && !_min_state.gameOver()) {
			List<OthelloMove> minMove = _min_state.generateMoves();
			for (int i = 0; i < minMove.size(); i++) {
				if (System.currentTimeMillis() < _min_time_bound) {
					Integer fromMaxValue = MaxTour(_min_state.applyMoveCloning(minMove.get(i)), _min_depth, _min_depth_bound, _min_time_bound);
					if (minValue > fromMaxValue) {
						minValue = fromMaxValue;
					}
				} else break;
			}
		} else {
			minValue = _min_state.score();
			if (player_number == 1) {
				minValue = -minValue;
			}
		}
		return minValue;
	}
	
	private Integer MaxTour(OthelloState _max_state, int _max_depth, int _max_depth_bound, long _max_time_bound) {
		_max_depth++;
		Integer maxValue = new Integer(Integer.MIN_VALUE);
		if (_max_depth < _max_depth_bound && !_max_state.gameOver()) {
			List<OthelloMove> maxMove = _max_state.generateMoves();
			for (int i = 0; i < maxMove.size(); i++) {
				if (System.currentTimeMillis() < _max_time_bound) {
					Integer fromMinValue = MinTour(_max_state.applyMoveCloning(maxMove.get(i)), _max_depth, _max_depth_bound, _max_time_bound);
					if (maxValue < fromMinValue) {
						maxValue = fromMinValue;
					}
				} else break;
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
