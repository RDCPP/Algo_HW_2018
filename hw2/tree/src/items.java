public class items {
    public int profit;
    public int weight;
    public double unit_profit;

    public items(int a, int b){
        profit = a;
        weight = b;
        unit_profit = 1.0 * a / b;
    }

    public double get_Unit_profit() {
        return unit_profit;
    }

    @Override
    public String toString() {
        return Double.toString(unit_profit);
    }
}
