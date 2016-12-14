package teads_sponsored_contest;

import java.util.*;
import java.util.function.BiConsumer;

class Solution {

	private static class Person {

		private Integer id;
		private Set<Person> neigbors;
		private Integer distance;
		private Person parent;

		public Person(Integer id) {
			this.id = id;
			this.neigbors = new HashSet<>();
			this.distance = 0;
		}

		public Integer getDistance() {
			return distance;
		}

		public void setDistance(Integer distance) {
			this.distance = distance;
		}

		public Integer getId() {
			return id;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((id == null) ? 0 : id.hashCode());
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
			Person other = (Person) obj;
			if (id == null) {
				if (other.id != null)
					return false;
			} else if (!id.equals(other.id))
				return false;
			return true;
		}

		public void addNeighbor(Person person) {
			neigbors.add(person);
		}

		public Set<Person> getNeighbors() {
			return neigbors;
		}

		public void setParent(Person parent) {
			this.parent = parent;
		}

		public Person getParent() {
			return parent;
		}
	}

	private static class Graph {

		private Map<Integer, Person> persons = new HashMap<>();

		public void addRelation(int idOne, int idTwo) {
			addIfNotPresent(idOne);
			addIfNotPresent(idTwo);
			addNeighbors(idOne, idTwo);
		}

		private void addIfNotPresent(int id) {
			if (!persons.containsKey(id)) {
				persons.put(id, new Person(id));
			}
		}

		private void addNeighbors(int idOne, int idTwo) {
			Person personOne = persons.get(idOne);
			Person personTwo = persons.get(idTwo);
			personOne.addNeighbor(personTwo);
			personTwo.addNeighbor(personOne);
		}

		public Integer findMinimalAmount() {


			Person root =   persons.values().stream().findFirst().get();
			
			Person maxDistancePerson = breadthFirstSearchFarestPerson(root);

			maxDistancePerson = breadthFirstSearchFarestPerson(maxDistancePerson);
			
			Integer minDistance = (maxDistancePerson.getDistance() + 1) / 2;
			
			return minDistance;
		}

		private Person breadthFirstSearchFarestPerson(Person root) {

			Person farestPerson = root;

			BiConsumer<Integer, Person> resetPerson = (id, person) -> {
				person.setDistance(null);
				person.setParent(null);
			};

			persons.forEach(resetPerson);
			Comparator<Person> byDistance = (Person p1, Person p2)->p1.getDistance().compareTo(p2.getDistance());

			Queue<Person> queue = new LinkedList<>();
			root.setDistance(0);
			queue.add(root);

			while (!queue.isEmpty()) {
				Person current = queue.poll();
				
				current.getNeighbors().stream().
						filter(neighbor -> neighbor.distance == null).
						forEach(neighbor -> {neighbor.setDistance(current.getDistance() + 1); neighbor.setParent(current); queue.add(neighbor);});
			}

			farestPerson = persons.values().stream().max(byDistance).get();
			
			return farestPerson;
		}
	}

	public static void main(String args[]) {

		Scanner in = new Scanner(System.in);
		int numberOfEdges = in.nextInt(); // the number of adjacency relations
		Graph graph = new Graph();

		for (int i = 0; i < numberOfEdges; i++) {
			int idOne = in.nextInt(); // the ID of a person which is
										// adjacent to yi
			int idTwo = in.nextInt(); // the ID of a person which is

			graph.addRelation(idOne, idTwo);
			// adjacent to xi
		}

		Integer answer = graph.findMinimalAmount();
		System.out.println(answer);
	}
}