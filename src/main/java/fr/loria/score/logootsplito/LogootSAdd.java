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

import java.util.LinkedList;
import java.util.List;

public class LogootSAdd<T> implements LogootSOperation<T> {
    Identifier id;
    List<T> l;

    public LogootSAdd() {
    }

    public LogootSAdd(Identifier id, List<T> l) {
        this.id = id;
        this.l = l;
    }

    @Override
    public LogootSAdd<T> clone() throws CloneNotSupportedException {
        LogootSAdd<T> o = (LogootSAdd<T>) super.clone();
        o.id = id.clone();
        o.l = new LinkedList<T>(l);
        return o;
    }

    @Override
    public List<TextOperation> execute(LogootSDoc<T> doc) {
        return doc.addBlock(id, l);
    }

    @Override
    public String toString() {
        return "LogootSAdd{" + "id=" + id + ", l=" + l + '}';
    }

}
