/**
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.jasig.cas.client.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Filters that redirects to the supplied url based on an exception.  Exceptions and the urls are configured via
 * init filter name/param values.
 * <p>
 * If there is an exact match the filter uses that value.  If there's a non-exact match (i.e. inheritance), then the filter
 * uses the last value that matched.
 * <p>
 * If there is no match it will redirect to a default error page.  The default exception is configured via the "defaultErrorRedirectPage" property.
 *  
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.1.4
 *
 */
public final class ErrorRedirectFilter implements Filter {

	private final Log log = LogFactory.getLog(getClass());
	
	private final List<ErrorHolder> errors = new ArrayList<ErrorHolder>();
	
	private String defaultErrorRedirectPage;
	
	public void destroy() {
		// nothing to do here
	}

	public void doFilter(final ServletRequest request, final ServletResponse response,
			final FilterChain filterChain) throws IOException, ServletException {
		final HttpServletResponse httpResponse = (HttpServletResponse) response;
		try {
			filterChain.doFilter(request, response);
		} catch (final ServletException e) {
			final Throwable t = e.getCause();
			ErrorHolder currentMatch = null;
            for (final ErrorHolder errorHolder : this.errors) {
				if (errorHolder.exactMatch(t)) {
					currentMatch = errorHolder;
					break;
				} else if (errorHolder.inheritanceMatch(t)) {
					currentMatch = errorHolder;
				}
			}
			
			if (currentMatch != null) {
				httpResponse.sendRedirect(currentMatch.getUrl());
			} else {
				httpResponse.sendRedirect(defaultErrorRedirectPage);
			}
		}
	} 	

	public void init(final FilterConfig filterConfig) throws ServletException {
		this.defaultErrorRedirectPage = filterConfig.getInitParameter("defaultErrorRedirectPage");
		
		final Enumeration<?> enumeration = filterConfig.getInitParameterNames();
		while (enumeration.hasMoreElements()) {
			final String className = (String) enumeration.nextElement();
			try {
				if (!className.equals("defaultErrorRedirectPage")) {
					this.errors.add(new ErrorHolder(className, filterConfig.getInitParameter(className)));
				}
			} catch (final ClassNotFoundException e) {
				log.warn("Class [" + className + "] cannot be found in ClassLoader.  Ignoring.");
			}
		}
	}
	
	protected final class ErrorHolder {
		
		private Class<?> className;
		
		private String url;
		
		protected ErrorHolder(final String className, final String url) throws ClassNotFoundException {
			this.className = Class.forName(className);
			this.url = url;
		}
		
		public boolean exactMatch(final Throwable e) {
			return this.className.equals(e.getClass());
		}
		
		public boolean inheritanceMatch(final Throwable e) {
			return className.isAssignableFrom(e.getClass());
		}
		
		public String getUrl() {
			return this.url;
		}
	}
}
