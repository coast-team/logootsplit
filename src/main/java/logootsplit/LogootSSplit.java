package logootsplit;

import crdt.Operation;
import java.util.ArrayList;
import java.util.List;

public class LogootSSplit implements LogootSOperation{
    
    private LogootSElement element;
    private int offset;
    

    public LogootSSplit(LogootSElement el, int offset) {
        this.element=el;
        this.offset=offset;  
    }

    @Override
    public void apply(LogootSDocument doc) {
        List<Integer> list=doc.getAllLike(element);
        if(!list.isEmpty()){
            int index=0;
            int p=offset+this.element.getIdAt(this.element.size()-1).getOffset();
            LogootSElement el;//=doc.getEl(list.get(0));
            int o;//=el.getIdAt(el.size()-1).getOffset();
            while (index<list.size()){
                int length=doc.get(list.get(index)).size();
                el = doc.getEl(list.get(index));
                o=el.getIdAt(el.size()-1).getOffset();
                if(p>=o+length){//no split in this element
                    index++;
                }
                else{
                    if(p<=o){//no split
                        return;
                    }
                    else{
                        //String s=doc.get(list.get(index));
                        List s=doc.get(list.get(index));
                        //String ns=s.substring(p-o);
                        List ns=new ArrayList(s.subList(p-o, s.size()));
                        LogootSElement el2=new LogootSElement(el, p-o);
                        doc.delete(list.get(index), p-o, s.size());
                        doc.add(el2, ns);    
                    }
                }
            }
        }
        /*LogootSElement el = this.element.origin();
        LogootSIdentifier id = el.getIdAt(el.size() - 1);
        int elOffset = this.element.getIdAt(this.element.size() - 1).getOffset();
        int maxpeer = this.offset + elOffset;
        int i = 0;
        while (i < maxpeer) {
            int index=doc.IndexOf(el, false);
            if (index==-1) {
                i++;
                id.setOffset(i);
            } else {
                if (i + doc.get(index).length() < maxpeer) {//split after this content
                    i = i + doc.get(index).length();
                    id.setOffset(i);
                } else {
                    if (i + doc.get(index).length() == maxpeer) {//split already done
                        i = maxpeer;
                    } else {//split in this content
                        String s=doc.get(index);
                        String ns=s.substring(maxpeer-i);
                        LogootSElement el2=new LogootSElement(el, maxpeer-i);
                        doc.delete(index, maxpeer-i, s.length());
                        doc.add(el2, ns);
                    }
                }
            }
        }*/
    }

    @Override
    public Operation clone() {
        return new LogootSSplit(element.clone(), offset);
    }

    
}