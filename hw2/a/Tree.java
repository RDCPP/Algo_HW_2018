public class Tree<T extends Comparable> implements Comparable<Tree<T>> {
    private Tree<T> left;
    private Tree<T> right;

    private T value;

    Tree(T value) {
        this.value = value;
    }

    void appendLeft(T value) {
        left = new Tree<>(value);
    }
    void appendRight(T value) {
        right = new Tree<>(value);
    }
    Tree<T> getLeft() {
        return left;
    }
    Tree<T> getRight() {
        return right;
    }
    T getItem() {
        return value;
    }

    @Override
    public int compareTo(Tree<T> o) {
        return o.value.compareTo(value);
    }
}
