package fr.loria.score.logootsplito;

import java.io.Serializable;
import java.util.List;

public abstract class LogootSBlock<T> implements Serializable {
    IdentifierInterval id;
    boolean mine = false;

    public LogootSBlock() {

    }

    public LogootSBlock(IdentifierInterval id) {
        this.id = id;
    }

    public boolean isMine() {
        return mine;
    }

    public void setMine(boolean mine) {
        this.mine = mine;
    }

    abstract int numberOfElements();

    abstract List<T> getElements(int begin, int end);

    abstract T getElement(int i);

    abstract void addBlock(int pos, List<T> contains);

    abstract void delBlock(int begin, int end, int nbElement);

    IdentifierInterval getId() {
        return id;
    }
}
