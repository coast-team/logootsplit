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
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LogootSRopes<T> implements LogootSDoc<T>, Serializable {

    private static final long serialVersionUID = 5787396481247100129L;
    
    int replicaNumber = 0;
    int clock = 0;
    RopesNodes root = null;
    private HashMap<List<Integer>, LogootSBlock> mapBaseToBlock = new HashMap<List<Integer>, LogootSBlock>();


    public LogootSRopes() {

    }


    HashMap<List<Integer>, LogootSBlock> getMapBaseToBlock() {
        return mapBaseToBlock;
    }

    public LogootSBlock getBlock(IdentifierInterval id) {
        LogootSBlock ret = mapBaseToBlock.get(id.base);
        if (ret == null) {
            ret = new LogootSBlock<String>(id);
            mapBaseToBlock.put(id.base, ret);
        }
        return ret;
    }

    @Override
    public List<TextOperation> addBlock(Identifier id, List<T> str) {
        List<TextOperation> l = new ArrayList<TextOperation>();
        IdentifierInterval idi = new IdentifierInterval(id.base, id.last, id.last + str.size() - 1);
        if (root == null) {
            LogootSBlock bl = new LogootSBlock(idi);
            mapBaseToBlock.put(bl.id.base, bl);
            root = new RopesNodes(str, id.getLast(), bl);
            l.add(new TextInsert(0, Utils.convertCharactersListToString(str)));
            return l;
        } else {
            return addBlock(idi, str, root, 0);
        }
    }

    List<TextOperation> addBlock(IdentifierInterval idi, List<T> str, RopesNodes from, int startOffset) {
        LinkedList<RopesNodes> path = new LinkedList();
        LinkedList<RopesNodes> path2;
        List<TextOperation> result = new ArrayList<TextOperation>();
        boolean con = true;
        int i = startOffset;
        while (con) {
            path.add(from);
            IteratorHelperIdentifier ihi = new IteratorHelperIdentifier(idi, from.getIdentifierInterval());
            int split;
            switch (ihi.computeResults()) {
                case B1AfterB2:
                    if (from.getRight() == null) {
                        from.setRight(new RopesNodes(str, idi.getBegin(), getBlock(idi)));
                        i = i + from.getSizeNodeAndChildren(0) + from.getSize();
                        result.add(new TextInsert(i, Utils.convertCharactersListToString(str)));
                        con = false;
                    } else {
                        i = i + from.getSizeNodeAndChildren(0) + from.getSize();
                        from = from.getRight();
                    }
                    break;
                case B1BeforeB2:
                    if (from.getLeft() == null) {
                        from.setLeft(new RopesNodes(str, idi.getBegin(), getBlock(idi)));
                        result.add(new TextInsert(i, Utils.convertCharactersListToString(str)));
                        con = false;
                    } else {
                        from = from.getLeft();
                    }
                    break;
                case B1InsideB2: //split b2 the object node
                    split = Math.min(from.maxOffset(), ihi.getNextOffset());
                    RopesNodes rp = new RopesNodes(str, idi.getBegin(), getBlock(idi));
                    path.add(from.split(split - from.offset + 1, rp));
                    i=i+from.getSizeNodeAndChildren(0);
                    result.add(new TextInsert(i+split-from.offset+1, Utils.convertCharactersListToString(str)));
                    con = false;
                    break;
                case B2insideB1: // split b1 the node to insert
                    int split2 = /*Math.min(idi.getEnd(), */ ihi.getNextOffset()/*)*/;

                    List ls = str.subList(0, split2 + 1 - idi.getBegin());
                    IdentifierInterval idi1 = new IdentifierInterval(idi.base, idi.getBegin(), split2);
                    if (from.getLeft() == null) {
                        from.setLeft(new RopesNodes(ls, idi1.getBegin(), getBlock(idi1)));
                        result.add(new TextInsert(i, Utils.convertCharactersListToString(ls)));
                    } else {
                        result.addAll(addBlock(idi1, ls, from.getLeft(), i));
                    }

                    
                    //i=i+ls.size();

                    ls = str.subList(split2 + 1 - idi.getBegin(), str.size());
                    idi1 = new IdentifierInterval(idi.base, split2 + 1, idi.end);
                    i = i + from.getSizeNodeAndChildren(0) + from.getSize();
                    if (from.getRight() == null) {
                        from.setRight(new RopesNodes(ls, idi1.getBegin(), getBlock(idi1)));
                        result.add(new TextInsert(i, Utils.convertCharactersListToString(ls)));
                    } else {
                        result.addAll(addBlock(idi1, ls, from.getRight(), i));
                    }
                    con = false;
                    break;
                case B1concatB2: //node to insert concat the node
                    if (from.getLeft() != null) {
                    	path2 = new LinkedList<RopesNodes>(path);
                        path2.add(from.getLeft());
                        getXest(RopesNodes.RIGHT, path2);

                        split = from.getIdBegin().minOffsetAfterPrev(path2.getLast().getIdEnd(), idi.getBegin());
                        List l = new ArrayList(str.subList(split - idi.getBegin(), str.size()));
                        from.appendBegin(l);
                        result.add(new TextInsert(i+ from.getSizeNodeAndChildren(0), Utils.convertCharactersListToString(l)));


                        ascendentUpdate(path, l.size());
                        str = new ArrayList(str.subList(0, split - idi.getBegin()));
                        idi = new IdentifierInterval(idi.base, idi.begin, split-1);

                        //check if previous is smaller or not
                        if (idi.end >= idi.begin) {
                            from = from.getLeft();
                        } else {
                            con = false;
                            break;
                        }
                    } else {
                        result.add(new TextInsert(i, Utils.convertCharactersListToString(str)));
                        from.appendBegin(str);
                        ascendentUpdate(path, str.size());
                        con = false;
                        break;
                    }


                    break;
                case B2ConcatB1://concat at end
                    if (from.getRight() != null) {
                    	path2 = new LinkedList<RopesNodes>(path);
                        path2.add(from.getRight());
                        getXest(RopesNodes.LEFT, path2);

                        split = from.getIdEnd().maxOffsetBeforeNex(path2.getLast().getIdBegin(), idi.getEnd());
                        List l = new ArrayList(str.subList(0, split + 1 - idi.getBegin()));
                        i = i + from.getSizeNodeAndChildren(0) + from.getSize();
                        from.appendEnd(l);
                        result.add(new TextInsert(i, Utils.convertCharactersListToString(l)));

                        ascendentUpdate(path, l.size());
                        str = new ArrayList(str.subList(split + 1 - idi.getBegin(), str.size()));
                        idi = new IdentifierInterval(idi.base, split + 1, idi.end);
                        if (idi.end >= idi.begin) {
                            from = from.getRight();
                            i = i + l.size();
                        } else {
                            con = false;
                            break;
                        }
                    } else {
                        i = i + from.getSizeNodeAndChildren(0) + from.getSize();
                        result.add(new TextInsert(i, Utils.convertCharactersListToString(str)));
                        from.appendEnd(str);
                        ascendentUpdate(path, str.size());
                        con = false;
                        break;
                    }

                    break;
                default:
                    throw new UnsupportedOperationException("Not implemented yet");
            }
        }
        balance(path);
        return result;

    }

    boolean searchFull(RopesNodes node, Identifier id, LinkedList<RopesNodes> path) {
        if (node == null) {
            return false;
        }
        path.add(node);
        if (node.getIdBegin().compareTo(id) == 0
                || searchFull(node.getLeft(), id, path)
                || searchFull(node.getRight(), id, path)) {
            return true;
        }
        path.removeLast();
        return false;
    }

    RopesNodes mkNode(Identifier id1, Identifier id2, List l) {
        List<Integer> base = IDFactory.createBetweenPosition(id1, id2, replicaNumber, clock++);
        IdentifierInterval idi = new IdentifierInterval(base, 0, l.size() - 1);
        LogootSBlock newBlock = new LogootSBlock(idi);
        mapBaseToBlock.put(idi.base, newBlock);
        return new RopesNodes(l, 0, newBlock);
    }
    //Todo: improve readability with search function

    @Override
    public LogootSOperation insertLocal(int pos, List l) {
        if (root == null) {//empty tree
            root = mkNode(null, null, l);
            root.block.setMine(true);
            return new LogootSAdd(root.getIdBegin(), l);
        } else {
            RopesNodes newNode;
            int length = this.viewLength();
            LinkedList<RopesNodes> path;
            if (pos == 0) {//begin of string
                path = new LinkedList();
                path.add(root);
                RopesNodes n = getXest(RopesNodes.LEFT, path);
                if (n.isAppendableBefore()) {
                    Identifier id = n.appendBegin(l);
                    ascendentUpdate(path, l.size());
                    return new LogootSAdd(id, l);
                } else {//add node
                    newNode = mkNode(null, n.getIdBegin(), l);
                    newNode.block.setMine(true);
                    n.setLeft(newNode);
                }
            } else if (pos >= length) {//end
                path = new LinkedList();
                path.add(root);
                RopesNodes n = getXest(RopesNodes.RIGHT, path);
                if (n.isAppendableAfter()) {//append
                    Identifier id = n.appendEnd(l);
                    ascendentUpdate(path, l.size());
                    return new LogootSAdd(id, l);
                } else {//add at end
                    newNode = mkNode(n.getIdEnd(), null, l);
                    newNode.block.setMine(true);
                    n.setRight(newNode);
                }

            } else {//middle
                ResponseIntNode inPos = search(pos);
                if (inPos.getI() > 0) {//split
                    Identifier id1 = inPos.getNode().block.id.getBaseId(inPos.getNode().offset + inPos.getI() - 1);
                    Identifier id2 = inPos.getNode().block.id.getBaseId(inPos.getNode().offset + inPos.getI());
                    newNode = mkNode(id1, id2, l);
                    newNode.block.setMine(true);
                    path = inPos.getPath();
                    path.add(inPos.getNode().split(inPos.getI(), newNode));
                } else {
                    ResponseIntNode prev = search(pos - 1);
                    if (inPos.getNode().isAppendableBefore() && inPos.getNode().getIdBegin().hasPlaceBefore(prev.getNode().getIdEnd(), l.size())) {//append before
                        Identifier id = inPos.getNode().appendBegin(l);
                        ascendentUpdate(inPos.path, l.size());

                        return new LogootSAdd(id, l);
                    } else {

                        if (prev.getNode().isAppendableAfter() && prev.getNode().getIdEnd().hasPlaceAfter(inPos.getNode().getIdBegin(), l.size())) {//append after
                            Identifier id = prev.getNode().appendEnd(l);
                            ascendentUpdate(prev.path, l.size());

                            return new LogootSAdd(id, l);
                        } else {
                            newNode = mkNode(prev.getNode().getIdEnd(), inPos.getNode().getIdBegin(), l);
                            newNode.block.setMine(true);
                            newNode.setRight(prev.getNode().getRight());
                            prev.getNode().setRight(newNode);
                            path = prev.getPath();
                            path.add(newNode);
                        }
                    }
                }
            }
            balance(path);

            return new LogootSAdd(newNode.getIdBegin(), l);
        }
    }

    RopesNodes getXest(int i, RopesNodes n) {
        while (n.getChild(i) != null) {
            n = n.getChild(i);
        }
        return n;
    }

    RopesNodes getXest(int i, LinkedList<RopesNodes> path) {
        RopesNodes n = path.getLast();
        while (n.getChild(i) != null) {
            n = n.getChild(i);
            path.add(n);
        }
        return n;
    }

    int search(Identifier id, LinkedList<RopesNodes> path) {
        int i = 0;

        RopesNodes node = root;
        while (node != null) {
            path.addLast(node);
            if (id.compareTo(node.getIdBegin()) < 0) {
                node = node.getLeft();
            } else if (id.compareTo(node.getIdEnd()) > 0) {
                i = i + node.getSizeNodeAndChildren(0) + node.getSize();
                node = node.getRight();
            } else {
                i = i + node.getSizeNodeAndChildren(0);
                return i;
            }
        }
        return -1;
    }

    ResponseIntNode search(int pos) {
        RopesNodes node = root;
        LinkedList<RopesNodes> path = new LinkedList();
        while (node != null) {
            path.add(node);
            int before = node.getLeft() == null ? 0 : node.getLeft().sizeNodeAndChildren;
            if (pos < before) {//Before
                node = node.getLeft();
            } else if (pos < before + node.str.size()) {
                return new ResponseIntNode(pos - before, node, path);
            } else {
                pos -= before + node.str.size();
                node = node.getRight();
            }
        }
        return null;
    }

    /**
     * @param path   the value of path
     * @param string the value of string
     */
    void ascendentUpdate(LinkedList<RopesNodes> path, int string) {
    	for(int i = path.size()-1; i >= 0; i--) {
    		path.get(i).addString(string);
    	}
    }

    @Override
    public LogootSOperation delLocal(int begin, int end) {
        int length = end - begin + 1;
        List<IdentifierInterval> li = new LinkedList<IdentifierInterval>();
        do {
            ResponseIntNode start = search(begin);

            int be = start.node.offset + start.getI();
            int en = Math.min(be + length - 1, start.node.maxOffset());
            //int i = this.view().length();
            li.add(new IdentifierInterval(start.getNode().block.getId().getBase(), be, en));
            RopesNodes r = start.node.deleteOffsets(be, en);
            length -= en - be + 1;
            //begin -= en - be+1;

            if (start.node.getSize() == 0) {
                delNode(start.getPath());
            } else if (r != null) {//node has been splited
                start.path.add(r);
                balance(start.path);
            } else {
                this.ascendentUpdate(start.path, be - en - 1);
            }
        } while (length > 0);

        return new LogootSDel(li);
    }

    void delNode(LinkedList<RopesNodes> path) {
        RopesNodes node = path.getLast();
        if (node.block.numberOfElements() == 0) {
            this.mapBaseToBlock.remove(node.block.id.base);
        }
        if (node.getRight() == null) {
            if (node == root) {
                root = node.getLeft();
            } else {
                path.removeLast();
                path.getLast().replaceChildren(node, node.getLeft());
            }
        } else if (node.getLeft() == null) {
            if (node == root) {
                root = node.getRight();
            } else {
                path.removeLast();
                path.getLast().replaceChildren(node, node.getRight());
            }
        } else {//two children
            path.add(node.getRight());
            RopesNodes min = getMinPath(path);
            node.become(min);
            path.removeLast();
            path.getLast().replaceChildren(min, min.getRight());
        }
        balance(path);

    }

    RopesNodes getMinPath(LinkedList<RopesNodes> path) {
        RopesNodes node = path.getLast();
        if (node == null) {
            return null;
        }
        while (node.getLeft() != null) {
            node = node.getLeft();
            path.add(node);
        }
        return node;
    }

    RopesNodes getLeftest(RopesNodes node) {
        if (node == null) {
            return null;
        }
        while (node.getLeft() != null) {
            node = node.getLeft();
        }
        return node;
    }

    Identifier getMinId(RopesNodes node) {
        RopesNodes back = getLeftest(node);
        return back != null ? back.getIdBegin() : null;
    }

    /*
     * Balancing 
     */

    /**
     * Balance tree on path ascendent
     *
     * @param path
     */
    void balance(LinkedList<RopesNodes> path) {
        LinkedList<RopesNodes> path2 =/*new LinkedList(*/path;//);
        RopesNodes node = path2.isEmpty() ? null : path2.removeLast();
        RopesNodes father = path2.isEmpty() ? null : path2.getLast();
        while (node != null) {
            node.sumDirectChildren();
            int balance = node.balanceScore();
            while (Math.abs(balance) >= 2) {
                if (balance >= 2) {
                    if (node.getRight() != null && node.getRight().balanceScore() <= -1) {
                        father = rotateRL(node, father);//double left
                    } else {
                        father = rotateLeft(node, father);
                    }
                } else /*if (balance <= -2)*/ {
                    if (node.getLeft() != null && node.getLeft().balanceScore() >= 1) {
                        father = rotateLR(node, father);//Double right
                    } else {
                        father = rotateRight(node, father);
                    }
                }
                path2.addLast(father);
                balance = node.balanceScore();
            }

            node = path2.isEmpty() ? null : path2.removeLast();
            father = path2.isEmpty() ? null : path2.getLast();
        }
    }

    RopesNodes rotateLeft(RopesNodes node, RopesNodes father) {
        RopesNodes r = node.getRight();
        if (node == root) {
            root = r;
        } else {
            father.replaceChildren(node, r);
        }
        node.setRight(r.getLeft());
        r.setLeft(node);
        node.sumDirectChildren();
        r.sumDirectChildren();
        return r;
    }

    RopesNodes rotateRight(RopesNodes node, RopesNodes father) {
        RopesNodes r = node.getLeft();
        if (node == root) {
            root = r;
        } else {
            father.replaceChildren(node, r);
        }
        node.setLeft(r.getRight());
        r.setRight(node);
        node.sumDirectChildren();
        r.sumDirectChildren();
        return r;
    }

    RopesNodes rotateRL(RopesNodes node, RopesNodes father) {
        rotateRight(node.getRight(), node);
        return rotateLeft(node, father);
    }

    RopesNodes rotateLR(RopesNodes node, RopesNodes father) {
        rotateLeft(node.getLeft(), node);
        return rotateRight(node, father);
    }

    @Override
    public List<TextOperation> delBlock(IdentifierInterval id) {
        List<TextOperation> l = new ArrayList<TextOperation>();
        int i;
        while (true) {
            LinkedList<RopesNodes> path = new LinkedList<RopesNodes>();
            if ((i = search(id.getBeginId(), path)) == -1) {
                if (id.getBegin() < id.end) {
                    id = new IdentifierInterval(id.base, id.getBegin() + 1, id.end);
                } else {
                    return l;
                }
            } else {
                RopesNodes node = path.getLast();
                int end = Math.min(id.end, node.maxOffset());
                int pos = i + id.getBegin() - node.offset;
                int length = end - id.getBegin() + 1;
                l.add(new TextDelete(pos, length));
                RopesNodes t = node.deleteOffsets(id.getBegin(), end);
                if (node.getSize() == 0) {//del node
                    delNode(path);
                } else if (t != null) {
                    path.add(t);
                    balance(path);
                } else {
                    ascendentUpdate(path, id.getBegin() - end - 1);
                }
                if (end == id.end) {
                    break;
                } else {
                    id = new IdentifierInterval(id.base, end, id.end);
                }
            }
        }
        return l;
    }

    boolean getNext(LinkedList<RopesNodes> path) {
        RopesNodes node = path.getLast();
        if (node.getRight() == null) {
            if (path.size() > 1) {
                RopesNodes father = path.get(path.size() - 2);
                if (father.getLeft() == node) {
                    path.removeLast();
                    return true;
                }
            }
            return false;
        } else {
            path.add(node.getRight());
            getXest(RopesNodes.LEFT, path);
            return true;
        }
    }

    @Override
    public LogootSDoc<T> duplicate(int newReplicaNumber) {
            LogootSRopes<T> copy = this.copy();
            copy.setReplicaNumber(newReplicaNumber);
            copy.clock = 0;
            return copy;
    }

    public LogootSRopes<T> copy() {
        LogootSRopes<T> o = new LogootSRopes<T>();
        o.clock = this.clock;
        o.root = root != null ? root.copy() : null;
        o.replicaNumber = this.replicaNumber;
        o.mapBaseToBlock = new HashMap<List<Integer>, LogootSBlock>();
        for (Map.Entry<List<Integer>, LogootSBlock> e : this.mapBaseToBlock.entrySet()) {
            List<Integer> key = new LinkedList<Integer>(e.getKey());
            LogootSBlock value = e.getValue().copy();
            o.mapBaseToBlock.put(key, value);
        }

        return o;        
    }

    @Override
    public void setReplicaNumber(int i) {
        this.replicaNumber = i;
    }

    @Override
    public String view() {
        if (root == null) {
            return "";
        }
        StringBuilder ret = new StringBuilder(root.sizeNodeAndChildren);
        LinkedList<RopesNodes> pile = new LinkedList();

        pile.add(root);
        RopesNodes<T> n = root;
        while (pile.size() > 0) {
            while (n.getLeft() != null) {
                pile.addLast(n.getLeft());
                n = n.getLeft();
            }
            do {
                for (T t : n.str) {
                    ret.append(t);
                }
                pile.removeLast();
                if (n.getRight() != null) {
                    pile.addLast(n.getRight());
                    n = n.getRight();
                    break;
                }
                n = pile.size() > 0 ? pile.getLast() : null;
            } while (pile.size() > 0);
        }

        return ret.toString();
    }

    @Override
    public int viewLength() {
        int ret = root == null ? 0 : root.sizeNodeAndChildren;
        return ret;
    }

    public static class RopesNodes<T> implements Serializable {
        public static final int LEFT = 0;
        public static final int RIGHT = 1;
        int offset;
        List<T> str;
        LogootSBlock<String> block;
        private RopesNodes[] childrenLeftRight = new RopesNodes[2];
        private int height = 1;
        private int sizeNodeAndChildren = 0;

        public RopesNodes() {

        }

        public RopesNodes(List<T> str, int offset, LogootSBlock block) {
            this(str, offset, block, true);
        }

        public RopesNodes(List<T> str, int offset, LogootSBlock block, boolean newer) {
            this.str = new ArrayList(str);
            this.block = block;
            this.offset = offset;
            this.sizeNodeAndChildren = str.size();
            if (newer && block != null) {
                block.addBlock(offset, str);
            }
        }

        public Identifier getIdBegin() {
            return this.block.id.getBaseId(offset);
        }

        public Identifier getIdEnd() {
            return this.block.id.getBaseId(offset + str.size() - 1);
        }

        public RopesNodes<T> copy(){
            RopesNodes<T> o=new RopesNodes<T>();
            o.str = new ArrayList<T>(str); // do not need to clone value since they are atoms (which are never modified)
            o.offset = this.offset;
            o.height = this.height;
            o.sizeNodeAndChildren = this.sizeNodeAndChildren;
            o.block = this.block.copy();
            o.childrenLeftRight[LEFT] = this.childrenLeftRight[LEFT] != null ? this.childrenLeftRight[LEFT].copy() : null;
            o.childrenLeftRight[RIGHT] = this.childrenLeftRight[RIGHT] != null ? this.childrenLeftRight[RIGHT].copy() : null;

            return o;          
        }

        public void addString(int string) {
            this.sizeNodeAndChildren += string;
        }

        public RopesNodes getChild(int i) {
            return childrenLeftRight[i];
        }

        public Identifier appendEnd(List s) {
            int b = this.maxOffset() + 1;
            str.addAll(s);
            block.addBlock(b, s);
            return this.block.id.getBaseId(b);
        }

        public Identifier appendBegin(List s) {
            str.addAll(0, s);
            offset -= s.size();
            block.addBlock(this.offset, s);
            return this.getIdBegin();
        }

        public RopesNodes deleteOffsets(int begin, int end) {
            int sizeToDelete = end - begin + 1;
            this.block.delBlock(begin, end, sizeToDelete);
            if (sizeToDelete == this.str.size()) {
                this.str.clear();
                return null;
            }
            RopesNodes ret = null;
            if (end == this.offset + str.size() - 1) {
                this.str = new ArrayList(str.subList(0, begin - offset));
            } else if (begin == this.offset) {
                this.str = new ArrayList(str.subList(end - offset + 1, str.size()));
                offset = end + 1;
            } else {
                ret = this.split(end - offset + 1);
                str = new ArrayList(str.subList(0, begin - offset));
            }
            return ret;
        }

        public RopesNodes split(int size) {
            this.height++;
            RopesNodes n = new RopesNodes(new ArrayList(str.subList(size, str.size())), offset + size, block, false);
            this.str = new ArrayList(str.subList(0, size));
            if (this.childrenLeftRight[RIGHT] != null) {
                n.childrenLeftRight[RIGHT] = this.childrenLeftRight[RIGHT];
                n.height += n.childrenLeftRight[RIGHT].height + 1;
                n.sizeNodeAndChildren += n.childrenLeftRight[RIGHT].sizeNodeAndChildren;
            }
            this.childrenLeftRight[RIGHT] = n;
            return n;
        }

        public RopesNodes split(int size, RopesNodes node) {

            this.height++;
            RopesNodes n = split(size);
            n.childrenLeftRight[LEFT] = node;
            n.height++;
            return n;
        }

        int maxOffset() {
            return offset + str.size() - 1;
        }

        int getSize() {
            return str.size();
        }

        public RopesNodes getLeft() {
            return this.childrenLeftRight[LEFT];
        }

        public void setLeft(RopesNodes n) {
            this.childrenLeftRight[LEFT] = n;
        }

        public RopesNodes getRight() {
            return this.childrenLeftRight[RIGHT];
        }

        public void setRight(RopesNodes n) {
            this.childrenLeftRight[RIGHT] = n;
        }

        public void sumDirectChildren() {
            height = Math.max(getSubtreeHeight(LEFT), getSubtreeHeight(RIGHT)) + 1;
            sizeNodeAndChildren = getSizeNodeAndChildren(LEFT) + getSizeNodeAndChildren(RIGHT) + str.size();
        }

        public int getSubtreeHeight(int i) {
            RopesNodes s = childrenLeftRight[i];
            return s == null ? 0 : s.height;
        }

        public int getSizeNodeAndChildren(int i) {
            RopesNodes s = childrenLeftRight[i];
            return s == null ? 0 : s.sizeNodeAndChildren;
        }

        public int getHeightOfTree() {
            return height;
        }

        public int getSizeNodeAndChildren() {
            return sizeNodeAndChildren;
        }

        public void replaceChildren(RopesNodes node, RopesNodes by) {
            if (childrenLeftRight[LEFT] == node) {
                childrenLeftRight[LEFT] = by;
            } else if (childrenLeftRight[RIGHT] == node) {
                childrenLeftRight[RIGHT] = by;
            }
        }

        public int balanceScore() {
            return getSubtreeHeight(RIGHT) - getSubtreeHeight(LEFT);
        }

        public void become(RopesNodes node) {
            this.sizeNodeAndChildren = -str.size() + node.str.size();
            this.str = node.str;
            this.offset = node.offset;
            this.block = node.block;
        }

        public boolean isAppendableAfter() {
            return this.block.isMine() && block.id.end == this.maxOffset();
        }

        public boolean isAppendableBefore() {
            return this.block.isMine() && block.id.getBegin() == this.offset;
        }

        @Override
        public String toString() {
            StringBuffer str2 = new StringBuffer();
            for (Object o : str) {
                str2.append(o);
            }

            return new IdentifierInterval(this.block.id.base, this.offset, this.maxOffset()).toString() + "," + str2.toString();
        }

        public String viewRec() {
            StringBuffer str2 = new StringBuffer();
            if (getLeft() != null || getRight() != null) {
                str2.append("( ");
            }
            if (getLeft() != null) {
                str2.append(getLeft().viewRec());
            }
            if (getLeft() != null || getRight() != null) {
                str2.append(" , ");
            }
            for (Object o : str) {
                str2.append(o);
            }
            if (getLeft() != null || getRight() != null) {
                str2.append(" , ");
            }
            if (getRight() != null) {
                str2.append(getRight().viewRec());
            }
            if (getLeft() != null || getRight() != null) {
                str2.append(" )");
            }
            return str2.toString();
        }

        public IdentifierInterval getIdentifierInterval() {
            return new IdentifierInterval(this.block.id.base, this.offset, this.offset + str.size() - 1);
        }
    }

    public static class ResponseIntNode {

        int i;
        RopesNodes node;
        LinkedList path;

        public ResponseIntNode(int i, RopesNodes node, LinkedList path) {
            this.i = i;
            this.node = node;
            this.path = path;
        }

        public LinkedList getPath() {
            return path;
        }

        public int getI() {
            return i;
        }

        public RopesNodes getNode() {
            return node;
        }
    }
    
    public int getId() {
        return replicaNumber;
    }    
}
