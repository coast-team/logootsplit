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

import java.util.List;

public interface LogootSDoc<T> {

    public LogootSDoc<T> duplicate(int newReplicaNumber);

    public void setReplicaNumber(int i);

    public LogootSOperation insertLocal(int pos, List<T> l);

    public LogootSOperation delLocal(int begin, int end);

    public List<TextOperation> addBlock(Identifier id, List<T> l);

    public List<TextOperation> delBlock(IdentifierInterval id);

    public String view();

    public int viewLength();
}
