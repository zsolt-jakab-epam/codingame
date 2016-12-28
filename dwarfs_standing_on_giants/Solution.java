package dwarfs_standing_on_giants;

import java.io.InputStream;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Auto-generated code below aims at helping you parse the standard input
 * according to the problem statement.
 **/
class Solution {

	private static class Person {

		Integer id;
		Integer distance;
		Boolean isRoot;
		Map<Integer, Person> neighbors;

		public Person(Integer id) {
			this.id = id;
			this.neighbors = new HashMap<>();
			this.isRoot = true;
			this.distance = null;
		}

		public Integer getDistance() {
			return distance;
		}

		public void setDistance(Integer distance) {
			this.distance = distance;
		}

		public Collection<Person> getNeighbors() {
			return neighbors.values();
		}

		public void addNeighbor(Person neighbor) {
			neighbors.put(neighbor.getId(), neighbor);
			neighbor.hasParent();
		}

		public Boolean isRoot() {
			return isRoot;
		}

		private void hasParent() {
			isRoot = false;
		}

		private Integer getId() {
			return id;
		}
	}

	private static class Graph {
		private Map<Integer, Person> people = new HashMap<>();

		public Collection<Person> getNodes() {
			return people.values();
		}

		public void addRelation(int idOne, int idTwo) {
			addIfNotPresent(idOne);
			addIfNotPresent(idTwo);
			Person personOne = people.get(idOne);
			Person personTwo = people.get(idTwo);
			personOne.addNeighbor(personTwo);
		}

		private void addIfNotPresent(int id) {
			if (!people.containsKey(id)) {
				people.put(id, new Person(id));
			}
		}
	}

	public static class GraphSearcher {

		Collection<Person> people;

		public GraphSearcher(Graph graph) {
			people = graph.getNodes();
		}

		public Integer getMostPeopleInAPath() {
			return getRoots().mapToInt(root -> breadthFirstSearchMostPeopleInAPathFrom(root)).max().getAsInt();
		}

		private Integer breadthFirstSearchMostPeopleInAPathFrom(Person root) {

			people.forEach(person -> person.setDistance(null));

			Queue<Person> queue = new LinkedList<>();

			root.setDistance(0);

			Integer longestPath = 0;

			queue.add(root);

			while (!queue.isEmpty()) {
				Person current = queue.poll();

				longestPath = current.getNeighbors().stream().filter(isWorthToExplore(current))
						.peek(exploreNeighbor(queue, current)).mapToInt(Person::getDistance).max().orElse(longestPath);
			}

			return longestPath + 1;
		}

		private Predicate<? super Person> isWorthToExplore(Person current) {
			return neighbor -> neighbor.distance == null || neighbor.getDistance() <= current.getDistance();
		}

		private Consumer<? super Person> exploreNeighbor(Queue<Person> queue, Person current) {
			return neighbor -> {
				neighbor.setDistance(current.getDistance() + 1);
				queue.add(neighbor);
			};
		}

		private Stream<Person> getRoots() {
			return people.stream().filter(Person::isRoot);
		}
	}

	private static class GraphReader {

		private Graph graph;
		private Scanner in;

		public GraphReader(InputStream inputStream) {
			this.in = new Scanner(inputStream);
		}

		public void readGraph() {
			graph = new Graph();
			int numberOfRelations = in.nextInt();

			IntStream.range(0, numberOfRelations).forEach(i -> graph.addRelation(in.nextInt(), in.nextInt()));
		}

		public Graph getGraph() {
			return graph;
		}
	}

	public static void main(String args[]) {

		GraphReader graphReader = new GraphReader(System.in);

		graphReader.readGraph();

		Graph graph = graphReader.getGraph();

		GraphSearcher graphSearcher = new GraphSearcher(graph);

		System.out.println(graphSearcher.getMostPeopleInAPath());
	}
}