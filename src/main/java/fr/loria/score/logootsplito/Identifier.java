/**
 * LogootSplit
 * https://bitbucket.org/oster/logootsplit/
 * Copyright (c) 2014, LORIA / Inria / SCORE Team, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library. If not, see <http://www.gnu.org/licenses/>.
 */
package fr.loria.score.logootsplito;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Identifier implements Comparable, Iterable, Serializable {

    List<Integer> base;
    Integer last;


    public Identifier() {

    }

    /**
     * -1 this< t 0 this=t 1 this > t
     */
    public Identifier(List<Integer> base) {
        this.base = base;
    }

    public Identifier(List<Integer> base, Integer u) {
        this.base = base;
        this.last = u;
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

    @Override
    public int compareTo(Object t) {
        if (t instanceof Identifier) {
            return Identifier.compareTo(this.iterator(), ((Identifier) t).iterator());
        }
        throw new UnsupportedOperationException("Not supported yet, identifier is not a " + t.getClass().getName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Identifier that = (Identifier) o;

        if (base != null ? !base.equals(that.base) : that.base != null) return false;
        if (last != null ? !last.equals(that.last) : that.last != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = base != null ? base.hashCode() : 0;
        result = 31 * result + (last != null ? last.hashCode() : 0);
        return result;
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

    @Override
    public String toString() {
        return "Identifier{" + base + "," + last + '}';
    }

    public Identifier copy() {
        Identifier o = new Identifier();
        o.base = new LinkedList(base);
        o.last = last;
        return o;
    }

    boolean hasPlaceAfter(Identifier next, int length) {
        int max = length + last;
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

    boolean hasPlaceBefore(Identifier prev, int length) {
        int min = last - length;
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
}
