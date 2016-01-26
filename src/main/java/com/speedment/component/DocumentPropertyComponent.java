/*
 * Copyright 2016 Speedment, Inc..
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

package com.speedment.component;

import com.speedment.annotation.Api;
import com.speedment.config.Document;
import com.speedment.config.db.Dbms;
import com.speedment.config.db.ForeignKey;
import com.speedment.config.db.Index;
import com.speedment.config.db.Project;
import com.speedment.config.db.Schema;
import com.speedment.config.db.Table;
import com.speedment.internal.ui.config.DocumentProperty;
import java.util.Map;
import java.util.function.Function;

/**
 * Describes which implementations the {@link Document} interface to use at
 * different places in the config tree. This is purely used by the UI.
 * 
 * @author Emil Forslund
 * @since 2.3
 */
@Api(version = "2.3")
public interface DocumentPropertyComponent extends Component {
    
    final String[]
        DBMSES              = {Project.DBMSES},
        SCHEMAS             = {Project.DBMSES, Dbms.SCHEMAS},
        TABLES              = {Project.DBMSES, Dbms.SCHEMAS, Schema.TABLES},
        COLUMNS             = {Project.DBMSES, Dbms.SCHEMAS, Schema.TABLES, Table.COLUMNS},
        PRIMARY_KEY_COLUMNS = {Project.DBMSES, Dbms.SCHEMAS, Schema.TABLES, Table.PRIMARY_KEY_COLUMNS},
        FOREIGN_KEYS        = {Project.DBMSES, Dbms.SCHEMAS, Schema.TABLES, Table.FOREIGN_KEYS},
        FOREIGN_KEY_COLUMNS = {Project.DBMSES, Dbms.SCHEMAS, Schema.TABLES, Table.FOREIGN_KEYS, ForeignKey.FOREIGN_KEY_COLUMNS},
        INDEXES             = {Project.DBMSES, Dbms.SCHEMAS, Schema.TABLES, Table.INDEXES},
        INDEX_COLUMNS       = {Project.DBMSES, Dbms.SCHEMAS, Schema.TABLES, Table.FOREIGN_KEYS, Index.INDEX_COLUMNS};
    
    @FunctionalInterface
    public interface Constructor<PARENT extends DocumentProperty> {
        DocumentProperty create(PARENT parent, Map<String, Object> data);
    }

    /**
     * {@inheritDoc} 
     */
    @Override
    default Class<? extends Component> getComponentClass() {
        return DocumentPropertyComponent.class;
    }

    /**
     * Sets the method used to produce an observable view of a branch in the 
     * tree. The specified {@code keyPath} describes where in the tree this
     * method should be used. The last string in the array is the key for the
     * collection that is added to.
     * <p>
     * <b>Example:</b>
     * <ul>
     *     <li><pre>setConstructor(ProjectProperty::new);</pre>
     *     <li><pre>setConstructor(DbmsProperty::new, "dbmses")</pre>
     *     <li><pre>setConstructor(SchemaProperty::new, "dbmses", "schemas");</pre>
     *     <li><pre>setConstructor(TableProperty::new, "dbmses", "schemas", "tables");</pre>
     * </ul>
     * 
     * @param <PARENT>     the type of the parent
     * @param constructor  the new constructor to use
     * @param keyPath      the path to the collection where to use it
     */
    <PARENT extends DocumentProperty> void setConstructor(Constructor<PARENT> constructor, String... keyPath);
    
    /**
     * Creates a new observable document using the installed constructor, at
     * the path specified by {@code keyPath}. To change the implementation, use 
     * {@link #setConstructor(Function, String[])}.
     * 
     * @param keyPath   the path of the constructor
     * @return          the created document
     */
    Constructor<?> getConstructor(String... keyPath);
}