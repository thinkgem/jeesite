/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.common.persistence.dialect.db;

import org.apache.commons.lang3.StringUtils;

import com.thinkgem.jeesite.common.persistence.dialect.Dialect;

/**
 * Sql 2005的方言实现
 * @author poplar.yfyang
 * @version 1.0 2010-10-10 下午12:31
 * @since JDK 1.5
 */
public class SQLServer2005Dialect implements Dialect {

    @Override
    public boolean supportsLimit() {
        return true;
    }

    @Override
    public String getLimitString(String sql, int offset, int limit) {
        return getLimitString(sql, offset,
                limit, Integer.toString(limit));
    }

    /**
     * Add a LIMIT clause to the given SQL SELECT
     * <p/>
     * The LIMIT SQL will look like:
     * <p/>
     * WITH query AS
     * (SELECT TOP 100 percent ROW_NUMBER() OVER (ORDER BY CURRENT_TIMESTAMP) as __row_number__, * from table_name)
     * SELECT *
     * FROM query
     * WHERE __row_number__ BETWEEN :offset and :lastRows
     * ORDER BY __row_number__
     *
     * @param querySqlString   The SQL statement to base the limit query off of.
     * @param offset           Offset of the first row to be returned by the query (zero-based)
     * @param limit            Maximum number of rows to be returned by the query
     * @param limitPlaceholder limitPlaceholder
     * @return A new SQL statement with the LIMIT clause applied.
     */
    private String getLimitString(String querySqlString, int offset, int limit, String limitPlaceholder) {
        StringBuilder pagingBuilder = new StringBuilder();
        String orderby = getOrderByPart(querySqlString);
        String distinctStr = "";

        String loweredString = querySqlString.toLowerCase();
        String sqlPartString = querySqlString;
        if (loweredString.trim().startsWith("select")) {
            int index = 6;
            if (loweredString.startsWith("select distinct")) {
                distinctStr = "DISTINCT ";
                index = 15;
            }
            sqlPartString = sqlPartString.substring(index);
        }
        pagingBuilder.append(sqlPartString);

        // if no ORDER BY is specified use fake ORDER BY field to avoid errors
        if (StringUtils.isEmpty(orderby)) {
            orderby = "ORDER BY CURRENT_TIMESTAMP";
        }

        StringBuilder result = new StringBuilder();
        result.append("WITH query AS (SELECT ")
                .append(distinctStr)
                .append("TOP 100 PERCENT ")
                .append(" ROW_NUMBER() OVER (")
                .append(orderby)
                .append(") as __row_number__, ")
                .append(pagingBuilder)
                .append(") SELECT * FROM query WHERE __row_number__ BETWEEN ")
                .append(offset + 1).append(" AND ").append(offset + limit)
                .append(" ORDER BY __row_number__");

        return result.toString();
    }

    static String getOrderByPart(String sql) {
        String loweredString = sql.toLowerCase();
        int orderByIndex = loweredString.indexOf("order by");
        if (orderByIndex != -1) {
            // if we find a new "order by" then we need to ignore
            // the previous one since it was probably used for a subquery
            return sql.substring(orderByIndex);
        } else {
            return "";
        }
    }
}
