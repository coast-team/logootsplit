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

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class IdentifierTest {

    @Test
    public void testSomeMethod() {
        Identifier id1 = new Identifier(Arrays.asList(0, 0, 7, 0), 0);
        Identifier id2 = new Identifier(Arrays.asList(0, 0, 7, 0, 0, 536870914, 7, 3));
        assertEquals(-1, id1.compareTo(id2));
    }
}