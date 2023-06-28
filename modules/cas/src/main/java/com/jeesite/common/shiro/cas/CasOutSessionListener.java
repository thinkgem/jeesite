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

package com.jeesite.common.shiro.cas;

import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

import org.jasig.cas.client.session.SessionMappingStorage;

import com.jeesite.common.utils.SpringUtils;

/**
 * Listener to detect when an HTTP session is destroyed and remove it from the map of
 * managed sessions.  Also allows for the programmatic removal of sessions.
 * <p>
 * Enables the CAS Single Sign out feature.
 *
 * Scott Battaglia
 * @version $Revision$ Date$ 2015-12-6
 * @since 3.1
 */
public final class CasOutSessionListener implements HttpSessionListener {

	private CasOutHandler casOutHandler;
	
    @Override
	public void sessionCreated(final HttpSessionEvent event) {
        // nothing to do at the moment
    }

    @Override
	public void sessionDestroyed(final HttpSessionEvent event) {
        final HttpSession session = event.getSession();
        getSessionMappingStorage().removeBySessionById(session.getId());
    }

    public SessionMappingStorage getSessionMappingStorage() {
    	if (casOutHandler == null){
    		casOutHandler = SpringUtils.getBean(CasOutHandler.class);
    	}
    	return casOutHandler.getSessionMappingStorage();
    }
    
}
