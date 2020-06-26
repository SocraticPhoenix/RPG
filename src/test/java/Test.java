import com.gmail.socraticphoenix.rpg.modifiers.SortedList;

import java.util.Arrays;
import java.util.List;

public class Test {

    public static void main(String[] args) {
        List<Integer> l1 = Arrays.asList(1, 4, 5, 3, 2);

        System.out.println(new SortedList<>(l1));

        List<Integer> l2 = new SortedList<>();
        l1.forEach(l2::add);
        System.out.println(l2);
    }

}
