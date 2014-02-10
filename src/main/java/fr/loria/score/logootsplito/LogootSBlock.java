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

public abstract class LogootSBlock<T> implements Serializable, Cloneable {
    IdentifierInterval id;
    boolean mine = false;

    public LogootSBlock() {

    }

    public LogootSBlock(IdentifierInterval id) {
        this.id = id;
    }

    public boolean isMine() {
        return mine;
    }

    public void setMine(boolean mine) {
        this.mine = mine;
    }

    abstract int numberOfElements();

    abstract List<T> getElements(int begin, int end);

    abstract T getElement(int i);

    abstract void addBlock(int pos, List<T> contains);

    abstract void delBlock(int begin, int end, int nbElement);

    IdentifierInterval getId() {
        return id;
    }

    public LogootSBlock<T> clone() throws CloneNotSupportedException {
        LogootSBlock<T> o = (LogootSBlock<T>) super.clone();
        o.id = id.clone();
        return o;
    }

}
