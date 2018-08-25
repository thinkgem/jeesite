/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.common.collect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.beanutils.PropertyUtils;

import com.jeesite.common.callback.MethodCallback;
import com.jeesite.common.lang.ObjectUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.reflect.ReflectUtils;

/**
 * List工具类
 * @author ThinkGem
 * @version 2015-1-23
 */
@SuppressWarnings("rawtypes")
public class ListUtils extends org.apache.commons.collections.ListUtils {

	/**
     * 是否包含字符串
     * @param str 验证字符串
     * @param strs 字符串组
     * @return 包含返回true
     */ 	
    public static boolean inString(String str, List<String> strs){
		if (str != null && strs != null){
        	for (String s : strs){
        		if (str.equals(StringUtils.trim(s))){
        			return true;
        		}
        	}
    	}
    	return false;
    }
    
	public static <E> ArrayList<E> newArrayList() {
		return new ArrayList<E>();
	}
	
	@SafeVarargs
	public static <E> ArrayList<E> newArrayList(E... elements) {
		ArrayList<E> list = new ArrayList<E>(elements.length);
		Collections.addAll(list, elements);
		return list;
	}
	
	public static <E> ArrayList<E> newArrayList(Iterable<? extends E> elements) {
		return (elements instanceof Collection) ? new ArrayList<E>(cast(elements))
				: newArrayList(elements.iterator());
	}
	
	public static <E> ArrayList<E> newArrayList(Iterator<? extends E> elements) {
		ArrayList<E> list = newArrayList();
		addAll(list, elements);
		return list;
	}
	
	public static <E> LinkedList<E> newLinkedList() {
		return new LinkedList<E>();
	}
	
	public static <E> LinkedList<E> newLinkedList(Iterable<? extends E> elements) {
		LinkedList<E> list = newLinkedList();
		addAll(list, elements);
		return list;
	}
	
	public static <E> CopyOnWriteArrayList<E> newCopyOnWriteArrayList() {
		return new CopyOnWriteArrayList<E>();
	}
	
	public static <E> CopyOnWriteArrayList<E> newCopyOnWriteArrayList(Iterable<? extends E> elements) {
		Collection<? extends E> elementsCollection = (elements instanceof Collection)
				? cast(elements) : newArrayList(elements);
		return new CopyOnWriteArrayList<E>(elementsCollection);
	}
	
	private static <T> Collection<T> cast(Iterable<T> iterable) {
		return (Collection<T>) iterable;
	}

	private static <T> boolean addAll(Collection<T> addTo, Iterator<? extends T> iterator) {
		boolean wasModified = false;
		while (iterator.hasNext()) {
			wasModified |= addTo.add(iterator.next());
		}
		return wasModified;
	}

	public static <T> boolean addAll(Collection<T> addTo, Iterable<? extends T> elementsToAdd) {
		if (elementsToAdd instanceof Collection) {
			Collection<? extends T> c = cast(elementsToAdd);
			return addTo.addAll(c);
		}
		return addAll(addTo, elementsToAdd.iterator());
	}
	
	/**
	 * 提取集合中的对象的两个属性(通过Getter函数), 组合成Map.
	 * @param collection 来源集合.
	 * @param keyPropertyName 要提取为Map中的Key值的属性名.
	 * @param valuePropertyName 要提取为Map中的Value值的属性名.
	 */
	@SuppressWarnings("unchecked")
	public static Map extractToMap(final Collection collection, final String keyPropertyName,
			final String valuePropertyName) {
		Map map = new HashMap(collection.size());
		try {
			for (Object obj : collection) {
				map.put(PropertyUtils.getProperty(obj, keyPropertyName),
						PropertyUtils.getProperty(obj, valuePropertyName));
			}
		} catch (Exception e) {
			throw ReflectUtils.convertReflectionExceptionToUnchecked("", e);
		}
		return map;
	}

	/**
	 * 提取集合中的对象的一个属性(通过Getter函数), 组合成List.
	 * @param collection 来源集合.
	 * @param propertyName 要提取的属性名.
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> extractToList(final Collection collection, final String propertyName) {
		if (collection == null){
			return newArrayList();
		}
		List list = new ArrayList(collection.size());
		try {
			for (Object obj : collection) {
				list.add(PropertyUtils.getProperty(obj, propertyName));
			}
		} catch (Exception e) {
			throw ReflectUtils.convertReflectionExceptionToUnchecked("", e);
		}
		return list;
	}

	/**
	 * 提取集合中的对象的一个属性(通过Getter函数), 组合成List.
	 * @param collection 来源集合.
	 * @param propertyName 要提取的属性名.
	 * @param prefix 符合前缀的信息(为空则忽略前缀)
	 * @param isNotBlank 仅包含不为空值(空字符串也排除)
	 */
	public static List<String> extractToList(final Collection collection, final String propertyName, 
			final String prefix, final boolean isNotBlank) {
		List<String> list = new ArrayList<String>(collection.size());
		try {
			for (Object obj : collection) {
				String value = (String)PropertyUtils.getProperty(obj, propertyName);
				if (StringUtils.isBlank(prefix) || StringUtils.startsWith(value, prefix)){
					if (isNotBlank){
						if (StringUtils.isNotBlank(value)){
							list.add(value);
						}
					}else{
						list.add(value);
					}					
				}				
			}
		} catch (Exception e) {
			throw ReflectUtils.convertReflectionExceptionToUnchecked("", e);
		}
		return list;
	}

	/**
	 * 提取集合中的对象的一个属性(通过Getter函数), 组合成由分割符分隔的字符串.
	 * 
	 * @param collection 来源集合.
	 * @param propertyName 要提取的属性名.
	 * @param separator 分隔符.
	 */
	public static String extractToString(final Collection collection, final String propertyName, final String separator) {
		List list = extractToList(collection, propertyName);
		return StringUtils.join(list, separator);
	}

	/**
	 * 转换Collection所有元素(通过toString())为String, 中间以 separator分隔。
	 */
	public static String convertToString(final Collection collection, final String separator) {
		return StringUtils.join(collection, separator);
	}

	/**
	 * 转换Collection所有元素(通过toString())为String, 每个元素的前面加入prefix，后面加入postfix，如<div>mymessage</div>。
	 */
	public static String convertToString(final Collection collection, final String prefix, final String postfix) {
		StringBuilder builder = new StringBuilder();
		for (Object o : collection) {
			builder.append(prefix).append(o).append(postfix);
		}
		return builder.toString();
	}

	/**
	 * 判断是否为空.
	 */
	public static boolean isEmpty(Collection collection) {
		return (collection == null || collection.isEmpty());
	}

	/**
	 * 判断是否为不为空.
	 */
	public static boolean isNotEmpty(Collection collection) {
		return !(isEmpty(collection));
	}

	/**
	 * 取得Collection的第一个元素，如果collection为空返回null.
	 */
	public static <T> T getFirst(Collection<T> collection) {
		if (isEmpty(collection)) {
			return null;
		}
		return collection.iterator().next();
	}

	/**
	 * 获取Collection的最后一个元素 ，如果collection为空返回null.
	 */
	public static <T> T getLast(Collection<T> collection) {
		if (isEmpty(collection)) {
			return null;
		}
		//当类型为List时，直接取得最后一个元素 。
		if (collection instanceof List) {
			List<T> list = (List<T>) collection;
			return list.get(list.size() - 1);
		}
		//其他类型通过iterator滚动到最后一个元素.
		Iterator<T> iterator = collection.iterator();
		while (true) {
			T current = iterator.next();
			if (!iterator.hasNext()) {
				return current;
			}
		}
	}

	/**
	 * 返回a+b的新List.
	 */
	public static <T> List<T> union(final Collection<T> a, final Collection<T> b) {
		List<T> result = new ArrayList<T>(a);
		result.addAll(b);
		return result;
	}

	/**
	 * 返回a-b的新List.
	 */
	public static <T> List<T> subtract(final Collection<T> a, final Collection<T> b) {
		List<T> list = new ArrayList<T>(a);
		for (T element : b) {
			list.remove(element);
		}
		return list;
	}

	/**
	 * 返回a与b的交集的新List.
	 */
	public static <T> List<T> intersection(Collection<T> a, Collection<T> b) {
		List<T> list = new ArrayList<T>();
		for (T element : a) {
			if (b.contains(element)) {
				list.add(element);
			}
		}
		return list;
	}
	
//	/**
//	 * 将字符串使用“,”分隔，转换为，多对多中间表列表的转换
//	 * @param clazz 当前实体类
//	 * @param attrNameOne 中间表A字段
//	 * @param attrValueOne 中间表A字段值
//	 * @param attrNameTwo 中间表B字段名
//	 * @param attrValueTwos 中间表B字段值（多个采用“,”分隔）
//	 */
//	public static <T> List<T> toManyToManyList(Class<T> clazz, String attrNameOne,
//			String attrValueOne, String attrNameTwo, String attrValueTwos){
//		List<T> list = ListUtils.newArrayList();
//		if (attrValueTwos != null){
//			for (String attrValueB : StringUtils.split(attrValueTwos, ",")){
//				try {
//					T t = clazz.newInstance();
//					ReflectUtils.invokeSetter(t, attrNameOne, attrValueOne);
//					ReflectUtils.invokeSetter(t, attrNameTwo, attrValueB);
//					list.add(t);
//				} catch (InstantiationException e) {
//					e.printStackTrace();
//				} catch (IllegalAccessException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//		return list;
//	}
	
	/**
	 * 列表分页方法
	 * @param list 数据源
	 * @param pageSize 每页大小
	 * @param pageCallback 分页回调，返回当前页的数据及分页信息（pageList, pageNo, pageSize）
	 * @author ThinkGem
	 */
	public static <T> void pageList(List<T> list, int pageSize, MethodCallback pageCallback){
		if (list != null && list.size() > 0){

			int count = list.size(), pageNo = 1;
			int totalPage = (count + pageSize - 1) / pageSize;
			
			while(true){

				// 执行回调，分页后的数据
				List<T> pageList = getPageList(list, pageNo, pageSize, totalPage);
				if (pageList.size() > 0){
					pageCallback.execute(pageList, pageNo, pageSize, totalPage);
				}
				
				// 如果为最后一页，则跳出循环
				if (pageNo >= totalPage){
					break;
				}
				
				// 页数加一继续下一页
				pageNo++;
			}
		}
	}
	
	/**
	 * 列表分页方法
	 * @param list 源数据
	 * @param pageNo 当前页码
	 * @param pageSize 每页显示条数
	 * @param totalPage 总页码数
	 * @author ThinkGem
	 */
	private static <T> List<T> getPageList(List<T> list, int pageNo, int pageSize, int totalPage) {
		int fromIndex = 0; // 从哪里开始截取
		int toIndex = 0; // 截取几个
		if (list == null || list.size() == 0){
			return new ArrayList<T>();
		}
		// 当前页小于或等于总页数时执行
		if (pageNo <= totalPage && pageNo != 0) {
			fromIndex = (pageNo - 1) * pageSize;
			if (pageNo == totalPage) {
				toIndex = list.size();
			} else {
				toIndex = pageNo * pageSize;
			}
		}
		return list.subList(fromIndex, toIndex);
	}
	
	/**
	 * 本地列表排序
	 * @param list 需要排序的列表
	 * @param orderBy 排序的键值（如：id desc）
	 * @author ThinkGem
	 */
	public static <T> List<T> listOrderBy(List<T> list, String orderBy){
		if (list != null && StringUtils.isNotBlank(orderBy)){
			final String[] ss = orderBy.trim().split(" ");
			if (ss != null){
				final String t = ss.length==2 ? ss[1] : "asc";
				Collections.sort(list, new Comparator<T>() {
					@Override
					public int compare(T o1, T o2) {
						String s1 = StringUtils.EMPTY, s2 = StringUtils.EMPTY;
						if (o1 instanceof Map){
							s1 = ObjectUtils.toString(((Map)o1).get(ss[0]));
							s2 = ObjectUtils.toString(((Map)o2).get(ss[0]));
						}else{
							s1 = ObjectUtils.toString(ReflectUtils.invokeGetter(o1, ss[0]));
							s2 = ObjectUtils.toString(ReflectUtils.invokeGetter(o2, ss[0]));
						}
						if ("asc".equalsIgnoreCase(t)){
							return s1.compareTo(s2);
						}else{
							return s2.compareTo(s1);
						}
					}
				});
			}
		}
		return list;
	}
}
