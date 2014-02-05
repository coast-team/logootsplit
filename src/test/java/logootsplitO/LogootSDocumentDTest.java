package logootsplitO;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Stephane Martin <stephane@stephanemartin.fr>
 */
public class LogootSDocumentDTest {

    static List<Character> getListFromString(String str) {
        List<Character> l = new LinkedList();
        for (int i = 0; i < str.length(); i++) {
            l.add(str.charAt(i));
        }
        return l;
    }

    public LogootSDocumentDTest() {
    }
    LogootSDocumentD doc;
    LogootSDocumentD doc2;

    @Before
    public void setup() {
        doc = new LogootSDocumentD();
        doc2 = new LogootSDocumentD();
        doc2.setReplicaNumber(2);
    }

    @Test
    public void testSomeMethod() {
    }
    
    static LogootSBlock[] getFromMap(HashMap<List<Integer>, LogootSBlockLight> map){
        
        return map.values().toArray(new  LogootSBlock[map.size()]);
        
    }
    
    
    @Test
    public void SimpleAddDelTest() {
        LogootSDocumentD doc3 = new LogootSDocumentD();

        LogootSOpAdd op1 = doc.insertLocal(0, getListFromString("Test1234"));
        assertEquals("Test1234", doc.view());
        assertEquals(1, doc.getMapBaseToBlock().size());
        assertEquals(8,getFromMap(doc.getMapBaseToBlock())[0].numberOfElements());
        
        
        
        LogootSOpAdd op2 = doc.insertLocal(5, getListFromString("haha"));
        assertEquals("Test1haha234", doc.view());

        assertEquals("[h, a, h, a]", op2.l.toString());
        assertEquals("[T, e, s, t, 1, 2, 3, 4]", op1.l.toString());

        doc2.addBlock(op2.id, op2.l);
        assertEquals("haha", doc2.view());

        doc2.addBlock(op1.id, op1.l);
        assertEquals("Test1haha234", doc2.view());

        doc3.addBlock(op1.id, op1.l);
        doc3.addBlock(op2.id, op2.l);
        assertEquals("Test1haha234", doc3.view());


        /**
         * Del
         */
        LogootSOpDel op3 = doc3.delLocal(4, 9);

        assertEquals("Test34", doc3.view());
        assertEquals(3, op3.lid.size());
        doc2.delBlock(op3.lid.get(0));
        doc2.delBlock(op3.lid.get(1));
        doc2.delBlock(op3.lid.get(2));
        assertEquals("Test34", doc2.view());

        /**
         * Make another del
         */
        assertEquals("Test1haha234", doc.view());
        LogootSOpDel op4 = doc.delLocal(3, 6);
        assertEquals("Tesha234", doc.view());
        assertEquals(8, doc.getList().size());
        //assertEquals(2,op4.lid.size());

        
        /**
         * integration of del
         */
        doc.delBlock(op3.lid.get(0));
        doc.delBlock(op3.lid.get(1));
        doc.delBlock(op3.lid.get(2));

        assertEquals("Tes34", doc.view());

        doc2.delBlock(op4.lid.get(0));
        doc2.delBlock(op4.lid.get(1));
        assertEquals("Tes34", doc2.view());


        doc3.delBlock(op4.lid.get(0));
        doc3.delBlock(op4.lid.get(1));
        assertEquals("Tes34", doc3.view());

    }

    
    @Test
    public void maxOffsetTest(){
        Identifier id1=new Identifier(Arrays.asList(0,0,9,0),0);
        Identifier id2=new Identifier(Arrays.asList(0,0,9,0,7,1),0);
        int max= LogootSDocumentD.maxOffsetBeforeNex(id1,id2,12);
        assertEquals(7, max);
        
    }
    @Test
    public void maxOffsetTest2(){
        Identifier id1=new Identifier(Arrays.asList(0,0,9,0),3);
        Identifier id2=new Identifier(Arrays.asList(0,0,9,0,7,1),0);
        int max= LogootSDocumentD.maxOffsetBeforeNex(id1,id2,12);
        assertEquals(7, max);
        
    }
}