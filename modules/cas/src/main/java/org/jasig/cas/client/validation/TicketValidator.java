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

package org.jasig.cas.client.validation;

/**
 * Contract for a validator that will confirm the validity of a supplied ticket.
 * <p>
 * Validator makes no statement about how to validate the ticket or the format of the ticket (other than that it must be a String).
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.1
 */
public interface TicketValidator {

    /**
     * Attempts to validate a ticket for the provided service.
     *
     * @param ticket the ticket to attempt to validate.
     * @param service the service this ticket is valid for.
     * @return an assertion from the ticket.
     * @throws TicketValidationException if the ticket cannot be validated.
     *
     */
    Assertion validate(String ticket, String service) throws TicketValidationException;
}
