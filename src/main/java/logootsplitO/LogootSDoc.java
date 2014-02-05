package logootsplitO;

import java.util.List;
import crdt.Document;

/**
 *
 * @author Stephane Martin <stephane@stephanemartin.fr>
 */
public interface LogootSDoc<T> extends Document{
    public void addBlock(Identifier id,List<T> l);
    //public void addBlock(LogootSBlock block);
    //void delBlock(LogootSBlock block, int begin, int fin);
    public void delBlock(IdentifierInterval id);
    public LogootSOp insertLocal(int pos,List<T> l);
    public LogootSOp delLocal(int begin,int end);
    public LogootSDoc create();
    //public void setAlgo(LogootSAlgo algo);
    public void setReplicaNumber(int i);
}
