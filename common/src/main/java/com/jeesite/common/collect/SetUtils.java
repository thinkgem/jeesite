/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.common.collect;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Set工具类
 * @author ThinkGem
 * @version 2015-01-15
 */
public class SetUtils extends org.apache.commons.collections.SetUtils {

	public static <E> HashSet<E> newHashSet() {
		return new HashSet<E>();
	}

	@SafeVarargs
	public static <E> HashSet<E> newHashSet(E... elements) {
		HashSet<E> set = newHashSet(elements.length);
		Collections.addAll(set, elements);
		return set;
	}

	public static <E> HashSet<E> newHashSet(int initialCapacity) {
		return new HashSet<E>(initialCapacity);
	}

	public static <E> HashSet<E> newHashSet(Iterable<? extends E> elements) {
		return (elements instanceof Collection) ? new HashSet<E>(cast(elements)) : newHashSet(elements.iterator());
	}

	public static <E> HashSet<E> newHashSet(Iterator<? extends E> elements) {
		HashSet<E> set = newHashSet();
		addAll(set, elements);
		return set;
	}

	public static <E> Set<E> newConcurrentHashSet() {
		return Collections.newSetFromMap(new ConcurrentHashMap<E, Boolean>());
	}

	public static <E> Set<E> newConcurrentHashSet(Iterable<? extends E> elements) {
		Set<E> set = newConcurrentHashSet();
		addAll(set, elements);
		return set;
	}

	public static <E> LinkedHashSet<E> newLinkedHashSet() {
		return new LinkedHashSet<E>();
	}

	public static <E> LinkedHashSet<E> newLinkedHashSet(int initialCapacity) {
		return new LinkedHashSet<E>(initialCapacity);
	}

	public static <E> LinkedHashSet<E> newLinkedHashSet(Iterable<? extends E> elements) {
		if (elements instanceof Collection) {
			return new LinkedHashSet<E>(cast(elements));
		}
		LinkedHashSet<E> set = newLinkedHashSet();
		addAll(set, elements);
		return set;
	}

	@SuppressWarnings("rawtypes")
	public static <E extends Comparable> TreeSet<E> newTreeSet() {
		return new TreeSet<E>();
	}

	@SuppressWarnings("rawtypes")
	public static <E extends Comparable> TreeSet<E> newTreeSet(Iterable<? extends E> elements) {
		TreeSet<E> set = newTreeSet();
		addAll(set, elements);
		return set;
	}

	public static <E> TreeSet<E> newTreeSet(Comparator<? super E> comparator) {
		return new TreeSet<E>(comparator);
	}

	public static <E> Set<E> newIdentityHashSet() {
		return Collections.newSetFromMap(MapUtils.<E, Boolean> newIdentityHashMap());
	}

	public static <E> CopyOnWriteArraySet<E> newCopyOnWriteArraySet() {
		return new CopyOnWriteArraySet<E>();
	}

	public static <E> CopyOnWriteArraySet<E> newCopyOnWriteArraySet(Iterable<? extends E> elements) {
		Collection<? extends E> elementsCollection = (elements instanceof Collection) ? cast(elements) : ListUtils.newArrayList(elements);
		return new CopyOnWriteArraySet<E>(elementsCollection);
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
}
