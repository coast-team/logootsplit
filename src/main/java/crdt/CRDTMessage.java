package crdt;

import java.io.Serializable;

/**
 *
 * @author urso
 */
public interface CRDTMessage extends Cloneable, Serializable {
    public static CRDTMessage emptyMessage = new CRDTMessage() {

        @Override
        public CRDTMessage concat(CRDTMessage msg) {
            return msg;
        }

        @Override
        public void execute(CRDT crdt) {
        }

        @Override
        public CRDTMessage clone() {
            return this; 
        }

        @Override
        public int size() {
            return 1;
        }
    };
    
    public CRDTMessage concat(CRDTMessage msg);

    public void execute(CRDT crdt);
    public CRDTMessage clone();
    
    /**
     * Number of messages embeded in this one.
     */
    public int size();
}
