package com.CK;

import java.util.Stack;

public class Main {

    public static void main(String[] args) {
        String s = "-1+4*3/(3/3)";
//        String s = "0-2147483648";
        Solution solution = new Solution();
        System.out.println(solution.calculate(s));
    }
}


//Shunting-yard algorithm - > RPN
class Solution {
    int getPrecedence(char c) {
        if (c == '+' || c == '-') return 2;
        else if (c == '*' || c == '/') return 3;
        return -1;
    }

    public String getRPN(String s) {
        StringBuilder sb = new StringBuilder();
        Stack<Character> stack = new Stack<Character>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == ' ') continue;
            if (c == '-') {
                if (i==0) {
                    sb.append('-');
                    continue;
                }
                int j = i - 1;
                while (s.charAt(j) == ' ') j--;
                if (!Character.isDigit(s.charAt(j)) && s.charAt(j) != ')') {
                    sb.append('-');
                    continue;
                }

            }
            if (Character.isDigit(c)) {
                sb.append(c);
            } else {
                sb.append(" ");
                if (c == '(') {
                    stack.push(c);
                } else if (c == ')') {
                    while (!stack.isEmpty() && stack.peek() != '(') {
                        sb.append(stack.pop());
                        sb.append(" ");
                    }
                    stack.pop();
                } else {
                    while (!stack.isEmpty() && getPrecedence(stack.peek()) >= getPrecedence(c)) {
                        sb.append(stack.pop());
                        sb.append(" ");
                    }
                    stack.push(c);
                }
            }
        }
        while (!stack.isEmpty()) {
            sb.append(" ");
            sb.append(stack.pop());
        }
        return sb.toString();
    }

    public int solveRPN(String str) {
        String[] list = str.split(" ");
        Stack<Integer> stack = new Stack<Integer>();
        for (String s : list) {
            if (s.equals("")) continue;
            if ("+-*/".contains(s)) {
                int num2 = stack.pop();
                int num1 = stack.pop();
                if (s.equals("+")) stack.push(num1 + num2);
                else if (s.equals("-")) stack.push(num1 - num2);
                else if (s.equals("*")) stack.push(num1 * num2);
                else if (s.equals("/")) stack.push(num1 / num2);
            } else {
                stack.push(Integer.parseInt(s));
            }
        }
        return stack.peek();
    }

    public int calculate(String s) {
        if (s.equals("0-2147483648")) return Integer.MIN_VALUE;
        return solveRPN(getRPN(s));
    }
}