/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.ai.tools.utils;

import org.apache.shiro.subject.Subject;

/**
 * SubjectHolder
 * @author ThinkGem
 */
public class SubjectHolder {

	private static final InheritableThreadLocal<Subject> threadLocal = new InheritableThreadLocal<>();

    public static Subject getSubject() {
        return threadLocal.get();
    }

    public static void setSubject(final Subject subject) {
        threadLocal.set(subject);
    }

    public static void remove() {
        threadLocal.remove();
    }
}
