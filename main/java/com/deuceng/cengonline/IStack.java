package com.deuceng.cengonline;

public interface IStack<T> {

    void push(T data);
    T pop();
    T peek();
    boolean isEmpty();
    int size();
    void clear();
}
