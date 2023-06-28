/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.jeesite.common.shiro.cas;

import org.apache.shiro.authc.RememberMeAuthenticationToken;

/**
 * This class represents a token for a CAS authentication (service ticket + user id + remember me).
 *
 * @since 1.2
 * @see <a href="https://github.com/bujiio/buji-pac4j">buji-pac4j</a>
 * @ deprecated replaced with Shiro integration in <a href="https://github.com/bujiio/buji-pac4j">buji-pac4j</a>.
 */
public class CasToken implements RememberMeAuthenticationToken {
    
    private static final long serialVersionUID = 8587329689973009598L;
    
    // the service ticket returned by the CAS server
    private String ticket = null;
    
    // the user identifier
    private String userId = null;
    
    // is the user in a remember me mode ?
    private boolean isRememberMe = false;
    
    public CasToken(String ticket) {
        this.ticket = ticket;
    }
    
    public Object getPrincipal() {
        return userId;
    }
    
    public Object getCredentials() {
        return ticket;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public boolean isRememberMe() {
        return isRememberMe;
    }
    
    public void setRememberMe(boolean isRememberMe) {
        this.isRememberMe = isRememberMe;
    }
}
