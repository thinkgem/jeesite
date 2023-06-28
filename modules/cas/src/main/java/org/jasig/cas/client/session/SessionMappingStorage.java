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

package org.jasig.cas.client.session;

import jakarta.servlet.http.HttpSession;

/**
 * Stores the mapping between sessions and keys to be retrieved later.
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.1
 *
 */
public interface SessionMappingStorage {
	
	/**
	 * Remove the HttpSession based on the mappingId.
	 * 
	 * @param mappingId the id the session is keyed under.
	 * @return the HttpSession if it exists.
	 */
	HttpSession removeSessionByMappingId(String mappingId);
	
	/**
	 * Remove a session by its Id.
	 * @param sessionId the id of the session.
	 */
	void removeBySessionById(String sessionId);
	
	/**
	 * Add a session by its mapping Id.
	 * @param mappingId the id to map the session to.
	 * @param session the HttpSession.
	 */
	void addSessionById(String mappingId, HttpSession session);

}
