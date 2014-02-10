package logootsplitO;

import java.io.Serializable;
import java.util.List;

/**
 * This block kind contain no elements only ids.
 * The elements are on view
 * @author Stephane Martin <stephane@stephanemartin.fr>
 */
public class LogootSBlockLight<T> extends LogootSBlock<T> implements Serializable{
    /*int lastOffset=0;
    int firstOffset=0;
    * */
    int nbElement=0;
    public LogootSBlockLight(IdentifierInterval id, int list) {
        super(id);
        nbElement=list;
    }

    public LogootSBlockLight(IdentifierInterval id) {
        super(id);
    }

    public LogootSBlockLight() {
    }

    public LogootSBlockLight<T> clone() {
        return new LogootSBlockLight(id.clone(), this.nbElement);
    }
    
    @Override
    List<T> getElements(int begin, int end) {
        throw new UnsupportedOperationException("Version light contains no data");
        
    }

    @Override
    void addBlock(int pos, List<T> contains) {
        nbElement+=contains.size();
        this.getId().begin=Math.min(this.getId().begin, pos);
        this.getId().end=Math.max(this.getId().end, pos+contains.size()-1);
        
    }
    

    @Override
    void delBlock(int begin, int end,int nbElement) {
       this.nbElement-=nbElement;
    }

    @Override
    T getElement(int i) {
        throw new UnsupportedOperationException("Version light contains no data");
    }

    @Override
    int numberOfElements() {
        return nbElement;
    }

    @Override
    public String toString() {
        return "{" + nbElement +","+this.id+(isMine()?"mine":"its") +"}";
    }

    
    
}
