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
package org.apache.juneau.http.annotation;

import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.*;

/**
 * Swagger license annotation.
 *
 * <p>
 * License information for the exposed API.
 *
 * <p>
 * Used to populate the auto-generated Swagger documentation and UI for server-side <ja>@RestResource</ja>-annotated classes.
 *
 * <h5 class='section'>Examples:</h5>
 * <p class='bcode w800'>
 * 	<jc>// Normal</jc>
 * 	<ja>@RestResource</ja>(
 * 		swagger=<ja>@ResourceSwagger</ja>(
 * 			license=<ja>@License</ja>(
 * 				name=<js>"Apache 2.0"</js>,
 * 				url=<js>"http://www.apache.org/licenses/LICENSE-2.0.html"</js>
 * 			)
 * 		)
 * 	)
 * </p>
 * <p class='bcode w800'>
 * 	<jc>// Free-form</jc>
 * 	<ja>@RestResource</ja>(
 * 		swagger=<ja>@ResourceSwagger</ja>(
 * 			license=<ja>@License</ja>({
 * 				<js>"name:'Apache 2.0',"</js>,
 * 				<js>"url:'http://www.apache.org/licenses/LICENSE-2.0.html'"</js>
 * 			})
 * 		)
 * 	)
 * </p>
 *
 * <ul class='seealso'>
 * 	<li class='link'>{@doc juneau-rest-server.Swagger}
 * 	<li class='extlink'>{@doc SwaggerLicenseObject}
 * </ul>
 */
@Documented
@Retention(RUNTIME)
public @interface License {

	/**
	 * <mk>name</mk> field of the {@doc SwaggerLicenseObject}.
	 *
	 * <ul class='notes'>
	 * 	<li>
	 * 		The format is a plain-text string.
	 * 	<li>
	 * 		Supports {@doc DefaultRestSvlVariables}
	 * 		(e.g. <js>"$L{my.localized.variable}"</js>).
	 * </ul>
	 */
	String name() default "";

	/**
	 * <mk>url</mk> field of the {@doc SwaggerLicenseObject}.
	 *
	 * <ul class='notes'>
	 * 	<li>
	 * 		The format is a plain-text string.
	 * 	<li>
	 * 		Supports {@doc DefaultRestSvlVariables}
	 * 		(e.g. <js>"$L{my.localized.variable}"</js>).
	 * </ul>
	 */
	String url() default "";

	/**
	 * Free-form value for the {@doc SwaggerLicenseObject}.
	 *
	 * <p>
	 * This is a JSON object that makes up the swagger information for this field.
	 *
	 * <p>
	 * The following are completely equivalent ways of defining the swagger description of the license information:
	 * <p class='bcode w800'>
	 * 	<jc>// Normal</jc>
	 * 	<ja>@RestResource</ja>(
	 * 		swagger=<ja>@ResourceSwagger</ja>(
	 * 			license=<ja>@License</ja>(
	 * 				name=<js>"Apache 2.0"</js>,
	 * 				url=<js>"http://www.apache.org/licenses/LICENSE-2.0.html"</js>
	 * 			)
	 * 		)
	 * 	)
	 * </p>
	 * <p class='bcode w800'>
	 * 	<jc>// Free-form</jc>
	 * 	<ja>@RestResource</ja>(
	 * 		swagger=<ja>@ResourceSwagger</ja>(
	 * 			license=<ja>@License</ja>({
	 * 				<js>"name: 'Apache 2.0',"</js>,
	 * 				<js>"url: 'http://www.apache.org/licenses/LICENSE-2.0.html'"</js>
	 * 			})
	 * 		)
	 * 	)
	 * </p>
	 * <p class='bcode w800'>
	 * 	<jc>// Free-form with variables</jc>
	 * 	<ja>@RestResource</ja>(
	 * 		swagger=<ja>@ResourceSwagger</ja>(<js>"$L{licenseSwagger}"</js>)
	 * 	)
	 * </p>
	 * <p class='bcode w800'>
	 * 	<mc>// Contents of MyResource.properties</mc>
	 * 	<mk>licenseSwagger</mk> = <mv>{ name: "Apache 2.0", url: "http://www.apache.org/licenses/LICENSE-2.0.html" }</mv>
	 * </p>
	 *
	 * <p>
	 * 	The reasons why you may want to use this field include:
	 * <ul>
	 * 	<li>You want to pull in the entire Swagger JSON definition for this field from an external source such as a properties file.
	 * 	<li>You want to add extra fields to the Swagger documentation that are not officially part of the Swagger specification.
	 * </ul>
	 *
	 * <ul class='notes'>
	 * 	<li>
	 * 		The format is a {@doc juneau-marshall.JsonDetails.SimplifiedJson} object.
	 * 	<li>
	 * 		The leading/trailing <c>{ }</c> characters are optional.
	 * 		<br>The following two example are considered equivalent:
	 * 		<p class='bcode w800'>
	 * 	<ja>@License</ja>(<js>"{name: 'Apache 2.0'}"</js>)
	 * 		</p>
	 * 		<p class='bcode w800'>
	 * 	<ja>@License</ja>(<js>"name: 'Apache 2.0'"</js>)
	 * 		</p>
	 * 	<li>
	 * 		Multiple lines are concatenated with newlines so that you can format the value to be readable.
	 * 	<li>
	 * 		Supports {@doc DefaultRestSvlVariables}
	 * 		(e.g. <js>"$L{my.localized.variable}"</js>).
	 * 	<li>
	 * 		Values defined in this field supersede values pulled from the Swagger JSON file and are superseded by individual values defined on this annotation.
	 * </ul>
	 */
	String[] value() default {};
}
