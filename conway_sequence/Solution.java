package conway_sequence;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class Solution {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int originalNumber = in.nextInt();
        int lineToDisplay = in.nextInt();
        
        Queue<Integer> actLine = new LinkedList<>(Arrays.asList(originalNumber));
        
        for (int i = 1; i < lineToDisplay; i++) {       	
        	actLine = calculateNextLine(actLine);
		}
        
        System.out.println(actLine.stream().map(String::valueOf).collect(Collectors.joining(" ")));
    }

	private static Queue<Integer> calculateNextLine(Queue<Integer> actLine) {
		Queue<Integer> nextLine = new LinkedList<>();
		Integer actNumber = actLine.remove();
		Integer actCount = 1;

		while(!actLine.isEmpty()) {
			if (actNumber.equals(actLine.element())) {
				actCount++;
			} else {
				nextLine.add(actCount);
				nextLine.add(actNumber);
				actCount = 1;
				actNumber = actLine.element();
			}
			actLine.remove();
		}
		nextLine.add(actCount);
		nextLine.add(actNumber);
		
		return nextLine;
	}
}