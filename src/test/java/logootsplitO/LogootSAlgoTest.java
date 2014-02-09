package logootsplitO;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import crdt.CRDTMessage;
import crdt.OperationBasedOneMessage;
import java.util.HashMap;
import java.util.List;
import crdt.SequenceOperation;

/**
 *
 * @author Stephane Martin <stephane@stephanemartin.fr>
 */
public class LogootSAlgoTest {

    LogootSAlgo alg1;
    LogootSAlgo alg2;
    LogootSAlgo alg3;

    @Before
    public void setup() {
        alg1 = new LogootSAlgo(new LogootSDocumentD(), 1);
        alg2 = new LogootSAlgo(new LogootSDocumentD(), 50);
        alg3 = new LogootSAlgo(new LogootSDocumentD(), 75);
    }

    static LogootSBlock[] getFromMap(HashMap<List<Integer>, LogootSBlock> map) {
        return map.values().toArray(new LogootSBlock[map.size()]);
    }
   
    @Test
    public void adddel() throws Exception {
        CRDTMessage p = alg3.insert(0, "abcd");
        alg2.applyRemote(p);
        alg1.applyRemote(p);
        assertEquals("abcd", alg2.lookup());
        CRDTMessage p2 = alg2.remove(0, 2);
        CRDTMessage p3 = alg2.remove(0, 2);

        alg1.applyRemote(p2);
        assertEquals("cd", alg1.lookup());
        alg1.applyRemote(p3);
        assertEquals("", alg1.lookup());


        alg3.applyRemote(p3);
        assertEquals("ab", alg3.lookup());
        alg3.applyRemote(p2);
        assertEquals("", alg3.lookup());

    }

    @Test
    public void SimpleAddDelTest() throws Exception {




        CRDTMessage op1 = alg1.insert(0, "Test1234");
        assertEquals("Test1234", alg1.lookup());

        CRDTMessage op2 = alg1.insert(5, "haha");
        assertEquals("Test1haha234", alg1.lookup());


        alg2.applyRemote(op2);
        assertEquals("haha", alg2.lookup());

        alg2.applyRemote(op1);
        assertEquals("Test1haha234", alg2.lookup());

        alg3.applyRemote(op1);
        alg3.applyRemote(op2);
        assertEquals("Test1haha234", alg3.lookup());


        /**
         * Del
         */
        CRDTMessage op3 = alg3.remove(4, 6);

        assertEquals("Test34", alg3.lookup());
        alg2.applyRemote(op3);
        assertEquals("Test34", alg2.lookup());

        /**
         * Make another del
         */
        assertEquals("Test1haha234", alg1.lookup());
        CRDTMessage op4 = alg1.remove(3, 4);
        assertEquals("Tesha234", alg1.lookup());

        //assertEquals(2,op4.lid.size());


        /**
         * integration of del
         */
        alg1.applyRemote(op3);

        assertEquals("Tes34", alg1.lookup());

        alg2.applyRemote(op4);

        assertEquals("Tes34", alg2.lookup());


        alg3.applyRemote(op4);

        assertEquals("Tes34", alg3.lookup());
        CRDTMessage op5 = alg3.insert(2, "toto");
        CRDTMessage op6 = alg2.insert(3, "jiji");

        alg2.applyRemote(op5);
        alg3.applyRemote(op6);
        assertEquals(alg2.lookup(), alg3.lookup());

    }

    @Test
    public void testRnd() throws Exception {
        CRDTMessage op1 = alg1.insert(0, "test");
        CRDTMessage op2 = alg2.insert(0, "jklm");
        alg3.applyRemote(op2);
        alg3.applyRemote(op1);
        alg3.insert(4, "now");

        System.out.println("");
    }

    @Test
    public void testEmpty() {
        assertEquals("", alg1.lookup());
    }

    @Test
    public void testInsert() throws Exception {
        String content = "abcdejk", c2 = "fghi";
        int pos = 3;
        alg1.applyLocal(SequenceOperation.insert(0, content));
        assertEquals(content, alg1.lookup());
        alg1.applyLocal(SequenceOperation.insert(pos, c2));
        assertEquals(content.substring(0, pos) + c2 + content.substring(pos), alg1.lookup());
    }

    @Test
    public void testDelete() throws Exception {
        String content = "abcdefghijk";
        int pos = 3, off = 4;
        alg1.applyLocal(SequenceOperation.insert(0, content));
        assertEquals(content, alg1.lookup());
        alg1.applyLocal(SequenceOperation.delete(pos, off));
        assertEquals(content.substring(0, pos) + content.substring(pos + off), alg1.lookup());
    }

    @Test
    public void testConcurrentDelete() throws Exception {
        String content = "abcdefghij";
        CRDTMessage m1 = alg1.applyLocal(SequenceOperation.insert(0, content));
        assertEquals(content, alg1.lookup());
        alg1.applyLocal(SequenceOperation.insert(2, "2"));
        assertEquals("ab2cdefghij", alg1.lookup());
        alg1.applyLocal(SequenceOperation.insert(7, "7"));
        assertEquals("ab2cdef7ghij", alg1.lookup());


        alg2.applyRemote(m1);
        assertEquals(content, alg2.lookup());
        CRDTMessage m2 = alg2.applyLocal(SequenceOperation.delete(1, 8));
        assertEquals("aj", alg2.lookup());
        alg1.applyRemote(m2);
        assertEquals("a27j", alg1.lookup());
    }

    @Test
    public void testMultipleDeletions() throws Exception {

        String content = "abcdefghij";
        CRDTMessage m1 = alg1.applyLocal(SequenceOperation.insert(0, content));
        alg1.applyLocal(SequenceOperation.insert(2, "28"));
        assertEquals("ab28cdefghij", alg1.lookup());
        alg1.applyLocal(SequenceOperation.insert(10, "73"));
        assertEquals("ab28cdefgh73ij", alg1.lookup());
        CRDTMessage m2 = alg1.applyLocal(SequenceOperation.delete(3, 8));
        assertEquals("ab23ij", alg1.lookup());


        alg2.applyRemote(m1);

        alg2.applyLocal(SequenceOperation.insert(4, "01"));
        assertEquals("abcd01efghij", alg2.lookup());
        alg2.applyRemote(m2);

        assertEquals("ab01ij", alg2.lookup());

    }

    @Test
    public void testUpdate() throws Exception {
        String content = "abcdefghijk", upd = "xy";
        int pos = 3, off = 5;
        alg1.applyLocal(SequenceOperation.insert(0, content));
        assertEquals(content, alg1.lookup());
        alg1.applyLocal(SequenceOperation.replace(pos, off, upd));
        assertEquals(content.substring(0, pos) + upd + content.substring(pos + off), alg1.lookup());
    }
    static LogootSOpAdd extractOpAdd(CRDTMessage mess){
        return (LogootSOpAdd) ((OperationBasedOneMessage)mess).getOperation();
    }
     static LogootSOpDel extractOpDel(CRDTMessage mess){
        return (LogootSOpDel) ((OperationBasedOneMessage)mess).getOperation();
    }
    @Test 
    public void testAppending() throws Exception{
        CRDTMessage op1 = alg1.insert(0, "Test1234");
        CRDTMessage op2 = alg1.insert(8, "la suite");
        CRDTMessage op3 = alg1.insert(0, "before");
        
        assertEquals("beforeTest1234la suite", alg1.lookup());
             
        alg2.applyRemote(op2);
        alg2.applyRemote(op1);   
        alg2.applyRemote(op3);
        
        
        assertEquals("beforeTest1234la suite", alg2.lookup());
       // System.out.println(alg1.lookup());
        assertEquals(1,((LogootSDocumentD)alg1.getLDoc()).getMapBaseToBlock().size());
        
        
    }
    
    
    /*TODO: check if we have to enable this test. Commented out because it requires too much classes from jbenchmarker
    @Test
    public void testGC() throws Exception {
        Trace trace = new RandomTrace(4200, RandomTrace.FLAT, new StandardSeqOpProfile(0.8, 0.1, 40, 5.0), 0.1, 10, 3.0, 13);
        CausalSimulator cd = new CausalSimulator(new LogootSplitOFactory(LogootSplitOFactory.TypeDoc.String));
        cd.run(trace);
        alg1 = (LogootSAlgo) cd.getReplicas().get(new Integer(1));
        alg2 = (LogootSAlgo) cd.getReplicas().get(new Integer(2));
        alg3 = (LogootSAlgo) cd.getReplicas().get(new Integer(2));
        assertTrue("Doc is empty", alg1.getLDoc().viewLength() > 0);
        CRDTMessage m1 = alg1.remove(0, alg1.getLDoc().viewLength());
        alg2.applyRemote(m1);
        alg3.applyRemote(m1);
        //System.out.println(alg1.lookup());
        assertEquals(0, alg1.getLDoc().viewLength());
        assertEquals(0, alg2.getLDoc().viewLength());
        assertEquals(0, alg3.getLDoc().viewLength());
        assertEquals(0, ((LogootSDocumentD) alg1.getLDoc()).getMapBaseToBlock().size());
        assertEquals(0, ((LogootSDocumentD) alg2.getLDoc()).getMapBaseToBlock().size());
        assertEquals(0, ((LogootSDocumentD) alg3.getLDoc()).getMapBaseToBlock().size());
        assertEquals(0, ((LogootSDocumentD) alg1.getLDoc()).getList().size());
        assertEquals(0, ((LogootSDocumentD) alg2.getLDoc()).getList().size());
        assertEquals(0, ((LogootSDocumentD) alg3.getLDoc()).getList().size());



    }
    */
}