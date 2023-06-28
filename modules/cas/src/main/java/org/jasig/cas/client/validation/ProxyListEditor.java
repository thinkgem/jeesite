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

import org.jasig.cas.client.util.CommonUtils;

import java.beans.PropertyEditorSupport;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Convert a String-formatted list of acceptable proxies to an array.
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.1
 * 
 */
public final class ProxyListEditor extends PropertyEditorSupport {

	public void setAsText(final String text) throws IllegalArgumentException {
		final BufferedReader reader = new BufferedReader(new StringReader(text));
		final List<String[]> proxyChains = new ArrayList<String[]>();

		try {
			String line;
			while ((line = reader.readLine()) != null) {
				if (CommonUtils.isNotBlank(line)) {
					proxyChains.add(line.trim().split(" "));
				}
			}
		} catch (final IOException e) {
			// ignore this
		} finally {
			try {
				reader.close();
			} catch (final IOException e) {
				// nothing to do
			}
		}

		setValue(new ProxyList(proxyChains));
	}
}
