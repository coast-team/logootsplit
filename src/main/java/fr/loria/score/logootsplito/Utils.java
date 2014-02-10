package fr.loria.score.logootsplito;

import java.util.LinkedList;
import java.util.List;

public class Utils {

    public static List<Character> convertStringToCharactersList(String str) {
        List<Character> l = new LinkedList();
        for (int i = 0; i < str.length(); i++) {
            l.add(str.charAt(i));
        }
        return l;
    }

    public static <T> String convertCharactersListToString(List<T> r) {
        StringBuilder str = new StringBuilder(r.size());
        for (T o : r) {
            str.append(o);
        }
        return str.toString();
    }

}
