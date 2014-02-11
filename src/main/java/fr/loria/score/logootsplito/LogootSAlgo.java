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

public class LogootSAlgo {

    private LogootSDoc<Character> doc;

    public LogootSAlgo(LogootSDoc<Character> doc, int siteId) {
        this.doc = doc;
        this.doc.setReplicaNumber(siteId);
    }

    public void setReplicaNumber(int replicaNumber) {
        this.doc.setReplicaNumber(replicaNumber);
    }

    public LogootSDoc<Character> getDoc() {
        return this.doc;
    }

    public String lookup() {
        return this.doc.view();
    }

    public LogootSOp insert(int position, String content) {
        return (new TextInsert(position, content)).applyTo(this.doc);
    }

    public LogootSOp remove(int position, int length) {
        return (new TextDelete(position, length)).applyTo(this.doc);
    }

    public List<TextOperation> applyRemote(LogootSOp op) {
        return op.execute(this.doc);
    }
}
