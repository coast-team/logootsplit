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

public class LogootSBlock<T> implements Serializable {

    int nbElement = 0;
    IdentifierInterval id;
    boolean mine = false;

    public LogootSBlock() {

    }

    public LogootSBlock(IdentifierInterval id, int list) {
        this.id = id;
        nbElement = list;
    }

    public LogootSBlock(IdentifierInterval id) {
        this.id = id;
    }

    public IdentifierInterval getId() {
        return id;
    }

    public int numberOfElements() {
        return nbElement;
    }

    public boolean isMine() {
        return mine;
    }

    public void setMine(boolean mine) {
        this.mine = mine;
    }

    public void addBlock(int pos, List<T> contains) {
        nbElement += contains.size();
        this.getId().begin = Math.min(this.getId().begin, pos);
        this.getId().end = Math.max(this.getId().end, pos + contains.size() - 1);

    }

    public void delBlock(int begin, int end, int nbElement) {
        this.nbElement -= nbElement;
    }

     public LogootSBlock<T> copy(){
        LogootSBlock<T> o = new LogootSBlock<T>();
        o.nbElement=this.nbElement;
        o.mine=false;//copy is used to create a new CRDT, not a true copy.
        o.id = this.id.copy();

        return o;
    }

    @Override
    public String toString() {
        return "{" + nbElement + "," + this.id + (isMine() ? "mine" : "its") + "}";
    }

}
