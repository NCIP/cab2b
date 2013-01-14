/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package cab2b.server.caarray.resulttransformer;

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

    /**
     * @param src
     * @param indexMap [2, 0, 1] means 0th dimension of dest is 2nd dimension of
     *            src etc...
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
                srcArr = Array.get(srcArr, destToSrcIndexMap.get(indices[i]).value());
            }
            // copy value
            Object value = Array.get(srcArr, destToSrcIndexMap.get(indices[n - 1]).value());
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
}
