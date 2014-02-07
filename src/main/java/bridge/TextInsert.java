package bridge;

import java.util.List;
import logootsplitO.LogootSDoc;
import logootsplitO.LogootSOp;

public class TextInsert implements TextOperation{
    
    private int offset;
    private List content;
    
    public TextInsert(int offset,List content){
        this.offset=offset;
        this.content=content;
    }

    @Override
    public void applyToText() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public LogootSOp toCDRTOp(LogootSDoc doc) {
        return doc.insertLocal(offset,content);
        
    }
    
}