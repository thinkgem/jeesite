/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.common.callback;

/**
 * 能接受异常的 Function
 * @author ThinkGem
 */
@FunctionalInterface
public interface FunctionCatchException<T, R> {

    R apply(T t) throws Exception;

}
