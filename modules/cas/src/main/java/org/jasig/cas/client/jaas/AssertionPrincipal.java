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

import java.io.Serializable;

import org.jasig.cas.client.authentication.SimplePrincipal;
import org.jasig.cas.client.validation.Assertion;

/**
 * Principal implementation that contains the CAS ticket validation assertion.
 *
 * @author Marvin S. Addison
 * @version $Revision$
 * @since 3.1.11
 *
 */
public class AssertionPrincipal extends SimplePrincipal implements Serializable {
    
    /** AssertionPrincipal.java */
    private static final long serialVersionUID = 2288520214366461693L;

    /** CAS assertion describing authenticated state */
    private Assertion assertion;

    /**
     * Creates a new principal containing the CAS assertion.
     *
     * @param name Principal name.
     * @param assertion CAS assertion.
     */
    public AssertionPrincipal(final String name, final Assertion assertion) {
        super(name);
        this.assertion = assertion;
    }

    /**
     * @return CAS ticket validation assertion.
     */
    public Assertion getAssertion() {
        return this.assertion;
    }
}
