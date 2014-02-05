/*
 *  Replication Benchmarker
 *  https://github.com/score-team/replication-benchmarker/
 *  Copyright (C) 2013 LORIA / Inria / SCORE Team
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package logootsplitO;

import java.util.List;

/**
 *
 * @author Stephane Martin <stephane@stephanemartin.fr>
 */
public class LogootSOpAdd extends LogootSOp{
    Identifier id;
    List l;

    public LogootSOpAdd(Identifier id, List l) {
        this.id = id;
        this.l = l;
    }
    
    
    @Override
    public LogootSOpAdd clone() {
        return new  LogootSOpAdd(id,l);
    }

   

    @Override
    public void apply(LogootSDoc doc) {
        doc.addBlock(id, l);
    }

    @Override
    public String toString() {
        return "LogootSOpAdd{" + "id=" + id + ", l=" + l + '}';
    }
    
}
