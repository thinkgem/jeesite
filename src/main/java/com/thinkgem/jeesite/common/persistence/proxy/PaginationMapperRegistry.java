/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.common.persistence.proxy;

import org.apache.ibatis.binding.BindingException;
import org.apache.ibatis.binding.MapperRegistry;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;

/**
 * <p>
 * .
 * </p>
 *
 * @author poplar.yfyang
 * @version 1.0 2012-05-13 上午10:06
 * @since JDK 1.5
 */
public class PaginationMapperRegistry extends MapperRegistry {
    public PaginationMapperRegistry(Configuration config) {
        super(config);
    }

    @Override
    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        if (!hasMapper(type)) {
            throw new BindingException("Type " + type + " is not known to the MapperRegistry.");
        }
        try {
            return PaginationMapperProxy.newMapperProxy(type, sqlSession);
        } catch (Exception e) {
            throw new BindingException("Error getting mapper instance. Cause: " + e, e);
        }
    }
}
