package de.comparus.opensource.longmap;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Objects;

public class LongMapImpl<V> implements LongMap<V> {
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    static final int MAXIMUM_CAPACITY = 1 << 30;

    final float loadFactor;
    private Node<V>[] table;
    private int size;

    public LongMapImpl(int initCapacity, float loadFactor) {
        this.loadFactor = loadFactor;
        if (initCapacity <= 0)
            throw new IllegalArgumentException("Initial capacity must be a positive number");
        if (initCapacity > MAXIMUM_CAPACITY)
            initCapacity = MAXIMUM_CAPACITY;
        if (loadFactor <= 0)
            throw new IllegalArgumentException("Load factor must be > then 0");
        this.table = new Node[initCapacity];
    }

    public LongMapImpl() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }


    public V put(long key, V value) {
        checkForResize();
        return putVal(key, value, table, true);
    }

    V putVal(long key, V value, Node<V>[] table, boolean increaseSize) {
        int index = calculateIndex(key, table.length);
        Node<V> newNode = new Node<>(key, value);
        Node<V> current = table[index];
        if (current == null) {
            table[index] = newNode;
        } else {
            while (current.next != null) {
                if (current.key == key) {
                    V prevValue = current.value;
                    current.value = value;
                    return prevValue;
                }
                current = current.next;
            }
            if (current.key == key) {
                V prevValue = current.value;
                current.value = value;
                return prevValue;
            }
            current.next = newNode;
        }
        if (increaseSize) {
            size++;
        }
        return value;
    }


    public V get(long key) {
        int index = calculateIndex(key, table.length);
        Node<V> current = table[index];
        while (current != null) {
            if (current.key == key) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    public V remove(long key) {
        int index = calculateIndex(key, table.length);
        Node<V> current = table[index];
        if (current != null) {
            if (current.key == key) {
                table[index] = current.next;
                size--;
                return current.value;
            }
            while (current.next != null) {
                if (current.next.key == key) {
                    V value = current.next.value;
                    current.next = current.next.next;
                    size--;
                    return value;
                }
                current = current.next;
            }
        }
        return null;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean containsKey(long key) {
        for (Node<V> node : table) {
            Node<V> current = node;
            while (current != null) {
                if (current.key == key) {
                    return true;
                }
                current = current.next;
            }
        }
        return false;
    }

    public boolean containsValue(V value) {
        for (Node<V> node : table) {
            Node<V> current = node;
            while (current != null) {
                if (current.value.equals(value)) {
                    return true;
                }
                current = current.next;
            }
        }
        return false;
    }

    public long[] keys() {
        long[] keys = new long[size];
        int index = 0;
        for (Node<V> node : table) {
            if (node != null) {
                keys[index++] = node.key;
            }
        }
        return keys;
    }

    public V[] values() {
        Object[] values = new Object[size];
        int index = 0;
        for (Node<V> node : table) {
            if (node != null) {
                values[index++] = node.value;
            }
        }
        V[] castedValues = (V[]) Array.newInstance(values[0].getClass(), size);
        for (int i = 0; i < values.length; i++) {
            castedValues[i] = (V) values[i];
        }
        return castedValues;
    }

    public long size() {
        return size;
    }

    public void clear() {
        size = 0;
        Arrays.fill(table, null);
    }

    public static int calculateIndex(Long key, int tableCapacity) {
        return Objects.hashCode(key) & (tableCapacity - 1);
    }

    private void checkForResize() {
        if (size / (float) table.length > loadFactor) {
            System.out.println("resize value " + (table.length << 1) + "");
            resize(table.length << 1);
        }
    }

    private void resize(int newSize) {
        int oldCap = (table == null) ? 0 : table.length;
        if (oldCap >= MAXIMUM_CAPACITY) {
            return;
        }
        Node<V>[] newTable = new Node[newSize];
        for (Node<V> head : table) {
            Node<V> current = head;
            if (current != null) {
                while (current.next != null) {
                    putVal(current.key, current.value, newTable, false);
                    current = current.next;
                }
                putVal(current.key, current.value, newTable, false);
            }

        }
        table = newTable;
    }

    static class Node<V> {
        long key;
        V value;
        Node<V> next;

        public Node(Long key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
