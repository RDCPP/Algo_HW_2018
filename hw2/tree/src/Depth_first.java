import java.util.*;
import java.lang.*;
public class Depth_first {
    public static int N;
    public static int W;
    public static int maxprofit = 0;
    public static void main(String[] argc){
        Scanner sc = new Scanner(System.in);
        ArrayList<items> itemList = new ArrayList<>();
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
        DNode root = new DNode(itemList,N,W);
        System.out.println(root.toString());
        DTree d_tree = new DTree(root);
        DNode current = root;
        while(!(d_tree.root.visited)){
            if(current.profit == maxprofit && current.weight == W && current.profit == current.bound){
                current = current.parent;
                d_tree.size += 1;
                DNode dn = new DNode(current,itemList,N,W,false);
                System.out.println(dn.toString());
                current = dn;
            }
            else if(current.visited){
                if(current.parent.right == current){
                    current = current.parent;
                    current.visited = true;
                }
                else{
                    current = current.parent;
                    if(current.bound > maxprofit){
                        d_tree.size += 1;
                        DNode dn = new DNode(current,itemList,N,W,false);
                        System.out.println(dn.toString());
                        current = dn;
                    }
                    else{
                        current.visited = true;
                    }
                }
            }
            else if(current.item_no == N-1){
                if(current.parent.left == current){
                    current = current.parent;
                    d_tree.size += 1;
                    DNode dn = new DNode(current,itemList,N,W,false);
                    System.out.println(dn.toString());
                    current = dn;
                }
                else{
                    current = current.parent;
                    current.visited = true;
                }
            }
            else if(current.bound > maxprofit){
                d_tree.size += 1;
                if(current.weight + itemList.get(current.item_no + 1).weight <= W){
                    DNode dn = new DNode(current,itemList,N,W,true);
                    System.out.println(dn.toString());
                    current = dn;
                }
                else{
                    DNode dn1 = new DNode(current,itemList,N,W,true);
                    System.out.println(dn1.toString());
                    d_tree.size++;
                    DNode dn = new DNode(current,itemList,N,W,false);
                    System.out.println(dn.toString());
                    current = dn;
                }
            }
            else if(current.bound <= maxprofit){
                if(current.parent.right == current){
                    current = current.parent;
                    current.visited = true;
                }
                else{
                    current.parent = current;
                }
            }
            if(current.profit > maxprofit){
                maxprofit = current.profit;
            }
        }
        System.out.println("maxprofit : " + maxprofit);
        System.out.println("visited node number : " + d_tree.size);
    }
}

class DTree{
    public int size;
    public DNode root;
    public DTree(DNode n){
        size = 1;
        root = n;
    }
}

