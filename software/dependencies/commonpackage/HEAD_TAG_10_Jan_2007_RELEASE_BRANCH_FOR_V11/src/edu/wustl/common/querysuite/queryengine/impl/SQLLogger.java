/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.common.querysuite.queryengine.impl;

import edu.wustl.common.util.FileLogger;

class SQLLogger extends FileLogger<String> {
    SQLLogger() {
        super(false, false);
    }

    @Override
    protected String getLogFileExtension() {
        return "sql";
    }

    @Override
    protected String getBaseDir() {
        return System.getProperty("user.home") + "/sql_log";
    }

    @Override
    protected String getLogFileNamePrefix() {
        return "query";
    }
}
