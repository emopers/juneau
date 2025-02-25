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

import static org.apache.juneau.internal.StringUtils.*;
import static org.apache.juneau.internal.ClassUtils.*;
import static org.apache.juneau.httppart.HttpPartType.*;

import java.lang.reflect.*;
import java.util.*;

import org.apache.juneau.*;
import org.apache.juneau.http.annotation.*;
import org.apache.juneau.httppart.*;
import org.apache.juneau.httppart.bean.*;
import org.apache.juneau.internal.*;
import org.apache.juneau.reflect.*;

/**
 * Contains the meta-data about a Java method on a REST proxy class.
 *
 * <p>
 * Captures the information in {@link RemoteMethod @RemoteMethod} annotations for caching and reuse.
 *
 * <ul class='seealso'>
 * 	<li class='link'>{@doc juneau-rest-client.RestProxies}
 * </ul>
 */
public class RemoteMethodMeta {

	private final String httpMethod;
	private final String fullPath, path;
	private final RemoteMethodArg[] pathArgs, queryArgs, headerArgs, formDataArgs, otherArgs;
	private final RemoteMethodBeanArg[] requestArgs;
	private final RemoteMethodArg bodyArg;
	private final RemoteMethodReturn methodReturn;
	private final Method method;
	private final Class<?>[] exceptions;

	/**
	 * Constructor.
	 *
	 * @param parentPath The absolute URL of the REST interface backing the interface proxy.
	 * @param m The Java method.
	 * @param useMethodSignatures If <jk>true</jk> then the default path for the method should be the full method signature.
	 * @param defaultMethod The default HTTP method if not specified through annotation.
	 */
	public RemoteMethodMeta(final String parentPath, Method m, boolean useMethodSignatures, String defaultMethod) {
		Builder b = new Builder(parentPath, m, useMethodSignatures, defaultMethod);
		this.method = m;
		this.httpMethod = b.httpMethod;
		this.path = b.path;
		this.fullPath = b.fullPath;
		this.pathArgs = b.pathArgs.toArray(new RemoteMethodArg[b.pathArgs.size()]);
		this.queryArgs = b.queryArgs.toArray(new RemoteMethodArg[b.queryArgs.size()]);
		this.formDataArgs = b.formDataArgs.toArray(new RemoteMethodArg[b.formDataArgs.size()]);
		this.headerArgs = b.headerArgs.toArray(new RemoteMethodArg[b.headerArgs.size()]);
		this.requestArgs = b.requestArgs.toArray(new RemoteMethodBeanArg[b.requestArgs.size()]);
		this.otherArgs = b.otherArgs.toArray(new RemoteMethodArg[b.otherArgs.size()]);
		this.bodyArg = b.bodyArg;
		this.methodReturn = b.methodReturn;
		this.exceptions = m.getExceptionTypes();
	}

	private static final class Builder {
		String httpMethod, fullPath, path;
		List<RemoteMethodArg>
			pathArgs = new LinkedList<>(),
			queryArgs = new LinkedList<>(),
			headerArgs = new LinkedList<>(),
			formDataArgs = new LinkedList<>(),
			otherArgs = new LinkedList<>();
		List<RemoteMethodBeanArg>
			requestArgs = new LinkedList<>();
		RemoteMethodArg bodyArg;
		RemoteMethodReturn methodReturn;

		Builder(String parentPath, Method m, boolean useMethodSignatures, String defaultMethod) {

			MethodInfo mi = getMethodInfo(m);

			RemoteMethod rm = mi.getAnnotation(RemoteMethod.class);

			httpMethod = rm == null ? "" : rm.method();
			path = rm == null ? "" : rm.path();

			if (path.isEmpty()) {
				path = HttpUtils.detectHttpPath(m, ! useMethodSignatures);
				if (useMethodSignatures)
					path += HttpUtils.getMethodArgsSignature(m, true);
			}
			if (httpMethod.isEmpty())
				httpMethod = HttpUtils.detectHttpMethod(m, ! useMethodSignatures, defaultMethod);

			path = trimSlashes(path);

			if (! isOneOf(httpMethod, "DELETE", "GET", "POST", "PUT", "OPTIONS", "HEAD", "CONNECT", "TRACE", "PATCH"))
				throw new RemoteMetadataException(m,
					"Invalid value specified for @RemoteMethod(httpMethod) annotation.  Valid values are [DELTE,GET,POST,PUT].");

			methodReturn = new RemoteMethodReturn(mi);

			fullPath = path.indexOf("://") != -1 ? path : (parentPath.isEmpty() ? urlEncodePath(path) : (trimSlashes(parentPath) + '/' + urlEncodePath(path)));

			for (ParamInfo mpi : mi.getParams()) {
				RemoteMethodArg rma = RemoteMethodArg.create(mpi);
				boolean annotated = false;
				if (rma != null) {
					annotated = true;
					HttpPartType pt = rma.getPartType();
					if (pt == HEADER)
						headerArgs.add(rma);
					else if (pt == QUERY)
						queryArgs.add(rma);
					else if (pt == FORMDATA)
						formDataArgs.add(rma);
					else if (pt == PATH)
						pathArgs.add(rma);
					else if (pt == BODY)
						bodyArg = rma;
					else
						annotated = false;
				}
				RequestBeanMeta rmba = RequestBeanMeta.create(mpi, PropertyStore.DEFAULT);
				if (rmba != null) {
					annotated = true;
					requestArgs.add(new RemoteMethodBeanArg(mpi.getIndex(), null, rmba));
				}
				if (! annotated) {
					otherArgs.add(new RemoteMethodArg(mpi.getIndex(), BODY, null));
				}
			}
		}
	}

	/**
	 * Returns the value of the {@link RemoteMethod#method() @RemoteMethod(httpMethod)} annotation on this Java method.
	 *
	 * @return The value of the annotation, never <jk>null</jk>.
	 */
	public String getHttpMethod() {
		return httpMethod;
	}

	/**
	 * Returns the absolute URL of the REST interface invoked by this Java method.
	 *
	 * @return The absolute URL of the REST interface, never <jk>null</jk>.
	 */
	public String getFullPath() {
		return fullPath;
	}

	/**
	 * Returns the {@link Path @Path} annotated arguments on this Java method.
	 *
	 * @return A map of {@link Path#value() @Path(value)} names to zero-indexed argument indices.
	 */
	public RemoteMethodArg[] getPathArgs() {
		return pathArgs;
	}

	/**
	 * Returns the {@link Query @Query} annotated arguments on this Java method.
	 *
	 * @return A map of {@link Query#value() @Query(value)} names to zero-indexed argument indices.
	 */
	public RemoteMethodArg[] getQueryArgs() {
		return queryArgs;
	}

	/**
	 * Returns the {@link FormData @FormData} annotated arguments on this Java method.
	 *
	 * @return A map of {@link FormData#value() @FormData(value)} names to zero-indexed argument indices.
	 */
	public RemoteMethodArg[] getFormDataArgs() {
		return formDataArgs;
	}

	/**
	 * Returns the {@link Header @Header} annotated arguments on this Java method.
	 *
	 * @return A map of {@link Header#value() @Header(value)} names to zero-indexed argument indices.
	 */
	public RemoteMethodArg[] getHeaderArgs() {
		return headerArgs;
	}

	/**
	 * Returns the {@link Request @Request} annotated arguments on this Java method.
	 *
	 * @return A list of zero-indexed argument indices.
	 */
	public RemoteMethodBeanArg[] getRequestArgs() {
		return requestArgs;
	}

	/**
	 * Returns the remaining non-annotated arguments on this Java method.
	 *
	 * @return A list of zero-indexed argument indices.
	 */
	public RemoteMethodArg[] getOtherArgs() {
		return otherArgs;
	}

	/**
	 * Returns the argument annotated with {@link Body @Body}.
	 *
	 * @return A index of the argument with the {@link Body @Body} annotation, or <jk>null</jk> if no argument exists.
	 */
	public RemoteMethodArg getBodyArg() {
		return bodyArg;
	}

	/**
	 * Returns whether the method returns the HTTP response body or status code.
	 *
	 * @return Whether the method returns the HTTP response body or status code.
	 */
	public RemoteMethodReturn getReturns() {
		return methodReturn;
	}

	/**
	 * Returns the HTTP path of this method.
	 *
	 * @return
	 * 	The HTTP path of this method relative to the parent interface.
	 * 	<br>Never <jk>null</jk>.
	 * 	<br>Never has leading or trailing slashes.
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Returns the underlying Java method that this metadata is about.
	 *
	 * @return
	 * 	The underlying Java method that this metadata is about.
	 * 	<br>Never <jk>null</jk>.
	 */
	public Method getJavaMethod() {
		return method;
	}

	/**
	 * Returns the exceptions thrown by this method.
	 *
	 * @return The exceptions thrown by this method.  Never <jk>null</jk>.
	 */
	public Class<?>[] getExceptions() {
		return exceptions;
	}
}
