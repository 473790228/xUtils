/*
 * Copyright (c) 2013. wyouflf (wyouflf@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lidroid.xutils.http;

import com.lidroid.xutils.util.core.LruMemoryCache;

/**
 * Author: wyouflf
 * Date: 13-8-1
 * Time: 下午12:04
 */
public class HttpGetCache {

    /**
     * key: url
     * value: response result
     */
    private final LruMemoryCache<String, String> mMemoryCache;

    private final static int DEFAULT_CACHE_SIZE = 1024 * 100;// string length
    private final static long DEFAULT_EXPIRY_TIME = 1000 * 60; // 60 seconds

    private boolean enabled = true;

    private int cacheSize = DEFAULT_CACHE_SIZE;

    private static long defaultExpiryTime = DEFAULT_EXPIRY_TIME;

    /**
     * HttpGetCache(HttpGetCache.DEFAULT_CACHE_SIZE, HttpGetCache.DEFAULT_EXPIRY_TIME);
     */
    public HttpGetCache() {
        this(HttpGetCache.DEFAULT_CACHE_SIZE, HttpGetCache.DEFAULT_EXPIRY_TIME);
    }

    public HttpGetCache(int strLength, long defaultExpiryTime) {
        this.cacheSize = strLength;
        HttpGetCache.defaultExpiryTime = defaultExpiryTime;

        mMemoryCache = new LruMemoryCache<String, String>(this.cacheSize) {
            @Override
            protected int sizeOf(String key, String value) {
                if (value == null) return 0;
                return value.length();
            }
        };
    }

    public void setCacheSize(int strLength) {
        mMemoryCache.setMaxSize(strLength);
    }

    public static void setDefaultExpiryTime(long defaultExpiryTime) {
        HttpGetCache.defaultExpiryTime = defaultExpiryTime;
    }

    public static long getDefaultExpiryTime() {
        return HttpGetCache.defaultExpiryTime;
    }

    public void put(String url, String result) {
        put(url, result, defaultExpiryTime);
    }

    public void put(String url, String result, long expiry) {
        if (!enabled || url == null || result == null || expiry < 1) return;

        mMemoryCache.put(url, result, System.currentTimeMillis() + expiry);
    }

    public String get(String url) {
        return (enabled && url != null) ? mMemoryCache.get(url) : null;
    }

    public void clear() {
        mMemoryCache.evictAll();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
