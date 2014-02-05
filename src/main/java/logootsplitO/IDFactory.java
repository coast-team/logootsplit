package logootsplitO;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Stephane Martin <stephane@stephanemartin.fr>
 */
public class IDFactory implements Serializable {

    public static final boolean alea = false;
    static Random rnd = new Random();

    static public List<Integer> createBetweenPosition(Identifier id1, Identifier id2, int replicaNumber, int clock) {
        Iterator<Integer> s1;
        Iterator<Integer> s2;
        s1 = new IDFactory.infinitString((int) (Integer.MIN_VALUE + 1), id1 != null ? id1.iterator() : null);
        s2 = new IDFactory.infinitString((int) (Integer.MAX_VALUE), id2 != null ? id2.iterator() : null);
        LinkedList<Integer> sb = new LinkedList();

        do {
            long b1 = s1.next();
            long b2 = s2.next();
            if (b2 - b1 > 2) {
                if (replicaNumber <= b1 || replicaNumber >= b2) {

                    int r = ((int) ((rnd.nextDouble() * (b2 - b1 - 2)) + b1)) + 1;

                    //sb.addLast(new Integer((int) ((b1 + b2) / (long) 2)));
                    sb.addLast(r);
                }
                break;
            } else {
                sb.addLast((int) b1);
            }
        }while(true);

        // sb.addLast(Integer.MIN_VALUE);
        //sb.addAll(Integer.toHexString(id.getReplicaNumber()).getBytes()); 
        sb.add(replicaNumber);
        // sb.addLast(Integer.MIN_VALUE);
        sb.add(clock);

        return sb;
    }

    static class infinitString implements Iterator<Integer> {

        Iterator<Integer> it;
        int ch;

        public infinitString(int ch, Iterator<Integer> it) {
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
