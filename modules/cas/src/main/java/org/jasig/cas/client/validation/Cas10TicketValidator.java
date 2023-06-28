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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

/**
 * Implementation of a Ticket Validator that can validate tickets conforming to the CAS 1.0 specification.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.1
 */
public final class Cas10TicketValidator extends AbstractCasProtocolUrlBasedTicketValidator {

    public Cas10TicketValidator(final String casServerUrlPrefix) {
        super(casServerUrlPrefix);
    }

    protected String getUrlSuffix() {
        return "validate";
    }

    protected Assertion parseResponseFromServer(final String response) throws TicketValidationException {
        if (!response.startsWith("yes")) {
            throw new TicketValidationException("CAS Server could not validate ticket.");
        }

        try {
            final BufferedReader reader = new BufferedReader(new StringReader(response));
            reader.readLine();
            final String name = reader.readLine();

            return new AssertionImpl(name);
        } catch (final IOException e) {
            throw new TicketValidationException("Unable to parse response.", e);
        }
    }
}
