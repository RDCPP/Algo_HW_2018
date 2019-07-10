import java.util.*;
import java.lang.*;
public class Breadth_first {
    public static int N;
    public static int W;
    public static int maxprofit = 0;
    public static void main(String[] argc){
        Scanner sc = new Scanner(System.in);
        ArrayList<items> itemList = new ArrayList<>();
        Queue<Node> q = new LinkedList<>();
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
        Tree br_tree = new Tree(root);
        q.add(root);
        while(!q.isEmpty()){
            if(q.element().profit > maxprofit){
                maxprofit = q.element().profit;
            }
            else if(q.element().bound > maxprofit && q.element().item_no < N-1){
                Node nleft = new Node(q.element(),itemList,N,W,true);
                System.out.println(nleft.toString());
                br_tree.size++;
                if(nleft.weight <= W){
                    q.add(nleft);
                }
                Node nright = new Node(q.element(),itemList,N,W,false);
                System.out.println(nright.toString());
                br_tree.size++;
                q.add(nright);
                if(nright.profit > maxprofit){
                    maxprofit = nright.profit;
                }
                q.remove();
            }
            else{
                q.remove();
            }
        }
        System.out.println("maxprofit : " + maxprofit);
        System.out.println("visited node number : " + br_tree.size);
    }
}

