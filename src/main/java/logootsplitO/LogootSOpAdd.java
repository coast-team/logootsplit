package logootsplitO;

import java.util.List;

/**
 *
 * @author Stephane Martin <stephane@stephanemartin.fr>
 */
public class LogootSOpAdd extends LogootSOp{
    Identifier id;
    List l;

    public LogootSOpAdd(Identifier id, List l) {
        this.id = id;
        this.l = l;
    }
    
    
    @Override
    public LogootSOpAdd clone() {
        return new  LogootSOpAdd(id,l);
    }

   

    @Override
    public void apply(LogootSDoc doc) {
        doc.addBlock(id, l);
    }

    @Override
    public String toString() {
        return "LogootSOpAdd{" + "id=" + id + ", l=" + l + '}';
    }
    
}