package logootsplitO;

import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Stephane Martin <stephane@stephanemartin.fr>
 */
public class IdentifierTest {
    
    @Test
    public void testSomeMethod() {
        Identifier id1=new Identifier(Arrays.asList(0,0,7,0),0);
        Identifier id2=new Identifier(Arrays.asList(0,0,7,0,0,536870914,7,3));
        assertEquals(-1, id1.compareTo(id2));
    }
}