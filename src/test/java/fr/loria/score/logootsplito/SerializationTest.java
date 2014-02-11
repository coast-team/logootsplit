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

import java.io.*;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertThat;


public class SerializationTest {



    @Before
    public void setup() {

    }

    @Test
    public void serializeIdentifier() {
        Identifier id = new Identifier(Arrays.asList(new Integer[]{1, 2, 3, 4}), new Integer(12));
        Identifier idAfter = SerializationUtils.serializeAndDeserialize(id);


        //assertThat(3, is(not(4)));

        assertThat(id.base, not(sameInstance(idAfter.base)));
        assertThat(id.base, equalTo(idAfter.base));

        //assertNotSame(id.base, idAfter.base);
        //assertEquals(id.base, idAfter.base);
        //assertNotSame(id.last, idAfter.last);
        //assertEquals(id.last, idAfter.last);
    }



}
