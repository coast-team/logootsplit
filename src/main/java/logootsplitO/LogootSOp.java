package logootsplitO;

import bridge.TextOperation;
import crdt.Operation;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Stephane Martin <stephane@stephanemartin.fr>
 */
public abstract class LogootSOp  implements  Operation, Serializable{
    

  @Override
  abstract public LogootSOp clone();
     
  abstract public List<TextOperation> apply(LogootSDoc doc);
}
