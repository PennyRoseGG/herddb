/*
 Licensed to Diennea S.r.l. under one
 or more contributor license agreements. See the NOTICE file
 distributed with this work for additional information
 regarding copyright ownership. Diennea S.r.l. licenses this file
 to you under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance
 with the License.  You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 KIND, either express or implied.  See the License for the
 specific language governing permissions and limitations
 under the License.

 */
package herddb.core;

import herddb.model.Record;
import herddb.utils.Bytes;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * A page of data loaded in memory
 *
 * @author enrico.olivelli
 */
public class DataPage {

    public final Map<Bytes, Record> data;
    public final long pageId;
    public final boolean readonly;
    public final AtomicLong usedMemory;

    public DataPage(long pageId, long estimatedSize, Map<Bytes, Record> data, boolean readonly) {
        this.pageId = pageId;
        this.readonly = readonly;
        this.data = data;
        this.usedMemory = new AtomicLong(estimatedSize);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 73 * hash + (int) (this.pageId ^ (this.pageId >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DataPage other = (DataPage) obj;
        if (this.pageId != other.pageId) {
            return false;
        }
        return true;
    }

    Record remove(Bytes key) {
        if (readonly) {
            throw new IllegalStateException("page " + pageId + " is readonly!");
        }
        return data.remove(key);
    }

    Record get(Bytes key) {
        return data.get(key);
    }

    Record put(Bytes key, Record newRecord) {
        if (readonly) {
            throw new IllegalStateException("page " + pageId + " is readonly!");
        }
        Record prev = data.put(key, newRecord);
        if (prev != null) {
            usedMemory.addAndGet(newRecord.value.data.length - prev.value.data.length);
        } else {
            usedMemory.addAndGet(newRecord.value.data.length);
        }
        return prev;
    }

    int size() {
        return data.size();
    }

    long getUsedMemory() {
        return usedMemory.get();
    }

    void clear() {
        if (readonly) {
            throw new IllegalStateException("page " + pageId + " is readonly!");
        }
        data.clear();
        usedMemory.set(0);
    }

    @Override
    public String toString() {
        return "DataPage{" + "pageId=" + pageId + ", readonly=" + readonly + ", usedMemory=" + usedMemory + '}';
    }

}