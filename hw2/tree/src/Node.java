import java.util.ArrayList;

public class Node {
    public int profit;
    public int weight;
    public int totweight;
    public double bound;
    public int item_no;
    public Node parent;
    public Node left;
    public Node right;
    public Node(ArrayList<items> li,int N,int W){
        profit = 0;
        weight = 0;
        totweight = 0;
        bound = 0.0;
        item_no = -1;
        parent = null;
        left = null;
        right = null;
        int i = 0;
        while (i < N){
            if(totweight + li.get(i).weight > W){
                bound += (W - totweight) * (li.get(i).unit_profit);
                break;
            }
            totweight += li.get(i).weight;
            bound += li.get(i).profit;
            i++;
        }
    }

    public Node(Node p,ArrayList<items> li,int N,int W,boolean left){
        item_no = p.item_no + 1;
        if(left){
            profit = p.profit + li.get(item_no).profit;
            weight = p.weight + li.get(item_no).weight;
            p.left = this;
            this.parent = p;
        }
        else{
            profit = p.profit;
            weight = p.weight;
            p.right = this;
            this.parent = p;
        }
        totweight = weight;
        bound = profit;
        if(weight > W) {
            bound = -1.0;
        }
        else{
            int i = item_no + 1;
            while(i < N){
                if(totweight + li.get(i).weight > W){
                    bound += (W - totweight) * (li.get(i).unit_profit);
                    break;
                }
                totweight += li.get(i).weight;
                bound += li.get(i).profit;
                i++;
            }
        }
    }

    @Override
    public String toString() {
        return "profit : " + Integer.toString(profit) + " weight : " + Integer.toString(weight) + " bound : " + Double.toString(bound);
    }
}
