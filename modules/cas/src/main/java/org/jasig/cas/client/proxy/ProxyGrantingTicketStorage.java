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

/**
 * Interface for the storage and retrieval of ProxyGrantingTicketIds by mapping
 * them to a specific ProxyGrantingTicketIou.
 *
 * @author Scott Battaglia
 * @version $Revision: 11729 $ $Date: 2007-09-26 14:22:30 -0400 (Tue, 26 Sep 2007) $
 * @since 3.0
 */
public interface ProxyGrantingTicketStorage {

    /**
     * Method to save the ProxyGrantingTicket to the backing storage facility.
     *
     * @param proxyGrantingTicketIou used as the key
     * @param proxyGrantingTicket    used as the value
     */
    public void save(String proxyGrantingTicketIou, String proxyGrantingTicket);

    /**
     * Method to retrieve a ProxyGrantingTicket based on the
     * ProxyGrantingTicketIou. Note that implementations are not guaranteed to
     * return the same result if retrieve is called twice with the same
     * proxyGrantingTicketIou.
     *
     * @param proxyGrantingTicketIou used as the key
     * @return the ProxyGrantingTicket Id or null if it can't be found
     */
    public String retrieve(String proxyGrantingTicketIou);
    
    /**
     * Called on a regular basis by an external timer,
     * giving implementations a chance to remove stale data.
     */
    public void cleanUp();
}
