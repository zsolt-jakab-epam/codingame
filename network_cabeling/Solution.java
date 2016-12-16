package network_cabeling;

import java.util.*;
import java.io.*;
import java.math.*;

class Solution {

	private static class MinHeap {
		private int size = 0;
		private int[] heap = new int[100000];

		public int min() {
			return heap[0];
		}

		public int extractMin() {
			int min = heap[0];
			heap[0] = heap[--size];

			int pos = 0;
			while (2 * pos + 2 <= size && (heap[pos] > heap[2 * pos + 1] || heap[pos] > heap[2 * pos + 2])) {
				if (heap[2 * pos + 1] <= heap[2 * pos + 2]) {
					swap(pos, 2 * pos + 1);
					pos = 2 * pos + 1;
				} else if (2 * pos + 2 < size) {
					swap(pos, 2 * pos + 2);
					pos = 2 * pos + 2;
				} else {
					break;
				}
			}

			return min;
		}

		public void insert(int key) {
			int pos = size++;
			heap[pos] = key;

			while (pos != 0 && heap[pos] < heap[(pos - 1) / 2]) {
				swap(pos, (pos - 1) / 2);
				pos = (pos - 1) / 2;
			}
		}

		private void swap(int pos1, int pos2) {
			int tmp = heap[pos1];
			heap[pos1] = heap[pos2];
			heap[pos2] = tmp;
		}

		public int getSize() {
			return size;
		}
	}

	private static class MaxHeap {
		private int size = 0;
		private int[] heap = new int[100000];

		public int max() {
			return heap[0];
		}

		public int extractMax() {
			int max = heap[0];
			heap[0] = heap[--size];

			int pos = 0;
			while (2 * pos + 2 <= size && (heap[pos] < heap[2 * pos + 1] || heap[pos] < heap[2 * pos + 2])) {
				if (heap[2 * pos + 1] >= heap[2 * pos + 2]) {
					swap(pos, 2 * pos + 1);
					pos = 2 * pos + 1;
				} else if (2 * pos + 2 < size) {
					swap(pos, 2 * pos + 2);
					pos = 2 * pos + 2;
				} else {
					break;
				}
			}

			return max;
		}

		public void insert(int key) {
			int pos = size++;
			heap[pos] = key;

			while (pos != 0 && heap[pos] > heap[(pos - 1) / 2]) {
				swap(pos, (pos - 1) / 2);
				pos = (pos - 1) / 2;
			}
		}

		private void swap(int pos1, int pos2) {
			int tmp = heap[pos1];
			heap[pos1] = heap[pos2];
			heap[pos2] = tmp;
		}

		public int getSize() {
			return size;
		}
	}
	
	private static class MedianHeap {

		private MinHeap heapHigh = new MinHeap();
		
		private MaxHeap heapLow = new MaxHeap();

		public void add(int key) {
			
			if (key > heapLow.max()) {
				heapHigh.insert(key);
			} else {
				heapLow.insert(key);
			}
			
			if (heapLow.getSize() < heapHigh.getSize()) {
				heapLow.insert(heapHigh.extractMin());
			}
			
			if (heapHigh.getSize() + 1 < heapLow.getSize()) {
				heapHigh.insert(heapLow.extractMax());
			}
		}

		public int getMedian() {
			return heapLow.max();
		}

	}

	private static class BusinessPark {

		private List<Long> buildings;
		
		private MedianHeap medianHeap;

		private Long minX;

		private Long maxX;

		public BusinessPark(int numberOfBuildings) {
			buildings = new ArrayList<>(numberOfBuildings);
			medianHeap = new MedianHeap();
		}

		public void addBuilding(long x, long y) {

			buildings.add(y);
			medianHeap.add((int) y);

			if (minX == null || x < minX) {
				minX = x;
			}

			if (maxX == null || maxX < x) {
				maxX = x;
			}
		}

		public Long calculateMinimalLengthOfCable() {
			Long yPosOfMainCable = calculateYPositionOfMainCable();
			
			Long MinimalLengthOfCable = maxX - minX;
			MinimalLengthOfCable += buildings.stream().mapToLong(pos -> Math.abs(pos - yPosOfMainCable)).sum();
			return MinimalLengthOfCable;
		}

		private Long calculateYPositionOfMainCable() {
			return (long) medianHeap.getMedian();
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