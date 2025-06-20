package org.example;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

@SpringBootApplication
//@EnableConfigServer
public class Application {
    public static void main(String[] args) throws InterruptedException {
//        SpringApplication.run(Application.class, args);

//        LRUCache lruCache = new LRUCache(2);
//        lruCache.put(1,1);
//        lruCache.put(2,2);
//        lruCache.get(1);
//        lruCache.put(3,3);


//        RandomizedSet randomizedSet = new RandomizedSet();
//        randomizedSet.insert(1);
//        randomizedSet.remove(2);
//        randomizedSet.insert(2);
//        randomizedSet.getRandom();
//        randomizedSet.remove(1);
//        randomizedSet.insert(2);
//        randomizedSet.getRandom();



//        Thread t = new Thread(() -> {
//            System.out.println("Child thread running...");
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {}
//            System.out.println("Child thread done.");
//        });
//
//        t.start();
//
//        Thread.sleep(2000);
//        System.out.println("Main thread waiting...");
//        t.join(); // ✅ 当前线程（main）等 t 结束
//        System.out.println("Main thread continues.");
        new Application().outer();


}


    private final ReentrantLock lock = new ReentrantLock();

    public void outer() throws InterruptedException {

        Runnable t = ()->{
            lock.lock();
            System.out.println("t acquired");
        };
        new Thread(t).start();
        new Thread(t).start();


//        lock.lock();
//        try {
//            System.out.println("outer acquired");
//            Thread.sleep(2000);
//            inner();
//        } finally {
////            lock.unlock();
//        }
    }

    public void inner() {
        lock.lock();
        try {
            System.out.println("inner acquired");
        } finally {
//            lock.unlock();
        }
    }
}



class Node {
    int key;
    int value;
    Node pre;
    Node next;
    Node(int key, int value){
        this.key = key;
        this.value = value;
    }
    public int getKey(){
        return this.key;
    }
    public int getValue(){
        return this.value;
    }
}

class LRUCache {
    Node oldest = new Node(0, 0);
    Node newest = new Node(0, 0);
    Map<Integer, Node> items = new HashMap();
    int capacity = 0;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.oldest.next = newest;
        this.newest.pre = oldest;
    }

    public int get(int key) {
        Node node = items.get(key);
        if (node != null){
            remove(node);
            insert(node);
            return node.getValue();
        } else {
            return -1;
        }
    }

    public void put(int key, int value) {
        Node node = items.get(key);
        if (node != null){
            remove(node);
            insert(node);
        } else {
            insert(new Node(key, value));
            if(capacity < items.size()) {
                remove(this.oldest.next);
            }
        }
    }

    private void insert(Node node){
        node.pre = newest.pre;
        node.next = this.newest;
        this.newest.pre.next = node;
        this.newest.pre = node;
        items.put(node.getKey(), node);
    }
    private void remove(Node node){
        node.pre.next = node.next;
        node.next.pre = node.pre;
        items.remove(node.getKey());
    }
}

/**
 * Your LRUCache object will be instantiated and called as such:
 * LRUCache obj = new LRUCache(capacity);
 * int param_1 = obj.get(key);
 * obj.put(key,value);
 */




class RandomizedSet {
    List<Integer> list = new ArrayList();
    Map<Integer, Integer> numberIndexes = new HashMap();
    Random r = new Random();

    public RandomizedSet() {

    }

    public boolean insert(int val) {
        if(numberIndexes.containsKey(val)){
            return false;
        }else {
            list.add(val);
            numberIndexes.put(val, list.size() - 1);
            return true;
        }
    }

    public boolean remove(int val) {
        Integer index = numberIndexes.get(val);
        if(index == null){
            return false;
        } else {
            int lastNumber = numberIndexes.get(numberIndexes.size() - 1);
            list.set(index, lastNumber);
            list.remove(numberIndexes.size() - 1);
            numberIndexes.put(lastNumber, index);
            numberIndexes.remove(val);
            return true;
        }
    }

    public int getRandom() {
        return list.get(r.nextInt(numberIndexes.size()));
    }
}