package bender_episode_one;

import java.util.*;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;
import java.io.*;
import java.math.*;

class Solution {

	private static final char SUICIDE_POS = '$';
	private static final char START_POS = '@';
	private static final char WALL = '#';
	private static final char OBSTACLE = 'X';
	private static final char SOUTH = 'S';
	private static final char EAST = 'E';
	private static final char NORTH = 'N';
	private static final char WEST = 'W';
	private static final char INVENTER = 'I';
	private static final char BEER = 'B';
	private static final char TELEPORT = 'T';
	private static final char SPACE = ' ';

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

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + x;
			result = prime * result + y;
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
			Coordinate other = (Coordinate) obj;
			if (x != other.x)
				return false;
			if (y != other.y)
				return false;
			return true;
		}
	}

	private enum Direction {
		SOUTH, EAST, NORTH, WEST, LOOP;

		public Direction next() {
			Direction next = null;

			switch (this) {
			case SOUTH:
				next = EAST;
				break;
			case EAST:
				next = NORTH;
				break;
			case NORTH:
				next = WEST;
				break;
			case WEST:
				next = SOUTH;
				break;
			}

			return next;
		}

		public Direction prev() {
			Direction prev = null;

			switch (this) {
			case SOUTH:
				prev = WEST;
				break;
			case EAST:
				prev = SOUTH;
				break;
			case NORTH:
				prev = EAST;
				break;
			case WEST:
				prev = NORTH;
				break;
			}
			return prev;
		}
	}

	private static class Move {

		private Coordinate coordinate;
		private Direction direction;
		private Boolean breakerMode;
		private Boolean inverterMode;

		public Move(Coordinate coordinate, Direction direction, Boolean breakerMode, Boolean inverterMode) {
			this.coordinate = coordinate;
			this.direction = direction;
			this.breakerMode = breakerMode;
			this.inverterMode = inverterMode;
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
			result = prime * result + ((breakerMode == null) ? 0 : breakerMode.hashCode());
			result = prime * result + ((coordinate == null) ? 0 : coordinate.hashCode());
			result = prime * result + ((direction == null) ? 0 : direction.hashCode());
			result = prime * result + ((inverterMode == null) ? 0 : inverterMode.hashCode());
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
			if (breakerMode == null) {
				if (other.breakerMode != null)
					return false;
			} else if (!breakerMode.equals(other.breakerMode))
				return false;
			if (coordinate == null) {
				if (other.coordinate != null)
					return false;
			} else if (!coordinate.equals(other.coordinate))
				return false;
			if (direction != other.direction)
				return false;
			if (inverterMode == null) {
				if (other.inverterMode != null)
					return false;
			} else if (!inverterMode.equals(other.inverterMode))
				return false;
			return true;
		}
	}

	private static class TeleportPair {

		private Coordinate t1;
		private Coordinate t2;

		public void add(Coordinate teleport) {
			if (t1 == null) {
				t1 = teleport;
			} else if (t2 == null) {
				t2 = teleport;
			}
		}

		public Coordinate getOther(Coordinate teleport) {
			if (t1.equals(teleport)) {
				return t2;
			}
			return t1;
		}
	}

	private static class WorldMap {

		private Map<Coordinate, Character> map = new HashMap<>();
		private TeleportPair teleports = new TeleportPair();
		private Coordinate startPos;
		private Coordinate suicidePos;

		public void addLine(int lineInd, String line) {
			IntStream.range(0, line.length()).forEach(colInd -> processCharacter(lineInd, colInd, line.charAt(colInd)));
		}

		private void processCharacter(int lineInd, int colInd, Character ch) {
			Coordinate coord = new Coordinate(colInd, lineInd);
			map.put(coord, ch);

			switch (ch) {
			case START_POS:
				startPos = coord;
				break;
			case SUICIDE_POS:
				suicidePos = coord;
				break;
			case TELEPORT:
				teleports.add(coord);
				break;
			}
		}

		public Coordinate getStartPos() {
			return startPos;
		}

		public Coordinate getSuicidePos() {
			return suicidePos;
		}

		public Coordinate getOtherTeleport(Coordinate teleport) {
			return teleports.getOther(teleport);
		}

		public char get(Coordinate coord) {
			return map.get(coord);
		}

		public void set(Coordinate coord, char content) {
			map.put(coord, content);
		}
	}

	private static class BenderMoveSimulator {

		private Boolean invertedMode;
		private Boolean breakerMode;
		private WorldMap worldMap;
		private Coordinate coord;
		private Direction dir;
		private Move prevMove;
		private Move actMove;
		private Set<Move> takenMoves;
		private List<Direction> path;

		public BenderMoveSimulator(WorldMap worldMap) {
			this.worldMap = worldMap;
		}

		public void simulateMoves() {
			initializeSimulator();

			while (hasMove()) {
				move();
			}
			printResult();
		}

		private void printResult() {
			if (worldMap.get(coord) == SUICIDE_POS) {
				while (!path.isEmpty()) {
					System.out.println(path.remove(0));
				}
			} else {
				System.out.println("LOOP");
			}

		}

		private void initializeSimulator() {
			takenMoves = new HashSet<>();
			path = new LinkedList<>();
			coord = worldMap.getStartPos();
			dir = Direction.SOUTH;
			invertedMode = false;
			breakerMode = false;
			prevMove = null;
			actMove = null;
		}

		private void move() {
			processActCoord();
			makeStep();
			prevMove = actMove;
			actMove = new Move(coord, dir, breakerMode, invertedMode);
			if (prevMove != null) {
				takenMoves.add(prevMove);
			}
			
			path.add(dir);
		}

		private void processActCoord() {

			switch (worldMap.get(coord)) {
			case SOUTH:
				dir = Direction.SOUTH;
				break;
			case EAST:
				dir = Direction.EAST;
				break;
			case NORTH:
				dir = Direction.NORTH;
				break;
			case WEST:
				dir = Direction.WEST;
				break;
			case INVENTER:
				invertedMode = !invertedMode;
				break;
			case BEER:
				breakerMode = !breakerMode;
				break;
			case TELEPORT:
				coord = worldMap.getOtherTeleport(coord);
				break;
			case OBSTACLE:
				worldMap.set(coord, SPACE);
				takenMoves.clear();
				break;
			}
		}

		private void makeStep() {
			Coordinate nextCoord = getNextCoord();

			if (isWall(nextCoord)) {
				setStartDir();
				nextCoord = getNextCoord();
				while (isWall(nextCoord)) {
					changeDir();
					nextCoord = getNextCoord();
				}
			}

			coord = nextCoord;
		}

		private void setStartDir() {
			if (!invertedMode) {
				dir = Direction.SOUTH;
			} else if (invertedMode) {
				dir = Direction.WEST;
			}
		}

		private void changeDir() {
			if (!invertedMode) {
				dir = dir.next();
			} else if (invertedMode) {
				dir = dir.prev();
			}
		}

		private Coordinate getNextCoord() {
			Coordinate nextCoord = null;
			switch (dir) {
			case SOUTH:
				nextCoord = new Coordinate(coord.getX(), coord.getY() + 1);
				break;
			case EAST:
				nextCoord = new Coordinate(coord.getX() + 1, coord.getY());
				break;
			case NORTH:
				nextCoord = new Coordinate(coord.getX(), coord.getY() - 1);
				break;
			case WEST:
				nextCoord = new Coordinate(coord.getX() - 1, coord.getY());
				break;
			}
			return nextCoord;
		}

		private boolean isWall(Coordinate nextCoord) {
			Character content = worldMap.get(nextCoord);

			if (content == WALL || (content == OBSTACLE && !breakerMode)) {
				return true;
			}
			return false;
		}

		private boolean hasMove() {
			return worldMap.get(coord) != SUICIDE_POS && (actMove == null || !takenMoves.contains(actMove));
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
			worldMap.addLine(i, in.nextLine());
		}

		simulator.simulateMoves();
	}
}