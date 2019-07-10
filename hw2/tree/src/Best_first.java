import java.util.*;
import java.lang.*;
public class Best_first {
    public static int N;
    public static int W;
    public static int maxprofit = 0;
    public static void main(String[] argc){
        Scanner sc = new Scanner(System.in);
        ArrayList<items> itemList = new ArrayList<>();
        PriorityQueue<Node> pq = new PriorityQueue<>(new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                if(Double.compare(o1.bound,o2.bound) == 1) return -1;
                else if(Double.compare(o1.bound,o2.bound) == -1) return 1;
                else return 0;
            }        });
        N = sc.nextInt();
        W = sc.nextInt();
        for(int i = 0;i<N;i++){
            int a = sc.nextInt();
            int b = sc.nextInt();
            items it = new items(a,b);
            itemList.add(it);
        }
        U_sort u = new U_sort();
        Collections.sort(itemList,u);
        System.out.println(itemList);
        Node root = new Node(itemList,N,W);
        System.out.println(root.toString());
        Tree be_tree = new Tree(root);
        pq.add(root);
        while(!pq.isEmpty()){
            if(pq.element().profit > maxprofit){
                maxprofit = pq.element().profit;
            }
            if(pq.element().bound > maxprofit && pq.element().item_no < N){
                Node nleft = new Node(pq.element(),itemList,N,W,true);
                System.out.println(nleft.toString());
                be_tree.size++;
                if(nleft.weight <= W){
                    pq.add(nleft);
                }
                Node nright = new Node(pq.element(),itemList,N,W,false);
                System.out.println(nright.toString());
                be_tree.size++;
                pq.add(nright);
                if(nright.profit > maxprofit){
                    maxprofit = nright.profit;
                }
                pq.remove();
            }
            else{
                pq.remove();
            }
        }
        System.out.println("maxprofit : " + maxprofit);
        System.out.println("visited node number : " + be_tree.size);
    }
}


