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

import fr.loria.score.logootsplito.LogootSRopes.RopesNodes;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class LogootSRopesTest {

    private LogootSAlgo alg1;
    private LogootSAlgo alg2;
    private LogootSAlgo alg3;
    private LogootSAlgo alg4;

    static LinkedList<RopesNodes> browse(LogootSAlgo alg) {
        RopesNodes node = ((LogootSRopes) alg.getDoc()).root;
        LinkedList<RopesNodes> ret = new LinkedList<RopesNodes>();
        browsT(node, ret);
        return ret;
    }

    static void browsT(RopesNodes node, LinkedList<RopesNodes> list) {
        if (node == null) {
            return;
        }

        browsT(node.getLeft(), list);
        list.add(node);
        browsT(node.getRight(), list);
    }

    static boolean scoreCheck(LogootSAlgo alg) {
        RopesNodes node = ((LogootSRopes) alg.getDoc()).root;
        LinkedList<RopesNodes> ret = new LinkedList<RopesNodes>();
        return scoreCheckT(node, ret);
    }

    static boolean scoreCheckT(RopesNodes node, LinkedList<RopesNodes> list) {
        if (node == null) {
            return true;
        }

        boolean ret = true;
        list.add(node);
        ret &= scoreCheckT(node.getLeft(), list);
        ret &= scoreCheckT(node.getRight(), list);

        int nodeinsub = node.str.size() + node.getSizeNodeAndChildren(0) + node.getSizeNodeAndChildren(1);
        if (node.getSizeNodeAndChildren() != nodeinsub) {
            System.err.println("error length : " + node.getSizeNodeAndChildren() + "<>" + nodeinsub + " " + list);
            ret = false;

        }
        nodeinsub = 1 + Math.max(node.getSubtreeHeight(0), node.getSubtreeHeight(1));
        if (node.getHeightOfTree() != nodeinsub) {
            System.err.println("error height : " + node.getHeightOfTree() + "<>" + nodeinsub + " " + list);
            ret = false;
        }

        int bal = node.balanceScore();
        if (Math.abs(bal) > 1) {
            System.err.println("Balance broken " + bal + ":" + node.getSubtreeHeight(1) + " vs " + node.getSubtreeHeight(0) + " " + list);
            ret = false;
        }
        list.removeLast();
        return ret;
    }

    static boolean scoreChecks(LogootSAlgo... a) {
        boolean ret = true;
        for (int i = 0; i < a.length; i++) {
            ret &= scoreCheck(a[i]);
        }
        return ret;
    }

    @Before
    public void setup() {
        this.alg1 = new LogootSAlgo(new LogootSRopes<Character>(), 1);
        this.alg2 = new LogootSAlgo(new LogootSRopes<Character>(), 50);
        this.alg3 = new LogootSAlgo(new LogootSRopes<Character>(), 75);
        this.alg4 = new LogootSAlgo(new LogootSRopes<Character>(), 77);
    }

    @Test
    public void simpleAddMergeCheck() throws Exception {
        LinkedList<LogootSOperation> messages = new LinkedList<LogootSOperation>();
        messages.add(alg1.insert(0, "Hello"));

        assertEquals("Hello", alg1.lookup());

        messages.add(alg1.insert(5, " world"));

        assertEquals("Hello world", alg1.lookup());
        LogootSRopes<Character> doc = (LogootSRopes<Character>) alg1.getDoc();
        LogootSRopes.RopesNodes<Character> root = doc.root;
        assertNull(root.getLeft());
        assertNull(root.getRight());
        assertEquals("Hello world", Utils.convertCharactersListToString(root.str));
        assertEquals(1, root.getHeightOfTree());
        assertEquals(11, root.getSizeNodeAndChildren());

        messages.add(alg1.insert(0, "---"));

        root = doc.root;
        assertNull(root.getLeft());
        assertNull(root.getRight());
        assertEquals("---Hello world", Utils.convertCharactersListToString(root.str));
        assertEquals(1, root.getHeightOfTree());
        assertEquals(14, root.getSizeNodeAndChildren());

        // Apply to other sites

        List<TextOperation> lop= alg2.applyRemote(messages.pop());
        assertEquals(1,lop.size());
        TextInsert ti= (TextInsert) lop.get(0);
        assertEquals("Hello", ti.getContent());
        assertEquals(0, ti.getOffset());

        assertEquals("Hello", alg2.lookup());
        LogootSRopes<Character> doc2 = (LogootSRopes<Character>) alg2.getDoc();
        root = doc2.root;
        assertEquals(1, root.getHeightOfTree());
        assertEquals(5, root.getSizeNodeAndChildren());
        assertEquals("Hello", Utils.convertCharactersListToString(root.str));
        assertNull(root.getLeft());
        assertNull(root.getRight());

        lop = alg2.applyRemote(messages.pop());
        assertEquals(1,lop.size());
        ti= (TextInsert) lop.get(0);
        assertEquals(" world", ti.getContent());
        assertEquals(5, ti.getOffset());

        assertEquals("Hello world", alg2.lookup());
        doc2 = (LogootSRopes<Character>) alg2.getDoc();
        root = doc2.root;
        assertEquals(1, root.getHeightOfTree());
        assertEquals(11, root.getSizeNodeAndChildren());
        assertEquals("Hello world", Utils.convertCharactersListToString(root.str));
        assertNull(root.getLeft());
        assertNull(root.getRight());

        lop = alg2.applyRemote(messages.pop());
        assertEquals(1,lop.size());
        ti= (TextInsert) lop.get(0);
        assertEquals("---", ti.getContent());
        assertEquals(0, ti.getOffset());

        assertEquals("---Hello world", alg2.lookup());
        doc2 = (LogootSRopes<Character>) alg2.getDoc();
        root = doc2.root;
        assertEquals(1, root.getHeightOfTree());
        assertEquals(14, root.getSizeNodeAndChildren());
        assertEquals("---Hello world", Utils.convertCharactersListToString(root.str));
        assertNull(root.getLeft());
        assertNull(root.getRight());
    }

    @Test
    public void testTest() throws Exception {
        LinkedList<LogootSOperation> messages = new LinkedList();
        List<TextOperation> lop;
        TextInsert ti;
        messages.add(alg3.insert(0, " world"));

        lop = alg1.applyRemote(messages.get(0));
        assertEquals(1,lop.size());
        ti= (TextInsert) lop.get(0);
        assertEquals(" world", ti.getContent());
        assertEquals(0, ti.getOffset());
        
        messages.add(alg1.insert(0, "Hello"));
        messages.add(alg1.insert(11, "Every ones ?"));

        LogootSRopes<Character> doc = (LogootSRopes<Character>) alg1.getDoc();
        LinkedList<RopesNodes> lp = new LinkedList<RopesNodes>();
        doc.searchFull(doc.root, ((LogootSAdd<Character>) messages.getLast()).id, lp);
        assertTrue(lp.size() > 0);
        doc.searchFull(doc.root, ((LogootSAdd<Character>) messages.getLast()).id, lp);
        lp.clear();
        assertFalse(doc.searchFull(doc.root, new Identifier(Arrays.asList(0, 0, 0, 0, 0), 42), lp));
        assertEquals(lp.size(), 0);
    }

    @Test
    public void simpleAddCheck() throws Exception {
        LinkedList<LogootSOperation> messages = new LinkedList();
        List<TextOperation> lop;
        TextInsert ti;
        messages.add(alg3.insert(0, " world"));

        lop = alg1.applyRemote(messages.get(0));
        messages.add(alg1.insert(0, "Hello"));
        
        assertEquals(1,lop.size());
        ti= (TextInsert) lop.get(0);
        assertEquals(" world", ti.getContent());
        assertEquals(0, ti.getOffset());

        assertEquals("Hello world", alg1.lookup());
        LogootSRopes<Character> doc = (LogootSRopes<Character>) alg1.getDoc();
        LogootSRopes.RopesNodes root = doc.root;
        LogootSRopes.RopesNodes prev = root.getLeft();
        assertEquals(" world", Utils.convertCharactersListToString(root.str));
        assertEquals("Hello", Utils.convertCharactersListToString(prev.str));
        assertEquals(2, root.getHeightOfTree());
        assertEquals(1, prev.getHeightOfTree());
        assertEquals(11, root.getSizeNodeAndChildren());
        assertEquals(5, prev.getSizeNodeAndChildren());
        assertEquals(1, prev.getHeightOfTree());
        assertEquals(6, root.getSize());
        assertEquals(5, prev.getSize());

        messages.add(alg1.insert(11, "Every ones ?"));

        root = doc.root;
        LogootSRopes.RopesNodes next = root.getRight();
        LogootSRopes.RopesNodes previous = root.getLeft();
        assertEquals(2, root.getHeightOfTree());
        assertEquals(1, next.getHeightOfTree());
        assertEquals(1, previous.getHeightOfTree());
        assertEquals(23, root.getSizeNodeAndChildren());
        assertEquals(12, next.getSizeNodeAndChildren());
        assertEquals(5, previous.getSizeNodeAndChildren());
        assertEquals("Hello", Utils.convertCharactersListToString(previous.str));
        assertEquals(" world", Utils.convertCharactersListToString(root.str));
        assertEquals("Every ones ?", Utils.convertCharactersListToString(next.str));
        
        lop = alg2.applyRemote(messages.pollLast());
        assertEquals(1,lop.size());
        ti= (TextInsert) lop.get(0);
        assertEquals("Every ones ?", ti.getContent());
        assertEquals(0, ti.getOffset());
        
        lop = alg2.applyRemote(messages.pollLast());
        assertEquals(1,lop.size());
        ti= (TextInsert) lop.get(0);
        assertEquals("Hello", ti.getContent());
        assertEquals(0, ti.getOffset());
        
        lop = alg2.applyRemote(messages.pollLast());
        assertEquals(1,lop.size());
        ti= (TextInsert) lop.get(0);
        assertEquals(" world", ti.getContent());
        assertEquals(5, ti.getOffset());

        root = ((LogootSRopes<Character>) alg2.getDoc()).root;
        next = root.getRight();
        previous = root.getLeft();
        assertEquals(2, root.getHeightOfTree());
        assertEquals(1, next.getHeightOfTree());
        assertEquals(1, previous.getHeightOfTree());
        assertEquals(23, root.getSizeNodeAndChildren());
        assertEquals(12, next.getSizeNodeAndChildren());
        assertEquals(5, previous.getSizeNodeAndChildren());
        assertEquals("Hello", Utils.convertCharactersListToString(previous.str));
        assertEquals(" world", Utils.convertCharactersListToString(root.str));
        assertEquals("Every ones ?", Utils.convertCharactersListToString(next.str));
    }

    @Test
    public void addDel() throws Exception {
        
        List<TextOperation> lop;
        TextInsert ti;
        TextDelete td;
        
        LogootSOperation p = alg3.insert(0, "abcd");
        
        lop = alg2.applyRemote(p);
        assertEquals(1,lop.size());
        ti= (TextInsert) lop.get(0);
        assertEquals("abcd", ti.getContent());
        assertEquals(0, ti.getOffset());
        
        lop = alg1.applyRemote(p);
        assertEquals(1,lop.size());
        ti= (TextInsert) lop.get(0);
        assertEquals("abcd", ti.getContent());
        assertEquals(0, ti.getOffset());

        assertEquals("abcd", alg2.lookup());

        LogootSOperation p2 = alg2.remove(0, 2);
        LogootSOperation p3 = alg2.remove(0, 2);

        
        lop = alg1.applyRemote(p2);
        assertEquals(1,lop.size());
        td= (TextDelete) lop.get(0);
        assertEquals(0,td.getOffset());
        assertEquals(2, td.getLength());
        
        assertEquals("cd", alg1.lookup());
        
        lop = alg1.applyRemote(p3);
        assertEquals(1,lop.size());
        td= (TextDelete) lop.get(0);
        assertEquals(0,td.getOffset());
        assertEquals(2, td.getLength());
        
        assertEquals("", alg1.lookup());

        lop = alg3.applyRemote(p3);
         assertEquals(1,lop.size());
        td= (TextDelete) lop.get(0);
        assertEquals(2,td.getOffset());
        assertEquals(2, td.getLength());
        
        assertEquals("ab", alg3.lookup());
        
        lop = alg3.applyRemote(p2);
        assertEquals(1,lop.size());
        td= (TextDelete) lop.get(0);
        assertEquals(0,td.getOffset());
        assertEquals(2, td.getLength());

        assertEquals("", alg3.lookup());
    }

    @Test
    public void simpleAddDel() throws Exception {
        
        List<TextOperation> lop;
        TextInsert ti;
        TextDelete td;
        
        LogootSOperation op1 = alg1.insert(0, "Test1234");

        assertEquals("Test1234", alg1.lookup());
        assertTrue(scoreChecks(alg1, alg2, alg3));

        LogootSOperation op2 = alg1.insert(5, "haha");

        assertEquals("Test1haha234", alg1.lookup());
        assertEquals("[T, e, s, t, 1, 2, 3, 4]", ((LogootSAdd<Character>) op1).l.toString());
        assertEquals("[h, a, h, a]", ((LogootSAdd<Character>) op2).l.toString());
        assertTrue(scoreChecks(alg1, alg2, alg3));

        alg2.applyRemote(op2);

        assertEquals("haha", alg2.lookup());
        assertTrue(scoreChecks(alg1, alg2, alg3));

        
        lop = alg2.applyRemote(op1);
        
        assertEquals(2,lop.size());
        ti= (TextInsert) lop.get(0);
        assertEquals("Test1",ti.getContent());
        assertEquals(0, ti.getOffset());
        
        ti= (TextInsert) lop.get(1);
        assertEquals("234",ti.getContent());
        assertEquals(9, ti.getOffset());
        
        assertEquals("Test1haha234", alg2.lookup());
        assertTrue(scoreChecks(alg1, alg2, alg3));
        
        lop = alg3.applyRemote(op1);
        
        assertEquals(1,lop.size());
        ti= (TextInsert) lop.get(0);
        assertEquals("Test1234",ti.getContent());
        assertEquals(0, ti.getOffset());
        
        lop = alg3.applyRemote(op2);
        
        assertEquals(1,lop.size());
        ti= (TextInsert) lop.get(0);
        assertEquals("haha",ti.getContent());
        assertEquals(5, ti.getOffset());

        assertEquals("Test1haha234", alg3.lookup());
        assertTrue(scoreChecks(alg1, alg2, alg3));

        // Del
        LogootSOperation op3 = alg3.remove(4, 6);

        assertEquals("Test34", alg3.lookup());


        lop = alg2.applyRemote(op3);
        assertEquals(3,lop.size());
        td= (TextDelete) lop.get(0);
        assertEquals(4,td.getOffset());
        assertEquals(1,td.getLength());
        
        td= (TextDelete) lop.get(1);
        assertEquals(4,td.getOffset());
        assertEquals(4,td.getLength());
        
        td= (TextDelete) lop.get(2);
        assertEquals(4,td.getOffset());
        assertEquals(1,td.getLength());

        assertEquals("Test34", alg2.lookup());

        // Another Del
        assertEquals("Test1haha234", alg1.lookup());

        LogootSOperation op4 = alg1.remove(3, 4);

        assertEquals("Tesha234", alg1.lookup());

        // integration of Del
        lop = alg1.applyRemote(op3);
        assertEquals(2,lop.size());
        
        td= (TextDelete) lop.get(0);
        assertEquals(3,td.getOffset());
        assertEquals(2,td.getLength());
        
        td= (TextDelete) lop.get(1);
        assertEquals(3,td.getOffset());
        assertEquals(1,td.getLength());

        assertEquals("Tes34", alg1.lookup());

        lop = alg2.applyRemote(op4);
        assertEquals(1,lop.size());
        
        td= (TextDelete) lop.get(0);
        assertEquals(3,td.getOffset());
        assertEquals(1,td.getLength());

        assertEquals("Tes34", alg2.lookup());

        lop = alg3.applyRemote(op4);
        assertEquals(1,lop.size());
        
        td= (TextDelete) lop.get(0);
        assertEquals(3,td.getOffset());
        assertEquals(1,td.getLength());

        assertEquals("Tes34", alg3.lookup());

        LogootSOperation op5 = alg3.insert(2, "toto");

        assertTrue(scoreChecks(alg1, alg2, alg3));

        LogootSOperation op6 = alg2.insert(3, "jiji");

        lop = alg1.applyRemote(op6);
        assertEquals(1,lop.size());
        
        ti= (TextInsert) lop.get(0);
        assertEquals("jiji",ti.getContent());
        assertEquals(3,ti.getOffset());
        
        lop = alg1.applyRemote(op5);
        assertEquals(1,lop.size());
        
        ti= (TextInsert) lop.get(0);
        assertEquals("toto",ti.getContent());
        assertEquals(2,ti.getOffset());
        
        lop = alg2.applyRemote(op5);
        assertEquals(1,lop.size());
        
        ti= (TextInsert) lop.get(0);
        assertEquals("toto",ti.getContent());
        assertEquals(2,ti.getOffset());
        
        lop = alg3.applyRemote(op6);
        assertEquals(1,lop.size());
        
        ti= (TextInsert) lop.get(0);
        assertEquals("jiji",ti.getContent());
        assertEquals(7,ti.getOffset());
        

        assertTrue(scoreChecks(alg1, alg2, alg3));
        assertEquals(alg1.lookup(), alg2.lookup());
        assertEquals(alg2.lookup(), alg3.lookup());
    }

    @Test
    public void testEmpty() {
        assertEquals("", alg1.lookup());
    }

    @Test
    public void testInsert() throws Exception {
        String content = "abcdejk", c2 = "fghi";
        int pos = 3;
        alg1.insert(0, content);
        assertEquals(content, alg1.lookup());
        alg1.insert(pos, c2);
        assertEquals(content.substring(0, pos) + c2 + content.substring(pos), alg1.lookup());
    }

    @Test
    public void testDelete() throws Exception {
        String content = "abcdefghijk";
        int pos = 3, off = 4;
        alg1.insert(0, content);
        assertEquals(content, alg1.lookup());
        alg1.remove(pos, off);
        assertEquals(content.substring(0, pos) + content.substring(pos + off), alg1.lookup());
    }

    @Test
    public void testConcurrentDelete() throws Exception {
        String content = "abcdefghij";
        LogootSOperation m1 = alg1.insert(0, content);
        assertEquals(content, alg1.lookup());
        alg1.insert(2, "2");
        assertEquals("ab2cdefghij", alg1.lookup());
        alg1.insert(7, "7");
        assertEquals("ab2cdef7ghij", alg1.lookup());

        alg2.applyRemote(m1);
        assertEquals(content, alg2.lookup());
        LogootSOperation m2 = alg2.remove(1, 8);
        assertEquals("aj", alg2.lookup());
        alg1.applyRemote(m2);
        assertEquals("a27j", alg1.lookup());
    }

    @Test
    public void testAppending() throws Exception {
        LogootSOperation op1 = alg1.insert(0, "Test1234");
        LogootSOperation op2 = alg1.insert(8, "la suite");
        LogootSOperation op3 = alg1.insert(0, "before");

        assertEquals("beforeTest1234la suite", alg1.lookup());

        alg2.applyRemote(op2);
        alg2.applyRemote(op1);
        alg2.applyRemote(op3);

        assertEquals("beforeTest1234la suite", alg2.lookup());
        assertEquals(1, ((LogootSRopes) alg1.getDoc()).getMapBaseToBlock().size());
    }

    @Test
    public void testMede() {
        IdentifierInterval id1 = new IdentifierInterval(Arrays.asList(1, 0, -2), 0, 4);
        IdentifierInterval id2 = new IdentifierInterval(Arrays.asList(1, 0), -1, -1);
        assertEquals(id1.getEndId().compareTo(id2.getBeginId()), -1);
    }

    @Test
    public void testConflictAppendInside() throws Exception {
        LogootSAdd m1 = (LogootSAdd) alg1.insert(0, "toto");
        LogootSAdd m2 = (LogootSAdd) alg1.insert(4, "hihi");
        List<Integer> base = new LinkedList(m1.id.base);
        base.add(5);
        base.add(5);
        Identifier id = new Identifier(base, 6);
        LogootSOperation m3 = new LogootSAdd(id, Arrays.asList('k', 'o', 'k', 'o'));
        alg1.applyRemote(m3);
        assertEquals("totohikokohi", alg1.lookup());
        alg2.applyRemote(m1);
        alg2.applyRemote(m3);
        alg2.applyRemote(m2);
        assertEquals("totohikokohi", alg2.lookup());

    }

    @Test
    public void testConflictAppendB2concatB1() throws Exception {
        LogootSAdd m0 = (LogootSAdd) alg1.insert(0, "yoyo");
        LogootSAdd m1 = (LogootSAdd) alg3.insert(0, "toto");
        LogootSAdd m2 = (LogootSAdd) alg3.insert(4, "hihi");
        List base = new LinkedList(m1.id.base);
        base.add(5);
        base.add(5);
        Identifier id = new Identifier(base, 6);
        LogootSAdd m3 = new LogootSAdd(id, Arrays.asList('k', 'o', 'k', 'o'));
        alg3.applyRemote(m3);
        alg3.applyRemote(m0);
        assertEquals("yoyototohikokohi", alg3.lookup());
        alg2.applyRemote(m0);
        alg2.applyRemote(m1);
        alg2.applyRemote(m3);
        alg2.applyRemote(m2);
        assertEquals("yoyototohikokohi", alg2.lookup());
    }

    @Test
    public void testConflictAppendB2concatB12() throws Exception {
        LogootSAdd m0 = (LogootSAdd) alg1.insert(0, "yoyo");
        LogootSAdd m1 = (LogootSAdd) alg3.insert(0, "toto");
        LogootSAdd m2 = (LogootSAdd) alg3.insert(4, "hihi");
        List base = new LinkedList(m1.id.base);
        base.add(4);
        base.add(5);
        Identifier id = new Identifier(base, 6);
        LogootSAdd m3 = new LogootSAdd(id, Arrays.asList('k', 'o', 'k', 'o'));
        alg3.applyRemote(m3);
        alg3.applyRemote(m0);
        assertEquals("yoyototohkokoihi", alg3.lookup());
        alg2.applyRemote(m0);
        alg2.applyRemote(m1);
        alg2.applyRemote(m3);
        alg2.applyRemote(m2);
        assertEquals("yoyototohkokoihi", alg2.lookup());
    }

    @Test
    public void simpleRotateTest() {
        LogootSRopes<Character> doc = (LogootSRopes<Character>) alg1.getDoc();

        LogootSRopes.RopesNodes T1 = new LogootSRopes.RopesNodes(Arrays.asList("T1"), 0, null);
        LogootSRopes.RopesNodes T2 = new LogootSRopes.RopesNodes(Arrays.asList("T2"), 0, null);
        LogootSRopes.RopesNodes T3 = new LogootSRopes.RopesNodes(Arrays.asList("T3"), 0, null);
        LogootSRopes.RopesNodes a = new LogootSRopes.RopesNodes(Arrays.asList("a"), 0, null);
        LogootSRopes.RopesNodes s = new LogootSRopes.RopesNodes(Arrays.asList("s"), 0, null);
        a.setLeft(s);
        a.setRight(T3);
        s.setLeft(T1);
        s.setRight(T2);
        doc.root = a;

        doc.rotateRight(a, null);
        assertTrue("bad root", doc.root == s);
        assertTrue("bad T1", s.getLeft() == T1);
        assertTrue("bad a", s.getRight() == a);
        assertTrue("bad T2", a.getLeft() == T2);
        assertTrue("bad T3", a.getRight() == T3);
        doc.rotateLeft(s, null);
        assertTrue("bad root", doc.root == a);
        assertTrue("bad T1", s.getLeft() == T1);
        assertTrue("bad T2", s.getRight() == T2);
        assertTrue("bad s", a.getLeft() == s);
        assertTrue("bad T3", a.getRight() == T3);
    }

    @Test
    public void simpleRotateNodeTest() {
        LogootSRopes<Character> doc = (LogootSRopes<Character>) alg1.getDoc();

        LogootSRopes.RopesNodes T1 = new LogootSRopes.RopesNodes(Arrays.asList("T1"), 0, null);
        LogootSRopes.RopesNodes T2 = new LogootSRopes.RopesNodes(Arrays.asList("T2"), 0, null);
        LogootSRopes.RopesNodes T3 = new LogootSRopes.RopesNodes(Arrays.asList("T3"), 0, null);
        LogootSRopes.RopesNodes a = new LogootSRopes.RopesNodes(Arrays.asList("a"), 0, null);
        LogootSRopes.RopesNodes s = new LogootSRopes.RopesNodes(Arrays.asList("s"), 0, null);
        LogootSRopes.RopesNodes b = new LogootSRopes.RopesNodes(Arrays.asList("b"), 0, null);
        a.setLeft(s);
        a.setRight(T3);
        s.setLeft(T1);
        s.setRight(T2);
        doc.root = b;
        b.setRight(a);

        doc.rotateRight(a, b);
        assertTrue("bad root", doc.root == b);
        assertTrue("bad s", b.getRight() == s);
        assertTrue("bad T1", s.getLeft() == T1);
        assertTrue("bad a", s.getRight() == a);
        assertTrue("bad T2", a.getLeft() == T2);
        assertTrue("bad T3", a.getRight() == T3);
        doc.rotateLeft(s, b);
        assertTrue("bad root", doc.root == b);
        assertTrue("bad a", b.getRight() == a);
        assertTrue("bad T1", s.getLeft() == T1);
        assertTrue("bad T2", s.getRight() == T2);
        assertTrue("bad s", a.getLeft() == s);
        assertTrue("bad T3", a.getRight() == T3);
    }

    @Test
    public void doubleRotateLTest() {
        LogootSRopes<Character> doc = (LogootSRopes<Character>) alg1.getDoc();
        LogootSRopes.RopesNodes T1 = new LogootSRopes.RopesNodes(Arrays.asList("T1"), 0, null);
        LogootSRopes.RopesNodes T2 = new LogootSRopes.RopesNodes(Arrays.asList("T2"), 0, null);
        LogootSRopes.RopesNodes T3 = new LogootSRopes.RopesNodes(Arrays.asList("T3"), 0, null);
        LogootSRopes.RopesNodes T4 = new LogootSRopes.RopesNodes(Arrays.asList("T4"), 0, null);
        LogootSRopes.RopesNodes a = new LogootSRopes.RopesNodes(Arrays.asList("a"), 0, null);
        LogootSRopes.RopesNodes b = new LogootSRopes.RopesNodes(Arrays.asList("b"), 0, null);
        LogootSRopes.RopesNodes s = new LogootSRopes.RopesNodes(Arrays.asList("s"), 0, null);
        a.setLeft(T1);
        a.setRight(s);
        s.setLeft(b);
        s.setRight(T4);
        b.setLeft(T2);
        b.setRight(T3);
        doc.root = a;
        doc.rotateRL(a, null);
        assertTrue("bad root", doc.root == b);
        assertTrue("bad a", b.getLeft() == a);
        assertTrue("bad s", b.getRight() == s);
        assertTrue("bad T1", a.getLeft() == T1);
        assertTrue("bad T2", a.getRight() == T2);
        assertTrue("bad T3", s.getLeft() == T3);
        assertTrue("bad T4", s.getRight() == T4);
    }

    @Test
    public void doubleRotateLNodeTest() {
        LogootSRopes<Character> doc = (LogootSRopes<Character>) alg1.getDoc();
        LogootSRopes.RopesNodes T1 = new LogootSRopes.RopesNodes(Arrays.asList("T1"), 0, null);
        LogootSRopes.RopesNodes T2 = new LogootSRopes.RopesNodes(Arrays.asList("T2"), 0, null);
        LogootSRopes.RopesNodes T3 = new LogootSRopes.RopesNodes(Arrays.asList("T3"), 0, null);
        LogootSRopes.RopesNodes T4 = new LogootSRopes.RopesNodes(Arrays.asList("T4"), 0, null);
        LogootSRopes.RopesNodes a = new LogootSRopes.RopesNodes(Arrays.asList("a"), 0, null);
        LogootSRopes.RopesNodes b = new LogootSRopes.RopesNodes(Arrays.asList("b"), 0, null);
        LogootSRopes.RopesNodes s = new LogootSRopes.RopesNodes(Arrays.asList("s"), 0, null);
        LogootSRopes.RopesNodes r = new LogootSRopes.RopesNodes(Arrays.asList("r"), 0, null);
        a.setLeft(T1);
        a.setRight(s);
        s.setLeft(b);
        s.setRight(T4);
        b.setLeft(T2);
        b.setRight(T3);
        doc.root = r;
        r.setRight(a);
        doc.rotateRL(a, r);
        assertTrue("bad root", doc.root == r);
        assertTrue("bad r", r.getRight() == b);
        assertTrue("bad a", b.getLeft() == a);
        assertTrue("bad s", b.getRight() == s);
        assertTrue("bad T1", a.getLeft() == T1);
        assertTrue("bad T2", a.getRight() == T2);
        assertTrue("bad T3", s.getLeft() == T3);
        assertTrue("bad T4", s.getRight() == T4);
    }

    @Test
    public void doubleRotateRTest() {
        LogootSRopes<Character> doc = (LogootSRopes<Character>) alg1.getDoc();
        LogootSRopes.RopesNodes T1 = new LogootSRopes.RopesNodes(Arrays.asList("T1"), 0, null);
        LogootSRopes.RopesNodes T2 = new LogootSRopes.RopesNodes(Arrays.asList("T2"), 0, null);
        LogootSRopes.RopesNodes T3 = new LogootSRopes.RopesNodes(Arrays.asList("T3"), 0, null);
        LogootSRopes.RopesNodes T4 = new LogootSRopes.RopesNodes(Arrays.asList("T4"), 0, null);
        LogootSRopes.RopesNodes a = new LogootSRopes.RopesNodes(Arrays.asList("a"), 0, null);
        LogootSRopes.RopesNodes b = new LogootSRopes.RopesNodes(Arrays.asList("b"), 0, null);
        LogootSRopes.RopesNodes s = new LogootSRopes.RopesNodes(Arrays.asList("s"), 0, null);
        a.setLeft(s);
        a.setRight(T4);
        s.setLeft(T1);
        s.setRight(b);
        b.setLeft(T2);
        b.setRight(T3);
        doc.root = a;
        doc.rotateLR(a, null);
        assertTrue("bad root", doc.root == b);
        assertTrue("bad s", b.getLeft() == s);
        assertTrue("bad a", b.getRight() == a);
        assertTrue("bad T1", s.getLeft() == T1);
        assertTrue("bad T2", s.getRight() == T2);
        assertTrue("bad T3", a.getLeft() == T3);
        assertTrue("bad T4", a.getRight() == T4);
    }

    @Test
    public void doubleRotateRNodeTest() {
        LogootSRopes<Character> doc = (LogootSRopes<Character>) alg1.getDoc();
        LogootSRopes.RopesNodes T1 = new LogootSRopes.RopesNodes(Arrays.asList("T1"), 0, null);
        LogootSRopes.RopesNodes T2 = new LogootSRopes.RopesNodes(Arrays.asList("T2"), 0, null);
        LogootSRopes.RopesNodes T3 = new LogootSRopes.RopesNodes(Arrays.asList("T3"), 0, null);
        LogootSRopes.RopesNodes T4 = new LogootSRopes.RopesNodes(Arrays.asList("T4"), 0, null);
        LogootSRopes.RopesNodes a = new LogootSRopes.RopesNodes(Arrays.asList("a"), 0, null);
        LogootSRopes.RopesNodes b = new LogootSRopes.RopesNodes(Arrays.asList("b"), 0, null);
        LogootSRopes.RopesNodes s = new LogootSRopes.RopesNodes(Arrays.asList("s"), 0, null);
        LogootSRopes.RopesNodes r = new LogootSRopes.RopesNodes(Arrays.asList("r"), 0, null);
        a.setLeft(s);
        a.setRight(T4);
        s.setLeft(T1);
        s.setRight(b);
        b.setLeft(T2);
        b.setRight(T3);
        doc.root = r;
        r.setRight(a);
        doc.rotateLR(a, r);
        assertTrue("bad root", doc.root == r);
        assertTrue("bad b", r.getRight() == b);
        assertTrue("bad s", b.getLeft() == s);
        assertTrue("bad a", b.getRight() == a);
        assertTrue("bad T1", s.getLeft() == T1);
        assertTrue("bad T2", s.getRight() == T2);
        assertTrue("bad T3", a.getLeft() == T3);
        assertTrue("bad T4", a.getRight() == T4);
    }

    @Test
    public void splitTest() {
        LogootSRopes<Character> doc = (LogootSRopes<Character>) alg1.getDoc();
        LogootSRopes.RopesNodes a = new LogootSRopes.RopesNodes(Arrays.asList('a', 'b', 'c', 'd', 'e', 'f'), 0, null);
        doc.root = a;

        assertEquals("abcdef", alg1.lookup());

        a.split(3);

        assertEquals("[a, b, c]", a.str.toString());
        assertTrue("bad split", a.getRight() != null);
        assertTrue("bad split", a.getLeft() == null);
        assertTrue("bad split", a.getRight().getLeft() == null);
        assertTrue("bad split", a.getRight().getRight() == null);
        assertEquals("[d, e, f]", a.getRight().str.toString());
    }

    @Test
    public void splitTest2() {
        LogootSRopes<Character> doc = (LogootSRopes<Character>) alg1.getDoc();
        LogootSRopes.RopesNodes a = new LogootSRopes.RopesNodes(Arrays.asList('a', 'b', 'c', 'd', 'e', 'f'), 0, null);
        LogootSRopes.RopesNodes b = new LogootSRopes.RopesNodes(Arrays.asList('1', '2', '3', '4', '5', '6'), 0, null);
        doc.root = a;

        assertEquals("abcdef", alg1.lookup());

        a.split(3, b);

        assertEquals("[a, b, c]", a.str.toString());
        assertTrue("bad split", a.getRight() != null);
        assertTrue("bad split", a.getLeft() == null);
        assertTrue("bad split", a.getRight().getLeft() == b);
        assertTrue("bad split", a.getRight().getRight() == null);
        assertTrue("bad split", b.getLeft() == null);
        assertTrue("bad split", b.getRight() == null);

        assertEquals("[d, e, f]", a.getRight().str.toString());
        assertEquals("abc123456def", alg1.lookup());
    }
}
