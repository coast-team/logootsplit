package logootsplito;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Stephane Martin <stephane@stephanemartin.fr>
 */
public abstract class LogootSOp<T>  implements Serializable{
    
  @Override
  abstract public LogootSOp<T> clone();
     
  abstract public List<TextOperation> execute(LogootSDoc<T> doc);
}
