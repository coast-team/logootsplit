package logootsplitO;

import facade.TextOperation;
import java.util.List;

/**
 *
 * @author Stephane Martin <stephane@stephanemartin.fr>
 */
public class LogootSOpAdd<T> extends LogootSOp<T> {
    Identifier id;
    List<T> l;

    public LogootSOpAdd(Identifier id, List<T> l) {
        this.id = id;
        this.l = l;
    }
    
    @Override
    public LogootSOpAdd<T> clone() {
        return new  LogootSOpAdd<T>(id,l);
    }
    
    @Override
    public List<TextOperation> execute(LogootSDoc<T> doc) {
        return doc.addBlock(id, l);
    }

    @Override
    public String toString() {
        return "LogootSOpAdd{" + "id=" + id + ", l=" + l + '}';
    }
    
}
