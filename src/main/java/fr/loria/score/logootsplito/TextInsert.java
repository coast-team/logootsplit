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

public class TextInsert implements TextOperation, Serializable {

    private static final long serialVersionUID = 7489199565010828576L;
    
    private int offset;
    private List<Character> content;

    public TextInsert() 
    {        
    }
    
    public TextInsert(int offset, String content) {
        this.offset = offset;
        this.content = Utils.convertStringToCharactersList(content);
    }

    public int getOffset() {
        return this.offset;
    }

    public String getContent() {
        return Utils.convertCharactersListToString(this.content);
    }

    @Override
    public LogootSOperation applyTo(LogootSDoc<Character> doc) {
        return doc.insertLocal(offset, content);

    }

}
