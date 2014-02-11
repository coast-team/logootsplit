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

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;


public class SerializationTest {



    @Before
    public void setup() {

    }

    @Test
    public void serializeIdentifier() {
        Identifier id = new Identifier(Arrays.asList(new Integer[]{1, 2, 3, 4}), new Integer(12));
        Identifier idAfter = SerializationUtils.serializeAndDeserialize(id);

        assertEquals(id, idAfter);
        assertThat(id.base, allOf(not(sameInstance(idAfter.base)), equalTo(idAfter.base)));
        assertThat(id.last, allOf(not(sameInstance(idAfter.last)), equalTo(idAfter.last)));
   }

    @Test
    public void serializeIdentifierInterval() {
        IdentifierInterval iid = new IdentifierInterval(Arrays.asList(new Integer[]{1, 2, 3, 4}), new Integer(7), new Integer(9));
        IdentifierInterval iidAfter = SerializationUtils.serializeAndDeserialize(iid);

        assertThat(iid.base, allOf(not(sameInstance(iidAfter.base)), equalTo(iidAfter.base)));
        assertEquals(iid.begin, iidAfter.begin);
        assertEquals(iid.end, iidAfter.end);
    }


    @Test
    public void serializeLogootSOpAdd() {
        Identifier id = new Identifier(Arrays.asList(new Integer[]{1, 2, 3, 4}), new Integer(12));
        List<Character> atom = Arrays.asList('a', 'b', 'c');
        LogootSOpAdd add = new LogootSOpAdd(id, atom);
        LogootSOpAdd addAfterSerialization = SerializationUtils.serializeAndDeserialize(add);

        assertThat(add.id, allOf(not(sameInstance(addAfterSerialization.id)), equalTo(addAfterSerialization.id)));
        assertThat(add.l, allOf(not(sameInstance(addAfterSerialization.l)), equalTo(addAfterSerialization.l)));
    }

    @Test
    public void serializeLogootSOpDel() {
        IdentifierInterval iid = new IdentifierInterval(Arrays.asList(new Integer[]{1, 2, 3, 4}), new Integer(7), new Integer(9));
        IdentifierInterval iid2 = new IdentifierInterval(Arrays.asList(new Integer[]{5, 6, 7, 8}), new Integer(13), new Integer(17));
        List<IdentifierInterval> lid = Arrays.asList(iid, iid2);
        LogootSOpDel del = new LogootSOpDel(lid);
        LogootSOpDel delAfterSerialization = SerializationUtils.serializeAndDeserialize(del);

        List<IdentifierInterval> lidAfterSerialization = delAfterSerialization.getLid();
        assertThat(iid, allOf(not(sameInstance(lidAfterSerialization.get(0))), equalTo(lidAfterSerialization.get(0))));
        assertThat(iid2, allOf(not(sameInstance(lidAfterSerialization.get(1))), equalTo(lidAfterSerialization.get(1))));
    }

    @Test
    public void serializeLogootSBlock() {
        IdentifierInterval iid = new IdentifierInterval(Arrays.asList(new Integer[]{1, 2, 3, 4}), new Integer(7), new Integer(9));
        LogootSBlock block = new LogootSBlock(iid, 5);
        LogootSBlock blockAfterSerialization = SerializationUtils.serializeAndDeserialize(block);

        assertThat(block.id, allOf(not(sameInstance(blockAfterSerialization.id)), equalTo(blockAfterSerialization.id)));
        assertEquals(block.mine, blockAfterSerialization.mine);
        assertEquals(block.nbElement, blockAfterSerialization.nbElement);
    }

}
