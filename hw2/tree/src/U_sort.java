import java.util.Comparator;

public class U_sort implements Comparator<items> {

    @Override
    public int compare(items i1,items i2){
        double a = i1.get_Unit_profit();
        double b = i2.get_Unit_profit();
        if(Double.compare(a,b) == 1) return -1;
        else if(Double.compare(a,b) == -1) return 1;
        else return 0;
    }
}
