package pe.edu.utp.aed.fileexplorer.util.datastructures;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Implementación de una lista enlazada genérica.
 * @param <T> Tipo de los elementos de la lista.
 */
public class SinglyLinkedList<T> implements Iterable<T> {

    private Node head;
    private Node end;
    private int numberOfNodes;

    /**
     * Constructor para crear una lista enlazada vacía.
     */
    public SinglyLinkedList() {
        head = null;
        end = null;
        numberOfNodes = 0;
    }

    /**
     * Constructor para crear una lista enlazada con un primer elemento.
     * @param data El dato del primer elemento.
     */
    public SinglyLinkedList(T data) {
        this.head = new Node(data);
        end = head;
        numberOfNodes = 1;
    }

    /**
     * Agrega un elemento al final de la lista.
     * @param data El dato a agregar.
     */
    public void add(T data) {
        Node newNode = new Node(data);
        if (isEmpty()) {
            head = newNode;
            end = newNode;
        } else {
            end.setNext(newNode);
            end = newNode;
        }
        numberOfNodes++;
    }

    /**
     * Agrega los elementos de otra lista al final de esta lista.
     * @param dataLL La lista cuyos elementos se agregarán.
     */
    public void add(SinglyLinkedList<T> dataLL) {
        for (T data : dataLL) {
            add(data);
        }
    }

    /**
     * Agrega un elemento en una posición específica de la lista.
     * @param index La posición donde agregar el elemento.
     * @param data El dato a agregar.
     * @throws IndexOutOfBoundsException Si el índice está fuera de rango.
     */
    public void add(int index, T data) {
        if (index < 0 || index > size()) {
            throw new IndexOutOfBoundsException("Index is out of range.");
        }

        if (index == 0) {
            addFirst(data);
        } else if (index == size()) {
            addLast(data);
        } else {
            Node newNode = new Node(data);
            Node previous = getNode(index - 1);

            newNode.setNext(previous.getNext());
            previous.setNext(newNode);
            numberOfNodes++;
        }
    }

    /**
     * Agrega un elemento al principio de la lista.
     * @param data El dato a agregar.
     */
    public void addFirst(T data) {
        Node newNode = new Node(data);
        if (isEmpty()) {
            head = newNode;
            end = newNode;
        } else {
            newNode.setNext(head);
            head = newNode;
        }
        numberOfNodes++;
    }

    /**
     * Agrega un elemento al final de la lista.
     * @param data El dato a agregar.
     */
    public void addLast(T data) {
        add(data);
    }

    /**
     * Obtiene el nodo en una posición específica de la lista.
     * @param index La posición del nodo.
     * @return El nodo en la posición especificada.
     * @throws IndexOutOfBoundsException Si el índice está fuera de rango.
     */
    private Node getNode(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException("Index out of range.");
        }

        Node current = head;
        for (int i = 0; i < index; i++) {
            current = current.getNext();
        }
        return current;
    }

    /**
     * Obtiene el elemento en una posición específica de la lista.
     * @param index La posición del elemento.
     * @return El elemento en la posición especificada.
     * @throws IndexOutOfBoundsException Si el índice está fuera de rango.
     */
    public T get(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException("Index out of range.");
        }
        return getNode(index).getData();
    }

    /**
     * Obtiene el primer elemento de la lista.
     * @return El primer elemento de la lista.
     * @throws NoSuchElementException Si la lista está vacía.
     */
    public T getFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("LinkedList is empty. There are no elements.");
        }
        return head.getData();
    }

    /**
     * Obtiene el último elemento de la lista.
     * @return El último elemento de la lista.
     * @throws NoSuchElementException Si la lista está vacía.
     */
    public T getLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("LinkedList is empty. There are no elements.");
        }
        return end.getData();
    }

    /**
     * Elimina el elemento en una posición específica de la lista.
     * @param index La posición del elemento a eliminar.
     * @return El elemento eliminado.
     * @throws IndexOutOfBoundsException Si el índice está fuera de rango.
     */
    public T remove(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException("Index is out of range.");
        }

        if (index == 0) {
            return removeFirst();
        } else if (index == size() - 1) {
            return removeLast();
        } else {
            Node previousNode = getNode(index - 1);
            Node removedNode = previousNode.getNext();
            T removedData = removedNode.getData();
            previousNode.setNext(removedNode.getNext());
            removedNode.setNext(null);
            numberOfNodes--;
            return removedData;
        }
    }

    /**
     * Elimina la primera ocurrencia del elemento especificado de la lista.
     * @param data El elemento a eliminar.
     * @return El elemento eliminado, o null si no se encuentra en la lista.
     */
    public T remove(T data) {
        Node current = head;
        Node previous = null;

        while (current != null) {
            if (current.getData().equals(data)) {
                if (previous == null) {
                    head = current.getNext();
                    if (current.getNext() == null) {
                        end = null;
                    }
                } else {
                    previous.setNext(current.getNext());
                    if (current.getNext() == null) {
                        end = previous;
                    }
                }
                numberOfNodes--;
                return current.getData();
            }
            previous = current;
            current = current.getNext();
        }
        return null;
    }

    /**
     * Elimina el primer elemento de la lista.
     * @return El elemento eliminado.
     * @throws NoSuchElementException Si la lista está vacía.
     */
    public T removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("LinkedList is empty. There are no elements to remove.");
        }
        Node removedNode = head;
        T removedData = removedNode.getData();
        head = removedNode.getNext();
        removedNode.setNext(null);
        numberOfNodes--;
        if (isEmpty()) {
            end = null;
        }
        return removedData;
    }

    /**
     * Elimina el último elemento de la lista.
     *
     * @return Valor del elemento eliminado.
     * @throws NoSuchElementException Si la lista está vacía.
     */
    public T removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("LinkedList is empty. There are no elements to remove.");
        }
        if (size() == 1) {
            return removeFirst();
        } else {
            Node secondLast = getNode(size() - 2);
            T removedData = end.getData();
            end = secondLast;
            end.setNext(null);
            numberOfNodes--;
            return removedData;
        }
    }

    /**
     * Convierte la lista en un array.
     *
     * @return Array que contiene los elementos de la lista.
     */
    public T[] toArray() {
        T[] array = (T[]) new Object[size()];
        Node current = head;
        int index = 0;
        while (current != null) {
            array[index++] = current.getData();
            current = current.getNext();
        }
        return array;
    }

    /**
     * Elimina todos los elementos de la lista.
     */
    public void clear() {
        Node current = head;
        while (current != null) {
            Node nextNode = current.getNext();
            current.setData(null);
            current.setNext(null);
            current = nextNode;
        }
        head = null;
        end = null;
        numberOfNodes = 0;
    }

    /**
     * Obtiene el número de elementos en la lista.
     *
     * @return Número de elementos en la lista.
     */
    public int size() {
        return numberOfNodes;
    }

    /**
     * Verifica si la lista está vacía.
     *
     * @return true si la lista está vacía, false en caso contrario.
     */
    public boolean isEmpty() {
        return numberOfNodes == 0;
    }

    /**
     * Verifica si la lista contiene un elemento específico.
     *
     * @param data Elemento a buscar.
     * @return true si la lista contiene el elemento, false en caso contrario.
     */
    public boolean contains(T data) {
        Node current = head;
        while (current != null) {
            if (current.getData().equals(data)) {
                return true;
            }
            current = current.getNext();
        }
        return false;
    }

    /**
     * Retorna un iterador sobre los elementos de la lista.
     *
     * @return Iterador sobre los elementos de la lista.
     */
    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            Node current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                T data = current.getData();
                current = current.getNext();
                return data;
            }
        };
    }

    /**
     * Nodo interno de la lista enlazada.
     */
    private class Node {
        private T data;
        private Node next;

        Node(T data) {
            this.data = data;
            this.next = null;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node next) {
            this.next = next;
        }
    }
}
