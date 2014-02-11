/**
 * LogootSplit
 * https://bitbucket.org/oster/logootsplit/
 * Copyright (c) 2014, LORIA / Inria / SCORE Team, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library. If not, see <http://www.gnu.org/licenses/>.
 */
package fr.loria.score.logootsplito;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class LogootSOpDel<T> implements LogootSOp<T> {

    private List<IdentifierInterval> lid;

    public LogootSOpDel() {

    }

    public LogootSOpDel(List<IdentifierInterval> lid) {
        this.lid = lid;
    }

    @Override
    public LogootSOpDel<T> clone() throws CloneNotSupportedException {
        LogootSOpDel<T> o = (LogootSOpDel<T>) super.clone();
        o.lid = new LinkedList();

        for (IdentifierInterval id : this.lid) {
            o.lid.add(id.clone());
        }
        return o;
    }

    @Override
    public List<TextOperation> execute(LogootSDoc<T> doc) {
        List l = new ArrayList<TextOperation>();
        for (IdentifierInterval id : lid) {
            l.addAll(doc.delBlock(id));
        }
        return l;
    }

    @Override
    public String toString() {
        return "LogootSOpDel{" + "lid=" + lid + '}';
    }

}
