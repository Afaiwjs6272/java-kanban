package service;

import model.Task;

public class Node {
    Node next;
    Task data;
    Node prev;


    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public Node getPrev() {
        return prev;
    }

    public void setPrev(Node prev) {
        this.prev = prev;
    }

    public Task getData() {
        return data;
    }

    public void setData(Task data){
        this.data = data;
    }
}