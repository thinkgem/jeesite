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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.jasig.cas.client.session.HashMapBackedSessionMappingStorage;
import org.jasig.cas.client.session.SessionMappingStorage;
import org.jasig.cas.client.util.CommonUtils;
import org.jasig.cas.client.util.XmlUtils;

import com.jeesite.common.shiro.realm.LoginInfo;

/**
 * Performs CAS single sign-out operations in an API-agnostic fashion.
 *
 * @author Marvin S. Addison  2015-12-6 ThinkGem 增加手动指定ticket和logoutRequest参数
 * @version $Revision: 24094 $ $Date: 2011-06-20 21:39:49 -0400 (Mon, 20 Jun 2011) $
 * @since 3.1.12
 *
 */
public final class CasOutHandler {

    /** Logger instance */
    private final Log log = LogFactory.getLog(getClass());

    /** Mapping of token IDs and session IDs to HTTP sessions */
    private SessionMappingStorage sessionMappingStorage = new HashMapBackedSessionMappingStorage();
    
    /** The name of the artifact parameter.  This is used to capture the session identifier. */
    private String artifactParameterName = "ticket";

    /** Parameter name that stores logout request */
    private String logoutParameterName = "logoutRequest";


    public void setSessionMappingStorage(final SessionMappingStorage storage) {
        this.sessionMappingStorage = storage;
    }

    public SessionMappingStorage getSessionMappingStorage() {
        return this.sessionMappingStorage;
    }

    /**
     * @param name Name of the authentication token parameter.
     */
    public void setArtifactParameterName(final String name) {
        this.artifactParameterName = name;
    }

    /**
     * @param name Name of parameter containing CAS logout request message.
     */
    public void setLogoutParameterName(final String name) {
        this.logoutParameterName = name;
    }

    /**
     * Initializes the component for use.
     */
    public void init() {
        CommonUtils.assertNotNull(this.artifactParameterName, "artifactParameterName cannot be null.");
        CommonUtils.assertNotNull(this.logoutParameterName, "logoutParameterName cannot be null.");
        CommonUtils.assertNotNull(this.sessionMappingStorage, "sessionMappingStorage cannote be null."); 
    }
    
    /**
     * Determines whether the given request contains an authentication token.
     *
     * @param request HTTP reqest.
     *
     * @return True if request contains authentication token, false otherwise.
     */
    public boolean isTokenRequest(final HttpServletRequest request) {
        return CommonUtils.isNotBlank(CommonUtils.safeGetParameter(request, this.artifactParameterName));
    }

    /**
     * Determines whether the given request is a CAS logout request.
     *
     * @param request HTTP request.
     *
     * @return True if request is logout request, false otherwise.
     */
    public boolean isLogoutRequest(final HttpServletRequest request) {
        return "POST".equals(request.getMethod()) && !isMultipartRequest(request) &&
            CommonUtils.isNotBlank(CommonUtils.safeGetParameter(request, this.logoutParameterName));
    }

    /**
     * Associates a token request with the current HTTP session by recording the mapping
     * in the the configured {@link SessionMappingStorage} container.
     * 
     * @param request HTTP request containing an authentication token.
     */
    public void recordSession(final HttpServletRequest request) {
    	recordSession(request, null);
    }
    
    /**
     * Associates a token request with the current HTTP session by recording the mapping
     * in the the configured {@link SessionMappingStorage} container.
     * 
     * @param request HTTP request containing an authentication token.
     */
    public void recordSession(final HttpServletRequest request, String ticket) {

//      final HttpSession session = request.getSession(true);
    	final HttpSession session = request.getSession();
      
        final String token;
        if (ticket != null){
            token = ticket;
        }else{
            token = CommonUtils.safeGetParameter(request, this.artifactParameterName);
        }
        
        if (log.isDebugEnabled()) {
            log.debug("Recording session for token " + token);
        }

        try {
            this.sessionMappingStorage.removeBySessionById(session.getId());
        } catch (final Exception e) {
            // ignore if the session is already marked as invalid.  Nothing we can do!
        }
        sessionMappingStorage.addSessionById(token, session);
    }
   
    /**
     * Destroys the current HTTP session for the given CAS logout request.
     *
     * @param request HTTP request containing a CAS logout message.
     */
     public LoginInfo destroySession(final HttpServletRequest request) {
		final String logoutMessage = CommonUtils.safeGetParameter(request, this.logoutParameterName);
		if (log.isTraceEnabled()) {
			log.trace("Logout request:\n" + logoutMessage);
		}
		LoginInfo loginInfo = null;
		final String token = XmlUtils.getTextForElement(logoutMessage, "SessionIndex");
		if (CommonUtils.isNotBlank(token)) {
			final HttpSession session = this.sessionMappingStorage.removeSessionByMappingId(token);

			if (session != null) {
				String sessionID = session.getId();
				if (log.isDebugEnabled()) {
					log.debug("Invalidating session [" + sessionID + "] for token [" + token + "]");
				}
				try {
					PrincipalCollection pc = (PrincipalCollection) session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
					loginInfo = (pc != null ? (LoginInfo) pc.getPrimaryPrincipal() : null);
					session.invalidate();
				} catch (final IllegalStateException e) {
					log.debug("Error invalidating session.", e);
				}
			}
		}
		return loginInfo;
     }
     
//     /**
//      * Destroys the current HTTP session for the given CAS logout request.
//      *
//      * @param request HTTP request containing a CAS logout message.
//      * v4.0.5之后，替代方法：onLogoutSuccess(loginInfo, request)
//      */
//     @Deprecated
//     public User destroySession(final HttpServletRequest request, String logoutRequest) {
//		this.logoutParameterName = logoutRequest;
//		LoginInfo loginInfo = destroySession(request);
//		if (loginInfo != null) {
//			return UserUtils.get(loginInfo.getId());
//		}
//		return null;
//    }

    private boolean isMultipartRequest(final HttpServletRequest request) {
        return request.getContentType() != null && request.getContentType().toLowerCase().startsWith("multipart");
    }
    
}
