package logootsplitO;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Stephane Martin <stephane@stephanemartin.fr>
 */
public class IdentifierInterval implements Serializable {

    List<Integer> base;
    int begin;
    int end;

    public IdentifierInterval(List<Integer> base, int begin, int end) {
        this.base = base;
        this.begin = begin;
        this.end = end;
    }

    @Override
    @SuppressWarnings("CloneDoesntCallSuperClone")
    public IdentifierInterval clone() throws CloneNotSupportedException {
        List<Integer> baseCopy = new LinkedList<Integer>(base);
        return new IdentifierInterval(baseCopy, this.begin, this.end);
    }
    
    
    public List<Integer> getBase() {
        return base;
    }

    public Identifier getBeginId() {
        return new Identifier(base, begin);
    }

    public Identifier getEndId() {
        return new Identifier(base, end);
    }

    public int getBegin() {
        return begin;
    }

    public int getEnd() {
        return end;
    }

    public Identifier getBaseId() {
        return new Identifier(base);
    }

    public Identifier getBaseId(Integer u) {
        return new Identifier(base, u);
    }

    public void setBegin(int begin) {
        this.begin = begin;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public void addBegin(int begin) {
        this.begin += begin;
    }

    public void addEnd(int end) {
        this.end += end;
    }

    @Override
    public String toString() {
        return "IdentifiantInterval{" + base + ",[" + begin + ".." + end + "]}";
    }

}
