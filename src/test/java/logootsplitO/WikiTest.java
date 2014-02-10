package logootsplito;

import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Ignore;

public class WikiTest {

    class StringBufferTextDocument implements TextDocument {

        private StringBuffer state = new StringBuffer();

        @Override
        public void apply(TextOperation op) {
            if (op instanceof TextInsert) {
                TextInsert opIns = (TextInsert) op;
                state.insert(opIns.getOffset(), opIns.getContent());
            } else if (op instanceof TextDelete) {
                TextDelete opDel = (TextDelete) op;
                state.delete(opDel.getOffset(), opDel.getOffset() + opDel.getLength());
            } else {
                throw new UnsupportedOperationException("Unsupported type of TextOperation");
            }
        }

        public String toString() {
            return this.state.toString();
        }

    }

    @Before
    public void setup() {

    }

    @Test
    public void scenarioBasicAdd() {
        LogootSDoc<Character> logootS1 = LogootSFactory.create(12);
        TextOperation insert = new TextInsert(2, "abc");
        LogootSOp<Character> op = insert.applyTo(logootS1);


        LogootSDoc<Character> logootS2 = LogootSFactory.create(14);
        List<TextOperation> ops = op.execute(logootS2);

        assertEquals(logootS2.view(), "abc");
        assertEquals(logootS2.view(), logootS1.view());
        
        StringBufferTextDocument doc2 = new StringBufferTextDocument();
        for (TextOperation o : ops) {
            doc2.apply(o);
        }
        assertEquals(doc2.toString(), "abc");
    }
    
    @Test
    public void scenarioBasicDel() {
        LogootSDoc<Character> logootS1 = LogootSFactory.create(12);
        TextOperation insert = new TextInsert(2, "abc");
        LogootSOp<Character> op = insert.applyTo(logootS1);


        LogootSDoc<Character> logootS2 = LogootSFactory.create(14);
        List<TextOperation> ops = op.execute(logootS2);

        assertEquals("abc", logootS2.view());
        assertEquals(logootS1.view(), logootS2.view());
        
        StringBufferTextDocument doc2 = new StringBufferTextDocument();
        for (TextOperation o : ops) {
            doc2.apply(o);
        }
        assertEquals("abc", doc2.toString());
        
        
        // remove "bc"
        TextOperation delete = new TextDelete(1, 2); 
        op = delete.applyTo(logootS1);
        ops = op.execute(logootS2);
        assertEquals("a", logootS2.view());
        assertEquals(logootS1.view(), logootS2.view());

        for (TextOperation o : ops) {
            doc2.apply(o);
        }
        assertEquals("a", doc2.toString());
    }

    
        @Test
    public void scenarioAddAndSplit() {
        LogootSDoc<Character> logootS1 = LogootSFactory.create(12);
        TextOperation insert = new TextInsert(2, "abc");
        LogootSOp<Character> op = insert.applyTo(logootS1);


        LogootSDoc<Character> logootS2 = LogootSFactory.create(14);
        List<TextOperation> ops = op.execute(logootS2);

        assertEquals(logootS2.view(), "abc");
        assertEquals(logootS2.view(), logootS1.view());
        
        StringBufferTextDocument doc2 = new StringBufferTextDocument();
        for (TextOperation o : ops) {
            doc2.apply(o);
        }
        assertEquals(doc2.toString(), "abc");
    }
    
    
    
}
