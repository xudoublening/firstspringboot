package com.example.firstspringboot.dto;

import java.util.ArrayList;
import java.util.List;

public class Stack {
    private int top ;//栈指针
    private List<Object> stacks;

    public Stack(int stackLent) {
        stacks = new ArrayList<>(stackLent);
        top = 0;
    }
    public Boolean push(Object o){
        if (top == stacks.size()){
            return false;
        }
        top++;
        stacks.add(o);
        return true;
    }
    public Object pop(){
        if (top == 0){
            return -1;
        }
        top--;
        Object ob = stacks.get(top);
        stacks.remove(top);
        return ob;
    }

    public int getTop() {
        return top;
    }
}
