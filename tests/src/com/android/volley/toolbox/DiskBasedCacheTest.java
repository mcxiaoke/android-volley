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

import android.test.AndroidTestCase;

import com.android.volley.Cache;
import com.android.volley.toolbox.DiskBasedCache.CacheHeader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class DiskBasedCacheTest extends AndroidTestCase {

    // Simple end-to-end serialize/deserialize test.
    public void testCacheHeaderSerialization() throws Exception {
        Cache.Entry e = new Cache.Entry();
        e.data = new byte[8];
        e.serverDate = 1234567L;
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
        assertEquals(first.ttl, second.ttl);
        assertEquals(first.softTtl, second.softTtl);
        assertEquals(first.etag, second.etag);
        assertEquals(first.responseHeaders, second.responseHeaders);
    }

}
