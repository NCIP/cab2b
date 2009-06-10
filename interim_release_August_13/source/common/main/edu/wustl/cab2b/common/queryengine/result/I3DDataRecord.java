package edu.wustl.cab2b.common.queryengine.result;

import java.io.Serializable;
import java.util.List;

public interface I3DDataRecord extends IRecord {
    Object[][][] getCube();

    String[] getDim1Labels();

    String[] getDim2Labels();

    String[] getDim3Labels();

    public static class LazyParams implements ILazyParams {

        private static final long serialVersionUID = 3914794104672938082L;

        private List<Range> ranges;

        public LazyParams(List<Range> ranges) {
            this.ranges = ranges;
        }

        public List<Range> getRanges() {
            return ranges;
        }

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
