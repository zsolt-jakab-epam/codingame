package telephone_numbers;

import java.util.*;
import java.util.stream.IntStream;

class Solution {

	private static class Storage {

		Map<Integer,Storage> followers = new HashMap<>();
		
		public void addNumber(String number) {
			if (number.length() > 0) {
				Integer actDigit = Integer.valueOf(number.charAt(0));

				if (!followers.containsKey(actDigit)) {
					followers.put(actDigit, new Storage());
				}
				followers.get(actDigit).addNumber(number.substring(1));	
			}
		}
		
		public int size() {
			return followers.size() + followers.values().stream().mapToInt(Storage::size).sum();
		}
	}
	
    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int numberOfTelephoneNumbers = in.nextInt();     
        Storage storage = new Storage();
        
        IntStream.range(0, numberOfTelephoneNumbers). forEach(i -> storage.addNumber(in.next()));

        System.out.println(storage.size());
    }
}