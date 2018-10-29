package bigint;

/**
 * This class encapsulates a BigInteger, i.e. a positive or negative integer with 
 * any number of digits, which overcomes the computer storage length limitation of 
 * an integer.
 * 
 */
public class BigInteger {

	/**
	 * True if this is a negative integer
	 */
	boolean negative;
	
	/**
	 * Number of digits in this integer
	 */
	int numDigits;
	
	/**
	 * Reference to the first node of this integer's linked list representation
	 * NOTE: The linked list stores the Least Significant Digit in the FIRST node.
	 * For instance, the integer 235 would be stored as:
	 *    5 --> 3  --> 2
	 *    
	 * Insignificant digits are not stored. So the integer 00235 will be stored as:
	 *    5 --> 3 --> 2  (No zeros after the last 2)        
	 */
	DigitNode front;
	
	/**
	 * Initializes this integer to a positive number with zero digits, in other
	 * words this is the 0 (zero) valued integer.
	 */
	public BigInteger() {
		negative = false;
		numDigits = 0;
		front = null;
	}
	
	/**
	 * Parses an input integer string into a corresponding BigInteger instance.
	 * A correctly formatted integer would have an optional sign as the first 
	 * character (no sign means positive), and at least one digit character
	 * (including zero). 
	 * Examples of correct format, with corresponding values
	 *      Format     Value
	 *       +0            0
	 *       -0            0
	 *       +123        123
	 *       1023       1023
	 *       0012         12  
	 *       0             0
	 *       -123       -123
	 *       -001         -1
	 *       +000          0
	 *       
	 * Leading and trailing spaces are ignored. So "  +123  " will still parse 
	 * correctly, as +123, after ignoring leading and trailing spaces in the input
	 * string.
	 * 
	 * Spaces between digits are not ignored. So "12  345" will not parse as
	 * an integer - the input is incorrectly formatted.
	 * 
	 * An integer with value 0 will correspond to a null (empty) list - see the BigInteger
	 * constructor
	 * 
	 * @param integer Integer string that is to be parsed
	 * @return BigInteger instance that stores the input integer.
	 * @throws IllegalArgumentException If input is incorrectly formatted
	 */
	
	
	/*
	 * This removes zeros that are in the beginning
	 * Example:
	 * 
	 * 		Format		  Value
	 * 		00000123  -> 	123
	 * 		+0001     ->   	  1
	 * 		-000065   -> 	-65
	 */
	
	private static String removeZeros(String stringNum) {
		
		String result = "";
		int i = 0;
		while(stringNum.charAt(i) == '0') {
			i++;
		}
		StringBuffer sb = new StringBuffer(stringNum);
		
		sb.replace(0, i, "");
		
		result = sb.toString();
		
		return result;
	}
	
	public static BigInteger parse(String integer) 
	throws IllegalArgumentException {
		
		BigInteger bigint = new BigInteger(); //creates new linked list
		
		String result = ""; //the string after it's been parsed
		
		
		String stringNum = integer.trim(); // removes spaces
		
		
		//check if integer is a negative or positive value
		boolean isNegative = false;
		boolean isPositive = false;
		
		boolean error = false;
		
		int charCount = 0; //count number of signs
		
		//s.matches(".*-.*-.*")
		
		/*
		 * Check each character to see if there is any signs ("-" or "+").
		 * If the character at i is a sign, then add 1 to charCount to their respectful signs
		 * then make isNegative or isPositive true.
		 * If there is more than one sign then set error to true which would later throw an exception.
		 * If it does not pass the two if statements, then check if first character is a digit.
		 * If it is a digit then the integer is a positive
		 */
		
		for(int i = 0; i < stringNum.length(); i++) {
			char firstChar = stringNum.charAt(i);
			if(firstChar == '-') {
				charCount += 1;
				isNegative = true;
				
				if(charCount > 1 && stringNum.matches(".*-.*-.*")) {
					error = true;
				}
			}
			else if(firstChar == '+') {
				charCount += 1;
				isPositive = true;
				
				if(charCount > 1 && stringNum.matches(".*+.*+.*")) {
					error = true;
				}
			}
			else if(Character.isDigit(firstChar) == true) {
				isPositive = true;
			}
			else { //if doesnt pass any of these ifs then the string has a letter
				throw new IllegalArgumentException();
			}
		}
		if(error == true) {
			throw new IllegalArgumentException();
		}
		if(isPositive == true || isNegative == true) {
			result = stringNum.replaceAll("-+", "");
		}
		
		if(stringNum.charAt(0) == '+') {
			result = stringNum.substring(1);
			System.out.println(result);
		}
		//see if integer is only 0
		int zeroCount = 0;
		boolean zero = false;
		
		for(int i = 0; i < result.length(); i++) {
			if(result.charAt(i) == '0') {
				zeroCount += 1;
			}
			if(zeroCount == result.length()) {
				zero = true;
			}
		}
		if(zero == true) {
			result = "0";
			bigint.front = new DigitNode(0, null);
			return bigint;
		}
		else {
			result = removeZeros(result);
		}
		//reverse string
		
		//result = reverseString(result);
		int i = result.length() - 1;
		
		int lastDigit = Integer.parseInt(result.substring(i, result.length()));
		bigint.front = new DigitNode(lastDigit, null);
		
		for(DigitNode curr = bigint.front; i > 0; curr = curr.next, i--) {
			curr.next = new DigitNode(Integer.parseInt(result.substring(i - 1, i)), null);
		}
		if(isNegative == true) {
			bigint.negative = true;
		}
		
		bigint.numDigits = result.length();
		
		//traverse(bigint);
		
		System.out.print(bigint.numDigits);
		
		return bigint;
		
		
		/** Test Cases
		 * 0 in front of number
		 * Spaces in number
		 * special characters such as *+&^%$
		 * count negative (-) but not positive (+) symbols
		 * characters
		 */
	}

	
	private static void traverse(BigInteger front) {
		if (front == null) {
			System.out.println("Empty list");
			return;
		}
		System.out.print(front.front.digit);  // first item
		DigitNode ptr = front.front.next;    // prepare to loop starting with second item
		while (ptr != null) {
			System.out.print("->" + ptr.digit);
			ptr = ptr.next;
		}
		System.out.println();
	}
	
	private static BigInteger reverse(BigInteger front) {
		DigitNode ptr = front.front;
		DigitNode prev = null;
		DigitNode curr = null;
		
		while(ptr != null) {
			curr = ptr;
			ptr = ptr.next;
			
			//reverse list
			curr.next = prev;
			prev = curr;
			front.front = curr;
		}
		return front;
	}
	
	/**
	 * Adds the first and second big integers, and returns the result in a NEW BigInteger object. 
	 * DOES NOT MODIFY the input big integers.
	 * 
	 * NOTE that either or both of the input big integers could be negative.
	 * (Which means this method can effectively subtract as well.)
	 * 
	 * @param first First big integer
	 * @param second Second big integer
	 * @return Result big integer
	 */
	public static BigInteger add(BigInteger first, BigInteger second) {
		// following line is a placeholder - compiler needs a return
		// modify it according to need
		
		//check if both lists are 0
		
		BigInteger one = new BigInteger();
		BigInteger two = new BigInteger();
		
		BigInteger result = new BigInteger();

		
		if(first.front == null && second.front == null) {
			return null;
		}
		
		/*
		 * Check which list is greater
		 * 
		 */
		
		boolean isNegative = false;
		
		DigitNode ptr = first.front;
		DigitNode ptr2 = second.front;
		
		BigInteger larger = new BigInteger();
		BigInteger smaller = new BigInteger();
		
		DigitNode largeptr = null;
		DigitNode smallptr = null;
		
		DigitNode temp = null;
		DigitNode prev = null;
		
		int carry = 0;
		int sum = 0;
		
		int num = 0;
			
			/*
			 * if first and second dont have the same number of digits
			 * then add a 0 to the back
			 */
			
		if(first.numDigits != second.numDigits) {
			one = first;
			two = second;
			
			DigitNode pointer = one.front;
			DigitNode pointer2 = two.front;
				
			while(pointer != null || pointer2 != null) {
				if(pointer.next == null && pointer2.next != null) {
					pointer.next = new DigitNode(0, null);
					one.numDigits++;
				}
				else if(pointer.next != null && pointer2.next == null) {
					pointer2.next = new DigitNode(0, null);
					two.numDigits++;
				}
				if(one.numDigits == two.numDigits) {
					break;
				}
				else {
					pointer = pointer.next;
					pointer2 = pointer2.next;
				}
			}
			
		}
		else { // same amount of digits
			one = first;
			two = second;
			
		}
		
		one = reverse(one);
		two = reverse(two);
		
		//traverse(one); // reverse and zero WORKS
		//traverse(two);
		
		ptr = one.front;
		ptr2 = two.front;
			
		if((first.negative == true && second.negative == false)
				|| (first.negative == false && second.negative == true)) { //check if theres a negative
			
			boolean largeFound = false;
			
			int count1 = 0;
			int count2 = 0;

			//check which number is larger for subtraction
			while(ptr.next != null && ptr2.next != null) {
				if(largeFound == true) {
					break;
				}
				
				else if(ptr.digit > ptr2.digit) { //first is larger
					largeFound = true;
					
					larger = one;
					smaller = two;
					
				}
				else if(ptr.digit < ptr2.digit) { //second is larger
					largeFound = true;
					
					larger = two;
					smaller = one;
				}
				else { //digitnodes are the same
					largeFound = false;
					
					ptr = ptr.next;
					count1 += 1;
					ptr2 = ptr2.next;
					count2 += 1;
				}
			}
			
			if(larger.negative) { //larger is negative
				isNegative = true;
			}
			else { //smaller is negative
				isNegative = false;
			}
			
			
			//reverse linked lists again
			
			larger = reverse(larger);
			smaller = reverse(smaller);
			
			largeptr = larger.front;
			smallptr = smaller.front;
			
			//traverse(larger);
			//traverse(smaller);
			
			
			//actually subtracting
			while(largeptr != null || smallptr != null) {
				
				if(largeptr.digit >= smallptr.digit) {
					sum = carry + (largeptr != null?largeptr.digit:0) - (smallptr != null?smallptr.digit:0);
					carry = 0;
				}
				else if(largeptr.digit < smallptr.digit) {
					num = largeptr.digit + 10;
					
					if(largeptr.next != null) {
						largeptr.next.digit--;
					}
					
					sum = carry + (num - (smallptr != null?smallptr.digit:0));
					carry = 0;
					
					if(sum >= 10) {
						carry = 1;
					}
				}
				sum = sum % 10; // takes the last digit
			
				temp = new DigitNode(sum, null); // creates new node
			
				//System.out.println(result);
			
				if(result.front == null) { // front of result linked list
					result.front = temp;
					result.numDigits++;
				}
				else {
					prev.next = temp; // connect temp node with rest of list
					result.numDigits++;
				}
				
				prev = temp; // current node becomes prev
			
				if(largeptr != null) {
					largeptr = largeptr.next;
				}
				if(smallptr != null) {
					smallptr = smallptr.next;
				}
			}
			
		}
		else if((first.negative == true && second.negative == true)
				|| (first.negative == false && second.negative == false)){ //both are positive or negative so just add
			one = reverse(one);
			two = reverse(two);
			
			ptr = one.front;
			ptr2 = two.front;
			
			if(one.negative == true && two.negative == true) { //if both are negative
				isNegative = true;
			}
			else { // both are positive
				isNegative = false;
			}
			
			while(ptr != null && ptr2 != null) {
				
				sum = carry + (ptr != null?ptr.digit:0) + (ptr2 != null?ptr2.digit:0);
				carry = 0;
				
				if(sum >= 10) {
					carry = 1;
				}
				
				sum = sum % 10; // takes the last digit
				
				temp = new DigitNode(sum, null); // creates new node
			
				//System.out.println(result);
			
				if(result.front == null) { // front of result linked list
					result.front = temp;
					result.numDigits++;
				}
				else {
					prev.next = temp; // connect temp node with rest of list
					result.numDigits++;
				}
				
				prev = temp; // current node becomes prev
			
				if(ptr != null) {
					ptr = ptr.next;
				}
				if(ptr2 != null) {
					ptr2 = ptr2.next;
				}
			}
			
		}
		
		if(carry > 0) {
			temp.next = new DigitNode(carry, null);
		}
		
		if(isNegative == true) {
			result.negative = true;
		}
		else {
			result.negative = false;
		}
		
		//if(first.front.digit == 0 && first.front.digit == 0) {
			//return result = new BigInteger();
		//}
		
		int zeroRemove = 0;
		
		for(DigitNode p = result.front; p != null; p = p.next) {
			if(p.digit == 0) {
				zeroRemove++;
			}
			else {
				zeroRemove = 0;
			}
		}
		
		DigitNode zeroptr = result.front;
		if(zeroRemove > 0) {
			for(int i = 0; i < result.numDigits - zeroRemove; i++) {
				if(i == result.numDigits - zeroRemove - 1) {
					zeroptr.next = null;
				}
				zeroptr = zeroptr.next;
			}
			result.numDigits -= zeroRemove;
		}
		return result;
	}
	
	/**
	 * Returns the BigInteger obtained by multiplying the first big integer
	 * with the second big integer
	 * 
	 * This method DOES NOT MODIFY either of the input big integers
	 * 
	 * @param first First big integer
	 * @param second Second big integer
	 * @return A new BigInteger which is the product of the first and second big integers
	 */
	public static BigInteger multiply(BigInteger first, BigInteger second) {
		
		BigInteger result = new BigInteger();
		
		DigitNode ptr = first.front;
		DigitNode ptr2 = second.front;
		
		DigitNode temp = null;
		DigitNode prev = null;
		
		int carry = 0;
		int mod = 0;
		int zeroCount = 0;
		
		BigInteger tempint = new BigInteger();
		
		boolean isNegative = false;
		
		DigitNode zeroptr = first.front;
		DigitNode zeroptr2 = second.front;
		
		while(zeroptr.next != null) {
			zeroptr = zeroptr.next;
		}
		while(zeroptr2.next != null) {
			zeroptr2 = zeroptr2.next;
		}
		
		if(zeroptr.digit == 0 || zeroptr2.digit == 0) {
			return result;
		}
		
		result.front = new DigitNode(0, null);
		
		if((first.negative == true && second.negative == true)
				|| (first.negative == false && second.negative == false)) {
			isNegative = false;
		}
		else {
			isNegative = true;
		}

		
		for(ptr2 = second.front; ptr2 != null; ptr2 = ptr2.next) {
			
			for(ptr = first.front; ptr != null; ptr = ptr.next) {
				
				int product =  carry + ((ptr != null?ptr.digit:0) * (ptr2 != null?ptr2.digit:0)); //multiply and add any carries
				carry = 0; // reset carry
				
				if(product >= 10) { // carry if product >= 10
					carry = product / 10;
				}
				mod = product % 10; // takes last digit
				temp = new DigitNode(mod, null); // creates new node

				if(tempint.front == null) {
					tempint.front = temp;
					tempint.numDigits++;
				}
				else {
					prev.next = temp; // connect temp node with rest of list
					tempint.numDigits++;
				}
				prev = temp; //current node becomes prev
				
				if(ptr.next == null && carry > 0) {
					temp.next = new DigitNode(carry, null);
					tempint.numDigits++;
					carry = 0;
				}
				//System.out.println(tempint);
			}
			zeroCount++;
			
			for(int i = 1; i < zeroCount; i++) {
				DigitNode zerotemp = new DigitNode(0, null);
				zerotemp.next = tempint.front;
				tempint.front = zerotemp;
				tempint.numDigits++;
			}
			result = add(tempint, result);
			
			tempint.front = null; // reset
		}
		if(carry > 0) {
			prev.next = new DigitNode(carry, null);
			result.numDigits++;
			carry = 0;
		}
		
		if(isNegative == true) {
			result.negative = true;
		}
		else {
			result.negative = false;
		}
		
		//System.out.println(result.numDigits);
		
		return result;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		if (front == null) {
			return "0";
		}
		String retval = front.digit + "";
		for (DigitNode curr = front.next; curr != null; curr = curr.next) {
				retval = curr.digit + retval;
		}
		
		if (negative) {
			retval = '-' + retval;
		}
		return retval;
	}
	
}
