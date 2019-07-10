import java.util.*;

public class Monte_Carlo {
    public static void main(String[] argc){
        int totalsamplenodes = 0;
        int totalcompare = 0;
        int totalbest = 0;
        for(int N = 5;N <= 50;N++){
            for(int sample = 0;sample<100;sample++){
                int maxprofit = 0;
                int W = 0;
                int totalnodes = 0;
                ArrayList<items> itemList = new ArrayList<>();
                Random random = new Random();
                for(int i = 0;i<N;i++){
                    int a = random.nextInt(40) + 10;
                    int b = random.nextInt(18) + 2;
                    W += b;
                    items it = new items(a,b);
                    itemList.add(it);
                }
                W =  W/2;
                U_sort u = new U_sort();
                Collections.sort(itemList,u);
                Node root = new Node(itemList,N,W);
                Tree be_tree = new Tree(root);
                do{
                    PriorityQueue<Node> pq = new PriorityQueue<>(new Comparator<Node>() {
                        @Override
                        public int compare(Node o1, Node o2) {
                            if(Double.compare(o1.bound,o2.bound) == 1) return -1;
                            else if(Double.compare(o1.bound,o2.bound) == -1) return 1;
                            else return 0;
                        }        });
                    pq.add(root);
                    while(!pq.isEmpty()){
                        if(pq.element().profit > maxprofit){
                            maxprofit = pq.element().profit;
                        }
                        if(pq.element().bound > maxprofit && pq.element().item_no < N){
                            Node nleft = new Node(pq.element(),itemList,N,W,true);
                            be_tree.size++;
                            if(nleft.weight <= W){
                                pq.add(nleft);
                            }
                            Node nright = new Node(pq.element(),itemList,N,W,false);
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
                    totalbest += be_tree.size;
                }while(false);
                Node current = root;
                Node best = root;
                for(int num = 0;num<100;num++){
                    int numnodes = 0;
                    PriorityQueue<Node> pq = new PriorityQueue<>(new Comparator<Node>() {
                        @Override
                        public int compare(Node o1, Node o2) {
                            if(Double.compare(o1.bound,o2.bound) == 1) return -1;
                            else if(Double.compare(o1.bound,o2.bound) == -1) return 1;
                            else return 0;
                        }        });
                    pq.add(root);
                    do{
                        numnodes++;
                        if(!((current.bound > best.profit && W >= current.weight) && current.item_no < N-1)){
                            current = pq.element();
                            pq.remove();
                            continue;
                        }
                        Node left = new Node(current,itemList,N,W,true);
                        Node right = new Node(current,itemList,N,W,false);

                        if(current.profit > best.profit){
                            best = current;
                        }
                        if((left.bound > best.profit && W >= left.weight) && (right.bound > best.profit && W >= right.weight)){
                            pq.add(left);
                            pq.add(right);

                            Object[] nextnode = pq.toArray();
                            int r_num = (int)(Math.random() * pq.size());

                            pq.remove(nextnode[r_num]);
                            current = (Node) nextnode[r_num];
                        }
                        else{
                            if(left.bound > best.profit && W >= left.weight){
                                pq.add(left);
                            }
                            else{
                                numnodes += 1;
                            }
                            if(right.bound > best.profit && W >= right.weight){
                                pq.add(right);
                            }
                            else{
                                numnodes += 1;
                            }
                            current = pq.element();
                            pq.remove();
                        }
                    } while(pq.size() > 0);
                    totalnodes += numnodes;
                }
                totalsamplenodes += totalnodes;
//                System.out.println(Integer.toString(sample) + "th sample best search : " + Integer.toString(be_tree.size));
//                System.out.println(Integer.toString(sample) + "th sample monte carlo mean : " + Integer.toString(totalnodes/200));
            }
//            if(N == 5) totalcompare = totalsamplenodes/10000;
//            else totalcompare *= 1.41;
//            + " past total monte carlo mean * 1.41 : " + Integer.toString(totalcompare)
            System.out.println("# of item : " + Integer.toString(N) + " - total best search mean : " + Integer.toString(totalbest/100) + " total monte carlo mean : " + Integer.toString(totalsamplenodes/10000));
        }
    }
}
