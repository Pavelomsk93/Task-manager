package tracker.controllers;

import tracker.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {


    private final Map<Integer, Node> historyTable = new HashMap<>();
    private Node head = null;
    private Node tail = null;

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void add(Task task) {
        if (historyTable.containsKey(task.getId())) {
            removeNode(historyTable.get(task.getId()));
        }
        linkLast(task);
    }

    @Override
    public void remove(int id) {
        if(historyTable.containsKey(id)){
            removeNode(historyTable.get(id));
        }
    }

    public static class Node {
        public Task data;
        public Node next;
        public Node prev;

        public Node(Node prev, Task data, Node next) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }

    public void linkLast(Task task) {

        final Node newNode = new Node(tail, task, null);

        if (tail==null) {
            head = newNode;
            tail = newNode;
        } else {
            tail = newNode;
            tail.prev.next = newNode;
        }
        historyTable.put(newNode.data.getId(), newNode);
    }

    public List<Task> getTasks() {
        List<Task> browsingHistory = new ArrayList<>();
        Node node = head;
        while (node != null) {
            browsingHistory.add(node.data);
            node = node.next;
        }
        return browsingHistory;
    }

    public void removeNode(Node node) {
        if(node.next==null&&node.prev==null){
            head=null;
            tail=null;
        }
        if (node.next == null&&node.prev!=null) {
            node.prev.next = null;
            tail = node.prev;
        }
        if (node.prev == null&&node.next!=null) {
            node.next.prev = null;
            head = node.next;
        }
        if (node.prev != null & node.next != null) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
    }
}
