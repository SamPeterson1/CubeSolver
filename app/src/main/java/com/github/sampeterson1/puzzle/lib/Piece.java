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

package com.github.sampeterson1.puzzle.lib;

import com.github.sampeterson1.puzzle.templates.GroupedPuzzle;
import com.github.sampeterson1.puzzle.templates.Puzzle;

public class Piece {
	
	private Puzzle puzzle;
	private int index;
	private int position;
	
	private boolean solved;
	
	private Color[] colors;
	private PieceType type;
	
	public Piece(Puzzle puzzle, PieceType type, int position) {
		this.type = type;
		this.puzzle = puzzle;
		this.colors = new Color[type.getNumColors()];
		this.position = position;
	}
	
	public Piece(Puzzle puzzle, PieceType type, int position, int index) {
		this.type = type;
		this.position = position;
		this.index = index;
		this.puzzle = puzzle;
		this.colors = new Color[type.getNumColors()];
	}
	
	public int getPuzzleSize() {
		if(this.puzzle instanceof GroupedPuzzle)
			return ((GroupedPuzzle) puzzle).getSize();
		
		return 0;
	}
	
	public Puzzle getPuzzle() {
		return this.puzzle;
	}
	
	public boolean isSolved() {
		return this.solved;
	}
	
	public PieceType getType() {
		return this.type;
	}
	
	public int indexOfColor(Color c) {
		for(int i = 0; i < colors.length; i ++) {
			if(c == colors[i]) return i;
 		}
		return -1;
	}
	
	public int getIndex() {
		return this.index;
	}
	
	public int getPosition() {
		return this.position;
	}
	
	public Color getColor() {
		return this.colors[0];
	}
	
	public Color getColor(int index) {
		return this.colors[index];
	}

	public void setSolved(boolean solved) {
		this.solved = solved;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
	
	public void setPosition(int position) {
		this.position = position;
	}
	
	public void setColor(int index, Color color) {
		this.colors[index] = color;
	}
	
	public void setColor(Color color) {
		this.colors[0] = color;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("{" + colors[0]);
		for(int i = 1; i < colors.length; i ++) {
			sb.append(", " + colors[i]);
		}
		sb.append("}");
		
		return sb.toString();
	}
	
	@Override
	public Piece clone() {
		Piece piece = new Piece(puzzle, type, position, index);
		for(int i = 0; i < colors.length; i ++) {
			piece.setColor(i, colors[i]);
		}
		
		return piece;
	}
	
	public boolean hasExactColors(Piece other) {
		for(int i = 0; i < colors.length; i ++) {
			if(colors[i] != other.colors[i]) return false;
		}
		
		return true;
	}
	
	public boolean hasEquivalentColors(Piece other) {
		return hasColors(other.colors);
	}
	
	public boolean hasColor(Color color) {
		for(Color c : this.colors) {
			if(color == c) 
				return true;
		}
		
		return false;
	}
	
	public boolean hasColors(Color ...colors) {
		for(Color c1 : colors) {
			if(!hasColor(c1))
				return false;
		}
		
		return true;
	}
	
	public boolean matchesColors(Color ...colors) {
		for(int i = 0; i < colors.length; i ++) {
			if(colors[i] != this.colors[i]) return false;
		}
		
		return true;
	}
	
	public Color[] getColors() {
		return this.colors;
	}

	public boolean equalsPosition(Piece other) {
		return index == other.index && position == other.position && type == other.type;
	}

}
