package fr.loria.score.logootsplito;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class LogootSOpDel<T> implements LogootSOp<T> {

    private List<IdentifierInterval> lid;

    public LogootSOpDel() {

    }

    public LogootSOpDel(List<IdentifierInterval> lid) {
        this.lid = lid;
    }

    @Override
    public LogootSOp<T> clone() throws CloneNotSupportedException {
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
