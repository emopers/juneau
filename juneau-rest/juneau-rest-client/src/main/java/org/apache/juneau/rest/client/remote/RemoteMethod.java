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
package org.apache.juneau.rest.client.remote;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.io.*;
import java.lang.annotation.*;

import org.apache.juneau.http.annotation.*;

/**
 * Annotation applied to Java methods on REST proxy.
 *
 * <ul class='seealso'>
 * 	<li class='link'>{@doc juneau-rest-client.RestProxies}
 * </ul>
 */
@Documented
@Target(METHOD)
@Retention(RUNTIME)
@Inherited
public @interface RemoteMethod {

	/**
	 * REST service path.
	 *
	 * <p>
	 * The possible values are:
	 * <ul class='spaced-list'>
	 * 	<li>An absolute URL.
	 * 	<li>A relative URL interpreted as relative to the root URL defined on the <c>RestClient</c> and/or {@link RemoteResource#path()}.
	 * 	<li>No path.
	 * </ul>
	 *
	 * <p>
	 * If you do not specify a path, then the path is inferred from the Java method name.
	 *
	 * <h5 class='figure'>Example:</h5>
	 * <p class='bcode'>
	 * 	<jc>// POST /pet</jc>
	 * 	<ja>@RestMethod</ja>
	 * 	<jk>public void</jk> postPet(...) {...}
	 * </p>
	 */
	String path() default "";

	/**
	 * Defines the HTTP method to use for REST calls.
	 *
	 * <p>
	 * If not specified, then the method is inferred from the Java method name.
	 *
	 * <h5 class='figure'>Example:</h5>
	 * <p class='bcode'>
	 * 	<jc>// POST /pet</jc>
	 * 	<ja>@RestMethod</ja>
	 * 	<jk>public void</jk> postPet(...) {...}
	 * </p>
	 *
	 * <br>If the method cannot be inferred, then the default is <js>"GET"</js>.
	 */
	String method() default "";

	/**
	 * The value the remote method returns.
	 *
	 * <p>
	 * Possible values:
	 * <ul class='spaced-list'>
	 * 	<li>
	 * 		{@link RemoteReturn#BODY} (default) - The body of the HTTP response converted to a POJO.
	 * 		<br>The return type on the Java method can be any of the following:
	 * 		<ul class='spaced-list'>
	 * 			<li>
	 * 				<jk>void</jk> - Don't parse any response.  Note that the method will still throw an exception if an
	 * 				error HTTP status is returned.
	 * 			<li>
	 * 				Any parsable POJO - The body of the response will be converted to the POJO using the parser defined
	 * 				on the <c>RestClient</c>.
	 * 			<li>
	 * 				Any POJO annotated with the {@link Response @Response} annotation.
	 * 				This allows for response beans to be used which also allows for OpenAPI-based parsing and validation.
	 * 			<li>
	 * 				<c>HttpResponse</c> - Returns the raw <c>HttpResponse</c> returned by the inner
	 * 				<c>HttpClient</c>.
	 * 			<li>
	 * 				{@link Reader} - Returns access to the raw reader of the response.
	 * 			<li>
	 * 				{@link InputStream} - Returns access to the raw input stream of the response.
	 * 		</ul>
	 * 	<li>
	 * 		{@link RemoteReturn#STATUS} - The HTTP status code on the response.
	 * 		<br>The return type on the Java method can be any of the following:
	 * 		<ul>
	 * 			<li><jk>int</jk>/<c>Integer</c> - The HTTP response code.
	 * 			<li><jk>boolean</jk>/<c>Boolean</c> - <jk>true</jk> if the response code is <c>&lt;400</c>
	 * 		</ul>
	 * </ul>
	 */
	RemoteReturn returns() default RemoteReturn.BODY;
}
