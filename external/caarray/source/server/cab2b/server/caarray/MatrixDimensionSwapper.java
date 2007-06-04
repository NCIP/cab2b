package cab2b.server.caarray;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

public class MatrixDimensionSwapper {
    private static class Index {
        private int i;

        private int maxVal;

        private String label;

        Index(int maxVal, String label) {
            i = 0;
            this.maxVal = maxVal;
            this.label = label;
        }

        int value() {
            return i;
        }

        void inc() {
            if (i == maxVal - 1) {
                throw new IllegalStateException();
            }
            i++;
        }

        boolean canInc() {
            return i < maxVal - 1;
        }

        void reset() {
            i = 0;
        }

        boolean lessThan(int j) {
            return i < j;
        }

        @Override
        public String toString() {
            return label + " : " + String.valueOf(i);
        }

    }

    private static Object[][] swapDimensions(Object[][] src) {
        Object[][] dest = new Object[src[0].length][src.length];
        for (int i = 0; i < dest.length; i++) {
            for (int j = 0; j < dest[0].length; j++) {
                dest[i][j] = src[j][i];
            }
        }
        return dest;
    }

    /**
     * @param src
     * @param indexMap
     *            [2, 0, 1] means 0th dimension of dest is 2nd dimension of src
     *            etc...
     * @return
     */

    public static Object swapDimensions(Object src, int[] dimensionMap) {
        final int n = dimensionMap.length;

        int[] srcDims = new int[n];

        Object temp = src;
        for (int i = 0; i < n; i++) {
            if (temp == null || !temp.getClass().isArray()) {
                throw new IllegalArgumentException();
            }
            Object[] arr = (Object[]) temp;
            srcDims[i] = arr.length;
            temp = Array.get(arr, 0);
        }
        int[] destDims = new int[n];
        for (int i = 0; i < n; i++) {
            destDims[i] = srcDims[dimensionMap[i]];
        }
        Object dest = Array.newInstance(Object.class, destDims);

        Index[] indices = new Index[n];
        for (int i = 0; i < indices.length; i++) {
            indices[i] = new Index(destDims[i], "d" + i);
        }
        Map<Index, Index> destToSrcIndexMap = new HashMap<Index, Index>();
        for (int i = 0; i < dimensionMap.length; i++) {
            destToSrcIndexMap.put(indices[dimensionMap[i]], indices[i]);
        }

        while (true) {
            // innermost FOR loop (if there were 'numDimensions' FOR loops)
            Object destArr = dest, srcArr = src;
            // get the 1-D array
            for (int i = 0; i < n - 1; i++) {
                destArr = Array.get(destArr, indices[i].value());
                srcArr = Array.get(srcArr,
                                   destToSrcIndexMap.get(indices[i]).value());
            }
            // copy value
            Object value = Array.get(
                                     srcArr,
                                     destToSrcIndexMap.get(indices[n - 1]).value());
            Array.set(destArr, indices[n - 1].value(), value);
            // end innermost FOR loop

            // find index that can be incremented
            Index incrementableIndex = null;
            for (int i = n - 1; i >= 0; i--) {
                if (indices[i].canInc()) {
                    // this counter can be incremented...
                    incrementableIndex = indices[i];
                    break;
                }
                // cannot increment, so reset.
                indices[i].reset();
            }
            if (incrementableIndex == null) {
                break;
            }
            incrementableIndex.inc();
        }

        return dest;
    }

    private static Object[][][] swapDimensions(Object[][][] src, int[] indexMap) {
        // TODO validate input.
        int srcDim1 = src.length;
        int srcDim2 = src[0].length;
        int srcDim3 = src[0][0].length;

        int[] srcDims = new int[] { srcDim1, srcDim2, srcDim3 };
        int[] destDims = new int[] { srcDims[indexMap[0]], srcDims[indexMap[1]], srcDims[indexMap[2]] };
        Object[][][] dest = new Object[destDims[0]][destDims[1]][destDims[2]];

        Index i = new Index(destDims[0], "d0");
        Index j = new Index(destDims[1], "d1");
        Index k = new Index(destDims[2], "d2");

        Index[] myInts = new Index[] { i, j, k };
        Map<Index, Index> map = new HashMap<Index, Index>();
        // map.put(0, myInts[indexMap[0]]);
        // map.put(1, myInts[indexMap[1]]);
        // map.put(2, myInts[indexMap[2]]);

        for (int x = 0; x < indexMap.length; x++) {
            map.put(myInts[indexMap[x]], myInts[x]);
        }

        // dest[i][j][k] = src[k][i][j]
        for (i.reset(); i.canInc(); i.inc()) {
            for (j.reset(); j.canInc(); j.inc()) {
                for (k.reset(); k.canInc(); k.inc()) {
                    System.out.println(i + " " + j + " " + k);
                    dest[i.value()][j.value()][k.value()] = src[map.get(i).value()][map.get(
                                                                                            j).value()][map.get(
                                                                                                                k).value()];
                }
            }
        }

        return dest;
    }

    public static void main(String[] args) {
        int[] indexMap = new int[] { 0, 2, 1 };

        Object[][][] src = new Object[][][] { { { 1, 2, 3 }, { 4, 5, 6 } }, { { 7, 8, 9 }, { 10, 11, 12 } } };

        // Object[][][] src = new Object[1][5][12000];
        //
        // for (int j = 0; j < 5; j++) {
        // for (int k = 0; k < 12000; k++) {
        // src[0][j][k] = j * k;
        // }
        // }
        // print(src);
        // Object[][][] dest = swapDimensions(src, indexMap);
        // print(dest);

        Object o = src;
        print(src);
        print((Object[][][]) swapDimensions(o, indexMap));
        // Object[][] src = new Object[][] { { 1, 2, 3 }, { 4, 5, 6 } };
        // print(src);
        // print(swapDimensions(src));
    }

    private static void print(Object[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                System.out.print(arr[i][j] + " ");
            }
            System.out.println();

        }
        System.out.println("_________________________________________");
    }

    private static void print(Object[][][] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.println("Dim0 : " + i);
            for (int j = 0; j < arr[0].length; j++) {
                for (int k = 0; k < arr[0][0].length; k++) {
                    System.out.print(arr[i][j][k] + " ");
                }
                System.out.println();
            }
        }
        System.out.println("_________________________________________");
    }
}
