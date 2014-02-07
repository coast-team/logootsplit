package bridge;

import logootsplitO.LogootSDoc;
import logootsplitO.LogootSOp;

public class TextDelete implements TextOperation{
    
    private int offset;
    private int length; 

    public TextDelete(int offset,int length){
        this.offset=offset;
        this.length=length;
    }
    
    @Override
    public void applyToText() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public LogootSOp toCDRTOp(LogootSDoc doc) {
        return doc.delLocal(offset, length);
    }
    
    
}