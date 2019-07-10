import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    static int[] carlo(int CARLO_SIZE) {
        int cntSum = 0;
        int carlosSum = 0;

        for(int x = 0; x < 100; x++) {
            ArrayList<Item> randItems = new ArrayList<>();
            int randWeights = 0;

            for (int i = 0; i < CARLO_SIZE; i++) {
                int a = (int) (Math.random() * 40) + 10;
                int b = (int) (Math.random() * 17) + 2;
                randWeights += b;
                if (a > b) {
                    randItems.add(new Item(i, a, b));
                    randWeights += b;
                } else {
                    randItems.add(new Item(i, b, a));
                    randWeights += a;
                }
            }

            ZeroOneKnapsack knapsack = new ZeroOneKnapsack((int) (randWeights / 2), CARLO_SIZE);

            for (Item i : randItems) {
                knapsack.addItem(i);
                //            System.out.printf("%d: Weight: %d, Profit: %d\n", i.getIndex(), i.getWeight(), i.getValue());
            }

            knapsack.prepare();
            try {
                ReturnType bestItem = knapsack.bestFS();
//                System.out.println("Count: " + bestItem.numNodes);
                int carlos = 0;
                for (int j = 0; j < 200; j++) {
                    int carlo = knapsack.monteCarlo();
                    carlos += carlo;
                }
//                System.out.println("Avg: " + carlos / 200);

//                System.out.println("Count: " + bestItem.numNodes + ", Avg: " + (carlos / 200) + ", Cnt^2: " + (bestItem.numNodes * bestItem.numNodes));
//                            System.out.println("Best result: ");
                //            for(int index: bestItem.item.getItemIndexes()) {
                //                System.out.println(ZeroOneKnapsack.items[index]);
                //            }
                cntSum += bestItem.numNodes;
                carlosSum += carlos / 200;
            } catch (NotPreparedException e) {
                e.printStackTrace();
            }
        }
        return new int[]{cntSum / 100, carlosSum / 100};
    }

    public static void main(String[] args) {
        ArrayList<ArrayList<Item>> arrays = new ArrayList<>();
        ArrayList<Integer> weights = new ArrayList<>();
        try {
            String line;
            BufferedReader fr = new BufferedReader(new FileReader("data.txt"));
            ArrayList<Item> items = new ArrayList<>();

            while ((line = fr.readLine()) != null) {
                if (line.startsWith("#")) {
                    arrays.add(items);
                    items = new ArrayList<>();
                    continue;
                }
                if (items.size() == 0) {
                    weights.add(Integer.parseInt(line));
                    line = fr.readLine();
                }
                String[] sp = line.split("\\|");
                items.add(new Item(items.size(), Integer.parseInt(sp[0]), Integer.parseInt(sp[1])));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

//        ZeroOneKnapsack knapsack = new ZeroOneKnapsack(weights.get(0), arrays.get(0).size());
//        arrays.get(0).forEach(knapsack::addItem);

//        ZeroOneKnapsack knapsack = new ZeroOneKnapsack(16, 4);
//        knapsack.addItem(new Item(0, 40, 2));
//        knapsack.addItem(new Item(1, 30, 5));
//        knapsack.addItem(new Item(2, 50, 10));
//        knapsack.addItem(new Item(3, 10, 5));


        for(int i = 5; i <= 22; i++) {
            int[] result = Main.carlo(i);
            System.out.printf("# of item : %d - total best search mean : %d total monte carlo mean: %d\n", i, result[0], result[1]);
        }

//        System.out.println("CountAvg: " + (cntSum / 100) + ", CarloAvg: " + (carlosSum / 100));

//        for (int i = 0; i < weights.size(); i++) {
//            ZeroOneKnapsack knapsack = new ZeroOneKnapsack(weights.get(i), arrays.get(i).size());
//            arrays.get(i).forEach(knapsack::addItem);
//
//            knapsack.prepare();
//            try {
//                ReturnType bestItem  = knapsack.bestFS();
//                System.out.println("Best result: ");
//                for(int index: bestItem.getItemIndexes()) {
//                    System.out.println(ZeroOneKnapsack.items[index]);
//                }
//            } catch (NotPreparedException e) {
//                e.printStackTrace();
//            }
//        }
    }
}