package bender_episode_one;

import java.util.*;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;
import java.io.*;
import java.math.*;

class Solution {
	
	private static final char SUICIDE_POS = '$';
	private static final char START_POS = '@';

	private static class Coordinate {

		private int x;
		private int y;

		public Coordinate(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public int getX() {
			return x;
		}

		public void setX(int x) {
			this.x = x;
		}

		public int getY() {
			return y;
		}

		public void setY(int y) {
			this.y = y;
		}
	}

	private enum Direction {
		SOUTH, EAST, NORTH, WEST, LOOP;
	}

	private static class Move {

		private Coordinate coordinate;
		private Direction direction;

		public Move(Coordinate coordinate, Direction direction) {
			this.coordinate = coordinate;
			this.direction = direction;
		}

		public Coordinate getCoordinate() {
			return coordinate;
		}

		public Direction getDirection() {
			return direction;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((coordinate == null) ? 0 : coordinate.hashCode());
			result = prime * result + ((direction == null) ? 0 : direction.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Move other = (Move) obj;
			if (coordinate == null) {
				if (other.coordinate != null)
					return false;
			} else if (!coordinate.equals(other.coordinate))
				return false;
			if (direction != other.direction)
				return false;
			return true;
		}
	}

	private static class WorldMap {

		private Map<Coordinate, Character> map = new HashMap<>();
		private Coordinate startPos;
		private Coordinate suicidePos;

		public void addLine(int lineInd, String line) {
			IntStream.range(0, line.length()).forEach(colInd -> processCharacter(lineInd, colInd, line.charAt(colInd)));
		}

		private void processCharacter(int lineInd, int colInd, Character ch) {
			Coordinate coord = new Coordinate(lineInd, colInd);
			map.put(coord, ch);
			
			switch (ch) {
			case START_POS:
				startPos = coord;
				break;
			case SUICIDE_POS:
				suicidePos = coord;
				break;
			}
		}

		public Coordinate getStartPos() {
			return startPos;
		}

		public Coordinate getSuicidePos() {
			return suicidePos;
		}

		public char get(Coordinate coord) {
			return map.get(coord);
		}
	}
	
	private static class BenderMoveSimulator {

		private WorldMap worldMap;
		private Set<Move> takenMoves;
		List<Direction> path;
		private Move actMove;

		public BenderMoveSimulator(WorldMap worldMap) {
			this.worldMap = worldMap;
		}
	
		public void simulateMoves() {		
			initializeSimulator();			

			move();
			while(hasMove()){		
				move();
			}
		}

		private void initializeSimulator() {
			takenMoves = new HashSet<>();
			path = new LinkedList<>();
			actMove = new Move(worldMap.getStartPos(), Direction.SOUTH);
		}

		private void move() {
			takenMoves.add(actMove);
			path.add(actMove.getDirection());
			makeStep();
		}

		private void makeStep() {
			Coordinate nextCoord;
			switch (actMove.getDirection()) {
			case SOUTH:
				nextCoord = new Coordinate(actMove.getCoordinate().getX(), actMove.getCoordinate().getY());
				break;

			default:
				break;
			}
			
		}

		private boolean hasMove() {
			 Coordinate coord = actMove.getCoordinate();
			 return worldMap.get(coord) == SUICIDE_POS || takenMoves.contains(actMove);
		}
	}

	public static void main(String args[]) {
		Scanner in = new Scanner(System.in);
		WorldMap worldMap = new WorldMap();
		BenderMoveSimulator simulator = new BenderMoveSimulator(worldMap);

		int numberOfLines = in.nextInt();
		int numberOfColumns = in.nextInt();
		in.nextLine();
		for (int i = 0; i < numberOfLines; i++) {
			String row = in.nextLine();
		}

		simulator.simulateMoves();
	}
}