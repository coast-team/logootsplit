package logootsplitO;

import bridge.TextOperation;
import crdt.Operation;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Stephane Martin <stephane@stephanemartin.fr>
 */
public class LogootSDocumentD implements LogootSDoc, Serializable {

    private int clock = 0;
    private HashMap<List<Integer>, LogootSBlockLight> mapBaseToBlock = new HashMap<List<Integer>, LogootSBlockLight>(); //for test
    private ArrayList<LinkBlock> list = new ArrayList<LinkBlock>();//dichotomic ready
    private StringBuilder view = new StringBuilder();
    private int replicaNumber = 0;

    @Override
    public void setReplicaNumber(int i) {
        this.replicaNumber = i;
    }

    public static class LinkBlock implements Comparable, Serializable {

        LogootSBlockLight<Object> block;
        int offset;

        public LinkBlock(LogootSBlockLight block, int offset) {
            this.block = block;
            this.offset = offset;
        }

        @Override
        public int compareTo(Object t) {
            if (t instanceof LinkBlock) {
                this.getID().compareTo(((LinkBlock) t).getID());
            }
            throw new UnsupportedOperationException("Bad comparaison"); //To change body of generated methods, choose Tools | Templates.
        }

        public LogootSBlock getBlock() {
            return block;
        }

        public Identifier getID() {
            return new Identifier(this.block.id.getBase(), offset);
        }

        @Override
        public String toString() {
            return "L{" + block.id.base + "," + offset + '}';
        }
    }

    @Override
    public String view() {
        return view.toString();
    }

    @Override
    public int viewLength() {
        return view.length();
    }

    /**
     * search a position
     */
    int dicSearch(Identifier id, int min) {
        //int min = 0;
        int max = list.size() - 1;
        while (min <= max) {
            int i = (int) (min + max) / 2;

            int p = list.get(i).getID().compareTo(id);
            //int p = list.get(i).getID().compareTo(block.id.
            if (p < 0) {
                min = i + 1;
            } else if (p > 0) {
                max = i - 1;
            } else {
                min = i;
                break;
            }

        }
        return min;

    }

    /**
     * 
     *
     * @param l
     * @param l2
     * @return
     */
    static int maxOffsetBeforeNex(Identifier bI, Identifier nex, int max) {
        Iterator<Integer> i = bI.base.iterator();
        Iterator<Integer> i2 = nex.iterator();
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

    /**
     *
     * @param pos
     * @param block
     * @return inserted block
     */
    public void addBlock(LogootSBlockLight block, int begin, List elem) {

        int offset = begin;
        int pos = 0;
        int end = begin + elem.size() - 1;
        
        
        while (offset <= end) {
            //search the first position
            pos = dicSearch(block.getId().getBaseId(offset), pos);
            //computation of offset Max 
            int offsetMax;
            if (pos < list.size()) {
                Identifier beginId = new Identifier(block.id.base, offset);
                offsetMax = maxOffsetBeforeNex(beginId, list.get(pos).getID(), end);
            } else {
                offsetMax = end;
            }

            add(pos, block, offset, elem.subList(offset - begin, offsetMax - begin + 1));
            offset = offsetMax + 1;
            /* for (; offset <= offsetMax; offset++) {
             add(pos, block, offset, it.next());
             pos++;
             }*/
        }

    }

   private ArrayList makeOffsets(LogootSBlockLight block, int offset, List o) {
        ArrayList l = new ArrayList(o.size());
        for (int i = 0; i < o.size(); i++) {
            l.add(new LinkBlock(block, offset++));
        }
        return l;
    }

   /* private LinkBlock[] makeOffsets(LogootSBlock block, int offset, List o) {
        LinkBlock[] l=new LinkBlock[o.size()];
        for (int i = 0; i < o.size(); i++) {
            l[i]=new LinkBlock(block, offset++);
        }
        return l;
    }*/
    
    private char[] makeChar(List<Character> o) {
        char[] ret = new char[o.size()];

        for (int i = 0; i < o.size(); i++) {
            ret[i] = o.get(i);
        }
        
        return ret;
    }

    private void add(int pos, LogootSBlockLight block, int offset, List o) {

        list.addAll(pos, makeOffsets(block, offset, o));

        view.insert(pos, makeChar(o));


    }

    @Override
    public List<TextOperation> addBlock(Identifier id, List l) {
        LogootSBlockLight block = mapBaseToBlock.get(id.base);
        IdentifierInterval idi = new IdentifierInterval(id.base, id.last, id.last + l.size() - 1);
        if (block == null) {
            block = new LogootSBlockLight(idi, l.size());//TODO build factory
            mapBaseToBlock.put(id.base, block);
        } else {
            block.addBlock(id.last, l);
        }
        addBlock(block, id.last, l);
        return new ArrayList<TextOperation>();//only for compilation
    }

    public void delBlock(LogootSBlock block, int begin, int end) {

        int offset = begin;
        int pos = 0;
        LinkBlock lb;
        int nbElement = 0;
        while (offset <= end) {
            //search the first position.
            pos = dicSearch(block.getId().getBaseId(offset), pos);
            if (pos >= list.size()) {
                break;
            }
            lb = list.get(pos);
            //while we are in block
            if (lb.getBlock() != block) {//element does not existing
                pos++;
                offset++;
            } else {
                int b = pos;
                int e = pos;
                do {
                    if (lb.offset != offset) {
                        offset = lb.offset;
                    } else {
                        /* list.remove(pos);
                         view.deleteCharAt(pos);*/
                        e++;
                        offset++;
                        pos++;
                        nbElement++;
                        lb = pos < list.size() ? list.get(pos) : null;

                    }
                } while (lb != null && lb.getBlock() == block && offset <= end);
                if (e - b > 0) {
                    list.subList(b, e).clear();
                   // list.delRange(b, e);
                    view.delete(b, e);
                    pos = b;
                }
            }
        }
        block.delBlock(begin, end, nbElement);
        if (block.numberOfElements() == 0) {// little garbage collection
            this.mapBaseToBlock.remove(block.getId().getBase());
        }

    }

    @Override
    public List<TextOperation> delBlock(IdentifierInterval id) {
        LogootSBlock block = mapBaseToBlock.get(id.base);
        if (block != null) {
            delBlock(block, id.begin, id.end);
        }
        return new ArrayList<TextOperation>();//only for compilation
    }

    @Override
    public void apply(Operation op) {
    }

    @Override
    public LogootSOpAdd insertLocal(int pos, List l) {
        LinkBlock after = pos < list.size() ? list.get(pos) : null;
        LinkBlock before = pos > 0 ? list.get(pos - 1) : null;
        int offset;
        LogootSBlockLight block;
        if (after != null && after.block.id.begin==after.offset && after.block.mine && after.block.getId().begin - l.size() > Integer.MIN_VALUE) {// Block in position is mine
            //add before block
            block = after.block;
            //offset = after.offset;
            offset = block.id.begin - l.size();
            block.addBlock(offset, l);
        } else if (before != null && before.block.id.end==before.offset && before.block.mine && before.block.getId().end + l.size() < Integer.MAX_VALUE) {
            //add after block
            block = before.block;
            offset = block.id.end+1;
            //offset = block.id.begin - l.size();
            block.addBlock(offset, l);

        } else {
            // create new block

            List<Integer> base = IDFactory.createBetweenPosition(before == null ? null : before.getID(),
                    after == null ? null : after.getID(), replicaNumber, clock++);

            IdentifierInterval id = new IdentifierInterval(base, 0, l.size() - 1);
            block = new LogootSBlockLight(id);//TODO build factory
            block.setMine(true);
            offset = 0;
            block.addBlock(offset, l);
            mapBaseToBlock.put(block.getId().getBase(), block);
        }
        Identifier idi = new Identifier(block.getId().base, offset);
        //int i = pos;


        add(pos, block, offset, l);
        offset += l.size();
        //    i++;


        return new LogootSOpAdd(idi, l);
    }

    /**
     * Delete local begin inclusive to end inclusive
     *
     * @param begin
     * @param end
     * @return operation to make to other
     */
    @Override
    public LogootSOpDel delLocal(int begin, int end) {
        List<IdentifierInterval> li = new LinkedList<IdentifierInterval>();
        LinkBlock lb = list.get(begin);
        LogootSBlock block = lb.getBlock();
        int b = lb.offset;
        int e = b;
        int nbElement = 0;
        int i = begin;
        do {
            lb = list.get(i);
            if (lb.block != block) {
                addDelIdf(block, b, e, li, nbElement);
                block = lb.block;
                b = lb.offset;
                nbElement = 0;
            }
            e = lb.offset;
            
            i++;
            nbElement++;
            
        } while (i <= end);
        addDelIdf(block, b, e, li, nbElement);
        view.delete(begin, end + 1);
        list.subList(begin, end+1).clear();
        //list.delRange(begin,  end+1);
        return new LogootSOpDel(li);
    }

    private void addDelIdf(LogootSBlock block, int begin, int end, List<IdentifierInterval> li, int nbElement) {
        li.add(new IdentifierInterval(block.id.base, begin, end));
        block.delBlock(begin, end, nbElement);
        if (block.numberOfElements() == 0) {
            mapBaseToBlock.remove(block.getId().getBase());
        }
    }

    @Override
    public LogootSDoc create() {
        return new LogootSDocumentD();
    }

    /**
     * For test
     */
    public ArrayList<LinkBlock> getList() {
        return list;
    }

    public StringBuilder getView() {
        return view;
    }

    public HashMap<List<Integer>, LogootSBlockLight> getMapBaseToBlock() {
        return mapBaseToBlock;
    }
}
