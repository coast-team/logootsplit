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

import java.io.Serializable;
import java.util.List;

/**
 * This block kind contain no elements only ids.
 * The elements are on view
 */
public class LogootSBlockLight<T> extends LogootSBlock<T> implements Serializable, Cloneable {

    int nbElement = 0;

    public LogootSBlockLight() {

    }

    public LogootSBlockLight(IdentifierInterval id, int list) {
        super(id);
        nbElement = list;
    }

    public LogootSBlockLight(IdentifierInterval id) {
        super(id);
    }

    @Override
    public LogootSBlockLight<T> clone() throws CloneNotSupportedException {
        LogootSBlockLight<T> o = (LogootSBlockLight<T>) super.clone();
        return o;
    }

    @Override
    List<T> getElements(int begin, int end) {
        throw new UnsupportedOperationException("Version light contains no data");
    }

    @Override
    void addBlock(int pos, List<T> contains) {
        nbElement += contains.size();
        this.getId().begin = Math.min(this.getId().begin, pos);
        this.getId().end = Math.max(this.getId().end, pos + contains.size() - 1);

    }

    @Override
    void delBlock(int begin, int end, int nbElement) {
        this.nbElement -= nbElement;
    }

    @Override
    T getElement(int i) {
        throw new UnsupportedOperationException("Version light contains no data");
    }

    @Override
    int numberOfElements() {
        return nbElement;
    }

    @Override
    public String toString() {
        return "{" + nbElement + "," + this.id + (isMine() ? "mine" : "its") + "}";
    }


}
