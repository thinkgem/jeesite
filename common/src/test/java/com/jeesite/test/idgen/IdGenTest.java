/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.test.idgen;

import com.jeesite.common.collect.SetUtils;
import com.jeesite.common.idgen.IdGen;

import java.util.Set;

/**
 * ID算法测试类.
 * @author ThinkGem
 * @version 2023-6-9
 */
public class IdGenTest {

	public static void main(String[] args) {
		System.out.println(IdGen.nextCode("8") + " = 9");
		System.out.println(IdGen.nextCode("09") + " = 10");
		System.out.println(IdGen.nextCode("009") + " = 010");
		System.out.println(IdGen.nextCode("T09") + " = T10");
		System.out.println(IdGen.nextCode("TG09") + " = TG10");
		System.out.println(IdGen.nextCode("TG0101") + " = TG0102");
		System.out.println(IdGen.nextCode("TG0109") + " = TG0110");
		System.out.println(IdGen.nextCode("TG02T03") + " = TG02T04");
		System.out.println(IdGen.nextCode("TG02T099") + " = TG02T100");
		System.out.println(IdGen.nextCode("TG02T100") + " = TG02T101");
		System.out.println(IdGen.nextCode("TG02T10A") + " = TG02T10A001");
		System.out.println(IdGen.nextCode("1123117153417957377") + " = 1123117153417957379");
		System.out.println(IdGen.nextCode("0040009") + " = 0040010");
		System.out.println(IdGen.uuid());
		System.out.println(IdGen.nextId());
		// 数值型ID重复验证测试
		Set<String> set = SetUtils.newHashSet();
		try{
			for (int i=0; i<100; i++){
				String id = String.valueOf(IdGen.nextId());
				if (set.contains(id)){
					throw new Exception(id + " exists");
				}
				set.add(id);
				System.out.println(id);
				Thread.sleep(100);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}
