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

package org.jasig.cas.client.authentication;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Implementations of this should only have a default constructor if
 * you plan on constructing them via the web.xml.
 * 
 * @author Scott Battaglia
 * @version $Revision$
 * @since 1.0
 *
 */
public interface GatewayResolver {

	/**
	 * Determines if the request has  been gatewayed already.  Should also do gateway clean up.
	 * 
	 * @param request the Http Servlet Request
	 * @param serviceUrl the service url
	 * @return true if yes, false otherwise.
	 */
	boolean hasGatewayedAlready(HttpServletRequest request, String serviceUrl);
	
	/**
	 * Storage the request for gatewaying and return the service url, which can be modified.
	 * 
	 * @param request the HttpServletRequest.
	 * @param serviceUrl the service url
	 * @return the potentially modified service url to redirect to
	 */
	String storeGatewayInformation(HttpServletRequest request, String serviceUrl);
}
