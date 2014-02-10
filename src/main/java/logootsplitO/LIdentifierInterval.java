package logootsplitO;

//FIXME: useless class?
public class LIdentifierInterval{
    
    LIdentifier id;
    int begin;
    int end;
    
    
    public LIdentifier getBase() {
        return id;
    }

    public LIdentifier getBeginId(){
        return new LIdentifier(id,begin);
    }
    public int getBegin() {
        return begin;
    }

    public int getEnd() {
        return end;
    }
    public LIdentifier getBaseId(){
        return id;
    }

    public LIdentifier getBaseId(Integer u){
        return new LIdentifier(id,u);
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
        return "IdentifiantInterval{" +  id + ",[" + begin + ".." + end + "]}";
    }
    
}