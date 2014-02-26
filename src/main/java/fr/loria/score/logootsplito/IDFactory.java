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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

    public class IDFactory {

    private static Random rnd = new Random();

    public static List<Integer> createBetweenPosition(Identifier id1, Identifier id2, int replicaNumber, int clock) {
        Iterator<Integer> s1 = new IDFactory.InfiniteString((int) (Integer.MIN_VALUE + 1), id1 != null ? id1.iterator() : null);
        Iterator<Integer> s2 = new IDFactory.InfiniteString((int) (Integer.MAX_VALUE), id2 != null ? id2.iterator() : null);
        LinkedList<Integer> sb = new LinkedList();

        do {
            long b1 = s1.next();
            long b2 = s2.next();
            if (b2 - b1 > 2) {
                if (replicaNumber <= b1 || replicaNumber >= b2) {
                    int r = ((int) ((rnd.nextDouble() * (b2 - b1 - 2)) + b1)) + 1;
                    sb.addLast(r);
                }
                break;
            } else {
                sb.addLast((int) b1);
            }
        } while (true);

        sb.add(replicaNumber);
        sb.add(clock);

        return sb;
    }

    private static class InfiniteString implements Iterator<Integer> {

        private Iterator<Integer> it;
        private int ch;

        public InfiniteString(int ch, Iterator<Integer> it) {
            this.ch = ch;
            this.it = it;
        }

        @Override
        public boolean hasNext() {
            return true;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Integer next() {
            if (it != null && it.hasNext()) {
                return it.next();
            } else {
                return ch;
            }
        }
    }
}
