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

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class ScenarioTest {

    @Before
    public void setup() {

    }

    @Test
    public void scenarioBasicAdd() {
        LogootSDoc<Character> logootS1 = LogootSFactory.create(12);
        TextOperation insert = new TextInsert(2, "abc");
        LogootSOperation<Character> op = insert.applyTo(logootS1);

        LogootSDoc<Character> logootS2 = LogootSFactory.create(14);
        List<TextOperation> ops = op.execute(logootS2);

        assertEquals(logootS2.view(), "abc");
        assertEquals(logootS2.view(), logootS1.view());

        StringBufferTextDocument doc2 = new StringBufferTextDocument();
        for (TextOperation o : ops) {
            doc2.apply(o);
        }
        assertEquals(doc2.toString(), "abc");
    }

    @Test
    public void scenarioBasicDel() {
        LogootSDoc<Character> logootS1 = LogootSFactory.create(12);
        TextOperation insert = new TextInsert(2, "abc");
        LogootSOperation<Character> op = insert.applyTo(logootS1);

        LogootSDoc<Character> logootS2 = LogootSFactory.create(14);
        List<TextOperation> ops = op.execute(logootS2);

        assertEquals("abc", logootS2.view());
        assertEquals(logootS1.view(), logootS2.view());

        StringBufferTextDocument doc2 = new StringBufferTextDocument();
        for (TextOperation o : ops) {
            doc2.apply(o);
        }
        assertEquals("abc", doc2.toString());

        // remove "bc"
        TextOperation delete = new TextDelete(1, 2);
        op = delete.applyTo(logootS1);
        ops = op.execute(logootS2);
        assertEquals("a", logootS2.view());
        assertEquals(logootS1.view(), logootS2.view());

        for (TextOperation o : ops) {
            doc2.apply(o);
        }
        assertEquals("a", doc2.toString());
    }

    @Test
    public void scenarioAddAndSplit() {
        LogootSDoc<Character> logootS1 = LogootSFactory.create(12);
        TextOperation insert = new TextInsert(2, "abc");
        LogootSOperation<Character> op = insert.applyTo(logootS1);

        LogootSDoc<Character> logootS2 = LogootSFactory.create(14);
        List<TextOperation> ops = op.execute(logootS2);

        assertEquals(logootS2.view(), "abc");
        assertEquals(logootS2.view(), logootS1.view());

        StringBufferTextDocument doc2 = new StringBufferTextDocument();
        for (TextOperation o : ops) {
            doc2.apply(o);
        }
        assertEquals(doc2.toString(), "abc");
    }

    class StringBufferTextDocument implements TextDocument {

        private final StringBuffer state = new StringBuffer();

        @Override
        public void apply(TextOperation op) {
            if (op instanceof TextInsert) {
                TextInsert opIns = (TextInsert) op;
                state.insert(opIns.getOffset(), opIns.getContent());
            } else if (op instanceof TextDelete) {
                TextDelete opDel = (TextDelete) op;
                state.delete(opDel.getOffset(), opDel.getOffset() + opDel.getLength());
            } else {
                throw new UnsupportedOperationException("Unsupported type of TextOperation");
            }
        }

        @Override
        public String toString() {
            return this.state.toString();
        }

    }

}
