package pe.edu.utp.aed.fileexplorer.util.datastructures;

/**
 * Implementación de una pila utilizando una lista enlazada.
 * @param <T> El tipo de elementos que contendrá la pila.
 */
public class SimpleStack<T> {

    private final SinglyLinkedList<T> stack;

    /**
     * Constructor que inicializa una pila vacía.
     */
    public SimpleStack() {
        stack = new SinglyLinkedList<>();
    }

    /**
     * Inserta un elemento en la parte superior de la pila.
     * @param item El elemento a insertar.
     */
    public void push(T item) {
        stack.addFirst(item);
    }

    /**
     * Elimina y devuelve el elemento en la parte superior de la pila.
     * @return El elemento en la parte superior de la pila.
     */
    public T pop() {
        return stack.removeFirst();
    }

    /**
     * Devuelve el elemento en la parte superior de la pila sin eliminarlo.
     * @return El elemento en la parte superior de la pila.
     */
    public T peek() {
        return stack.getFirst();
    }

    /**
     * Verifica si la pila está vacía.
     * @return true si la pila está vacía, false de lo contrario.
     */
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    /**
     * Devuelve el número de elementos en la pila.
     * @return El número de elementos en la pila.
     */
    public int size() {
        return stack.size();
    }

    /**
     * Elimina todos los elementos de la pila.
     */
    public void clear() {
        stack.clear();
    }

    public boolean contains(T data) {
        return stack.contains(data);
    }
}
