package logootsplitO;

import bridge.TextOperation;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Stephane Martin <stephane@stephanemartin.fr>
 */
public class LogootSOpDel extends LogootSOp {

    List<IdentifierInterval> lid;

    public LogootSOpDel(List<IdentifierInterval> lid) {
        this.lid = lid;
    }

    @Override
    public LogootSOp clone() {
        return new LogootSOpDel(new LinkedList(lid));
    }

    @Override
    public List<TextOperation> apply(LogootSDoc doc) {
        List l = new ArrayList<TextOperation>();
        for (IdentifierInterval id : lid) {
            l.addAll(doc.delBlock(id));
        }
        return l;
    }

    @Override
    public String toString() {
        return "LogootSOpDel{" + "lid=" + lid + '}';
    }
    
}
