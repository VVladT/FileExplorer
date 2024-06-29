package pe.edu.utp.aed.fileexplorer.util.datastructures;

import java.util.ArrayList;
import java.util.List;

public class HashTable<K, V> {
    private static final int DEFAULT_CAPACITY = 17;
    private final SinglyLinkedList<Entry<K, V>>[] table;
    private int size;

    public HashTable() {
        table = new SinglyLinkedList[DEFAULT_CAPACITY];
        init();
        size = 0;
    }

    public HashTable(int capacity) {
        table = new SinglyLinkedList[capacity];
        init();
        size = 0;
    }

    private void init() {
        for (int i = 0; i < table.length; i++) {
            table[i] = new SinglyLinkedList<>();
        }
    }

    private int hash(K key) {
        int hashCode;

        if (key instanceof String) {
            hashCode = hashString((String) key);
        } else {
            hashCode = key.hashCode();
        }

        return Math.abs(hashCode) % DEFAULT_CAPACITY;
    }

    private int hashString(String key) {
        int hash = 0;

        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            hash = 31 * hash + c;
        }

        return hash;
    }

    public void put(K key, V value) {
        int index = hash(key);
        SinglyLinkedList<Entry<K, V>> entries = table[index];
        for (Entry<K, V> entry : entries) {
            if (entry.key.equals(key)) {
                entry.value = value;
                return;
            }
        }
        entries.add(new Entry<>(key, value));
        size++;
    }

    public V get(K key) {
        int index = hash(key);
        SinglyLinkedList<Entry<K, V>> entries = table[index];
        for (Entry<K, V> entry : entries) {
            if (entry.key.equals(key)) {
                return entry.value;
            }
        }
        return null;
    }

    public void remove(K key) {
        int index = hash(key);
        SinglyLinkedList<Entry<K, V>> entries = table[index];
        for (Entry<K, V> entry : entries) {
            if (entry.key.equals(key)) {
                entries.remove(entry);
                size--;
                return;
            }
        }
    }

    public boolean containsKey(K key) {
        int index = hash(key);
        SinglyLinkedList<Entry<K, V>> entries = table[index];
        for (Entry<K, V> entry : entries) {
            if (entry.key.equals(key)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsValue(V value) {
        for (SinglyLinkedList<Entry<K, V>> entries : table) {
            for (Entry<K, V> entry : entries) {
                if (entry.value.equals(value)) {
                    return true;
                }
            }
        }
        return false;
    }

    public int size() {
        return size;
    }

    private static class Entry<K, V> {
        private K key;
        private V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    public List<V> getValues() {
        List<V> values = new ArrayList<>();

        for (SinglyLinkedList<Entry<K, V>> entries : table) {
            for (Entry<K, V> entry : entries) {
                if (entry.value != null) {
                    values.add(entry.value);
                }
            }
        }

        return values;
    }
}
