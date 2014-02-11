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

import java.io.*;

public class SerializationUtils {


    static class SerializationException extends RuntimeException {
        public SerializationException(Exception e) {
            super(e);
        }
    }

    public static byte[] serialize(Object obj) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(512);
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(obj);
        } catch (IOException ex) {
            throw new SerializationException(ex);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException ex) {
                // ignore exception
            }
        }
        return bos.toByteArray();
    }

    public static Object deserialize(byte[] serializedObj) {
        ByteArrayInputStream bis = new ByteArrayInputStream(serializedObj);
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(bis);
            return in.readObject();
        } catch (ClassNotFoundException ex) {
            throw new SerializationException(ex);
        } catch (IOException ex) {
            throw new SerializationException(ex);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                // ignore exception
            }
        }
    }

    public static <T> T serializeAndDeserialize(T obj) {
        return (T) deserialize(serialize(obj));
    }
}
