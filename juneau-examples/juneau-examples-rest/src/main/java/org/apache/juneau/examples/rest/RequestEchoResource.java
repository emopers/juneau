// ***************************************************************************************************************************
// * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements.  See the NOTICE file *
// * distributed with this work for additional information regarding copyright ownership.  The ASF licenses this file        *
// * to you under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance            *
// * with the License.  You may obtain a copy of the License at                                                              *
// *                                                                                                                         *
// *  http://www.apache.org/licenses/LICENSE-2.0                                                                             *
// *                                                                                                                         *
// * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an  *
// * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the        *
// * specific language governing permissions and limitations under the License.                                              *
// ***************************************************************************************************************************
package org.apache.juneau.examples.rest;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.juneau.jsonschema.annotation.ExternalDocs;
import org.apache.juneau.annotation.*;
import org.apache.juneau.html.annotation.*;
import org.apache.juneau.http.annotation.*;
import org.apache.juneau.rest.*;
import org.apache.juneau.rest.annotation.*;
import org.apache.juneau.rest.converters.*;
import org.apache.juneau.rest.widget.*;
import org.apache.juneau.transforms.*;

/**
 * Sample REST resource for echoing HttpServletRequests back to the browser.
 *
 * <ul class='seealso'>
 * 	<li class='extlink'>{@source}
 * </ul>
 */
@RestResource(
	path="/echo",
	title="Request echo service",
	description="Echos the current HttpServletRequest object back to the browser.",
	swagger=@ResourceSwagger(
		contact=@Contact(name="Juneau Developer",email="dev@juneau.apache.org"),
		license=@License(name="Apache 2.0",url="http://www.apache.org/licenses/LICENSE-2.0.html"),
		version="2.0",
		termsOfService="You are on your own.",
		externalDocs=@ExternalDocs(description="Apache Juneau",url="http://juneau.apache.org")
	)
)
@HtmlDocConfig(
	widgets={
		ContentTypeMenuItem.class,
		ThemeMenuItem.class
	},
	navlinks={
		"up: request:/..",
		"options: servlet:/?method=OPTIONS",
		"$W{ContentTypeMenuItem}",
		"$W{ThemeMenuItem}",
		"source: $C{Source/gitHub}/org/apache/juneau/examples/rest/$R{servletClassSimple}.java"
	},
	aside={
		"<div style='max-width:400px;min-width:200px' class='text'>",
		"	<p>Shows how even arbitrary POJOs such as <c>HttpServletRequest</c> can be serialized by the framework.</p>",
		"	<p>Also shows how to specify serializer properties, filters, and swaps at the servlet level to control how POJOs are serialized.</p>",
		"	<p>Also provides an example of how to use the Traversable and Queryable APIs.</p>",
		"</div>"
	},
	nowrap="false"
)
@BeanConfig(
	maxDepth="5",
	detectRecursions="true",
	beanFilters={
		// Interpret these as their parent classes, not subclasses
		HttpServletRequest.class, HttpSession.class, ServletContext.class,
	},
	pojoSwaps={
		// Add a special filter for Enumerations
		EnumerationSwap.class
	}
)
public class RequestEchoResource extends BasicRest {

	/**
	 * GET request handler.
	 *
	 * @param req The HTTP servlet request.
	 * @return The same request to serialize as the response.
	 */
	@RestMethod(name="*", path="/*", converters={Traversable.class,Queryable.class}, summary="Serializes the incoming HttpServletRequest object.")
	public HttpServletRequest doGet(RestRequest req) {
		// Just echo the request back as the response.
		return req;
	}
}
