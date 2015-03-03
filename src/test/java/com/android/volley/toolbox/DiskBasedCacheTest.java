/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.volley.toolbox;

import com.android.volley.Cache;
import com.android.volley.toolbox.DiskBasedCache.CacheHeader;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class DiskBasedCacheTest {

    // Simple end-to-end serialize/deserialize test.
    @Test public void cacheHeaderSerialization() throws Exception {
        Cache.Entry e = new Cache.Entry();
        e.data = new byte[8];
        e.serverDate = 1234567L;
        e.lastModified = 13572468L;
        e.ttl = 9876543L;
        e.softTtl = 8765432L;
        e.etag = "etag";
        e.responseHeaders = new HashMap<String, String>();
        e.responseHeaders.put("fruit", "banana");

        CacheHeader first = new CacheHeader("my-magical-key", e);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        first.writeHeader(baos);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        CacheHeader second = CacheHeader.readHeader(bais);

        assertEquals(first.key, second.key);
        assertEquals(first.serverDate, second.serverDate);
        assertEquals(first.lastModified, second.lastModified);
        assertEquals(first.ttl, second.ttl);
        assertEquals(first.softTtl, second.softTtl);
        assertEquals(first.etag, second.etag);
        assertEquals(first.responseHeaders, second.responseHeaders);
    }

    @Test public void serializeInt() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DiskBasedCache.writeInt(baos, 0);
        DiskBasedCache.writeInt(baos, 19791214);
        DiskBasedCache.writeInt(baos, -20050711);
        DiskBasedCache.writeInt(baos, Integer.MIN_VALUE);
        DiskBasedCache.writeInt(baos, Integer.MAX_VALUE);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        assertEquals(DiskBasedCache.readInt(bais), 0);
        assertEquals(DiskBasedCache.readInt(bais), 19791214);
        assertEquals(DiskBasedCache.readInt(bais), -20050711);
        assertEquals(DiskBasedCache.readInt(bais), Integer.MIN_VALUE);
        assertEquals(DiskBasedCache.readInt(bais), Integer.MAX_VALUE);
    }

    @Test public void serializeLong() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DiskBasedCache.writeLong(baos, 0);
        DiskBasedCache.writeLong(baos, 31337);
        DiskBasedCache.writeLong(baos, -4160);
        DiskBasedCache.writeLong(baos, 4295032832L);
        DiskBasedCache.writeLong(baos, -4314824046L);
        DiskBasedCache.writeLong(baos, Long.MIN_VALUE);
        DiskBasedCache.writeLong(baos, Long.MAX_VALUE);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        assertEquals(DiskBasedCache.readLong(bais), 0);
        assertEquals(DiskBasedCache.readLong(bais), 31337);
        assertEquals(DiskBasedCache.readLong(bais), -4160);
        assertEquals(DiskBasedCache.readLong(bais), 4295032832L);
        assertEquals(DiskBasedCache.readLong(bais), -4314824046L);
        assertEquals(DiskBasedCache.readLong(bais), Long.MIN_VALUE);
        assertEquals(DiskBasedCache.readLong(bais), Long.MAX_VALUE);
    }

    @Test public void serializeString() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DiskBasedCache.writeString(baos, "");
        DiskBasedCache.writeString(baos, "This is a string.");
        DiskBasedCache.writeString(baos, "ファイカス");
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        assertEquals(DiskBasedCache.readString(bais), "");
        assertEquals(DiskBasedCache.readString(bais), "This is a string.");
        assertEquals(DiskBasedCache.readString(bais), "ファイカス");
    }

    @Test public void serializeMap() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Map<String, String> empty = new HashMap<String, String>();
        DiskBasedCache.writeStringStringMap(empty, baos);
        DiskBasedCache.writeStringStringMap(null, baos);
        Map<String, String> twoThings = new HashMap<String, String>();
        twoThings.put("first", "thing");
        twoThings.put("second", "item");
        DiskBasedCache.writeStringStringMap(twoThings, baos);
        Map<String, String> emptyKey = new HashMap<String, String>();
        emptyKey.put("", "value");
        DiskBasedCache.writeStringStringMap(emptyKey, baos);
        Map<String, String> emptyValue = new HashMap<String, String>();
        emptyValue.put("key", "");
        DiskBasedCache.writeStringStringMap(emptyValue, baos);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        assertEquals(DiskBasedCache.readStringStringMap(bais), empty);
        assertEquals(DiskBasedCache.readStringStringMap(bais), empty); // null reads back empty
        assertEquals(DiskBasedCache.readStringStringMap(bais), twoThings);
        assertEquals(DiskBasedCache.readStringStringMap(bais), emptyKey);
        assertEquals(DiskBasedCache.readStringStringMap(bais), emptyValue);
    }

    /** deserializing the old format into the new one. */
    @Test public void testCacheHeaderSerializationOldToNewFormat() throws Exception {

        final int CACHE_MAGIC = 0x20140623;
        final String key = "key";
        final String etag = "etag";
        final long serverDate = 1234567890l;
        final long ttl = 1357924680l;
        final long softTtl = 2468013579l;

        Map<String, String> responseHeaders = new HashMap<String, String>();
        responseHeaders.put("first", "thing");
        responseHeaders.put("second", "item");

        // write old sytle header (without lastModified)
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DiskBasedCache.writeInt(baos, CACHE_MAGIC);
        DiskBasedCache.writeString(baos, key);
        DiskBasedCache.writeString(baos, etag == null ? "" : etag);
        DiskBasedCache.writeLong(baos, serverDate);
        DiskBasedCache.writeLong(baos, ttl);
        DiskBasedCache.writeLong(baos, softTtl);
        DiskBasedCache.writeStringStringMap(responseHeaders, baos);

        // read / test new style header (with lastModified)
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        CacheHeader cacheHeader = CacheHeader.readHeader(bais);

        assertEquals(cacheHeader.key, key);
        assertEquals(cacheHeader.etag, etag);
        assertEquals(cacheHeader.serverDate, serverDate);
        assertEquals(cacheHeader.ttl, ttl);
        assertEquals(cacheHeader.softTtl, softTtl);
        assertEquals(cacheHeader.responseHeaders, responseHeaders);

        // the old format doesn't know lastModified
        assertEquals(cacheHeader.lastModified, 0);
    }

    @Test
    public void publicMethods() throws Exception {
        // Catch-all test to find API-breaking changes.
        assertNotNull(DiskBasedCache.class.getConstructor(File.class, int.class));
        assertNotNull(DiskBasedCache.class.getConstructor(File.class));

        assertNotNull(DiskBasedCache.class.getMethod("getFileForKey", String.class));
    }
}
