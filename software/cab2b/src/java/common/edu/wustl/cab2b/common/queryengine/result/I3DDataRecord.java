/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.common.queryengine.result;

import java.io.Serializable;
import java.util.List;

/**
 * Represents a record that contains additional three dimensional data.
 * 
 * @author srinath_k
 * 
 */
public interface I3DDataRecord extends IRecord {
    Object[][][] getCube();

    String[] getDim1Labels();

    String[] getDim2Labels();

    String[] getDim3Labels();

    /**
     * Specifies the parameters that should be used for lazily initializing a
     * record of type {@link I3DDataRecord}. It specifies the portions of the
     * 3D array that are to be initialized as a list of {@link Range}s.
     * 
     * @author srinath_k
     * 
     */
    public static class LazyParams implements ILazyParams {

        private static final long serialVersionUID = 3914794104672938082L;

        private List<Range> ranges;

        public LazyParams(List<Range> ranges) {
            this.ranges = ranges;
        }

        public List<Range> getRanges() {
            return ranges;
        }

        /**
         * Represents a "range" in a 3D array, defined by the start and end
         * indexes of each of the three dimensions.
         * <p>
         * Suppose an instance of this class is defined as
         * <code>si, di, sj, dj, sk, dk</code>. In this case, the cells that
         * are represented by this instance are <br>
         * <code>{(i, j ,k) : si &le; i &lt di and sj &le; j &lt dj and sk &le; k &lt dk}</code>
         * 
         * @author srinath_k
         * 
         */
        public static class Range implements Serializable {
            private static final long serialVersionUID = 6666813239974896969L;

            private int si, di, sj, dj, sk, dk;

            public Range(int si, int di, int sj, int dj, int sk, int dk) {
                this.si = si;
                this.di = di;
                this.sj = sj;
                this.dj = dj;
                this.sk = sk;
                this.dk = dk;
            }

            public int getDi() {
                return di;
            }

            public int getDj() {
                return dj;
            }

            public int getDk() {
                return dk;
            }

            public int getSi() {
                return si;
            }

            public int getSj() {
                return sj;
            }

            public int getSk() {
                return sk;
            }

        }
    }
}
