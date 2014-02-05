package logootsplit;

import crdt.CRDTMessage;
import jbenchmarker.core.MergeAlgorithm;
import jbenchmarker.core.SequenceOperation;
import jbenchmarker.factories.LogootSFactory;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author urso
 */
public class LogootSAlgorithmTest {
    
    public LogootSAlgorithmTest() {
    }

    private MergeAlgorithm replica;

    @Before
    public void setUp() throws Exception {
        replica = (MergeAlgorithm) new LogootSFactory().create();
    }

    @Test
    public void testEmpty() {
        assertEquals("", replica.lookup());
    }

    @Test
    public void testInsert() throws Exception {
        String content = "abcdejk", c2 = "fghi";
        int pos = 3;      
        replica.applyLocal(SequenceOperation.insert(0, content));
        assertEquals(content, replica.lookup());
        replica.applyLocal(SequenceOperation.insert(pos, c2));
        assertEquals(content.substring(0, pos) + c2 + content.substring(pos), replica.lookup());        
    }
        
    @Test
    public void testDelete() throws Exception {
        String content = "abcdefghijk";
        int pos = 3, off = 4;       
        replica.applyLocal(SequenceOperation.insert(0, content));
        assertEquals(content, replica.lookup());
        replica.applyLocal(SequenceOperation.delete(pos, off));
        assertEquals(content.substring(0, pos) + content.substring(pos+off), replica.lookup());        
    }
    
    @Test
    public void testConcurrentDelete() throws Exception {
        String content = "abcdefghij";
        CRDTMessage m1 = replica.applyLocal(SequenceOperation.insert(0, content));
        assertEquals(content, replica.lookup());
        replica.applyLocal(SequenceOperation.insert(2, "2"));
        assertEquals("ab2cdefghij", replica.lookup());
        replica.applyLocal(SequenceOperation.insert(7, "7"));
        assertEquals("ab2cdef7ghij", replica.lookup());
        
        MergeAlgorithm replica2 = (MergeAlgorithm) new LogootSFactory().create();
        replica2.setReplicaNumber(2);
        m1.execute(replica2);
        assertEquals(content, replica2.lookup());
        CRDTMessage m2 = replica2.applyLocal(SequenceOperation.delete(1, 8));
        assertEquals("aj", replica2.lookup());
        m2.execute(replica);
        assertEquals("a27j", replica.lookup());
    }
    
    @Test
    public void testMultipleDeletions() throws Exception {
         
        String content = "abcdefghij";
        CRDTMessage m1 = replica.applyLocal(SequenceOperation.insert(0, content));
        replica.applyLocal(SequenceOperation.insert(2, "28"));
        assertEquals("ab28cdefghij", replica.lookup());
        replica.applyLocal(SequenceOperation.insert(10, "73"));
        assertEquals("ab28cdefgh73ij", replica.lookup());
        CRDTMessage m2 = replica.applyLocal(SequenceOperation.delete(3, 8));
        assertEquals("ab23ij", replica.lookup());
        
        MergeAlgorithm replica2 = (MergeAlgorithm) new LogootSFactory().create();
        replica2.setReplicaNumber(2);
        m1.execute(replica2);
        replica2.applyLocal(SequenceOperation.insert(4, "01"));
        assertEquals("abcd01efghij", replica2.lookup());
        m2.execute(replica2);
        assertEquals("ab01ij", replica2.lookup());
        
    }
    
    @Test
    public void testMultipleUpdates() throws Exception {
         
        String content = "abcdefghij";
        CRDTMessage m1 = replica.applyLocal(SequenceOperation.insert(0, content));
        replica.applyLocal(SequenceOperation.insert(2, "2"));
        replica.applyLocal(SequenceOperation.insert(7, "7"));
        assertEquals("ab2cdef7ghij", replica.lookup());
        CRDTMessage m2 = replica.applyLocal(SequenceOperation.replace(1, 10,"test"));
        assertEquals("atestj", replica.lookup());
        
        MergeAlgorithm replica2 = (MergeAlgorithm) new LogootSFactory().create();
        replica2.setReplicaNumber(2);
        m1.execute(replica2);
        replica2.applyLocal(SequenceOperation.insert(4, "01"));
        m2.execute(replica2);
        assertEquals("atest01j", replica2.lookup()); 
    } 

    
    @Test
    public void testConcurrentUpdate() throws Exception{
        String content = "abcdefghij";
        CRDTMessage m1 = replica.applyLocal(SequenceOperation.insert(0, content));
        replica.applyLocal(SequenceOperation.replace(2, 4, "27"));
        assertEquals("ab27ghij", replica.lookup());
        
        MergeAlgorithm replica2 = (MergeAlgorithm) new LogootSFactory().create();
        replica2.setReplicaNumber(2);
        m1.execute(replica2);
        CRDTMessage m2 = replica2.applyLocal(SequenceOperation.replace(1, 8, "test"));
        m2.execute(replica);
        assertEquals("atest27j", replica.lookup());
    }
    
    @Test
    public void testUpdate() throws Exception {
        String content = "abcdefghijk", upd = "xy";
        int pos = 3, off = 5;       
        replica.applyLocal(SequenceOperation.insert(0, content));
        assertEquals(content, replica.lookup());
        replica.applyLocal(SequenceOperation.replace(pos, off, upd));
        assertEquals(content.substring(0, pos) + upd + content.substring(pos+off), replica.lookup());        
    }
}
