package teads_sponsored_contest;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

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
	}

	private static class Network {

		private Map<Integer, Person> peopleMap = new HashMap<>();

		public void addRelation(int idOne, int idTwo) {
			addIfNotPresent(idOne);
			addIfNotPresent(idTwo);
			addNeighbors(idOne, idTwo);
		}

		private void addIfNotPresent(int id) {
			if (!peopleMap.containsKey(id)) {
				peopleMap.put(id, new Person(id));
			}
		}

		private void addNeighbors(int idOne, int idTwo) {
			Person personOne = peopleMap.get(idOne);
			Person personTwo = peopleMap.get(idTwo);
			personOne.addNeighbor(personTwo);
			personTwo.addNeighbor(personOne);
		}

		public Collection<Person> getNodes() {
			return peopleMap.values();
		}
	}

	private static class NetworkSearcher {

		private Collection<Person> people;

		public NetworkSearcher(Network network) {
			people = network.getNodes();
		}

		public Integer findMinimalAmount() {

			Person root = people.stream().findFirst().get();

			Person onePeripheralPerson = breadthFirstSearchFarestPerson(root);

			Person otherPeripheralPerson = breadthFirstSearchFarestPerson(onePeripheralPerson);

			Integer minDistance = (otherPeripheralPerson.getDistance() + 1) / 2;

			return minDistance;
		}

		private Person breadthFirstSearchFarestPerson(Person root) {

			Person farestPerson = root;

			people.forEach(resetPerson());

			Queue<Person> queue = new LinkedList<>();
			root.setDistance(0);
			queue.add(root);

			while (!queue.isEmpty()) {
				Person current = queue.poll();

				farestPerson = current.getNeighbors().stream().filter(isNeigborUnExplored())
						.peek(exploreNeighbor(queue, current)).max(comparePersonByDistance()).orElse(farestPerson);
			}

			return farestPerson;
		}

		private Comparator<Person> comparePersonByDistance() {
			return (Person p1, Person p2) -> p1.getDistance().compareTo(p2.getDistance());
		}

		private Predicate<? super Person> isNeigborUnExplored() {
			return neighbor -> neighbor.distance == null;
		}

		private Consumer<? super Person> exploreNeighbor(Queue<Person> queue, Person current) {
			return neighbor -> {
				neighbor.setDistance(current.getDistance() + 1);
				neighbor.setParent(current);
				queue.add(neighbor);
			};
		}

		private Consumer<Person> resetPerson() {
			return (person) -> {
				person.setDistance(null);
				person.setParent(null);
			};
		}

	}

	public static void main(String args[]) {

		Scanner in = new Scanner(System.in);
		int numberOfEdges = in.nextInt();
		Network network = new Network();

		for (int i = 0; i < numberOfEdges; i++) {
			int idOne = in.nextInt(); 
			int idTwo = in.nextInt(); 

			network.addRelation(idOne, idTwo);
		}

		NetworkSearcher networkSearcher = new NetworkSearcher(network);

		Integer answer = networkSearcher.findMinimalAmount();

		System.out.println(answer);
	}
}