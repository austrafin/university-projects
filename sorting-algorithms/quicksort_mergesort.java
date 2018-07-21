import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import static java.lang.Math.pow;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.ListIterator;

public class COMP3722AssignmentB {

    private static final int QS_REC_ARRAYS = 0;
    private static final int QS_ITER_ARRAYS = 1;
    private static final int JAVA_ARRAYSORT = 2;
    private static final int MS_REC_ARRAYS = 3;
    private static final int MS_ITER_ARRAYS = 4;
    private static final int QS_REC_LINKEDLIST = 5;
    private static final int QS_REC_ARRAYLIST = 6;
    private static final int JAVA_COLLECTIONS_SORT_LINKEDLIST = 7;
    private static final int JAVA_COLLECTIONS_SORT_ARRAYLIST = 8;
    private static final int MS_REC_LINKEDLIST = 9;
    private static final int MS_REC_ARRAYLIST = 10;
    private static final int base = 2;
    private static int stackSize;
    private static int[] indeces;
    private static int[] tempArrayRec;
    private static int[] tempArrayIter;

    /* This function checks if subsets of array are sorted in ascending order */
    public static boolean isSorted(int[] array, int C, int size) {
        // This checks that the arrays are sorted
        int start = 0;
        for (int run = 0; run < C; ++run) {
            for (int i = start + 1; i < start + size; i++) {
                if (array[i - 1] > array[i]) {
                    System.out.println("Array not sorted");
                    return false;
                }
            }
            start = start + size;
        }
        return true;
    }

    private static <T extends Comparable> boolean isSorted(List<T> objects) {
        for (int i = 1; i < objects.size(); i++) {
            if (objects.get(i - 1).compareTo(objects.get(i)) > 0) {
                System.out.println("List not sorted");
                return false;
            }
        }
        return true;
    }

    public static int partition(int numbers[], int first, int last) {
        boolean toggle = true;
        int middle = (last + 1 - first) / 2 + first;
        int pivotValue = numbers[middle]; // choose the middle element
        int left = first + 1, right = last;
        int temp;

        // Swap first index value with middle element
        temp = numbers[first];
        numbers[first] = numbers[middle];
        numbers[middle] = temp;

        /* loop until left and right indeces have crossed over
         * equaly place dublicates on both partitions */
        do {
            while (numbers[left] <= pivotValue && left < last) {
                if (numbers[left] == pivotValue) {
                    if (toggle) {
                        ++left;
                        toggle = false;
                    }
                    else {
                        break;
                    }
                }
                else {
                    ++left;
                }
            }
            while (numbers[right] >= pivotValue && right > first) {

                if (numbers[right] == pivotValue) {
                    if (!toggle) {
                        --right;
                        toggle = true;
                    }
                    else {
                        break;
                    }
                }
                else {
                    --right;
                }
            }
            if (left < right) {
                // swap left and right index values
                temp = numbers[left];
                numbers[left] = numbers[right];
                numbers[right] = temp;
            }
        }
        while (left < right);

        // swap pivot value with right index value
        temp = numbers[right];
        numbers[right] = numbers[first];
        numbers[first] = temp;
        return right;
    }

    /* This method sorts an array or sub-array recursively */
    public static void quickSortRecursive(int numbers[], int first, int last) {
        if (last - first < 1) {
            return; // if only 1 (or less) elements in list, already sorted
        }
        int pivot = partition(numbers, first, last); // split array in two parts

        // Sort the smaller partition first
        if ((last + 1) / 2 > pivot) {
            quickSortRecursive(numbers, first, pivot - 1);
            quickSortRecursive(numbers, pivot + 1, last);
        }
        else {
            quickSortRecursive(numbers, pivot + 1, last);
            quickSortRecursive(numbers, first, pivot - 1);
        }
    }

    /* This method sorts an array or sub-array iteratively */
    public static void quickSortIterative(int numbers[], int first, int last) {
        int stackIndex = 0;
        int pivot;

        while (true) {
            while (last - first >= 1) {
                pivot = partition(numbers, first, last);

                /* If more elements on the right side of pivot, process left partition
                 * otherwise, sort the right partition */
                if ((pivot - first) < (last - pivot)) // Process left side
                {
                    /* No need to fill the stack if one or less items to sort on left side of the pivot
                     * just going to process the right side instead */
                    if (pivot - first > 1) {
                        // Store the right partition in the stack
                        indeces[stackIndex] = pivot + 1;
                        ++stackIndex;
                        indeces[stackIndex] = last; // 'last' stays the same
                        ++stackIndex;

                        // Next iteration, process left partition.
                        last = pivot - 1;
                    }
                    else {
                        first = pivot + 1;
                    }
                }
                else { // Process right side
                    /* No need to fill the stack if one or less items to sort on right side of the pivot
                     * just going to process the left side instead */
                    if (last - pivot > 1) {
                        // Store the left partition in the stack
                        indeces[stackIndex] = first; // 'first' stays the same
                        ++stackIndex;
                        indeces[stackIndex] = pivot - 1; // 'last' stored as one index left of pivot
                        ++stackIndex;

                        // Next iteration, process right partition.
                        first = pivot + 1;
                    }
                    else {
                        last = pivot - 1;
                    }
                }
            }
            // Take partition indeces from the stack
            if (stackIndex > 0) {
                --stackIndex;
                last = indeces[stackIndex];
                --stackIndex;
                first = indeces[stackIndex];
            }
            else {
                break;
            }
        }
    }

    /* This method sorts a list or sub-list recursively using Quicksort algorithm */
    public static <T extends Comparable> List<T> quickSortRecursiveLinkedList(List<T> objects) {
        if (objects.size() <= 1) {
            return objects; // if only 1 (or less) elements in list, already sorted
        }

        List<T> leftList = new LinkedList();
        List<T> rightList = new LinkedList();

        boolean toggle = true;
        ListIterator<T> it = objects.listIterator(objects.size());
        T pivotValue = it.previous();
        it.remove();
        T obj;

        while (it.hasPrevious()) {
            obj = it.previous();
            if (obj.compareTo(pivotValue) <= 0) {
                leftList.add(obj);
                it.remove();
            }
            else if (obj.compareTo(pivotValue) > 0) {
                rightList.add(obj);
                it.remove();
            }
            else {
                if (toggle) {
                    leftList.add(obj);
                    it.remove();
                    toggle = false;
                }
                else {
                    rightList.add(obj);
                    it.remove();
                    toggle = true;
                }
            }
        }

        // Sort the smaller partition first
        if (leftList.size() < rightList.size()) {
            leftList = quickSortRecursiveLinkedList(leftList);
            rightList = quickSortRecursiveLinkedList(rightList);
        }
        else {
            rightList = quickSortRecursiveLinkedList(rightList);
            leftList = quickSortRecursiveLinkedList(leftList);
        }

        leftList.add(pivotValue);
        ListIterator<T> leftIT = leftList.listIterator(leftList.size());
        ListIterator<T> rightIT = rightList.listIterator(rightList.size());
        while (rightIT.hasPrevious()) {
            leftIT.add(rightIT.previous());
            rightIT.remove();
            leftIT.previous();
        }

        return leftList;
    }

    /* This method sorts a list or sub-list recursively using Quicksort algorithm */
    public static <T extends Comparable> List<T> quickSortRecursiveArrayList(List<T> objects) {
        if (objects.size() <= 1) {
            return objects; // if only 1 (or less) elements in list, already sorted
        }

        List<T> leftList = new ArrayList();
        List<T> rightList = new ArrayList();

        boolean toggle = true;
        ListIterator<T> it = objects.listIterator(objects.size());
        T pivotValue = it.previous();
        it.remove();
        T obj;

        while (it.hasPrevious()) {
            obj = it.previous();
            if (obj.compareTo(pivotValue) <= 0) {
                leftList.add(obj);
                it.remove();
            }
            else if (obj.compareTo(pivotValue) > 0) {
                rightList.add(obj);
                it.remove();
            }
            else {
                if (toggle) {
                    leftList.add(obj);
                    it.remove();
                    toggle = false;
                }
                else {
                    rightList.add(obj);
                    it.remove();
                    toggle = true;
                }
            }
        }

        // Sort the smaller partition first
        if (leftList.size() < rightList.size()) {
            leftList = quickSortRecursiveArrayList(leftList);
            rightList = quickSortRecursiveArrayList(rightList);
        }
        else {
            rightList = quickSortRecursiveArrayList(rightList);
            leftList = quickSortRecursiveArrayList(leftList);
        }

        leftList.add(pivotValue);
        ListIterator<T> leftIT = leftList.listIterator(leftList.size());
        ListIterator<T> rightIT = rightList.listIterator(rightList.size());
        while (rightIT.hasPrevious()) {
            leftIT.add(rightIT.previous());
            rightIT.remove();
            leftIT.previous();
        }

        return leftList;
    }

    public static <T extends Comparable> List<T> mergeSortRecursiveLinkedList(List<T> objects) {
        if (objects.size() > 1) {
            //Recursively split into left and right partitions
            int midPoint = (objects.size() / 2) - 1;
            List<T> leftList = new LinkedList();
            List<T> rightList = new LinkedList();
            ListIterator<T> itNew = leftList.listIterator();
            ListIterator<T> it = objects.subList(0, midPoint + 1).listIterator(objects.subList(0, midPoint + 1).size());

            // Add elements from old list to new sublists
            while (it.hasPrevious()) {
                itNew.add(it.previous());
                it.remove();
                itNew.previous();
            }

            itNew = rightList.listIterator();
            it = objects.listIterator(objects.size());

            while (it.hasPrevious()) {
                itNew.add(it.previous());
                it.remove();
                itNew.previous();
            }

            leftList = mergeSortRecursiveLinkedList(leftList);
            rightList = mergeSortRecursiveLinkedList(rightList);

            // merge
            List<T> mergedList = new LinkedList();
            ListIterator<T> leftIT = leftList.listIterator(leftList.size());
            ListIterator<T> rightIT = rightList.listIterator(rightList.size());
            ListIterator<T> mergeIT = mergedList.listIterator();
            T leftObject, rightObject;

            if (leftIT.hasPrevious() && rightIT.hasPrevious()) {
                leftObject = leftIT.previous();
                rightObject = rightIT.previous();

                while (true) {
                    if (leftObject.compareTo(rightObject) > 0) {
                        mergeIT.add(leftObject);
                        mergeIT.previous();
                        if (leftIT.hasPrevious()) {
                            leftObject = leftIT.previous();
                            leftIT.remove();
                        }
                        else { // copy over the current right element
                            mergeIT.add(rightObject);
                            mergeIT.previous();
                            break;
                        }
                    }
                    else {
                        mergeIT.add(rightObject);
                        mergeIT.previous();
                        if (rightIT.hasPrevious()) {
                            rightObject = rightIT.previous();
                            rightIT.remove();
                        }
                        else { // copy over the current left element
                            mergeIT.add(leftObject);
                            mergeIT.previous();
                            break;
                        }
                    }
                }
            }

            // Copy over any left-over values
            while (leftIT.hasPrevious()) {
                mergeIT.add(leftIT.previous());
                mergeIT.previous();
                leftIT.remove();
            }
            while (rightIT.hasPrevious()) {
                mergeIT.add(rightIT.previous());
                mergeIT.previous();
                rightIT.remove();
            }
            return mergedList;
        }
        return objects; // if only one element in list
    }

    public static <T extends Comparable> List<T> mergeSortRecursiveArrayList(List<T> objects) {
        if (objects.size() > 1) {
            //Recursively split into left and right partitions
            int midPoint = (objects.size() / 2) - 1;
            List<T> leftList = new ArrayList();
            List<T> rightList = new ArrayList();
            ListIterator<T> itNew = leftList.listIterator();
            ListIterator<T> it = objects.subList(0, midPoint + 1).listIterator(objects.subList(0, midPoint + 1).size());

            // Add elements from old list to new sublists
            while (it.hasPrevious()) {
                itNew.add(it.previous());
                it.remove();
                itNew.previous();
            }

            itNew = rightList.listIterator();
            it = objects.listIterator(objects.size());

            while (it.hasPrevious()) {
                itNew.add(it.previous());
                it.remove();
                itNew.previous();
            }

            leftList = mergeSortRecursiveArrayList(leftList);
            rightList = mergeSortRecursiveArrayList(rightList);

            // merge
            List<T> mergedList = new ArrayList();
            ListIterator<T> leftIT = leftList.listIterator(leftList.size());
            ListIterator<T> rightIT = rightList.listIterator(rightList.size());
            ListIterator<T> mergeIT = mergedList.listIterator();
            T leftObject, rightObject;

            if (leftIT.hasPrevious() && rightIT.hasPrevious()) {
                leftObject = leftIT.previous();
                rightObject = rightIT.previous();

                while (true) {
                    if (leftObject.compareTo(rightObject) > 0) {
                        mergeIT.add(leftObject);
                        mergeIT.previous();
                        if (leftIT.hasPrevious()) {
                            leftObject = leftIT.previous();
                            leftIT.remove();
                        }
                        else { // copy over the current right element
                            mergeIT.add(rightObject);
                            mergeIT.previous();
                            break;
                        }
                    }
                    else {
                        mergeIT.add(rightObject);
                        mergeIT.previous();
                        if (rightIT.hasPrevious()) {
                            rightObject = rightIT.previous();
                            rightIT.remove();
                        }
                        else { // copy over the current left element
                            mergeIT.add(leftObject);
                            mergeIT.previous();
                            break;
                        }
                    }
                }
            }

            // Copy over any left-over values
            while (leftIT.hasPrevious()) {
                mergeIT.add(leftIT.previous());
                mergeIT.previous();
                leftIT.remove();
            }
            while (rightIT.hasPrevious()) {
                mergeIT.add(rightIT.previous());
                mergeIT.previous();
                rightIT.remove();
            }
            return mergedList;
        }
        return objects; // if only one element in list
    }

    public static <T extends Comparable> List<T> mergeSortRecursiveLinkedListWithSublists(List<T> objects) {
        if (objects.size() > 1) {
            //Recursively split into left and right partitions
            int midPoint = (objects.size() / 2) - 1;
            List<T> leftList = mergeSortRecursiveLinkedListWithSublists(objects.subList(0, midPoint + 1));
            List<T> rightList = mergeSortRecursiveLinkedListWithSublists(objects.subList(midPoint + 1, objects.size()));

            // merge
            List<T> mergedList = new LinkedList();
            ListIterator<T> leftIT = leftList.listIterator(leftList.size());
            ListIterator<T> rightIT = rightList.listIterator(rightList.size());
            ListIterator<T> mergeIT = mergedList.listIterator();
            T leftObject, rightObject;

            if (leftIT.hasPrevious() && rightIT.hasPrevious()) {
                leftObject = leftIT.previous();
                rightObject = rightIT.previous();

                while (true) {
                    if (leftObject.compareTo(rightObject) > 0) {
                        mergeIT.add(leftObject);
                        mergeIT.previous();
                        if (leftIT.hasPrevious()) {
                            leftObject = leftIT.previous();
                            leftIT.remove();
                        }
                        else { // copy over the current right element
                            mergeIT.add(rightObject);
                            mergeIT.previous();
                            break;
                        }
                    }
                    else {
                        mergeIT.add(rightObject);
                        mergeIT.previous();
                        if (rightIT.hasPrevious()) {
                            rightObject = rightIT.previous();
                            rightIT.remove();
                        }
                        else { // copy over the current left element
                            mergeIT.add(leftObject);
                            mergeIT.previous();
                            break;
                        }
                    }
                }
            }

            // Copy over any left-over values
            while (leftIT.hasPrevious()) {
                mergeIT.add(leftIT.previous());
                mergeIT.previous();
                leftIT.remove();
            }
            while (rightIT.hasPrevious()) {
                mergeIT.add(rightIT.previous());
                mergeIT.previous();
                rightIT.remove();
            }
            return mergedList;
        }
        return objects; // if only one element in list
    }

    public static <T extends Comparable> List<T> mergeSortRecursiveArrayListWithSublists(List<T> objects) {
        if (objects.size() > 1) {
            //Recursively split into left and right partitions
            int midPoint = (objects.size() / 2) - 1;
            List<T> leftList = mergeSortRecursiveArrayListWithSublists(objects.subList(0, midPoint + 1));
            List<T> rightList = mergeSortRecursiveArrayListWithSublists(objects.subList(midPoint + 1, objects.size()));

            // merge
            List<T> mergedList = new ArrayList();
            ListIterator<T> leftIT = leftList.listIterator(leftList.size());
            ListIterator<T> rightIT = rightList.listIterator(rightList.size());
            ListIterator<T> mergeIT = mergedList.listIterator();
            T leftObject, rightObject;

            if (leftIT.hasPrevious() && rightIT.hasPrevious()) {
                leftObject = leftIT.previous();
                rightObject = rightIT.previous();

                while (true) {
                    if (leftObject.compareTo(rightObject) > 0) {
                        mergeIT.add(leftObject);
                        mergeIT.previous();
                        if (leftIT.hasPrevious()) {
                            leftObject = leftIT.previous();
                            leftIT.remove();
                        }
                        else { // copy over the current right element
                            mergeIT.add(rightObject);
                            mergeIT.previous();
                            break;
                        }
                    }
                    else {
                        mergeIT.add(rightObject);
                        mergeIT.previous();
                        if (rightIT.hasPrevious()) {
                            rightObject = rightIT.previous();
                            rightIT.remove();
                        }
                        else { // copy over the current left element
                            mergeIT.add(leftObject);
                            mergeIT.previous();
                            break;
                        }
                    }
                }
            }

            // Copy over any left-over values
            while (leftIT.hasPrevious()) {
                mergeIT.add(leftIT.previous());
                mergeIT.previous();
                leftIT.remove();
            }
            while (rightIT.hasPrevious()) {
                mergeIT.add(rightIT.previous());
                mergeIT.previous();
                rightIT.remove();
            }
            return mergedList;
        }
        return objects; // if only one element in list
    }

    public static void merge(int original[], int tempArray[], int left, int right, int size) {
        int target = left, leftBound = right, rightBound = size;

        while (left < leftBound && right < rightBound) {
            if (original[left] <= original[right]) {
                tempArray[target] = original[left];
                ++left;
                ++target;
            }
            else if (original[right] < original[left]) {
                tempArray[target] = original[right];
                ++right;
                ++target;
            }
        }
        while (left < leftBound) {
            tempArray[target] = original[left];
            ++target;
            ++left;
        }
        while (right < rightBound) {
            tempArray[target] = original[right];
            ++target;
            ++right;
        }
    }

    public static void mergeSortRecursive(int numbers[], int left, int right) {
        int size = right + 1 - left;

        if (size > 1) {
            int midPoint = (size / 2) - 1 + left;
            mergeSortRecursive(numbers, left, midPoint);
            mergeSortRecursive(numbers, midPoint + 1, right);
            merge(numbers, tempArrayRec, left, midPoint + 1, size + left);
            for (int i = left; i <= right; ++i) {
                numbers[i] = tempArrayRec[i];
            }
        }
    }

    /* Iterative version of Mergesort. Divides the array (or subset of it) into partitions from bottom-up,
     * so instead of dividing from large to small, this algorithm starts processing the smallest
     * partitions straight away */
    public static void mergeSortIterative(int numbers[], int first, int last) {
        int[] original = numbers;
        int left, right, rightBound, upperBound = last + 1;
        int offset = 1; // defines the position of right index
        int temp[];
        while (offset < upperBound) {
            left = first;
            while (left < upperBound) {
                right = left + offset;
                rightBound = left + 2 * offset;

                /* If the offset from the left index exceeds the size of the array,
                trim it back to the length of the array. Same with right bound */
                if (right > upperBound) {
                    right = upperBound;
                }
                if (rightBound > upperBound) {
                    rightBound = upperBound;
                }
                merge(original, tempArrayIter, left, right, rightBound);
                left += 2 * offset; // move to the next partition
            }

            // swap memory arrays to avoid copying over
            temp = original;
            original = tempArrayIter;
            tempArrayIter = temp;
            offset *= 2;
        }

        /* if the temporary array was last used to store numbers,
         * then they must be transferred to the original */
        if (numbers != original) {
            for (int i = first; i < upperBound; ++i) {
                numbers[i] = original[i];
            }
        }
    }

    /* Method for benchmarking and reporting */
    public static void sort(String fileName, int sortingAlgorithm) throws FileNotFoundException, IOException {
        Random randomGenerator = new Random(5); // an arbitrary seed
        Random randomGenerator2 = new Random(5); // an arbitrary seed
        Random randomGenerator3 = new Random(5); // an arbitrary seed
        int elements = (int) pow(base, 19);
        int totalRuns = (int) (Math.log(elements) / Math.log(base));
        int numbers[] = new int[elements];
        int multiplier = base;
        int size = base;
        int start = 0;
        int C;
        StringBuilder report = new StringBuilder();

        List linkedList = new LinkedList();
        List arrList = new ArrayList();
        tempArrayIter = new int[numbers.length];
        tempArrayRec = new int[numbers.length];

        report.append("Number of Runs (C),Number of Keys (N),Time (ms)\n");
        long startTime = 0, endTime = 0, totalTime = 0;

        for (int i = 0; i < totalRuns; ++i) {
            C = elements / size; // Number of runs           
            stackSize = ((int) (Math.log(size) / Math.log(base)) + 1) * 2;
            indeces = new int[stackSize];
            //linkedList = new LinkedList();
            //arrList = new ArrayList();
            for (int i2 = 0; i2 < elements; ++i2) {
                // We can also use the random numbers in this array for lists
                linkedList.add(randomGenerator2.nextInt(100000000));
                arrList.add(randomGenerator3.nextInt(100000000));
                numbers[i2] = randomGenerator.nextInt(100000000);
            }

            switch (sortingAlgorithm) {
                case QS_REC_ARRAYS:
                    System.out.println("Sorting size: " + size);
                    startTime = System.nanoTime();
                    for (int run = 0; run < C; ++run) {
                        quickSortRecursive(numbers, start, start + size - 1);
                        start = start + size;
                    }
                    endTime = System.nanoTime();
                    totalTime = endTime - startTime;
                    isSorted(numbers, C, size);
                    break;
                case QS_ITER_ARRAYS:
                    System.out.println("Sorting size: " + size);
                    startTime = System.nanoTime();
                    for (int run = 0; run < C; ++run) {
                        quickSortIterative(numbers, start, start + size - 1);
                        start = start + size;
                    }
                    endTime = System.nanoTime();
                    totalTime = endTime - startTime;
                    isSorted(numbers, C, size);
                    break;
                case JAVA_ARRAYSORT:
                    System.out.println("Sorting size: " + size);
                    startTime = System.nanoTime();
                    for (int run = 0; run < C; ++run) {
                        Arrays.sort(numbers, start, start + size);
                        start = start + size;
                    }
                    endTime = System.nanoTime();
                    totalTime = endTime - startTime;
                    isSorted(numbers, C, size);
                    break;
                case MS_REC_ARRAYS:
                    System.out.println("Sorting size: " + size);
                    startTime = System.nanoTime();
                    for (int run = 0; run < C; ++run) {
                        mergeSortRecursive(numbers, start, start + size - 1);
                        start = start + size;
                    }
                    endTime = System.nanoTime();
                    totalTime = endTime - startTime;
                    isSorted(numbers, C, size);
                    break;
                case MS_ITER_ARRAYS:
                    System.out.println("Sorting size: " + size);
                    startTime = System.nanoTime();
                    for (int run = 0; run < C; ++run) {
                        mergeSortIterative(numbers, start, start + size - 1);
                        start = start + size;
                    }
                    endTime = System.nanoTime();
                    totalTime = endTime - startTime;
                    isSorted(numbers, C, size);
                    break;
                case QS_REC_LINKEDLIST:
                    System.out.println("Sorting size: " + size);
                    startTime = System.nanoTime();
                    for (int run = 0; run < C; ++run) {
                        // after each sort, the sublist is removed
                        quickSortRecursiveLinkedList(linkedList.subList(0, size));
                    }
                    endTime = System.nanoTime();
                    totalTime = endTime - startTime;
                    break;
                case QS_REC_ARRAYLIST:
                    System.out.println("Sorting size: " + size);
                    startTime = System.nanoTime();
                    for (int run = 0; run < C; ++run) {
                        // after each sort, the sublist is removed
                        quickSortRecursiveArrayList(arrList.subList(0, size));
                    }
                    endTime = System.nanoTime();
                    totalTime = endTime - startTime;
                    break;
                case JAVA_COLLECTIONS_SORT_LINKEDLIST:
                    System.out.println("Sorting size: " + size);
                    startTime = System.nanoTime();
                    for (int run = 0; run < C; ++run) {
                        Collections.sort(linkedList.subList(start, start + size));
                    }
                    endTime = System.nanoTime();
                    totalTime = endTime - startTime;
                    break;
                case JAVA_COLLECTIONS_SORT_ARRAYLIST:
                    System.out.println("Sorting size: " + size);
                    startTime = System.nanoTime();
                    for (int run = 0; run < C; ++run) {
                        Collections.sort(arrList.subList(start, start + size));
                    }
                    endTime = System.nanoTime();
                    totalTime = endTime - startTime;
                    break;
                case MS_REC_LINKEDLIST:
                    System.out.println("Sorting size: " + size);
                    startTime = System.nanoTime();
                    for (int run = 0; run < C; ++run) {
                        // after each sort, the sublist is removed
                        mergeSortRecursiveLinkedList(linkedList.subList(0, size));
                    }
                    endTime = System.nanoTime();
                    totalTime = endTime - startTime;
                    break;
                case MS_REC_ARRAYLIST:
                    System.out.println("Sorting size: " + size);
                    startTime = System.nanoTime();
                    for (int run = 0; run < C; ++run) {
                        // after each sort, the sublist is removed
                        mergeSortRecursiveArrayList(arrList.subList(0, size));
                    }
                    endTime = System.nanoTime();
                    totalTime = endTime - startTime;
                    break;
                default:
                    break;
            }
            report.append(Integer.toString(C));
            report.append(",");
            report.append(Integer.toString(size));
            report.append(",");
            report.append(Long.toString(totalTime));
            report.append("\n");
            size *= multiplier;
            start = 0;
        }
        System.out.print(report.toString());
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "utf-8"))) {
            writer.write(report.toString());
        }
    }

    private static void printExampleArrays(int size, int alg, Random randomGenerator) {
        int arr[];
        String algoString = new String();
        switch (alg) {
            case 0:
                algoString = "Quicksort (recursive)";
                break;
            case 1:
                algoString = "Quicksort (iterative)";
                break;
            case 2:
                algoString = "Mergesort (recursive)";
                break;
            case 3:
                algoString = "Mergesort (iterative)";
                break;
            default:
                break;
        }

        for (int k = 0; k < 3; ++k) {
            arr = new int[size];
            switch (k) {
                case 0:
                    // Random numbers
                    for (int i = 0; i < size; ++i) {
                        arr[i] = randomGenerator.nextInt(100);
                    }
                    break;
                case 1:
                    // Descending list of numbers
                    for (int i = 0; i < size; ++i) {
                        arr[i] = arr.length - 1 - i;
                    }
                    break;
                case 2:
                    // Ascending list of numbers
                    for (int i = 0; i < size; ++i) {
                        arr[i] = i;
                    }
                    break;
                default:
                    break;
            }

            System.out.println(algoString + " Original array:");
            System.out.println(Arrays.toString(arr));
            switch (alg) {
                case 0:
                    quickSortRecursive(arr, 0, arr.length - 1);
                    break;
                case 1:
                    quickSortIterative(arr, 0, arr.length - 1);
                    break;
                case 2:
                    mergeSortRecursive(arr, 0, arr.length - 1);
                    break;
                case 3:
                    mergeSortIterative(arr, 0, arr.length - 1);
                    break;
            }
            System.out.println(algoString + " Sorted array:");
            System.out.println(Arrays.toString(arr));
        }
    }

    /* Processes a few example data sets to
     * verify the correctness of the algorithms */
    public static void printExample(int sortAlgo) {
        int seed = 75;
        int size = 20;
        Random randomGenerator = new Random(seed);
        List<Integer> linkedListInteger;
        List<String> linkedListString;
        List<Integer> arrListInteger;
        List<String> arrListString;

        switch (sortAlgo) {
            case QS_REC_ARRAYS:
                // Quicksort recursive
                tempArrayRec = new int[size];
                printExampleArrays(size, 0, randomGenerator);
                break;
            case QS_ITER_ARRAYS:
                // Quicksort iterative
                tempArrayIter = new int[size];
                stackSize = ((int) (Math.log(size) / Math.log(2)) + 1) * 2;
                indeces = new int[stackSize];
                printExampleArrays(size, 1, randomGenerator);
                break;
            case MS_REC_ARRAYS:
                // Mergesort recursive
                tempArrayRec = new int[size];
                printExampleArrays(size, 2, randomGenerator);
                break;
            case MS_ITER_ARRAYS:
                // Mergesort iterative
                tempArrayIter = new int[size];
                printExampleArrays(size, 3, randomGenerator);
                break;
            case QS_REC_LINKEDLIST:
                // LinkedList Quicksort recursive
                linkedListInteger = new LinkedList();
                linkedListString = new LinkedList();

                for (int i = 0; i < size; ++i) {
                    linkedListInteger.add(randomGenerator.nextInt(100));
                    linkedListString.add(createRandomString());
                }
                //Integers
                System.out.println("LinkedList Quicksort (recursive) Original array:");
                System.out.println(linkedListInteger.toString());
                linkedListInteger = quickSortRecursiveLinkedList(linkedListInteger);
                System.out.println("LinkedList Quicksort (recursive) Sorted array:");
                System.out.println(linkedListInteger.toString());

                //Strings
                System.out.println("LinkedList Quicksort (recursive) Original array:");
                System.out.println(linkedListString.toString());
                linkedListString = quickSortRecursiveLinkedList(linkedListString);
                System.out.println("LinkedList Quicksort (recursive) Sorted array:");
                System.out.println(linkedListString.toString());
                break;
            case QS_REC_ARRAYLIST:
                // LinkedList Quicksort recursive
                arrListInteger = new ArrayList();
                arrListString = new ArrayList();

                for (int i = 0; i < size; ++i) {
                    arrListInteger.add(randomGenerator.nextInt(100));
                    arrListString.add(createRandomString());
                }
                //Integers
                System.out.println("ArrayList Quicksort (recursive) Original array:");
                System.out.println(arrListInteger.toString());
                arrListInteger = quickSortRecursiveArrayList(arrListInteger);
                System.out.println("ArrayList Quicksort (recursive) Sorted array:");
                System.out.println(arrListInteger.toString());

                //Strings
                System.out.println("ArrayList Quicksort (recursive) Original array:");
                System.out.println(arrListString.toString());
                arrListString = quickSortRecursiveArrayList(arrListString);
                System.out.println("ArrayList Quicksort (recursive) Sorted array:");
                System.out.println(arrListString.toString());
                break;
            case MS_REC_LINKEDLIST:
                // LinkedList Quicksort recursive
                linkedListInteger = new LinkedList();
                linkedListString = new LinkedList();

                for (int i = 0; i < size; ++i) {
                    linkedListInteger.add(randomGenerator.nextInt(100));
                    linkedListString.add(createRandomString());
                }
                //Integers
                System.out.println("LinkedList Mergesort (recursive) Original array:");
                System.out.println(linkedListInteger.toString());
                linkedListInteger = mergeSortRecursiveLinkedList(linkedListInteger);
                System.out.println("LinkedList Mergesort (recursive) Sorted array:");
                System.out.println(mergeSortRecursiveLinkedList(linkedListInteger).toString());

                //Strings
                System.out.println("LinkedList Mergesort (recursive) Original array:");
                System.out.println(linkedListString.toString());
                linkedListString = mergeSortRecursiveLinkedList(linkedListString);
                System.out.println("LinkedList Mergesort (recursive) Sorted array:");
                System.out.println(linkedListString.toString());
                break;
            case MS_REC_ARRAYLIST:
                // ArrayList Mergesort recursive
                arrListInteger = new ArrayList();
                arrListString = new ArrayList();

                for (int i = 0; i < size; ++i) {
                    arrListInteger.add(randomGenerator.nextInt(100));
                    arrListString.add(createRandomString());
                }
                //Integers
                System.out.println("ArrayList Mergesort (recursive) Original array:");
                System.out.println(arrListInteger.toString());
                arrListInteger = mergeSortRecursiveArrayList(arrListInteger);
                System.out.println("ArrayList Mergesort (recursive) Sorted array:");
                System.out.println(arrListInteger.toString());

                //Strings
                System.out.println("ArrayList Mergesort (recursive) Original array:");
                System.out.println(arrListString.toString());
                arrListString = mergeSortRecursiveLinkedList(arrListString);
                System.out.println("ArrayList Mergesort (recursive) Sorted array:");
                System.out.println(arrListString.toString());
                break;
            default:
                break;
        }
    }

    private static String createRandomString() {
        String s = new String("ABCDEFGHIJKLMNOPQRSTUVW");
        StringBuilder newString = new StringBuilder();
        Random randomGenerator = new Random();
        for (int i = 0; i < s.length(); ++i) {
            newString.append(s.charAt(randomGenerator.nextInt(s.length())));
        }
        return newString.toString();
    }

    public static void main(String[] args) throws UnsupportedEncodingException, IOException {

        printExample(QS_REC_ARRAYS);
        printExample(QS_ITER_ARRAYS);
        printExample(JAVA_ARRAYSORT);
        printExample(MS_REC_ARRAYS);
        printExample(MS_ITER_ARRAYS);
        printExample(QS_REC_LINKEDLIST);
        printExample(QS_REC_ARRAYLIST);
        printExample(JAVA_COLLECTIONS_SORT_LINKEDLIST);
        printExample(JAVA_COLLECTIONS_SORT_ARRAYLIST);
        printExample(MS_REC_LINKEDLIST);
        printExample(MS_REC_ARRAYLIST);

        System.out.println("Quicksort Recursive");
        sort("qs_report_base_" + base + "_recursive.csv", QS_REC_ARRAYS);
        System.out.println("Quicksort Iterative");
        sort("qs_report_base_" + base + "_iterative.csv", QS_ITER_ARRAYS);
        System.out.println("Java Arrays.sort()");
        sort("arraysSortJava_report_base_" + base + ".csv", JAVA_ARRAYSORT);
        System.out.println("Mergesort Recursive");
        sort("ms_report_base_" + base + "_recursive.csv", MS_REC_ARRAYS);
        System.out.println("Mergesort Iterative");
        sort("ms_report_base_" + base + "_iterative.csv", MS_ITER_ARRAYS);
        System.out.println("Quicksort LinkedList Recursive");
        sort("qs_report_base_" + base + "_linkedlist_recursive.csv", QS_REC_LINKEDLIST);
        System.out.println("Quicksort ArrayList Recursive");
        sort("qs_report_base_" + base + "_arraylist_recursive.csv", QS_REC_ARRAYLIST);
        System.out.println("Collections.sort() LinkedList");
        sort("collectionssort_report_base_" + base + "_linkedlist.csv", JAVA_COLLECTIONS_SORT_LINKEDLIST);
        System.out.println("Collections.sort() ArrayList");
        sort("collectionssort_report_base_" + base + "_arraylist.csv", JAVA_COLLECTIONS_SORT_ARRAYLIST);
        System.out.println("Mergesort LinkedList Recursive");
        sort("ms_report_base_" + base + "_linkedlist_recursive.csv", MS_REC_LINKEDLIST);
        System.out.println("Mergesort ArrayList Recursive");
        sort("ms_report_base_" + base + "_arraylist_recursive.csv", MS_REC_ARRAYLIST);
    }
}
