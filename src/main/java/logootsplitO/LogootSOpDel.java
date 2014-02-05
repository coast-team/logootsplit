package logootsplitO;

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
    public void apply(LogootSDoc doc) {
        for (IdentifierInterval id : lid) {
            doc.delBlock(id);
        }
    }

    @Override
    public String toString() {
        return "LogootSOpDel{" + "lid=" + lid + '}';
    }
    
}
