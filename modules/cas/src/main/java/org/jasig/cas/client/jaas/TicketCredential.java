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

import java.security.Principal;

/**
 * Strongly-typed wrapper for a ticket credential.
 *
 * @author Marvin S. Addison
 * @version $Revision$ $Date$
 * @since 3.1.12
 *
 */
public final class TicketCredential implements Principal {

    /** Hash code seed value */
    private static final int HASHCODE_SEED = 17;
    
    /** Ticket ID string */
    private String ticket;

    /**
     * Creates a new instance that wraps the given ticket.
     * @param ticket Ticket identifier string.
     */
    public TicketCredential(final String ticket) {
        this.ticket = ticket;
    }

    public String getName() {
        return this.ticket;
    }

    public String toString() {
        return this.ticket;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final TicketCredential that = (TicketCredential) o;

        if (ticket != null ? !ticket.equals(that.ticket) : that.ticket != null) return false;

        return true;
    }

    public int hashCode() {
        int hash = HASHCODE_SEED;
        hash = hash * 31 + (ticket == null ? 0 : ticket.hashCode());
        return hash;
    }
}
