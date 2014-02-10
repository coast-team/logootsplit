package logootsplito;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Stephane Martin <stephane@stephanemartin.fr>
 */
public class Identifier implements Comparable, Iterable, Serializable {

    List<Integer> base;
    Integer last;

    /**
     * -1 this< t 0 this=t 1 this > t
     *
     * @param t
     * @return
     */
    public Identifier(List<Integer> base) {
        this.base = base;
    }

    public Identifier(List<Integer> base, Integer u) {
        this.base = base;
        this.last = u;
    }

    @Override
    public int compareTo(Object t) {
        if (t instanceof Identifier) {
            return Identifier.compareTo(this.iterator(), ((Identifier) t).iterator());
        }
        throw new UnsupportedOperationException("Not supported yet, identifier is not a " + t.getClass().getName());
    }

    /**
     * compare S1 and S2
     *
     * @param s1
     * @param s2
     * @return -1 if s1<s2 ; 0 if s1==s2 ; 1 if s1>s2
     */
    private static int compareTo(Iterator<Integer> s1, Iterator<Integer> s2) {
        while (s1.hasNext() && s2.hasNext()) {
            int b1 = s1.next();
            int b2 = s2.next();
            if (b1 < b2) {
                return -1;
            }
            if (b1 > b2) {
                return 1;
            }
        }
        /* s1 is longer than s2 */
        if (s1.hasNext()) {
            return 1;
        }
        /* s2 is longer than s1 */
        if (s2.hasNext()) {
            return -1;
        }
        // Both identifiers have same size
        return 0;
    }

    public List<Integer> getBase() {
        return base;
    }

    public Integer getLast() {
        return last;
    }

    @Override
    public Iterator iterator() {
        return new Iterator_a(base.iterator(), this.last);
    }

    private static class Iterator_a implements Iterator {

        private Iterator it;
        private Object more;
        private Object nexte;

        public Iterator_a(Iterator it, Object more) {
            this.it = it;
            this.more = more;
            loadNext();
        }

        private void loadNext() {
            if (it.hasNext()) {
                nexte = it.next();
            } else {
                nexte = more;
                more = null;
            }
        }

        @Override
        public boolean hasNext() {
            return nexte != null;
        }

        @Override
        public Object next() {
            Object ret = nexte;
            loadNext();
            return ret;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not supported");
        }
    }

    @Override
    public String toString() {
        return "Identifiant{" + base + "," + last + '}';
    }

    boolean hasPlaceAfter(Identifier next, int lenght) {
        int max = lenght + last;
        Iterator<Integer> i = this.base.iterator();
        Iterator<Integer> i2 = next.iterator();
        while (i.hasNext() && i2.hasNext()) {
            if (!i.next().equals(i2.next())) {
                return true;
            }
        }

        if (i2.hasNext()) {
            return i2.next() >= max;
        } else {
            return true;
        }
    }

    boolean hasPlaceBefore(Identifier prev, int lenght) {
        int min = last - lenght;
        Iterator<Integer> i = this.base.iterator();
        Iterator<Integer> i2 = prev.iterator();
        while (i.hasNext() && i2.hasNext()) {
            if (!i.next().equals(i2.next())) {
                return true;
            }
        }

        if (i2.hasNext()) {
            return i2.next() < min;
        } else {
            return true;
        }
    }

    int minOffsetAfterPrev(Identifier prev, int min) {
        Iterator<Integer> i = this.base.iterator();
        Iterator<Integer> i2 = prev.iterator();
        while (i.hasNext() && i2.hasNext()) {
            if (!i.next().equals(i2.next())) {
                return min;
            }
        }

        if (i2.hasNext()) {
            return Math.max(i2.next(), min);
        } else {
            return min;
        }

    }

    /**
     *
     *
     * @param l
     * @param l2
     * @return
     */
    int maxOffsetBeforeNex(Identifier next, int max) {
        Iterator<Integer> i = this.base.iterator();
        Iterator<Integer> i2 = next.iterator();
        while (i.hasNext() && i2.hasNext()) {
            if (!i.next().equals(i2.next())) {
                return max;
            }
        }

        if (i2.hasNext()) {
            return Math.min(i2.next(), max);
        } else {
            return max;
        }

    }
}
