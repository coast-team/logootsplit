package crdt;

import java.io.Serializable;

/**
 * A message that contains one operation.
 * @author urso
 */
public  class OperationBasedOneMessage implements OperationBasedMessage, Serializable {
    Operation operation;

    public OperationBasedOneMessage(Operation operation) {
        this.operation = operation;
    }
    
    @Override
    public CRDTMessage concat(CRDTMessage msg){
       return new OperationBasedMessagesBag(this,(OperationBasedMessage)msg);
   }
    
    @Override
    public OperationBasedOneMessage clone(){
        return new OperationBasedOneMessage((Operation)this.operation.clone());
    }

    @Override
    public int size() {
        return 1;
    }
    @Override
    public void execute(CRDT cmrdt){
        cmrdt.applyOneRemote(this);   
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    @Override
    public String toString() {
        return "OperationBasedOneMessage{" + "operation=" + operation + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final OperationBasedOneMessage other = (OperationBasedOneMessage) obj;
        if (this.operation != other.operation && (this.operation == null || !this.operation.equals(other.operation))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.operation != null ? this.operation.hashCode() : 0);
        return hash;
    }
    
}
