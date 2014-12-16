/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.common.persistence.dialect;

/**
 * 类似hibernate的Dialect,但只精简出分页部分
 *
 * @author poplar.yfyang
 * @version 1.0 2011-11-18 下午12:31
 * @since JDK 1.5
 */
public interface Dialect {

    /**
     * 数据库本身是否支持分页当前的分页查询方式
     * 如果数据库不支持的话，则不进行数据库分页
     *
     * @return true：支持当前的分页查询方式
     */
    public boolean supportsLimit();

    /**
     * 将sql转换为分页SQL，分别调用分页sql
     *
     * @param sql    SQL语句
     * @param offset 开始条数
     * @param limit  每页显示多少纪录条数
     * @return 分页查询的sql
     */
    public String getLimitString(String sql, int offset, int limit);

}
