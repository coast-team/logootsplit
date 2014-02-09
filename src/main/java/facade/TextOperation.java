package facade;

import logootsplitO.LogootSDoc;
import logootsplitO.LogootSOp;

/**
 *
 * @author andre
 */
public interface TextOperation {

    public void applyToText();

    public LogootSOp<Character> toCDRTOp(LogootSDoc<Character> doc);

}
