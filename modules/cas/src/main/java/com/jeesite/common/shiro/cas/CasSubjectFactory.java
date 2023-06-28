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

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;

/**
 * {@link org.apache.shiro.mgt.SubjectFactory Subject} implementation to be used in CAS-enabled applications.
 *
 * @since 1.2
 * @see <a href="https://github.com/bujiio/buji-pac4j">buji-pac4j</a>
 * @ deprecated replaced with Shiro integration in <a href="https://github.com/bujiio/buji-pac4j">buji-pac4j</a>.
 */
public class CasSubjectFactory extends DefaultWebSubjectFactory {

    @Override
    public Subject createSubject(SubjectContext context) {

        //the authenticated flag is only set by the SecurityManager after a successful authentication attempt.
        boolean authenticated = context.isAuthenticated();

        //although the SecurityManager 'sees' the submission as a successful authentication, in reality, the
        //login might have been just a CAS rememberMe login.  If so, set the authenticated flag appropriately:
        if (authenticated) {

            AuthenticationToken token = context.getAuthenticationToken();

            if (token != null && token instanceof CasToken) {
                CasToken casToken = (CasToken) token;
                // set the authenticated flag of the context to true only if the CAS subject is not in a remember me mode
                if (casToken.isRememberMe()) {
                    context.setAuthenticated(false);
                }
            }
        }

        return super.createSubject(context);
    }
}
