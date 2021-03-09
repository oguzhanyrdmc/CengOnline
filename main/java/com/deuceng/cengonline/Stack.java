package com.deuceng.cengonline;

import java.util.ArrayList;

public class Stack<T> implements  IStack<T> {
    private int top;
    private ArrayList<T> elements;

    public Stack() {
        elements = new ArrayList<T>();
        top = -1;
    }
    public void push(T data) {
        top++;
        elements.add(data);

    }
    public T pop() {
        if (isEmpty()) {
            System.out.println("Stack is empty.");
            return null;
        }
        else {
            T retData = elements.get(top);
            top--;
            return retData;
        }
    }
    public T peek() {
        if (isEmpty()) {
            System.out.println("Stack is empty");
            return null;
        }
        else
            return elements.get(top);
    }
    public boolean isEmpty() {
        return (top == -1);
    }
    public int size() {
        return top+1;
    }
    public void clear(){
        while(!isEmpty()){
            top = -1;
            elements.clear();
        }
    }
}
