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

import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.authentication.AttributePrincipalImpl;
import org.jasig.cas.client.util.CommonUtils;
import org.opensaml.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;

import javax.net.ssl.HttpsURLConnection;

/**
 * TicketValidator that can understand validating a SAML artifact.  This includes the SOAP request/response.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.1
 */
public final class Saml11TicketValidator extends AbstractUrlBasedTicketValidator {

    /** Time tolerance to allow for time drifting. */
    private long tolerance = 1000L;

    public Saml11TicketValidator(final String casServerUrlPrefix) {
        super(casServerUrlPrefix);
    }

    protected String getUrlSuffix() {
        return "samlValidate";
    }

    protected void populateUrlAttributeMap(final Map<String, String> urlParameters) {
        final String service = urlParameters.get("service");
        urlParameters.remove("service");
        urlParameters.remove("ticket");
        urlParameters.put("TARGET", service);
    }

    @Override
    protected void setDisableXmlSchemaValidation(final boolean disabled) {
        if (disabled) {
            // according to our reading of the SAML 1.1 code, this should disable the schema checking.  However, there may be a couple
            // of error messages that slip through on start up!
            XML.parserPool.setDefaultSchemas(null, null);
        }
    }

    protected Assertion parseResponseFromServer(final String response) throws TicketValidationException {
        try {
        	final String removeStartOfSoapBody = response.substring(response.indexOf("<SOAP-ENV:Body>") + 15);
        	final String removeEndOfSoapBody = removeStartOfSoapBody.substring(0, removeStartOfSoapBody.indexOf("</SOAP-ENV:Body>"));
            final SAMLResponse samlResponse = new SAMLResponse(new ByteArrayInputStream(CommonUtils.isNotBlank(getEncoding()) ? removeEndOfSoapBody.getBytes(Charset.forName(getEncoding())) : removeEndOfSoapBody.getBytes()));

            if (!samlResponse.getAssertions().hasNext()) {
                throw new TicketValidationException("No assertions found.");
            }

            for (final Iterator<?> iter = samlResponse.getAssertions(); iter.hasNext();) {
                final SAMLAssertion assertion = (SAMLAssertion) iter.next();

                if (!isValidAssertion(assertion)) {
                    continue;
                }

                final SAMLAuthenticationStatement authenticationStatement = getSAMLAuthenticationStatement(assertion);

                if (authenticationStatement == null) {
                    throw new TicketValidationException("No AuthentiationStatement found in SAML Assertion.");
                }
                final SAMLSubject subject = authenticationStatement.getSubject();

                if (subject == null) {
                    throw new TicketValidationException("No Subject found in SAML Assertion.");
                }

                final SAMLAttribute[] attributes = getAttributesFor(assertion, subject);
                final Map<String,Object> personAttributes = new HashMap<String,Object>();
                for (final SAMLAttribute samlAttribute : attributes) {
                    final List<?> values = getValuesFrom(samlAttribute);

                    personAttributes.put(samlAttribute.getName(), values.size() == 1 ? values.get(0) : values);
                }

                final AttributePrincipal principal = new AttributePrincipalImpl(subject.getNameIdentifier().getName(), personAttributes);

                final Map<String,Object> authenticationAttributes = new HashMap<String,Object>();
                authenticationAttributes.put("samlAuthenticationStatement::authMethod", authenticationStatement.getAuthMethod());

                return new AssertionImpl(principal, authenticationAttributes);
            }
       } catch (final SAMLException e) {
            throw new TicketValidationException(e);
        }

        throw new TicketValidationException("No Assertion found within valid time range.  Either there's a replay of the ticket or there's clock drift. Check tolerance range, or server/client synchronization.");
    }

    private boolean isValidAssertion(final SAMLAssertion assertion) {
        final Date notBefore = assertion.getNotBefore();
        final Date notOnOrAfter = assertion.getNotOnOrAfter();

        if (assertion.getNotBefore() == null || assertion.getNotOnOrAfter() == null) {
            log.debug("Assertion has no bounding dates. Will not process.");
            return false;
        }

        final long currentTime = getCurrentTimeInUtc().getTime();

        if (currentTime + tolerance < notBefore.getTime()) {
            log.debug("skipping assertion that's not yet valid...");
            return false;
        }

        if (notOnOrAfter.getTime() <= currentTime - tolerance) {
            log.debug("skipping expired assertion...");
            return false;
        }

        return true;
    }

    private SAMLAuthenticationStatement getSAMLAuthenticationStatement(final SAMLAssertion assertion) {
        for (final Iterator<?> iter = assertion.getStatements(); iter.hasNext();) {
            final SAMLStatement statement = (SAMLStatement) iter.next();

            if (statement instanceof SAMLAuthenticationStatement) {
                return (SAMLAuthenticationStatement) statement;
            }
        }

        return null;
    }

    private SAMLAttribute[] getAttributesFor(final SAMLAssertion assertion, final SAMLSubject subject) {
        final List<SAMLAttribute> attributes = new ArrayList<SAMLAttribute>();
        for (final Iterator<?> iter = assertion.getStatements(); iter.hasNext();) {
            final SAMLStatement statement = (SAMLStatement) iter.next();

            if (statement instanceof SAMLAttributeStatement) {
                final SAMLAttributeStatement attributeStatement = (SAMLAttributeStatement) statement;
                // used because SAMLSubject does not implement equals
                if (subject.getNameIdentifier().getName().equals(attributeStatement.getSubject().getNameIdentifier().getName())) {
                    for (final Iterator<?> iter2 = attributeStatement.getAttributes(); iter2.hasNext();)
                    attributes.add((SAMLAttribute) iter2.next());
                }
            }
        }

        return attributes.toArray(new SAMLAttribute[attributes.size()]);
    }

    private List<?> getValuesFrom(final SAMLAttribute attribute) {
        final List<Object> list = new ArrayList<Object>();
        for (final Iterator<?> iter = attribute.getValues(); iter.hasNext();) {
            list.add(iter.next());
        }
        return list;
    }

    private Date getCurrentTimeInUtc() {
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("UTC"));
        return c.getTime();
    }

    protected String retrieveResponseFromServer(final URL validationUrl, final String ticket) {

        String MESSAGE_TO_SEND;

        try {
            MESSAGE_TO_SEND = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"><SOAP-ENV:Header/><SOAP-ENV:Body><samlp:Request xmlns:samlp=\"urn:oasis:names:tc:SAML:1.0:protocol\"  MajorVersion=\"1\" MinorVersion=\"1\" RequestID=\"" + SAMLIdentifierFactory.getInstance().getIdentifier() + "\" IssueInstant=\"" + CommonUtils.formatForUtcTime(new Date()) + "\">"
                + "<samlp:AssertionArtifact>" + ticket
                + "</samlp:AssertionArtifact></samlp:Request></SOAP-ENV:Body></SOAP-ENV:Envelope>";
        } catch (final SAMLException e) {
            throw new RuntimeException(e);
        }

        HttpURLConnection conn = null;

        try {
            conn = (HttpURLConnection) validationUrl.openConnection();
            if (this.hostnameVerifier != null && conn instanceof HttpsURLConnection) {
                ((HttpsURLConnection)conn).setHostnameVerifier(this.hostnameVerifier);
            }
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "text/xml"); 
            conn.setRequestProperty("Content-Length", Integer.toString(MESSAGE_TO_SEND.length()));
            conn.setRequestProperty("SOAPAction", "http://www.oasis-open.org/committees/security");
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            final DataOutputStream out = new DataOutputStream(conn.getOutputStream());
            out.writeBytes(MESSAGE_TO_SEND);
            out.flush();
            out.close();

            final BufferedReader in = new BufferedReader(CommonUtils.isNotBlank(getEncoding()) ? new InputStreamReader(conn.getInputStream(), Charset.forName(getEncoding())) : new InputStreamReader(conn.getInputStream()));
            final StringBuilder buffer = new StringBuilder(256);

            String line;

            while ((line = in.readLine()) != null) {
                buffer.append(line);
            }
            return buffer.toString();
        } catch (final IOException e) {
            throw new RuntimeException(e);       
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    public void setTolerance(final long tolerance) {
        this.tolerance = tolerance;
    }
}
