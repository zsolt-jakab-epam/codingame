package network_cabeling;

import java.util.*;

class Solution {

	private static class BusinessPark {

		private List<Long> buildings;

		private Long minX;

		private Long maxX;

		public BusinessPark(int numberOfBuildings) {
			buildings = new ArrayList<>();
		}

		public void addBuilding(long x, long y) {
			
			buildings.add(y);
			if (minX == null || x < minX) {
				minX = x;
			}
			if (maxX == null || maxX < x) {
				maxX = x;
			}
		}

		public Long calculateMinimalLengthOfCable() {		
			
			Long yPosOfMainCable = calculateYPositionOfMainCable();
			return  maxX - minX + buildings.stream().mapToLong(pos -> Math.abs(pos - yPosOfMainCable)).sum();
		}

		private Long calculateYPositionOfMainCable() {
			
			Collections.sort(buildings);
			int medianIndex = buildings.size() / 2;
			return buildings.get(medianIndex);
		}
	}

	public static void main(String args[]) {
		
		Scanner in = new Scanner(System.in);
		int numberOfBuildings = in.nextInt();
		BusinessPark businessPark = new BusinessPark(numberOfBuildings);

		for (int i = 0; i < numberOfBuildings; i++) {
			businessPark.addBuilding(in.nextInt(), in.nextInt());
		}

		System.out.println(businessPark.calculateMinimalLengthOfCable());
	}
}