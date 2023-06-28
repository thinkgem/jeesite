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

package org.jasig.cas.client.jaas;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

/**
 * Callback handler that provides the CAS service and ticket to a
 * {@link NameCallback} and {@link PasswordCallback} respectively,
 * which meets the requirements of the {@link CasLoginModule} JAAS module.
 *
 * @author Marvin S. Addison
 * @version $Revision$
 * @since 3.1.11
 *
 */
public class ServiceAndTicketCallbackHandler implements CallbackHandler {

    /** CAS service URL */
    private final String service;
   
    /** CAS service ticket */
    private final String ticket;
   
    /**
     * Creates a new instance with the given service and ticket.
     *
     * @param service CAS service URL.
     * @param ticket CAS service ticket.
     */
    public ServiceAndTicketCallbackHandler(final String service, final String ticket) {
        this.service = service;
        this.ticket = ticket;
    }

    public void handle(final Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        for (final Callback callback : callbacks) {
            if (callback instanceof NameCallback) {
                ((NameCallback) callback).setName(this.service);
            } else if (callback instanceof PasswordCallback) {
                ((PasswordCallback) callback).setPassword(this.ticket.toCharArray());
            } else {
                throw new UnsupportedCallbackException(callback, "Callback not supported.");
            }
        }
    }

}
