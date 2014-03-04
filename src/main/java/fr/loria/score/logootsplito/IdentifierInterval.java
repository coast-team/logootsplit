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
import java.util.LinkedList;
import java.util.List;

public class IdentifierInterval implements Serializable {

    private static final long serialVersionUID = -915306842429379721L;
    
    List<Integer> base;
    int begin;
    int end;

    public IdentifierInterval() {

    }

    public IdentifierInterval(List<Integer> base, int begin, int end) {
        this.base = base;
        this.begin = begin;
        this.end = end;
    }

   public IdentifierInterval copy() {
        IdentifierInterval o = new IdentifierInterval();
        o.begin = this.begin;
        o.end = this.end;
        o.base = new LinkedList<Integer>(base);
        return o;
    }
    
    
    public List<Integer> getBase() {
        return base;
    }

    public Identifier getBeginId() {
        return new Identifier(base, begin);
    }

    public Identifier getEndId() {
        return new Identifier(base, end);
    }

    public int getBegin() {
        return begin;
    }

    public int getEnd() {
        return end;
    }

    public Identifier getBaseId() {
        return new Identifier(base);
    }

    public Identifier getBaseId(Integer u) {
        return new Identifier(base, u);
    }

    public void setBegin(int begin) {
        this.begin = begin;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public void addBegin(int begin) {
        this.begin += begin;
    }

    public void addEnd(int end) {
        this.end += end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IdentifierInterval that = (IdentifierInterval) o;

        if (begin != that.begin) return false;
        if (end != that.end) return false;
        if (base != null ? !base.equals(that.base) : that.base != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = base != null ? base.hashCode() : 0;
        result = 31 * result + begin;
        result = 31 * result + end;
        return result;
    }

    @Override
    public String toString() {
        return "IdentifierInterval{" + base + ",[" + begin + ".." + end + "]}";
    }

}
