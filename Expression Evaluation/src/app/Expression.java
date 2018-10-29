package app;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import structures.Stack;

public class Expression {

	public static String delims = " \t*+-/()[]";
			
    /**
     * Populates the vars list with simple variables, and arrays lists with arrays
     * in the expression. For every variable (simple or array), a SINGLE instance is created 
     * and stored, even if it appears more than once in the expression.
     * At this time, values for all variables and all array items are set to
     * zero - they will be loaded from a file in the loadVariableValues method.
     * 
     * @param expr The expression
     * @param vars The variables array list - already created by the caller
     * @param arrays The arrays array list - already created by the caller
     */
    public static void 
    makeVariableLists(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	/** COMPLETE THIS METHOD **/
    	/** DO NOT create new vars and arrays - they are already created before being sent in
    	 ** to this method - you just need to fill them in.
    	 **/
    	 ArrayList<String> tokenized = new ArrayList<String>();
         
         ArrayList<Variable> list2 = new ArrayList<Variable>();
         
         String expr2 = expr.replaceAll("\\s", "");
         
         StringTokenizer expression = new StringTokenizer(expr2);
         
         while(expression.hasMoreTokens()) {
        	 //check if it is an array or variable
             String variable = expression.nextToken(delims);
             //tokenized.add(variable);
             
             String ch = variable;
             char c = ch.charAt(0);
             
             if(isNumeric(c)) {
                 continue;
             }
             else {
                 Variable var = new Variable(variable);
                 vars.add(var);
             }
             System.out.println("Variables: " + vars);
         }
             
         for(int i = 0; i < tokenized.size() - 1; i ++) {
             int index = expr.indexOf(tokenized.get(i));
                 
             //System.out.println("tokenized.get(i): " + tokenized.get(i));
             //System.out.println("Index: " + index);
                 
             if(expr.charAt(index + tokenized.get(i).length()) == '['){
                     
                 //System.out.println("tokenized.get(i).length()" + tokenized.get(i).length());

                 Array a = new Array(tokenized.get(i));

                 if(!(arrays.contains(a))) {
                     arrays.add(a);
                         
                     Variable b = new Variable(tokenized.get(i));
                     list2.add(b);
                 }
             }
         }
         vars.removeAll(list2);
         
         for(int i = 0; i < vars.size(); i++) {
             for(int j = i + 1; j < vars.size(); j++) {
                 if(vars.get(i).equals(vars.get(j))) {
                     vars.remove(j);
                     j--;
                 }
             }
         }
         
    }
    
    
    /**
     * Loads values for variables and arrays in the expression
     * 
     * @param sc Scanner for values input
     * @throws IOException If there is a problem with the input 
     * @param vars The variables array list, previously populated by makeVariableLists
     * @param arrays The arrays array list - previously populated by makeVariableLists
     */
    public static void 
    loadVariableValues(Scanner sc, ArrayList<Variable> vars, ArrayList<Array> arrays) 
    throws IOException {
        while (sc.hasNextLine()) {
            StringTokenizer st = new StringTokenizer(sc.nextLine().trim());
            int numTokens = st.countTokens();
            String tok = st.nextToken();
            Variable var = new Variable(tok);
            Array arr = new Array(tok);
            int vari = vars.indexOf(var);
            int arri = arrays.indexOf(arr);
            if (vari == -1 && arri == -1) {
            	continue;
            }
            int num = Integer.parseInt(st.nextToken());
            if (numTokens == 2) { // scalar symbol
                vars.get(vari).value = num;
            } else { // array symbol
            	arr = arrays.get(arri);
            	arr.values = new int[num];
                // following are (index,val) pairs
                while (st.hasMoreTokens()) {
                    tok = st.nextToken();
                    StringTokenizer stt = new StringTokenizer(tok," (,)");
                    int index = Integer.parseInt(stt.nextToken());
                    int val = Integer.parseInt(stt.nextToken());
                    arr.values[index] = val;              
                }
            }
        }
    }
    
    private static boolean isNumeric(char num) {
        return num >= '0' && num <= '9';
    }
    
    private static boolean isOperand(char a) {
        return(a >= 'a' && a <= 'z' || a >= 'A' && a <= 'Z');
    }
    
    private static boolean isOperator(char b) {
        return b == '+' || b == '-' || b == '*' || b == '/';
    }
    
    private static boolean isOperator2(char c) {
        return c == '+' || c == '*' || c == '/';
    }
    
    private static int precedence(char operator) {
    	int rank = 0;
    	
    	if(operator == '+') {
    		rank = 1;
    	}
    	else if(operator == '-') {
    		rank = 1;
    	}
    	else if(operator == '*') {
    		rank = 2;
    	}
    	else if(operator == '/') {
    		rank = 2;
    	}
		return rank;
    }
    
    private static float performOperation(char operation, float op1, float op2) {
        if(operation == '+') {
            return op1 + op2;
        }
        else if(operation == '-') {
            return op1 - op2;
        }
        else if(operation == '*') {
            return op1 * op2;
        }
        else if(operation == '/') {
            return op1 / op2;
        }
        return -1;
    }
    
    // reverse the stack to preserve order
    private static void reverseStack(Stack origin) {
    	Stack reversed = new Stack();
    	while(origin.isEmpty() != true) {
    		Object item = origin.peek(); // Object type can be referenced to any other type
    		reversed.push(item);
    		origin.pop();
    	}
    }
    
    /**
     * Evaluates the expression.
     * 
     * @param vars The variables array list, with values for all variables in the expression. 
     * @param arrays The arrays array list, with values for all array items
     * @return Result of evaluation
     */
    public static float evaluate(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	/** COMPLETE THIS METHOD **/
    	// following line just a placeholder for compilation
    	
    	// vars and arrays are variables so when you see a letter,
    	// you will get the values for the variables
    	
    	//example expression: 3 + 2 * (4 - 5)
    	
    	//first make two stacks, numbers and operators
    	
    	Stack<Float> numbers = new Stack<Float>();
    	Stack<Character> operators = new Stack<Character>();
    	
    	Stack<Float> reverseNum = new Stack<Float>();
    	Stack<Character> reverseOp = new Stack<Character>();
    	
    	float op1;
		float op2;
		char operator;
    	
    	float result = 0;
    	
    	float evalRes;
    	
    	String expr2 = expr.replaceAll("\\s", "");
    	
    	String str;
    	float num;
    	
    	StringBuilder vchars = new StringBuilder(); // get the multiple characters of variable
		StringBuilder newexpress = new StringBuilder(); // the final expression with everything
    	
		
		
    	// if it contains variables then convert variables into numbers
		
		if(vars.isEmpty() != true) {
    		for(int i = 0; i < expr2.length(); i++) {
    			char c = expr2.charAt(i);
    			
    			if(isOperand(c)) {
        			Variable var;
        			int vint = 0;
        			
      				
      				for(int a = i; a < expr2.length(); a++, i++) {
      					char vc = expr2.charAt(a);
      					
        				if(isOperator(vc) || vc == ')') {
        					break;
        				}
        				else {
        					if(a == expr2.length() - 1) {
        						vchars.append(vc);
          					}
        					else {
        						vchars.append(vc);
        					}
        				}
        			}
        			str = vchars.toString();
        			vchars = new StringBuilder(); // reset multiple digits tracker
        			i--;
        			
      				for(int v = 0; v < vars.size(); v++) {
						var = vars.get(v);
						if(var.name.equals(str)) {
							vint = var.value;
							newexpress.append(vint);
							
						}
      				}
    			}
    			else {
    				newexpress.append(c);
    			}
    		}
    		expr2 = newexpress.toString();
    		System.out.println("newexpress: " + expr2);
    	}

    	
    	
    	//evaluating the numbers
    	
    	for(int i = 0; i < expr2.length(); i++) {
    		char c = expr2.charAt(i);
    			
    		if(isNumeric(c)) {
    			StringBuilder sb = new StringBuilder();
    			
    			// multiple digits
    			
    			for(int a = i; a < expr2.length(); a++, i++) {
    				if(isOperator(expr2.charAt(a)) || expr2.charAt(a) == ')') {
    					break;
    				}
    				else {
    					sb.append(expr2.charAt(a));
    				}
    			}
    			
    			str = sb.toString();
    			num = Float.parseFloat(str);
    			
    			numbers.push(num);
    			i--;
    			//reset stringbuilder
    			sb = new StringBuilder();
    		}
    		else if(isOperator(c)) {
    			
    			if(i != 0) {
    				operators.push(c);
    			}
    			
    			if(c == '+' || c == '-') {
    				
    				if(i == 0 && c == '-') { //if theres a negative in the first index
    					c = expr2.charAt(i);
        				
        				StringBuilder sb = new StringBuilder();
    	    			
    	    			// multiple digits
    	    			
    	    			for(int a = i; a < expr2.length(); a++, i++) {
    	    				if(isOperator2(expr2.charAt(a)) || expr2.charAt(a) == ')') {
    	    					break;
    	    				}
    	    				else {
    	    					sb.append(expr2.charAt(a));
    	    				}
    	    			}
    	    			
    	    			str = sb.toString();
    	    			num = Float.parseFloat(str);
    	    			
    	    			numbers.push(num);
    	    			i--;
    	    			//reset stringbuilder
    	    			sb = new StringBuilder();
        			}
    				else {
    				int d = i;
        			d++;
        			char temp = expr2.charAt(d);
        			
        			if(temp == '-') { //handle negatives
        				i++;
        				c = expr2.charAt(i);
        				
        				StringBuilder sb = new StringBuilder();
    	    			
    	    			// multiple digits
    	    			
    	    			for(int a = i; a < expr2.length(); a++, i++) {
    	    				if(isOperator2(expr2.charAt(a)) || expr2.charAt(a) == ')') {
    	    					break;
    	    				}
    	    				else {
    	    					sb.append(expr2.charAt(a));
    	    				}
    	    			}
    	    			
    	    			str = sb.toString();
    	    			num = Float.parseFloat(str);
    	    			
    	    			numbers.push(num);
    	    			i--;
    	    			//reset stringbuilder
    	    			sb = new StringBuilder();
        			}
        			}
    			}
    			
    			else if(c == '*' || c == '/') {
    				i++;
    				
    				c = expr2.charAt(i);
    				
    				if(c == '-') {
    					StringBuilder sb = new StringBuilder();
    	    			
    	    			// multiple digits
    	    			
    	    			for(int a = i; a < expr2.length(); a++, i++) {
    	    				if(isOperator2(expr2.charAt(a)) || expr2.charAt(a) == ')') {
    	    					break;
    	    				}
    	    				else {
    	    					sb.append(expr2.charAt(a));
    	    				}
    	    			}
    	    			
    	    			str = sb.toString();
    	    			num = Float.parseFloat(str);
    	    			
    	    			numbers.push(num);
    	    			i--;
    	    			//reset stringbuilder
    	    			sb = new StringBuilder();
    	    			
    	    			operator = operators.pop();
    	    			op1 = numbers.pop();
    	    			op2 = numbers.pop();
    	    			numbers.push(performOperation(operator, op2, op1));
    					
    				}
    				
    				else if(isNumeric(c)) {
    					
    					StringBuilder sb = new StringBuilder();
    	    			
    	    			// multiple digits
    	    			
    	    			for(int a = i; a < expr2.length(); a++, i++) {
    	    				if(isOperator(expr2.charAt(a)) || expr2.charAt(a) == ')') {
    	    					break;
    	    				}
    	    				else {
    	    					sb.append(expr2.charAt(a));
    	    				}
    	    			}
    	    			
    	    			str = sb.toString();
    	    			num = Float.parseFloat(str);
    	    			
    	    			numbers.push(num);
    	    			i--;
    	    			
    	    			sb = new StringBuilder(); // reset stringbuilder
    					
    					//numbers.push((float)c - '0');
    					
    					operator = operators.pop();
    	    			op1 = numbers.pop();
    	    			op2 = numbers.pop();
    	    			numbers.push(performOperation(operator, op2, op1));
    				}
    			}
    		}
    		
    		else if(c == ')') { // if an open bracket
    			/**
    			if(numbers.size() == 1) {
    				break;
    			}
    			**/
    			int closeIndex = i;
    			int openIndex = 0;
    			// find the deepest pair of parenthesis open
    			// traverse the string backwards
    			for(int j = i; j >= 0; j--) {
    				char d = expr2.charAt(j);
    				if(d == '(') {
    					openIndex = j;
    					break;
    				}
    			}
    			
    			while(numbers.isEmpty() != true) {
    				numbers.pop();
    			}
    			while(operators.isEmpty() != true) {
    				operators.pop();
    			}
    			
    			evalRes = evaluate(expr2.substring(openIndex + 1, closeIndex), vars, arrays);
    			//numbers.push(evalRes);
    			
    			/**
    			int d = i;
    			d++;
    			if(d == expr2.length()) { // if i++ equals the string length then return the result
    				return evalRes;
    			}
    			**/
    			
    			String newString = expr2.substring(0, openIndex) + evalRes + expr2.substring(closeIndex + 1, expr2.length());
    			
    			expr2 = newString;
    			
    			System.out.println("new string: " + newString);
    			
    			i = -1;
    			
    			if(numbers.size() == 1 && operators.isEmpty()) { // if there is only one item in the stack
    	    		result = numbers.pop();
    	    		break;
    			}
    			
    			while(numbers.isEmpty() != true) {
    				numbers.pop();
    			}
    			while(operators.isEmpty() != true) {
    				operators.pop();
    			
    				
    			}
    		}
    	}
    	
    	//reverse the stacks
    	while(numbers.isEmpty() == false) {
    		reverseNum.push(numbers.pop());
    	}
    	while(operators.isEmpty() == false) {
    		reverseOp.push(operators.pop());
    	}
    	
    	//evaluate everything
    	if(reverseNum.size() == 1) { // if there is only one item in the stack
    		result = reverseNum.pop();
    	}
    	
    	// fix: not performing order of operations
    	
    	else { // there is more than 1 in the numbers stack
    		while(reverseNum.isEmpty() == false || reverseOp.isEmpty() == false) {
    			
    			op1 = reverseNum.pop();
    			op2 = reverseNum.pop();
    			operator = reverseOp.pop();
    			
    			result = performOperation(operator, op1, op2);
    			
    			reverseNum.push(result);
    			
    			if(reverseNum.size() == 1) {
    				result = reverseNum.pop();
    			}
    		}
    	}
    	return result;
    }
}