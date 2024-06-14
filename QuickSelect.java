import java.util.*;

// Inspired by  https://rosettacode.org/wiki/K-d_tree#Java
public class QuickSelect {
    private static final Random random = new Random(); // Create Random object

    // the main function
    public static KdTree.Node select(List<KdTree.Node> list, int n, Comparator<? super KdTree.Node> cmp) {
        return select(list, 0, list.size() - 1, n, cmp);
    }

    //The functions that actually does all the work
    //Btw the Comparator is just a kind of sort so we can use it
    public static KdTree.Node select(List<KdTree.Node> list, int left, int right, int n, Comparator<? super KdTree.Node> cmp) {
        for (;;) {
            if (left == right) // Condition to stop recursion
                return list.get(left);
            int pivot = pivotIndex(left, right); // Chose next pivot index
            pivot = partition(list, left, right, pivot, cmp); // recursion
            if (n == pivot)
                return list.get(n);
            else if (n < pivot)
                right = pivot - 1;
            else
                left = pivot + 1;
        }
    }
    //Does the partition / sorting  of the array based on the pivot
    private static <Node> int partition(List<KdTree.Node> list, int left, int right, int pivot, Comparator<? super KdTree.Node> cmp) {
        KdTree.Node pivotValue = list.get(pivot);
        swap(list, pivot, right);
        int store = left;
        for (int i = left; i < right; ++i) {
            if (cmp.compare(list.get(i), pivotValue) < 0) {
                swap(list, store, i);
                ++store;
            }
        }
        swap(list, right, store);
        return store;
    }

    private static <Node> void swap(List<KdTree.Node> list, int i, int j) {
        KdTree.Node value = list.get(i);
        list.set(i, list.get(j));
        list.set(j, value);
    }

    private static int pivotIndex(int left, int right) {
        return left + random.nextInt(right - left + 1);
    }
}