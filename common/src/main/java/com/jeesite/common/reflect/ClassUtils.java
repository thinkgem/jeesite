/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.common.reflect;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jeesite.common.codec.EncodeUtils;

/**
 * Class工具类，借鉴ibatis的io包的ResolverUtil类。
 * @author ThinkGem
 * @version 2016-4-28
 */
public class ClassUtils {
	
	/*
	 * An instance of Log to use for logging in this class.
	 */
	private static final Log log = LogFactory.getLog(ClassUtils.class);

	/**
	 * A simple interface that specifies how to test classes to determine if they
	 * are to be included in the results produced by the ResolverUtil.
	 */
	public static interface Test {
		/**
		 * Will be called repeatedly with candidate classes. Must return True if a class
		 * is to be included in the results, false otherwise.
		 */
		boolean matches(Class<?> type);
	}

	/**
	 * 查询实现类，继承与parentType，并不是当前类，不是抽象类。
	 * 
	 * A Test that checks to see if each class is assignable to the provided class. Note
	 * that this test will match the parent type itself if it is presented for matching.
	 */
	public static class IsA implements Test {
		private Class<?> parent;

		/** Constructs an IsA test using the supplied Class as the parent class/interface. */
		public IsA(Class<?> parentType) {
			this.parent = parentType;
		}

		/** Returns true if type is assignable to the parent type supplied in the constructor. */
		@Override
		public boolean matches(Class<?> type) {
			return type != null && parent.isAssignableFrom(type)	// 是继承与该类
					&& !type.getName().equals(parent.getName())		// 不包含当前类
					&& !Modifier.isAbstract(type.getModifiers());	// 不包含抽象类
		}

		@Override
		public String toString() {
			return "is assignable to " + parent.getSimpleName();
		}
	}

	/**
	 * 查询设置此注解的类。
	 * 
	 * A Test that checks to see if each class is annotated with a specific annotation. If it
	 * is, then the test returns true, otherwise false.
	 */
	public static class AnnotatedWith implements Test {
		private Class<? extends Annotation> annotation;

		/** Constructs an AnnotatedWith test for the specified annotation type. */
		public AnnotatedWith(Class<? extends Annotation> annotation) {
			this.annotation = annotation;
		}

		/** Returns true if the type is annotated with the class provided to the constructor. */
		@Override
		public boolean matches(Class<?> type) {
			return type != null && type.isAnnotationPresent(annotation);
		}

		@Override
		public String toString() {
			return "annotated with @" + annotation.getSimpleName();
		}
	}

	/** The set of matches being accumulated. */
	private Set<Class<?>> matches = new HashSet<Class<?>>();

	/**
	 * The ClassLoader to use when looking for classes. If null then the ClassLoader returned
	 * by Thread.currentThread().getContextClassLoader() will be used.
	 */
	private ClassLoader classloader;

	/**
	 * Provides access to the classes discovered so far. If no calls have been made to
	 * any of the {@code find()} methods, this set will be empty.
	 *
	 * @return the set of classes that have been discovered.
	 */
	public Set<Class<?>> getClasses() {
		return matches;
	}

	/**
	 * Returns the classloader that will be used for scanning for classes. If no explicit
	 * ClassLoader has been set by the calling, the context class loader will be used.
	 *
	 * @return the ClassLoader that will be used to scan for classes
	 */
	public ClassLoader getClassLoader() {
		return classloader == null ? Thread.currentThread().getContextClassLoader() : classloader;
	}

	/**
	 * Sets an explicit ClassLoader that should be used when scanning for classes. If none
	 * is set then the context classloader will be used.
	 *
	 * @param classloader a ClassLoader to use when scanning for classes
	 */
	public void setClassLoader(ClassLoader classloader) {
		this.classloader = classloader;
	}

	/**
	 * 查询实现类，继承与parentType，并不是当前类，不是抽象类。
	 * 
	 * Attempts to discover classes that are assignable to the type provided. In the case
	 * that an interface is provided this method will collect implementations. In the case
	 * of a non-interface class, subclasses will be collected.  Accumulated classes can be
	 * accessed by calling {@link #getClasses()}.
	 *
	 * @param parent the class of interface to find subclasses or implementations of
	 * @param packageNames one or more package names to scan (including subpackages) for classes
	 */
	public static <T> Set<Class<?>> findImplementations(Class<?> parent, String... packageNames) {
		if (packageNames == null) {
			return new HashSet<Class<?>>();
		}
		ClassUtils cu = new ClassUtils();
		Test test = new IsA(parent);
		for (String pkg : packageNames) {
			cu.find(test, pkg);
		}
		return cu.getClasses();
	}

	/**
	 * 查询设置此注解的类。
	 * 
	 * Attempts to discover classes that are annotated with the annotation. Accumulated
	 * classes can be accessed by calling {@link #getClasses()}.
	 * 
	 * @param annotation the annotation that should be present on matching classes
	 * @param packageNames one or more package names to scan (including subpackages) for classes
	 */
	public static Set<Class<?>> findAnnotated(Class<? extends Annotation> annotation, String... packageNames) {
		if (packageNames == null) {
			return new HashSet<Class<?>>();
		}
		ClassUtils cu = new ClassUtils();
		Test test = new AnnotatedWith(annotation);
		for (String pkg : packageNames) {
			cu.find(test, pkg);
		}
		return cu.getClasses();
	}

	/**
	 * Scans for classes starting at the package provided and descending into subpackages.
	 * Each class is offered up to the Test as it is discovered, and if the Test returns
	 * true the class is retained.  Accumulated classes can be fetched by calling
	 * {@link #getClasses()}.
	 *
	 * @param test an instance of {@link Test} that will be used to filter classes
	 * @param packageName the name of the package from which to start scanning for
	 *        classes, e.g. {@code net.sourceforge.stripes}
	 */
	public ClassUtils find(Test test, String packageName) {
		String path = getPackagePath(packageName);

		try {
			List<String> children = VFS.getInstance().list(path);
			for (String child : children) {
				if (child.endsWith(".class")) {
					addIfMatching(test, child);
				}
			}
		} catch (IOException ioe) {
			log.error("Could not read package: " + packageName, ioe);
		}

		return this;
	}

	/**
	 * Converts a Java package name to a path that can be looked up with a call to
	 * {@link ClassLoader#getResources(String)}.
	 * 
	 * @param packageName The Java package name to convert to a path
	 */
	protected String getPackagePath(String packageName) {
		return packageName == null ? null : packageName.replace('.', '/');
	}

	/**
	 * Add the class designated by the fully qualified class name provided to the set of
	 * resolved classes if and only if it is approved by the Test supplied.
	 *
	 * @param test the test used to determine if the class matches
	 * @param fqn the fully qualified name of a class
	 */
	protected void addIfMatching(Test test, String fqn) {
		try {
			String externalName = fqn.substring(0, fqn.indexOf('.')).replace('/', '.');
			ClassLoader loader = getClassLoader();
			log.debug("Checking to see if class " + externalName + " matches criteria [" + test + "]");

			Class<?> type = loader.loadClass(externalName);
			if (test.matches(type)) {
				matches.add((Class<?>) type);
			}
		} catch (Exception t) {
			log.warn("Could not examine class '" + fqn + "'" + " due to a " 
					+ t.getClass().getName() + " with message: " + t.getMessage());
		}
	}
}

/**
 * Provides a very simple API for accessing resources within an application server.
 * 
 * @author Ben Gunter
 */
abstract class VFS {
	private static final Log log = LogFactory.getLog(ClassUtils.class);

	/** The built-in implementations. */
	public static final Class<?>[] IMPLEMENTATIONS = { JBoss6VFS.class, DefaultVFS.class };

	/** The list to which implementations are added by {@link #addImplClass(Class)}. */
	public static final List<Class<? extends VFS>> USER_IMPLEMENTATIONS = new ArrayList<Class<? extends VFS>>();

	/** Singleton instance. */
	private static VFS instance;

	/**
	 * Get the singleton {@link VFS} instance. If no {@link VFS} implementation can be found for the
	 * current environment, then this method returns null.
	 */
	@SuppressWarnings("unchecked")
	public static VFS getInstance() {
		if (instance != null) {
			return instance;
		}

		// Try the user implementations first, then the built-ins
		List<Class<? extends VFS>> impls = new ArrayList<Class<? extends VFS>>();
		impls.addAll(USER_IMPLEMENTATIONS);
		impls.addAll(Arrays.asList((Class<? extends VFS>[]) IMPLEMENTATIONS));

		// Try each implementation class until a valid one is found
		VFS vfs = null;
		for (int i = 0; vfs == null || !vfs.isValid(); i++) {
			Class<? extends VFS> impl = impls.get(i);
			try {
				vfs = impl.getDeclaredConstructor().newInstance();
				if (vfs == null || !vfs.isValid()) {
					log.debug("VFS implementation " + impl.getName() + " is not valid in this environment.");
				}
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				log.error("Failed to instantiate " + impl, e);
				return null;
			}
		}

		log.debug("Using VFS adapter " + vfs.getClass().getName());
		return VFS.instance = vfs;
	}

	/**
	 * Adds the specified class to the list of {@link VFS} implementations. Classes added in this
	 * manner are tried in the order they are added and before any of the built-in implementations.
	 * 
	 * @param clazz The {@link VFS} implementation class to add.
	 */
	public static void addImplClass(Class<? extends VFS> clazz) {
		if (clazz != null) {
			USER_IMPLEMENTATIONS.add(clazz);
		}
	}

	/** Get a class by name. If the class is not found then return null. */
	protected static Class<?> getClass(String className) {
		try {
			return Thread.currentThread().getContextClassLoader().loadClass(className);
			//      return ReflectUtil.findClass(className);
		} catch (ClassNotFoundException e) {
			log.debug("Class not found: " + className);
			return null;
		}
	}

	/**
	 * Get a method by name and parameter types. If the method is not found then return null.
	 * 
	 * @param clazz The class to which the method belongs.
	 * @param methodName The name of the method.
	 * @param parameterTypes The types of the parameters accepted by the method.
	 */
	protected static Method getMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
		try {
			if (clazz == null) {
				return null;
			} else {
				return clazz.getMethod(methodName, parameterTypes);
			}
		} catch (SecurityException e) {
			log.error("Security exception looking for method " + clazz.getName() + "." + methodName + ".  Cause: " + e);
			return null;
		} catch (NoSuchMethodException e) {
			log.error("Method not found " + clazz.getName() + "." + methodName + "." + methodName + ".  Cause: " + e);
			return null;
		}
	}

	/**
	 * Invoke a method on an object and return whatever it returns.
	 * 
	 * @param method The method to invoke.
	 * @param object The instance or class (for static methods) on which to invoke the method.
	 * @param parameters The parameters to pass to the method.
	 * @return Whatever the method returns.
	 * @throws IOException If I/O errors occur
	 * @throws StripesRuntimeException If anything else goes wrong
	 */
	@SuppressWarnings("unchecked")
	protected static <T> T invoke(Method method, Object object, Object... parameters) throws IOException, RuntimeException {
		try {
			return (T) method.invoke(object, parameters);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			if (e.getTargetException() instanceof IOException) {
				throw (IOException) e.getTargetException();
			} else {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * Get a list of {@link URL}s from the context classloader for all the resources found at the
	 * specified path.
	 * 
	 * @param path The resource path.
	 * @return A list of {@link URL}s, as returned by {@link ClassLoader#getResources(String)}.
	 * @throws IOException If I/O errors occur
	 */
	protected static List<URL> getResources(String path) throws IOException {
		return Collections.list(Thread.currentThread().getContextClassLoader().getResources(path));
	}

	/** Return true if the {@link VFS} implementation is valid for the current environment. */
	public abstract boolean isValid();

	/**
	 * Recursively list the full resource path of all the resources that are children of the
	 * resource identified by a URL.
	 * 
	 * @param url The URL that identifies the resource to list.
	 * @param forPath The path to the resource that is identified by the URL. Generally, this is the
	 *            value passed to {@link #getResources(String)} to get the resource URL.
	 * @return A list containing the names of the child resources.
	 * @throws IOException If I/O errors occur
	 */
	protected abstract List<String> list(URL url, String forPath) throws IOException;

	/**
	 * Recursively list the full resource path of all the resources that are children of all the
	 * resources found at the specified path.
	 * 
	 * @param path The path of the resource(s) to list.
	 * @return A list containing the names of the child resources.
	 * @throws IOException If I/O errors occur
	 */
	public List<String> list(String path) throws IOException {
		List<String> names = new ArrayList<String>();
		for (URL url : getResources(path)) {
			names.addAll(list(url, path));
		}
		return names;
	}
}

/**
 * A default implementation of {@link VFS} that works for most application servers.
 * 
 * @author Ben Gunter
 */
class DefaultVFS extends VFS {
	private static final Log log = LogFactory.getLog(ClassUtils.class);

	/** The magic header that indicates a JAR (ZIP) file. */
	private static final byte[] JAR_MAGIC = { 'P', 'K', 3, 4 };

	@Override
	public boolean isValid() {
		return true;
	}

	@Override
	public List<String> list(URL url, String path) throws IOException {
		InputStream is = null;
		try {
			List<String> resources = new ArrayList<String>();

			// First, try to find the URL of a JAR file containing the requested resource. If a JAR
			// file is found, then we'll list child resources by reading the JAR.
			URL jarUrl = findJarForResource(url);
			if (jarUrl != null) {
				is = jarUrl.openStream();
				log.debug("Listing " + url);
				resources = listResources(new JarInputStream(is), path);
			} else {
				List<String> children = new ArrayList<String>();
				try {
					if (isJar(url)) {
						// Some versions of JBoss VFS might give a JAR stream even if the resource
						// referenced by the URL isn't actually a JAR
						is = url.openStream();
//						@SuppressWarnings("resource")
						JarInputStream jarInput = new JarInputStream(is);
						log.debug("Listing " + url);
						for (JarEntry entry; (entry = jarInput.getNextJarEntry()) != null;) {
							log.debug("Jar entry: " + entry.getName());
							children.add(entry.getName());
						}
					} else {
						/*
						 * Some servlet containers allow reading from directory resources like a
						 * text file, listing the child resources one per line. However, there is no
						 * way to differentiate between directory and file resources just by reading
						 * them. To work around that, as each line is read, try to look it up via
						 * the class loader as a child of the current resource. If any line fails
						 * then we assume the current resource is not a directory.
						 */
						is = url.openStream();
						try(BufferedReader reader = new BufferedReader(new InputStreamReader(is))){
							List<String> lines = new ArrayList<String>();
							for (String line; (line = reader.readLine()) != null;) {
								log.debug("Reader entry: " + line);
								lines.add(line);
								if (getResources(path + "/" + line).isEmpty()) {
									lines.clear();
									break;
								}
							}
	
							if (!lines.isEmpty()) {
								log.debug("Listing " + url);
								children.addAll(lines);
							}
						};
					}
				} catch (FileNotFoundException e) {
					/*
					 * For file URLs the openStream() call might fail, depending on the servlet
					 * container, because directories can't be opened for reading. If that happens,
					 * then list the directory directly instead.
					 */
					if ("file".equals(url.getProtocol())) {
						File file = new File(url.getFile());
						log.debug("Listing directory " + file.getAbsolutePath());
						if (file.isDirectory()) {
							log.debug("Listing " + url);
							children = Arrays.asList(file.list());
						}
					} else {
						// No idea where the exception came from so rethrow it
						throw e;
					}
				}

				// The URL prefix to use when recursively listing child resources
				String prefix = url.toExternalForm();
				if (!prefix.endsWith("/")) {
					prefix = prefix + "/";
				}

				// Iterate over immediate children, adding files and recursing into directories
				for (String child : children) {
					String resourcePath = path + "/" + child;
					resources.add(resourcePath);
					URL childUrl = new URL(prefix + child);
					resources.addAll(list(childUrl, resourcePath));
				}
			}

			return resources;
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (Exception e) {}
		}
	}

	/**
	 * List the names of the entries in the given {@link JarInputStream} that begin with the
	 * specified {@code path}. Entries will match with or without a leading slash.
	 * 
	 * @param jar The JAR input stream
	 * @param path The leading path to match
	 * @return The names of all the matching entries
	 * @throws IOException If I/O errors occur
	 */
	protected List<String> listResources(JarInputStream jar, String path) throws IOException {
		// Include the leading and trailing slash when matching names
		if (!path.startsWith("/")) {
			path = "/" + path;
		}
		if (!path.endsWith("/")) {
			path = path + "/";
		}

		// Iterate over the entries and collect those that begin with the requested path
		List<String> resources = new ArrayList<String>();
		for (JarEntry entry; (entry = jar.getNextJarEntry()) != null;) {
			if (!entry.isDirectory()) {
				// Add leading slash if it's missing
				String name = entry.getName();
				if (!name.startsWith("/")) {
					name = "/" + name;
				}

				// Check file name
				if (name.startsWith(path)) {
					log.debug("Found resource: " + name);
					resources.add(name.substring(1)); // Trim leading slash
				}
			}
		}
		return resources;
	}

	/**
	 * Attempts to deconstruct the given URL to find a JAR file containing the resource referenced
	 * by the URL. That is, assuming the URL references a JAR entry, this method will return a URL
	 * that references the JAR file containing the entry. If the JAR cannot be located, then this
	 * method returns null.
	 * 
	 * @param url The URL of the JAR entry.
	 * @return The URL of the JAR file, if one is found. Null if not.
	 * @throws MalformedURLException
	 */
	protected URL findJarForResource(URL url) throws MalformedURLException {
		log.debug("Find JAR URL: " + url);

		// If the file part of the URL is itself a URL, then that URL probably points to the JAR
		try {
			for (;;) {
				url = new URL(url.getFile());
				log.debug("Inner URL: " + url);
			}
		} catch (MalformedURLException e) {
			// This will happen at some point and serves as a break in the loop
		}

		// Look for the .jar extension and chop off everything after that
		StringBuilder jarUrl = new StringBuilder(url.toExternalForm());
		int index = jarUrl.lastIndexOf(".jar");
		if (index >= 0) {
			jarUrl.setLength(index + 4);
			log.debug("Extracted JAR URL: " + jarUrl);
		} else {
			log.debug("Not a JAR: " + jarUrl);
			return null;
		}

		// Try to open and test it
		try {
			URL testUrl = new URL(jarUrl.toString());
			if (isJar(testUrl)) {
				return testUrl;
			} else {
				// WebLogic fix: check if the URL's file exists in the filesystem.
				log.debug("Not a JAR: " + jarUrl);
				jarUrl.replace(0, jarUrl.length(), testUrl.getFile());
				File file = new File(jarUrl.toString());

				// File name might be URL-encoded
				if (!file.exists()) {
					try {
						file = new File(URLEncoder.encode(jarUrl.toString(), EncodeUtils.UTF_8));
					} catch (UnsupportedEncodingException e) {
						throw new RuntimeException("Unsupported encoding?  UTF-8?  That's unpossible.");
					}
				}

				if (file.exists()) {
					log.debug("Trying real file: " + file.getAbsolutePath());
					testUrl = file.toURI().toURL();
					if (isJar(testUrl)) {
						return testUrl;
					}
				}
			}
		} catch (MalformedURLException e) {
			log.warn("Invalid JAR URL: " + jarUrl);
		}

		log.debug("Not a JAR: " + jarUrl);
		return null;
	}

	/**
	 * Converts a Java package name to a path that can be looked up with a call to
	 * {@link ClassLoader#getResources(String)}.
	 * 
	 * @param packageName The Java package name to convert to a path
	 */
	protected String getPackagePath(String packageName) {
		return packageName == null ? null : packageName.replace('.', '/');
	}

	/**
	 * Returns true if the resource located at the given URL is a JAR file.
	 * 
	 * @param url The URL of the resource to test.
	 */
	protected boolean isJar(URL url) {
		return isJar(url, new byte[JAR_MAGIC.length]);
	}

	/**
	 * Returns true if the resource located at the given URL is a JAR file.
	 * 
	 * @param url The URL of the resource to test.
	 * @param buffer A buffer into which the first few bytes of the resource are read. The buffer
	 *            must be at least the size of {@link #JAR_MAGIC}. (The same buffer may be reused
	 *            for multiple calls as an optimization.)
	 */
	protected boolean isJar(URL url, byte[] buffer) {
		InputStream is = null;
		try {
			is = url.openStream();
			is.read(buffer, 0, JAR_MAGIC.length);
			if (Arrays.equals(buffer, JAR_MAGIC)) {
				log.debug("Found JAR: " + url);
				return true;
			}
		} catch (Exception e) {
			// Failure to read the stream means this is not a JAR
		} finally {
			try {
				if (is != null){
					is.close();
				}
			} catch (Exception e) {
				
			}
		}

		return false;
	}
}

/**
 * A {@link VFS} implementation that works with the VFS API provided by JBoss 6.
 * 
 * @author Ben Gunter
 */
class JBoss6VFS extends VFS {
	private static final Log log = LogFactory.getLog(ClassUtils.class);

	/** A class that mimics a tiny subset of the JBoss VirtualFile class. */
	static class VirtualFile {
		static Class<?> VirtualFile;
		static Method getPathNameRelativeTo, getChildrenRecursively;

		Object virtualFile;

		VirtualFile(Object virtualFile) {
			this.virtualFile = virtualFile;
		}

		String getPathNameRelativeTo(VirtualFile parent) {
			try {
				return invoke(getPathNameRelativeTo, virtualFile, parent.virtualFile);
			} catch (IOException e) {
				// This exception is not thrown by the called method
				log.error("This should not be possible. VirtualFile.getPathNameRelativeTo() threw IOException.");
				return null;
			}
		}

		List<VirtualFile> getChildren() throws IOException {
			List<?> objects = invoke(getChildrenRecursively, virtualFile);
			List<VirtualFile> children = new ArrayList<VirtualFile>(objects.size());
			for (Object object : objects) {
				children.add(new VirtualFile(object));
			}
			return children;
		}
	}

	/** A class that mimics a tiny subset of the JBoss VFS class. */
	static class VFS {
		static Class<?> VFS;
		static Method getChild;

		static VirtualFile getChild(URL url) throws IOException {
			Object o = invoke(getChild, VFS, url);
			return o == null ? null : new VirtualFile(o);
		}
	}

	/** Flag that indicates if this VFS is valid for the current environment. */
	private static Boolean valid;

	/** Find all the classes and methods that are required to access the JBoss 6 VFS. */
	protected static synchronized void initialize() {
		if (valid == null) {
			// Assume valid. It will get flipped later if something goes wrong.
			valid = true;

			// Look up and verify required classes
			VFS.VFS = checkNotNull(getClass("org.jboss.vfs.VFS"));
			VirtualFile.VirtualFile = checkNotNull(getClass("org.jboss.vfs.VirtualFile"));

			// Look up and verify required methods
			VFS.getChild = checkNotNull(getMethod(VFS.VFS, "getChild", URL.class));
			VirtualFile.getChildrenRecursively = checkNotNull(getMethod(VirtualFile.VirtualFile, "getChildrenRecursively"));
			VirtualFile.getPathNameRelativeTo = checkNotNull(getMethod(VirtualFile.VirtualFile, "getPathNameRelativeTo", VirtualFile.VirtualFile));

			// Verify that the API has not changed
			checkReturnType(VFS.getChild, VirtualFile.VirtualFile);
			checkReturnType(VirtualFile.getChildrenRecursively, List.class);
			checkReturnType(VirtualFile.getPathNameRelativeTo, String.class);
		}
	}

	/**
	 * Verifies that the provided object reference is null. If it is null, then this VFS is marked
	 * as invalid for the current environment.
	 * 
	 * @param object The object reference to check for null.
	 */
	protected static <T> T checkNotNull(T object) {
		if (object == null) {
			setInvalid();
		}
		return object;
	}

	/**
	 * Verifies that the return type of a method is what it is expected to be. If it is not, then
	 * this VFS is marked as invalid for the current environment.
	 * 
	 * @param method The method whose return type is to be checked.
	 * @param expected A type to which the method's return type must be assignable.
	 * @see Class#isAssignableFrom(Class)
	 */
	protected static void checkReturnType(Method method, Class<?> expected) {
		if (method != null && !expected.isAssignableFrom(method.getReturnType())) {
			log.error("Method " + method.getClass().getName() + "." + method.getName() + "(..) should return " + expected.getName() + " but returns " //
					+ method.getReturnType().getName() + " instead.");
			setInvalid();
		}
	}

	/** Mark this {@link VFS} as invalid for the current environment. */
	protected static void setInvalid() {
		if (JBoss6VFS.valid != null && JBoss6VFS.valid) {
			log.debug("JBoss 6 VFS API is not available in this environment.");
			JBoss6VFS.valid = false;
		}
	}

	static {
		initialize();
	}

	@Override
	public boolean isValid() {
		return valid;
	}

	@Override
	public List<String> list(URL url, String path) throws IOException {
		VirtualFile directory;
		directory = VFS.getChild(url);
		if (directory == null) {
			return Collections.emptyList();
		}

		if (!path.endsWith("/")) {
			path += "/";
		}

		List<VirtualFile> children = directory.getChildren();
		List<String> names = new ArrayList<String>(children.size());
		for (VirtualFile vf : children) {
			String relative = vf.getPathNameRelativeTo(directory);
			names.add(path + relative);
		}

		return names;
	}
}