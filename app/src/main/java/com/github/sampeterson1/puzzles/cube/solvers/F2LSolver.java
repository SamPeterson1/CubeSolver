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

package com.github.sampeterson1.puzzles.cube.solvers;

import com.github.sampeterson1.puzzle.lib.Color;
import com.github.sampeterson1.puzzle.lib.Piece;
import com.github.sampeterson1.puzzle.moves.Axis;
import com.github.sampeterson1.puzzle.moves.Move;
import com.github.sampeterson1.puzzles.cube.meta.Cube;

/*
 * This class solves the four bottom corners of the cube using the beginner's method.
 * 
 * This implementation is not meant to be readable.
 * See https://solvethecube.com/#step3 for more information.
 */
public class F2LSolver {

	private Cube cube;

	public F2LSolver(Cube cube) {
		this.cube = cube;
	}

	public void solve() {
		System.out.println("Solving F2L...");
		if(cube.getSize() > 2) {
			cube.pushRotations();
	
			Color avoid = cube.getSolveColor(Axis.U);
			for (int i = 0; i < 4; i++) {
				solveEdge(avoid);
			}
	
			cube.popRotations();
		}
	}

	public void solveEdge(Color avoid) {
		Piece toInsert = findEdge_U(avoid);

		if (toInsert != null) {
			allignEdge(toInsert);

			Color rColor = cube.getSolveColor(Axis.R);
			Color lColor = cube.getSolveColor(Axis.L);
			Color c1 = toInsert.getColor(0);

			if (c1 == rColor) {
				insertRight();
			} else if (c1 == lColor) {
				insertLeft();
			}
		} else {
			toInsert = findEdge_D(avoid);
			if (toInsert != null) {
				moveEdge(toInsert);
				solveEdge(avoid);
			}
		}
	}

	private void allignEdge(Piece toInsert) {
		while (toInsert.getPosition() != 0) {
			cube.makeMove(new Move(Axis.U, 0, true));
		}

		Color fColor = cube.getSolveColor(Axis.F);
		while (toInsert.getColor(1) != fColor) {
			cube.makeMove(new Move(Axis.U, 0, true));
			cube.makeRotation(Axis.U, false);
			fColor = cube.getSolveColor(Axis.F);
		}
	}

	private Piece findEdge_D(Color avoid) {
		for (int i = 4; i < 8; i++) {
			Piece piece = cube.getEdge(i).getPiece(0);
			cube.pushRotations();
			while (piece.getPosition() != 5) {
				cube.makeRotation(Axis.U, true);
			}
			Color fColor = cube.getSolveColor(Axis.F);
			if (piece.getColor(0) != avoid && piece.getColor(1) != avoid && piece.getColor(0) != fColor) {
				return piece;
			}
			cube.popRotations();
		}

		return null;
	}

	private Piece findEdge_U(Color avoid) {
		for (int i = 0; i < 4; i++) {
			Piece piece = cube.getEdge(i).getPiece(0);
			if (piece.getColor(0) != avoid && piece.getColor(1) != avoid) {
				return piece;
			}
		}

		return null;
	}

	private void insertLeft() {
		cube.makeMove(new Move(Axis.U, 0, false));
		cube.makeMove(new Move(Axis.L, 0, false));
		cube.makeMove(new Move(Axis.U, 0, true));
		cube.makeMove(new Move(Axis.L, 0, true));
		cube.makeMove(new Move(Axis.F, 0, false));
		cube.makeMove(new Move(Axis.L, 0, true));
		cube.makeMove(new Move(Axis.F, 0, true));
		cube.makeMove(new Move(Axis.L, 0, false));
	}

	private void insertRight() {
		cube.makeMove(new Move(Axis.U, 0, true));
		cube.makeMove(new Move(Axis.R, 0, true));
		cube.makeMove(new Move(Axis.U, 0, false));
		cube.makeMove(new Move(Axis.R, 0, false));
		cube.makeMove(new Move(Axis.F, 0, true));
		cube.makeMove(new Move(Axis.R, 0, false));
		cube.makeMove(new Move(Axis.F, 0, false));
		cube.makeMove(new Move(Axis.R, 0, true));
	}

	private void moveEdge(Piece toInsert) {
		while (toInsert.getPosition() != 5) {
			cube.makeRotation(Axis.U, true);
		}
		cube.makeMove(new Move(Axis.R, 0, true));
		cube.makeMove(new Move(Axis.U, 0, false));
		cube.makeMove(new Move(Axis.R, 0, false));
		cube.makeMove(new Move(Axis.F, 0, true));
		cube.makeMove(new Move(Axis.R, 0, false));
		cube.makeMove(new Move(Axis.F, 0, false));
		cube.makeMove(new Move(Axis.R, 0, true));
	}

}
