package logootsplitO;

import facade.TextOperation;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Stephane Martin <stephane@stephanemartin.fr>
 */
public class LogootSOpDel<T> extends LogootSOp<T> {

    List<IdentifierInterval> lid;

    public LogootSOpDel(List<IdentifierInterval> lid) {
        this.lid = lid;
    }

    @Override
    public LogootSOp<T> clone() {
        return new LogootSOpDel<T>(new LinkedList(lid));
    }

    @Override
    public List<TextOperation> execute(LogootSDoc<T> doc) {
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
