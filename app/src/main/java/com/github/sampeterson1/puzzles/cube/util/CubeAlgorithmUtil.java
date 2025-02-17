/*
 *	Stickers Twisty Puzzle Simulator and Solver
 *	Copyright (C) 2022 Sam Peterson <sam.peterson1@icloud.com>
 *	
 *	This program is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.
 *	
 *	This program is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *	GNU General Public License for more details.
 *	
 *	You should have received a copy of the GNU General Public License
 *	along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.github.sampeterson1.puzzles.cube.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.sampeterson1.math.Mathf;
import com.github.sampeterson1.puzzle.lib.PuzzleType;
import com.github.sampeterson1.puzzle.moves.Algorithm;
import com.github.sampeterson1.puzzle.moves.Axis;
import com.github.sampeterson1.puzzle.moves.InvalidAlgorithmException;
import com.github.sampeterson1.puzzle.moves.Move;
import com.github.sampeterson1.puzzle.moves.UniversalAlgorithmParser;

//Utilities relating to a Rubik's Cube algorithm
public class CubeAlgorithmUtil {
	
	private static Axis[] axes = {Axis.R, Axis.U, Axis.F, Axis.L, Axis.D, Axis.B};

	private static Map<String, String> moveReplacements = initMoveReplacements();
	
	private static Map<String, String> initMoveReplacements() {
		Map<String, String> moveReplacements = new HashMap<String, String>();
		
		moveReplacements.put("r", "L ~R");	
		moveReplacements.put("u", "D ~U");
		moveReplacements.put("f", "B ~F");	
		moveReplacements.put("l", "R ~L");	
		moveReplacements.put("d", "U ~D");	
		moveReplacements.put("b", "F ~B");	
		moveReplacements.put("x", "~R");
		moveReplacements.put("y", "~U");
		moveReplacements.put("z", "~F");
		moveReplacements.put("M", "R L' ~R'");
		
		return moveReplacements;
	}

	public static Algorithm parseAlgorithm(String str) throws InvalidAlgorithmException {
		return UniversalAlgorithmParser.parseAlgorithm(str, moveReplacements, 0, PuzzleType.CUBE);
	}
	
	public static Algorithm parseAlgorithm(String str, int puzzleSize) throws InvalidAlgorithmException {
		return UniversalAlgorithmParser.parseAlgorithm(str.toString(), moveReplacements, puzzleSize, PuzzleType.CUBE);
	}
	
	private static List<Move> mergeInversePairs(List<Move> moves) {
		if (moves.size() == 0) {
			return moves;
		}

		List<Move> newMoves = new ArrayList<Move>();
		
		newMoves.add(moves.get(0));
		int mergeIndex = -1;
		
		for (int i = 1; i < moves.size(); i ++) {
			Move move = moves.get(i);
			Move previous = moves.get(i - 1);
			
			if (i - mergeIndex >= 2 && move.getInverse().equals(previous)) {
				newMoves.remove(newMoves.size() - 1);
				mergeIndex = i;
			} else {
				newMoves.add(move);
				previous = move;
			}
		}
		
		return newMoves;
	}
	
	private static List<Move> mergeRepeatedMoves(List<Move> moves) {
		if (moves.size() == 0 || moves.size() == 1) {
			return moves;
		}

		List<Move> newMoves = new ArrayList<Move>();
		
		newMoves.add(moves.get(0));
		newMoves.add(moves.get(1));
		int mergeIndex = -1;
		
		for (int i = 2; i < moves.size(); i ++) {
			Move move = moves.get(i);
			Move prev1 = moves.get(i - 1);
			Move prev2 = moves.get(i - 2);
			
			if (i - mergeIndex >= 3 && move.equals(prev1) && prev1.equals(prev2)) {
				newMoves.remove(newMoves.size() - 1);				
				newMoves.remove(newMoves.size() - 1);
				newMoves.add(move.getInverse());
				mergeIndex = i;
			} else {
				newMoves.add(move);
			}
		}
		
		return newMoves;
	}
	
	public static Algorithm simplify(Algorithm alg) {
		int prevNumMoves = Integer.MAX_VALUE;
		List<Move> moves = alg.getMoves();
		
		while (moves.size() < prevNumMoves) {
			prevNumMoves = moves.size();
			moves = mergeRepeatedMoves(moves);
			moves = mergeInversePairs(moves);
		}
		
		return new Algorithm(moves);
	}
	
	private static Move getRandomMove(int puzzleSize) {
		int i = (int) Mathf.random(0, axes.length);
		Axis f = axes[i];

		int layer = (int) Mathf.random(0, puzzleSize);
		boolean cw = (Mathf.random(0, 1) < 0.5);

		return new Move(f, layer, cw);
	}
	
	public static Algorithm generateScramble(int length, int puzzleSize) {
		Algorithm scramble = new Algorithm();
		for(int i = 0; i < length; i ++) {
			scramble.addMove(getRandomMove(puzzleSize));
		}
		
		return scramble;
	}
	
}
