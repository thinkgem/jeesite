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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.cas.client.util.CommonUtils;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;

/**
 * Abstract validator implementation for tickets that must be validated against a server.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.1
 */
public abstract class AbstractUrlBasedTicketValidator implements TicketValidator {

    /**
     * Commons Logging instance.
     */
    protected final Log log = LogFactory.getLog(getClass());
   
    /**
     * Hostname verifier used when making an SSL request to the CAS server.
     */
    protected HostnameVerifier hostnameVerifier;

    /**
     * Prefix for the CAS server.   Should be everything up to the url endpoint, including the /.
     *
     * i.e. https://cas.rutgers.edu/
     */
    private final String casServerUrlPrefix;

    /**
     * Whether the request include a renew or not.
     */
    private boolean renew;

    /**
     * A map containing custom parameters to pass to the validation url.
     */
    private Map<String,String> customParameters;

    private String encoding;

    /**
     * Constructs a new TicketValidator with the casServerUrlPrefix.
     *
     * @param casServerUrlPrefix the location of the CAS server.
     */
    protected AbstractUrlBasedTicketValidator(final String casServerUrlPrefix) {
        this.casServerUrlPrefix = casServerUrlPrefix;
        CommonUtils.assertNotNull(this.casServerUrlPrefix, "casServerUrlPrefix cannot be null.");
    }

    /**
     * Template method for ticket validators that need to provide additional parameters to the validation url.
     *
     * @param urlParameters the map containing the parameters.
     */
    protected void populateUrlAttributeMap(final Map<String,String> urlParameters) {
        // nothing to do
    }

    /**
     * The endpoint of the validation URL.  Should be relative (i.e. not start with a "/").  I.e. validate or serviceValidate.
     * @return the endpoint of the validation URL.
     */
    protected abstract String getUrlSuffix();

    /**
     * Disable XML Schema validation.  Note, setting this to true may not be reversable. Defaults to false. Setting it to false
     * after setting it to true may not have any affect.
     *
     * @param disabled whether to disable or not.
     */
    protected abstract void setDisableXmlSchemaValidation(boolean disabled);

    /**
     * Constructs the URL to send the validation request to.
     *
     * @param ticket the ticket to be validated.
     * @param serviceUrl the service identifier.
     * @return the fully constructed URL.
     */
    protected final String constructValidationUrl(final String ticket, final String serviceUrl) {
        final Map<String,String> urlParameters = new HashMap<String,String>();

        log.debug("Placing URL parameters in map.");
        urlParameters.put("ticket", ticket);
        urlParameters.put("service", serviceUrl);

        if (this.renew) {
            urlParameters.put("renew", "true");
        }

        log.debug("Calling template URL attribute map.");
        populateUrlAttributeMap(urlParameters);

        log.debug("Loading custom parameters from configuration.");
        if (this.customParameters != null) {
            urlParameters.putAll(this.customParameters);
        }

        final String suffix = getUrlSuffix();
        final StringBuilder buffer = new StringBuilder(urlParameters.size()*10 + this.casServerUrlPrefix.length() + suffix.length() +1);

        int i = 0;

        buffer.append(this.casServerUrlPrefix);
        if (!this.casServerUrlPrefix.endsWith("/")) {
            buffer.append("/");
        }
        buffer.append(suffix);

        for (Map.Entry<String,String> entry : urlParameters.entrySet()) {
            final String key = entry.getKey();
            final String value = entry.getValue();

            if (value != null) {
                buffer.append(i++ == 0 ? "?" : "&");
                buffer.append(key);
                buffer.append("=");
                final String encodedValue = encodeUrl(value);
                buffer.append(encodedValue);
            }
        }

        return buffer.toString();

    }

    /**
     * Encodes a URL using the URLEncoder format.
     *
     * @param url the url to encode.
     * @return the encoded url, or the original url if "UTF-8" character encoding could not be found.                       
     */
    protected final String encodeUrl(final String url) {
    	if (url == null) {
    		return null;
    	}
    	
        try {
            return URLEncoder.encode(url, "UTF-8");
        } catch (final UnsupportedEncodingException e) {
            return url;
        }
    }

    /**
     * Parses the response from the server into a CAS Assertion.
     *
     * @param response the response from the server, in any format.
     * @return the CAS assertion if one could be parsed from the response.
     * @throws TicketValidationException if an Assertion could not be created.
     *
     */
    protected abstract Assertion parseResponseFromServer(final String response) throws TicketValidationException;

    /**
     * Contacts the CAS Server to retrieve the response for the ticket validation.
     *
     * @param validationUrl the url to send the validation request to.
     * @param ticket the ticket to validate.
     * @return the response from the CAS server.
     */

    protected abstract String retrieveResponseFromServer(URL validationUrl, String ticket);

    public Assertion validate(final String ticket, final String service) throws TicketValidationException {


        final String validationUrl = constructValidationUrl(ticket, service);
        if (log.isDebugEnabled()) {
            log.debug("Constructing validation url: " + validationUrl);
        }

        try {
        	log.debug("Retrieving response from server.");
            final String serverResponse = retrieveResponseFromServer(new URL(validationUrl), ticket);

            if (serverResponse == null) {
                throw new TicketValidationException("The CAS server returned no response.");
            }
            
            if (log.isDebugEnabled()) {
            	log.debug("Server response: " + serverResponse);
            }

            return parseResponseFromServer(serverResponse);
        } catch (final MalformedURLException e) {
            throw new TicketValidationException(e);
        }
    }

    public final void setRenew(final boolean renew) {
        this.renew = renew;
    }

    public final void setCustomParameters(final Map<String,String> customParameters) {
        this.customParameters = customParameters;
    }
    
    public final void setHostnameVerifier(final HostnameVerifier verifier) {
        this.hostnameVerifier = verifier;
    }

    public final void setEncoding(final String encoding) {
        this.encoding = encoding;
    }

    protected final String getEncoding() {
        return this.encoding;
    }
}
