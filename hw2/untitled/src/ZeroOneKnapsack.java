//import com.sun.tools.corba.se.idl.constExpr.Not;

import java.util.*;

class Item {
    private int index;
    private int value;
    private int weight;

    Item(int index, int value, int weight) { this.index = index; this.value = value; this.weight = weight; }

    public int getIndex() { return index; }
    public int getValue() {
        return value;
    }
    public int getWeight() {
        return weight;
    }
    double getProfit() {
        return ((float) value) / weight;
    }

    @Override
    public String toString() {
        return "Item #" + index + "-> Value: " + value + ", Weight: " + weight;
    }
}

class ReturnType {
    TreeItem item;
    int numNodes;

    public ReturnType(TreeItem item, int numNodes) {
        this.item = item;
        this.numNodes = numNodes;
    }
}

class NotPreparedException extends Exception { }

class TreeItem implements Comparable<TreeItem> {
    private ArrayList<Integer> indexes;
    private int level;
    private double bound;
    private int profit;
    private int weight;

    TreeItem(int level) {
        this.level = level;
        indexes = new ArrayList<>();
    }

    void calculateBound() {
        int k = level;
        int totalWeight = 0;
        double profit = 0;

        for(int index: indexes) {
            totalWeight += ZeroOneKnapsack.items[index].getWeight();
            profit += ZeroOneKnapsack.items[index].getValue();
        }

        this.profit = (int) profit;
        this.weight = totalWeight;

        while(k < ZeroOneKnapsack.items.length) {
            if(totalWeight + ZeroOneKnapsack.items[k].getWeight() > ZeroOneKnapsack.W) break;
            totalWeight += ZeroOneKnapsack.items[k].getWeight();
            k++;
        }

        for(int j = level; j < k; j++)
            profit += ZeroOneKnapsack.items[j].getValue();

        if(k == ZeroOneKnapsack.items.length)
            this.bound = profit;
        else
            this.bound = (profit + (ZeroOneKnapsack.W - totalWeight) * ZeroOneKnapsack.items[k].getProfit());
    }

    ArrayList<Integer> getItemIndexes() {
        return indexes;
    }

    int getValue() {
        int value = 0;
        for(int index: indexes) value += ZeroOneKnapsack.items[index].getValue();

        return value;
    }

    void addItem(int index) {
        indexes.add(index);
    }

    int getLevel() {
        return level;
    }

    double getBound() {
        return bound;
    }

    int getProfit() {
        return profit;
    }

    int getWeight() {
        return weight;
    }

    @Override
    public int compareTo(TreeItem o) {
        return (int) (this.bound - o.bound);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < level; i++) sb.append("  ");
        sb.append("Profit: ");
        sb.append(profit);
        sb.append(", Weight: ");
        sb.append(weight);
        sb.append(", MaxProfit: ");
        sb.append(bound);
        return sb.toString();
    }
}

public class ZeroOneKnapsack {
    static Item[] items;
    static int W;
    private Tree<TreeItem> root;
    boolean ready = false;

    ZeroOneKnapsack(int W, int size) {
        items = new Item[size];
        ZeroOneKnapsack.W = W;
    }

    void prepare() {
        ZeroOneKnapsack.items = Arrays.stream(items)
                .sorted((i, j) -> (int) (j.getProfit() - i.getProfit()))
                .toArray(Item[]::new);
        buildTree();
        ready = true;
    }

    ReturnType bestFS() throws NotPreparedException {
        if(!ready) throw new NotPreparedException();
        PriorityQueue<Tree<TreeItem>> pq = new PriorityQueue<>();
        Tree<TreeItem> v;

        Tree<TreeItem> bestNode = root;
        pq.add(root);

        int count = 1;
//        System.out.println(root.getItem());

        while(!pq.isEmpty()) {
            v = pq.poll();
            if(v.getItem().getBound() > bestNode.getItem().getValue()) {
                Tree<TreeItem> left = v.getLeft();
                Tree<TreeItem> right = v.getRight();

                for(Tree<TreeItem> item: Arrays.asList(left, right)) {
                    count++;
//                    System.out.println(item.getItem());
                    if (item.getItem().getWeight() <= W) {
                        if(item.getItem().getValue() > bestNode.getItem().getValue()) bestNode = item;
                        if(item.getItem().getBound() > bestNode.getItem().getValue()) {
                            pq.add(item);
                        }
                    }
                }
            }
        }

//        System.out.println("Visited " + count + " nodes");
        return new ReturnType(bestNode.getItem(), count);
    }

    int monteCarloDFS() throws NotPreparedException {
        if(!ready) throw new NotPreparedException();
        int i = 0, m = 1;
        int numnodes = 1, mprod = 1;
        Tree<TreeItem> v = root, bestNode = root;
        ArrayList<Tree<TreeItem>> promisingChildrens;

        while(i != items.length && m > 0) {
            i++;
            mprod = mprod * m;
            numnodes = numnodes + mprod * 2;
            if(v.getItem().getValue() > bestNode.getItem().getValue()) bestNode = v;

            m = 0;
            promisingChildrens = new ArrayList<>();
            if(v.getLeft().getItem().getBound() > bestNode.getItem().getValue() && W >= v.getLeft().getItem().getWeight()) {
                promisingChildrens.add(v.getLeft());
                m++;
            }
            if(v.getRight().getItem().getBound() > bestNode.getItem().getValue() && W >= v.getRight().getItem().getWeight()) {
                promisingChildrens.add(v.getRight());
                m++;
            }

            if(m != 0) {
                int rand = (int) (Math.random() * promisingChildrens.size());
//                System.out.println("Size: " + promisingChildrens.size() + ", Rand: " + rand);
                v = promisingChildrens.remove(rand);
            }
        }

        return numnodes;
    }

    boolean promising(TreeItem t, TreeItem bestNode) {
        return t.getBound() > bestNode.getValue() && W >= t.getWeight();
    }

    int monteCarlo() throws NotPreparedException {
        if(!ready) throw new NotPreparedException();
        int numnodes = 0;
        Tree<TreeItem> v = root, bestNode = root;
        PriorityQueue<Tree<TreeItem>> promisingChildrens = new PriorityQueue<>();

        do {
            numnodes++;

            boolean promising = promising(v.getItem(), bestNode.getItem()) && (v.getLeft() != null) && (v.getRight() != null);
            if(!promising) {
                v = promisingChildrens.poll();
                continue;
            }

            boolean leftPromising = promising(v.getLeft().getItem(), bestNode.getItem());
            boolean rightPromising = promising(v.getRight().getItem(), bestNode.getItem());

            if(v.getItem().getValue() > bestNode.getItem().getValue()) bestNode = v;

            if(leftPromising && rightPromising) {
                promisingChildrens.add(v.getRight());
                promisingChildrens.add(v.getLeft());

                Object[] items = promisingChildrens.toArray();
                int randgen = (int)(Math.random() * promisingChildrens.size());

                promisingChildrens.remove(items[randgen]);
                v = (Tree<TreeItem>) items[randgen];
            } else {
                if(leftPromising) {
                    promisingChildrens.add(v.getLeft());
                } else {
                    numnodes++;
                }
                if(rightPromising) {
                    promisingChildrens.add(v.getRight());
                } else {
                    numnodes++;
                }

                v = promisingChildrens.poll();
            }
        } while (promisingChildrens.size() > 0);

        return numnodes;
    }

    TreeItem DFS() throws NotPreparedException {
        if(!ready) throw new NotPreparedException();
        Stack<Tree<TreeItem>> stack = new Stack<>();
        Tree<TreeItem> v;

        Tree<TreeItem> bestNode = root;
        stack.push(root);

        int count = 1;
        System.out.println(root.getItem());

        while(!stack.isEmpty()) {
            v = stack.pop();
            if(v.getItem().getBound() > bestNode.getItem().getValue()) {
                Tree<TreeItem> left = v.getLeft();
                Tree<TreeItem> right = v.getRight();

                for(Tree<TreeItem> item: Arrays.asList(right, left)) {
                    System.out.println(item.getItem());
                    count++;
                    if(item.getItem().getWeight() <= W) {
                        if(item.getItem().getValue() > bestNode.getItem().getValue()) bestNode = item;
                        if(item.getItem().getBound() > bestNode.getItem().getValue()) {
                            stack.push(item);
                        }
                    }
                }
            }
        }

        System.out.println("Visited " + count + " nodes");
        return bestNode.getItem();
    }

    TreeItem BFS() throws NotPreparedException {
        if(!ready) throw new NotPreparedException();
        Queue<Tree<TreeItem>> queue = new MyQueue<>();
        Tree<TreeItem> v;

        Tree<TreeItem> bestNode = root;
        queue.add(root);

        int count = 1;
        System.out.println(root.getItem());

        while(!queue.isEmpty()) {
            v = queue.poll();
            Tree<TreeItem> left = v.getLeft();
            Tree<TreeItem> right = v.getRight();

            for(Tree<TreeItem> item: Arrays.asList(left, right)) {
                count++;
                System.out.println(item.getItem());
                if(item.getItem().getWeight() <= W) {
                    if(item.getItem().getValue() > bestNode.getItem().getValue()) bestNode = item;
                    if(item.getItem().getBound() > bestNode.getItem().getValue()) {
                        queue.add(item);
                    }
                }
            }
        }

        System.out.println("Visited " + count + " nodes");
        return bestNode.getItem();
    }

    void addItem(Item item) {
        int i = 0;
        while(i < items.length && items[i] != null) i++;
        if(i == items.length) return;
        items[i] = item;
    }

    private void buildTree() {
        root = new Tree<>(new TreeItem(0));
        root.getItem().calculateBound();
        append(root);
    }

    private void append(Tree<TreeItem> t) {
        if(t.getItem().getLevel() == items.length) return;

        TreeItem steal = new TreeItem(t.getItem().getLevel() + 1);
        TreeItem notSteal = new TreeItem(t.getItem().getLevel() + 1);

        steal.addItem(t.getItem().getLevel());

        for(int index: t.getItem().getItemIndexes()) {
            steal.addItem(index);
            notSteal.addItem(index);
        }

        steal.calculateBound();
        notSteal.calculateBound();

        t.appendLeft(steal);
        t.appendRight(notSteal);

        append(t.getLeft());
        append(t.getRight());
    }
}