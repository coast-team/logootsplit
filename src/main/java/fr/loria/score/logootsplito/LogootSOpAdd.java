package fr.loria.score.logootsplito;

import java.util.List;

public class LogootSOpAdd<T> implements LogootSOp<T> {
    Identifier id;
    List<T> l;

    public LogootSOpAdd() {

    }

    public LogootSOpAdd(Identifier id, List<T> l) {
        this.id = id;
        this.l = l;
    }

    @Override
    public LogootSOpAdd<T> clone() throws CloneNotSupportedException {
        return new LogootSOpAdd<T>(id, l);
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
