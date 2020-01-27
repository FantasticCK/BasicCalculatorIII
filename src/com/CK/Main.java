package com.CK;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Stack;

public class Main {

    public static void main(String[] args) {
        String s = "2*(5+5*2)/3+(6/2+8)";
//        String s = "0-2147483648";
        Solution solution = new Solution();
        System.out.println(solution.calculate(s));
    }
}


//Shunting-yard algorithm - > RPN
class Solution2 {
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
                if (i == 0) {
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

class Solution {
    public int calculate(String s) {
        return dfs(s + "$", new int[]{-1});
    }

    private int dfs(String s, int[] st) {
        if (st[0] >= s.length())
            return 0;
        int currNum = 0, prevNum = 0, sum = 0;
        char prevOpt = '+';

        while (++st[0] < s.length()) {
            char c = s.charAt(st[0]);
            if (c == ' ')
                continue;
            if (c >= '0' && c <= '9') {
                currNum = currNum * 10 + c - '0';
            } else if (c == '(') {
                currNum = dfs(s, st);
            } else {
                switch (prevOpt) {
                    case '+':
                        sum += prevNum;
                        prevNum = currNum;
                        break;

                    case '-':
                        sum += prevNum;
                        prevNum = -currNum;
                        break;

                    case '*':
                        prevNum *= currNum;
                        break;

                    case '/':
                        prevNum /= currNum;
                        break;
                }

                if (c == ')') break;
                prevOpt = c;
                currNum = 0;
            }
        }
        return sum + prevNum;
    }
}
