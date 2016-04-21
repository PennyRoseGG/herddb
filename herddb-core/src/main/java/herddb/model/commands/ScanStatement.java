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
package herddb.model.commands;

import herddb.model.Predicate;
import herddb.model.Projection;
import herddb.model.Table;
import herddb.model.TableAwareStatement;

/**
 * Lookup a bunch record with a condition
 *
 * @author enrico.olivelli
 */
public class ScanStatement extends TableAwareStatement {

    private final Predicate predicate;
    private final Projection projection;

    public ScanStatement(String tableSpace, Table table, Predicate predicate) {
        this(tableSpace, table.name, Projection.IDENTITY(table.columns), predicate);
    }

    public ScanStatement(String tableSpace, String table, final Projection projection, Predicate predicate) {
        super(table, tableSpace);
        this.predicate = predicate;
        this.projection = projection;
    }

    public Predicate getPredicate() {
        return predicate;
    }

    public Projection getProjection() {
        return projection;
    }

}