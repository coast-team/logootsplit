package facade;

import logootsplitO.LogootSDoc;
import logootsplitO.LogootSOp;

/**
 *
 * @author andre
 */
public interface TextOperation {

    public LogootSOp<Character> applyTo(LogootSDoc<Character> doc);

}
