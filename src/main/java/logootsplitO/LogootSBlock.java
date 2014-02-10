package logootsplito;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Stephane Martin <stephane@stephanemartin.fr>
 */
public abstract class LogootSBlock<T> implements Serializable{
    // List<ElementList> elements=new ArrayList();
     IdentifierInterval id;

     boolean mine=false;

    public boolean isMine() {
        return mine;
    }

    public void setMine(boolean mine) {
        this.mine = mine;
    }
     
    public LogootSBlock(IdentifierInterval id) {
        this.id = id;
    }

    public LogootSBlock() {
    }
   
    abstract int numberOfElements();
     
     abstract List<T> getElements(int begin,int end);
     abstract T getElement(int i);
     abstract void addBlock (int pos, List<T> contains);
     abstract void delBlock (int begin, int end,int nbElement);
     IdentifierInterval getId(){
         return id;
     }
}
