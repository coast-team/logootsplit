package logootsplitO;

import crdt.Operation;
import java.io.Serializable;

/**
 *
 * @author Stephane Martin <stephane@stephanemartin.fr>
 */
public abstract class LogootSOp  implements  Operation, Serializable{
    

  @Override
  abstract public LogootSOp clone();
     
  abstract public void apply(LogootSDoc doc);
}
