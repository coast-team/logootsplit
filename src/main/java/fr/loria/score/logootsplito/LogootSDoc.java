package fr.loria.score.logootsplito;

import java.util.List;

public interface LogootSDoc<T> {

    public LogootSDoc<T> duplicate(int newReplicaNumber);

    public void setReplicaNumber(int i);

    public LogootSOp insertLocal(int pos, List<T> l);

    public LogootSOp delLocal(int begin, int end);

    public List<TextOperation> addBlock(Identifier id, List<T> l);

    public List<TextOperation> delBlock(IdentifierInterval id);

    public String view();

    public int viewLength();
}
