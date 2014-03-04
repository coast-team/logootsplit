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

public class TextDelete implements TextOperation, Serializable {

    private static final long serialVersionUID = 6002253764872486505L;

    private int offset;
    private int length;

    public TextDelete() 
    {      
    }
    
    public TextDelete(int offset, int length) {
        this.offset = offset;
        this.length = length;
    }

    public int getOffset() {
        return this.offset;
    }

    public int getLength() {
        return this.length;
    }

    @Override
    public LogootSOperation applyTo(LogootSDoc<Character> doc) {
        return doc.delLocal(offset, offset + length - 1);
    }
}
