package logootsplito;

/**
 *
 * @author oster
 */
public class LogootSFactory {

    public static LogootSDoc<Character> create(int replicaNumber) {
        LogootSDoc<Character> logootS = new LogootSRopes<Character>();
        logootS.setReplicaNumber(replicaNumber);
        return logootS;
    }

    

}
