/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bridge;

import logootsplitO.LogootSDoc;
import logootsplitO.LogootSOp;

/**
 *
 * @author andre
 */
public interface TextOperation {
    
    public void applyToText();
    public LogootSOp toCDRTOp(LogootSDoc doc);
    
}
