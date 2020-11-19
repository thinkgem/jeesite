package com.thinkgem.jeesite.common.utils;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class StringUtilsTest {

    @Test
    public void toLongTest() {

        String s = "778351528171171845";

        Long value = StringUtils.toLong(s);
        System.out.println(value);//778351528171171845

        Assert.assertEquals(778351528171171845L, value.longValue());
    }

    @Test
    public void oldToLongTest() {

        String s = "778351528171171845";

        Long value = this.toLong(s);
        System.out.println(value);//778351528171171840

        Assert.assertNotEquals(778351528171171845L, value.longValue());
    }

    //这个是旧的 tolong方法
    public static Long toLong(Object val) {
        return StringUtils.toDouble(val).longValue();
    }
}