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

package org.jasig.cas.client.proxy;

import java.util.TimerTask;

/**
 * A {@link TimerTask} implementation which performs the
 * actual 'cleaning' by calling {@link ProxyGrantingTicketStorage#cleanUp()}.
 * <p>
 * By default, the {@link org.jasig.cas.client.validation.Cas20ProxyReceivingTicketValidationFilter} configures
 * a task that cleans up the {@link org.jasig.cas.client.proxy.ProxyGrantingTicketStorage} associated with it.
 * 
 * @author Brad Cupit (brad [at] lsu {dot} edu)
 * @version $Revision$ $Date$
 * @since 3.1.6
 */
public final class CleanUpTimerTask extends TimerTask {

    private final ProxyGrantingTicketStorage proxyGrantingTicketStorage;

    public CleanUpTimerTask(final ProxyGrantingTicketStorage proxyGrantingTicketStorage) {
        this.proxyGrantingTicketStorage = proxyGrantingTicketStorage;
    }
    public void run() {
        this.proxyGrantingTicketStorage.cleanUp();
    }
}
