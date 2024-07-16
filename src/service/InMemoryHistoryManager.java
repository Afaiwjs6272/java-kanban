package service;

import model.Task;
import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private final DoublyLinkedList tasksHistory = new DoublyLinkedList();

    @Override
    public void add(Task task) {
      tasksHistory.linkLast(task);
    }

    @Override
    public void remove(int id) {
        Node node = tasksHistory.history.get(id);
        if (node != null) {
            tasksHistory.removeNode(node);
            tasksHistory.history.remove(id);
        }
    }

    @Override
    public List<Task> getHistory() {
        return tasksHistory.getTasks();
    }

    public class DoublyLinkedList {

        private final Map<Integer, Node> history = new HashMap<>();
        public Node head;
        public Node tail;

        public void removeNode(Node node) {
            Node prev = node.getPrev();
            Node next = node.getNext();
            if (prev == null && next == null) {
                head = null;
                tail = null;
            } else if (prev == null) {
                next.setPrev(null);
                head = next;
            } else if (next == null) {
                prev.setNext(null);
                tail = prev;
            } else {
                prev.setNext(next);
                next.setPrev(prev);
            }
        }

        public List<Task> getTasks() {
            List<Task> tasks = new ArrayList<>();
            Node task = head;
            while (task != null) {
                tasks.add(task.getData());
                task = task.getNext();
            }
            return tasks;
        }

        public void linkLast(Task task) {
            Node node = new Node();
            node.setData(task);
            if (history.containsKey(task.getId())) {
                removeNode(history.get(task.getId()));
            } else if (head == null) {
                tail = node;
                head = node;
                node.setNext(null);
                node.setPrev(null);
            } else {
                node.setPrev(tail);
                node.setNext(null);
                tail.setNext(node);
                tail = node;
            }
            history.put(task.getId(),node);
        }
    }
}

