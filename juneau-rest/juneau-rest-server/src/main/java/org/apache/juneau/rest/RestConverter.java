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
package org.apache.juneau.rest;

import org.apache.juneau.rest.annotation.*;
import org.apache.juneau.rest.converters.*;
import org.apache.juneau.serializer.*;

/**
 * REST method response converter.
 *
 * <p>
 * Implements a filter mechanism for REST method calls that allows response objects to be converted to some other POJO
 * after invocation of the REST method.
 *
 * <p>
 * Converters are associated with REST methods through the following:
 * <ul class='javatree'>
 * 	<li class='ja'>{@link RestResource#converters()}
 * 	<li class='ja'>{@link RestMethod#converters()}
 * 	<li class='jf'>{@link RestContext#REST_converters}
 * 	<li class='jm'>{@link RestContextBuilder#converters(Class...)}
 * 	<li class='jm'>{@link RestContextBuilder#converters(RestConverter...)}
 * </ul>
 *
 * <h5 class='section'>Example:</h5>
 * <p class='bcode w800'>
 * 	<jk>public class</jk> RequestEchoResource <jk>extends</jk> RestServlet {
 *
 * 		<jc>// GET request handler</jc>
 * 		<ja>@RestMethod</ja>(name=<jsf>GET</jsf>, path=<js>"/*"</js>, converters={Queryable.<jk>class</jk>,Traversable.<jk>class</jk>})
 * 		<jk>public</jk> HttpServletRequest doGet(RestRequest req) {
 * 			res.setTitle(<js>"Contents of HttpServletRequest object"</js>);
 * 			<jk>return</jk> req;
 * 		}
 * 	}
 * </p>
 *
 * <p>
 * Converters can also be associated at the servlet level using the {@link RestResource#converters() @RestResource(converters)} annotation.
 * <br>Applying converters at the resource level is equivalent to applying converters to each resource method individually.
 *
 * <h5 class='topic'>How to implement</h5>
 *
 * Implementers should simply implement the {@link #convert(RestRequest, Object)} and return back a 'converted' object.
 * <br>It's up to the implementer to decide what this means.
 *
 * <p>
 * Subclasses must implement one of the following constructors:
 * <ul>
 * 	<li><jk>public</jk> T();  <jc>// No-arg constructor</jc>
 * 	<li><jk>public</jk> T(PropertyStore);  <jc>// Property store of the RestContext</jc>
 * </ul>
 *
 * <p>
 * Subclasses can also be defined as inner classes of the resource class.
 *
 * <h5 class='topic'>Predefined converters</h5>
 * <ul class='javatree'>
 * 	<li class='jc'>{@link Traversable} - Allows URL additional path info to address individual elements in a POJO tree.
 * 	<li class='jc'>{@link Queryable} - Allows query/view/sort functions to be performed on POJOs.
 * 	<li class='jc'>{@link Introspectable} - Allows Java public methods to be invoked on the returned POJOs.
 * </ul>
 *
 * <ul class='seealso'>
 * 	<li class='jf'>{@link RestContext#REST_converters} - Registering converters with REST resources.
 * 	<li class='link'>{@doc juneau-rest-server.Converters}
 * </ul>
 */
public interface RestConverter {

	/**
	 * Performs post-call conversion on the specified response object.
	 *
	 * @param req The servlet request.
	 * @param res The response object set by the REST method through the {@link RestResponse#setOutput(Object)} method.
	 * @return The converted object.
	 * @throws RestException Thrown if any errors occur during conversion.
	 * @throws SerializeException Generic serialization error occurred.
	 */
	public Object convert(RestRequest req, Object res) throws RestException, SerializeException;
}
