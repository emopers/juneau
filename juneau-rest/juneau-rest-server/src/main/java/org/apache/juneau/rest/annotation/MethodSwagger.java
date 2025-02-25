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
package org.apache.juneau.rest.annotation;

import org.apache.juneau.jsonschema.annotation.*;

/**
 * Extended annotation for {@link RestMethod#swagger() RestMethod.swagger()}.
 *
 * <ul class='seealso'>
 * 	<li class='link'>{@doc juneau-rest-server.Swagger}
 * </ul>
 */
public @interface MethodSwagger {

	/**
	 * Defines the swagger field <c>/paths/{path}/{method}/summary</c>.
	 *
	 * <ul class='notes'>
	 * 	<li>
	 * 		The format is plain text.
	 * 		<br>Multiple lines are concatenated with newlines.
	 * 	<li>
	 * 		Supports {@doc DefaultRestSvlVariables}
	 * 		(e.g. <js>"$L{my.localized.variable}"</js>).
	 * 	<li>
	 * 		Values defined on this annotation override values defined for the method in the class swagger.
	 * 	<li>
	 * 		If not specified, the value is pulled from {@link RestMethod#summary()}.
	 * </ul>
	 */
	String[] summary() default {};

	/**
	 * Defines the swagger field <c>/paths/{path}/{method}/description</c>.
	 *
	 * <ul class='notes'>
	 * 	<li>
	 * 		The format is plain text.
	 * 		<br>Multiple lines are concatenated with newlines.
	 * 	<li>
	 * 		Supports {@doc DefaultRestSvlVariables}
	 * 		(e.g. <js>"$L{my.localized.variable}"</js>).
	 * 	<li>
	 * 		Values defined on this annotation override values defined for the method in the class swagger.
	 * 	<li>
	 * 		If not specified, the value is pulled from {@link RestMethod#description()}.
	 * </ul>
	 */
	String[] description() default {};

	/**
	 * Defines the swagger field <c>/paths/{path}/{method}/operationId</c>.
	 *
	 * <ul class='notes'>
	 * 	<li>
	 * 		The format is plain text.
	 * 	<li>
	 * 		Supports {@doc DefaultRestSvlVariables}
	 * 		(e.g. <js>"$L{my.localized.variable}"</js>).
	 * 	<li>
	 * 		Values defined on this annotation override values defined for the method in the class swagger.
	 * 	<li>
	 * 		If not specified, the value used is the Java method name.
	 * </ul>
	 */
	String operationId() default "";

	/**
	 * Defines the swagger field <c>/paths/{path}/{method}/schemes</c>.
	 *
	 * <ul class='notes'>
	 * 	<li>
	 * 		The format is either a comma-delimited list of simple strings or a {@doc juneau-marshall.JsonDetails.SimplifiedJson} array.
	 * 	<li>
	 * 		Supports {@doc DefaultRestSvlVariables}
	 * 		(e.g. <js>"$L{my.localized.variable}"</js>).
	 * 	<li>
	 * 		Values defined on this annotation override values defined for the method in the class swagger.
	 * </ul>
	 */
	String[] schemes() default {};

	/**
	 * Defines the swagger field <c>/paths/{path}/{method}/deprecated</c>.
	 *
	 * <h5 class='section'>Example:</h5>
	 * <p class='bcode w800'>
	 * 	<ja>@RestMethod</ja>(
	 * 		swagger=<ja>@MethodSwagger</ja>(
	 * 			deprecated=<jk>true</jk>
	 * 		)
	 * 	)
	 * </p>
	 *
	 * <ul class='notes'>
	 * 	<li>
	 * 		The format is boolean.
	 * 	<li>
	 * 		Supports {@doc DefaultRestSvlVariables}
	 * 		(e.g. <js>"$L{my.localized.variable}"</js>).
	 * 	<li>
	 * 		Values defined on this annotation override values defined for the method in the class swagger.
	 * 	<li>
	 * 		If not specified, set to <js>"true"</js> if the method is annotated with {@link Deprecated @Deprecated}
	 * </ul>
	 */
	String deprecated() default "";

	/**
	 * Defines the swagger field <c>/paths/{path}/{method}/consumes</c>.
	 *
	 * <p>
	 * Use this value to override the supported <c>Content-Type</c> media types defined by the parsers defined via {@link RestMethod#parsers()}.
	 *
	 * <ul class='notes'>
	 * 	<li>
	 * 		The format is either a comma-delimited list of simple strings or a {@doc juneau-marshall.JsonDetails.SimplifiedJson} array.
	 * 	<li>
	 * 		Supports {@doc DefaultRestSvlVariables}
	 * 		(e.g. <js>"$L{my.localized.variable}"</js>).
	 * 	<li>
	 * 		Values defined on this annotation override values defined for the method in the class swagger.
	 * </ul>
	 */
	String[] consumes() default {};

	/**
	 * Defines the swagger field <c>/paths/{path}/{method}/consumes</c>.
	 *
	 * <p>
	 * Use this value to override the supported <c>Accept</c> media types defined by the serializers defined via {@link RestMethod#serializers()}.
	 *
	 * <ul class='notes'>
	 * 	<li>
	 * 		The format is either a comma-delimited list of simple strings or a {@doc juneau-marshall.JsonDetails.SimplifiedJson} array.
	 * 	<li>
	 * 		Supports {@doc DefaultRestSvlVariables}
	 * 		(e.g. <js>"$L{my.localized.variable}"</js>).
	 * 	<li>
	 * 		Values defined on this annotation override values defined for the method in the class swagger.
	 * </ul>
	 */
	String[] produces() default {};

	/**
	 * Defines the swagger field <c>/paths/{path}/{method}/externalDocs</c>.
	 *
	 * <h5 class='section'>Example:</h5>
	 * <p class='bcode w800'>
	 * 	<ja>@RestMethod</ja>(
	 * 		swagger=<ja>@MethodSwagger</ja>(
	 * 			externalDocs=<ja>@ExternalDocs</ja>(url=<js>"http://juneau.apache.org"</js>)
	 * 		)
	 * 	)
	 * </p>
	 *
	 * <ul class='notes'>
	 * 	<li>
	 * 		Supports {@doc DefaultRestSvlVariables}
	 * 		(e.g. <js>"$L{my.localized.variable}"</js>).
	 * 	<li>
	 * 		Values defined on this annotation override values defined for the method in the class swagger.
	 * </ul>
	 */
	ExternalDocs externalDocs() default @ExternalDocs;

	/**
	 * Defines the swagger field <c>/paths/{path}/{method}/parameters</c>.
	 *
	 * <p>
	 * This annotation is provided for documentation purposes and is used to populate the method <js>"parameters"</js>
	 * column on the Swagger page.
	 *
	 * <h5 class='section'>Example:</h5>
	 * <p class='bcode w800'>
	 * 	<ja>@RestMethod</ja>(
	 * 		name=<jsf>POST</jsf>, path=<js>"/{a}"</js>,
	 * 		description=<js>"This is my method."</js>,
	 * 		swagger=<ja>@MethodSwagger</ja>(
	 * 			parameters={
	 * 				<js>"{in:'path', name:'a', description:'The \\'a\\' attribute'},"</js>,
	 * 				<js>"{in:'query', name:'b', description:'The \\'b\\' parameter', required:true},"</js>,
	 * 				<js>"{in:'body', description:'The HTTP content'},"</js>,
	 * 				<js>"{in:'header', name:'D', description:'The \\'D\\' header'}"</js>
	 * 			}
	 * 		)
	 * 	)
	 * </p>
	 *
	 * <ul class='notes'>
	 * 	<li>
	 * 		The format is a {@doc juneau-marshall.JsonDetails.SimplifiedJson} array consisting of the concatenated individual strings.
	 * 		<br>The leading and trailing <js>'['</js> and <js>']'</js> characters are optional.
	 * 	<li>
	 * 		Supports {@doc DefaultRestSvlVariables}
	 * 		(e.g. <js>"$L{my.localized.variable}"</js>).
	 * 	<li>
	 * </ul>
	 */
	String[] parameters() default {};

	/**
	 * Defines the swagger field <c>/paths/{path}/{method}/responses</c>.
	 *
	 * <p>
	 * This annotation is provided for documentation purposes and is used to populate the method <js>"responses"</js>
	 * column on the Swagger page.
	 *
	 * <h5 class='section'>Example:</h5>
	 * <p class='bcode w800'>
	 * 	<ja>@RestMethod</ja>(
	 * 		name=<jsf>GET</jsf>, path=<js>"/"</js>,
	 * 		swagger=<ja>@MethodSwagger</ja>(
	 * 			responses={
					<js>"200:{ description:'Okay' },"</js>,
					<js>"302:{ description:'Thing wasn't found here', headers={Location:{description:'The place to find the thing.'}}}"</js>
	 * 			}
	 * 		)
	 * 	)
	 * </p>
	 *
	 * <ul class='notes'>
	 * 	<li>
	 * 		The format is a {@doc juneau-marshall.JsonDetails.SimplifiedJson} objc consisting of the concatenated individual strings.
	 * 		<br>The leading and trailing <js>'{'</js> and <js>'}'</js> characters are optional.
	 * 	<li>
	 * 		Supports {@doc DefaultRestSvlVariables}
	 * 		(e.g. <js>"$L{my.localized.variable}"</js>).
	 * </ul>
	 */
	String[] responses() default {};

	/**
	 * Optional tagging information for the exposed API.
	 *
	 * <p>
	 * Used to populate the Swagger tags field.
	 *
	 * <p>
	 * A comma-delimited list of tags for API documentation control.
	 * <br>Tags can be used for logical grouping of operations by resources or any other qualifier.
	 *
	 * <h5 class='section'>Example:</h5>
	 * <p class='bcode w800'>
	 * 	<ja>@RestMethod</ja>(
	 * 		swagger=<ja>@MethodSwagger</ja>(
	 * 			tags=<js>"foo,bar"</js>
	 * 		)
	 * 	)
	 * </p>
	 *
	 * <ul class='notes'>
	 * 	<li>
	 * 		Supports {@doc DefaultRestSvlVariables}
	 * 		(e.g. <js>"$L{my.localized.variable}"</js>).
	 * 	<li>
	 * 		Corresponds to the swagger field <c>/paths/{path}/{method}/tags</c>.
	 * </ul>
	 */
	String[] tags() default {};

	/**
	 * Defines the swagger field <c>/paths/{path}/{method}</c>.
	 *
	 * <p>
	 * Used for free-form Swagger documentation of a REST Java method.
	 *
	 * <h5 class='section'>Example:</h5>
	 * <p class='bcode w800'>
	 * 	<ja>@RestMethod</ja>(
	 * 		swagger=<ja>@MethodSwagger</ja>(
	 * 			<js>"tags:['pet'],"</js>,
	 * 			<js>"security:[ { petstore_auth:['write:pets','read:pets'] } ]"</js>
	 * 		)
	 * 	)
	 * </p>
	 *
	 * <ul class='notes'>
	 * 	<li>
	 * 		The format is a {@link SimpleJsonSerializer#DEFAULT Simple-JSON} object.
	 * 		<br>Multiple lines are concatenated with newlines.
	 * 		<br>Comments and whitespace are ignored.
	 * 		<br>The leading and trailing <js>'{'</js>/<js>'}'</js> characters are optional.
	 * 	<li>
	 * 		Supports {@doc DefaultRestSvlVariables}
	 * 		(e.g. <js>"$L{my.localized.variable}"</js>).
	 * 	<li>
	 * 		Values defined on this annotation override values defined for the method in the class swagger.
	 * 	<li>
	 *
	 * </ul>
	 */

	/**
	 * Free-form value for the swagger of a resource method.
	 *
	 * <p>
	 * This is a {@doc juneau-marshall.JsonDetails.SimplifiedJson} object that makes up the swagger information for this resource method.
	 *
	 * <p>
	 * The following are completely equivalent ways of defining the swagger description of a resource method:
	 * <p class='bcode w800'>
	 * 	<jc>// Normal</jc>
	 * 	<ja>@RestMethod</ja>(
	 * 		name=<js>"POST"</js>,
	 * 		path=<js>"/pet"</js>,
	 * 		swagger=<ja>@MethodSwagger</ja>(
	 * 			summary=<js>"Add pet"</js>,
	 * 			description=<js>"Adds a new pet to the store"</js>,
	 * 			tags=<js>"pet"</js>,
	 * 			externalDocs=<ja>@ExternalDocs</ja>(
	 * 				description=<js>"Home page"</js>,
	 * 				url=<js>"http://juneau.apache.org"</js>
	 * 			)
	 * 		)
	 * )
	 * </p>
	 * <p class='bcode w800'>
	 * 	<jc>// Free-form</jc>
	 * 	<ja>@RestMethod</ja>(
	 * 		name=<js>"POST"</js>,
	 * 		path=<js>"/pet"</js>,
	 * 		swagger=<ja>@MethodSwagger</ja>({
	 * 			<js>"summary: 'Add pet',"</js>,
	 * 			<js>"description: 'Adds a new pet to the store',"</js>,
	 * 			<js>"tags: ['pet'],"</js>,
	 * 			<js>"externalDocs:{"</js>,
	 * 				<js>"description: 'Home page',"</js>,
	 * 				<js>"url: 'http://juneau.apache.org'"</js>,
	 * 			<js>"}"</js>
	 * 		})
	 * )
	 * </p>
	 * <p class='bcode w800'>
	 * 	<jc>// Free-form with variables</jc>
	 * 	<ja>@RestMethod</ja>(
	 * 		name=<js>"POST"</js>,
	 * 		path=<js>"/pet"</js>,
	 * 		swagger=<ja>@MethodSwagger</ja>(<js>"$L{addPetSwagger}"</js>)
	 * )
	 * </p>
	 * <p class='bcode w800'>
	 * 	<mc>// Contents of MyResource.properties</mc>
	 * 	<mk>addPetSwagger</mk> = <mv>{ summary: "Add pet", description: "Adds a new pet to the store", tags: ["pet"], externalDocs:{ description: "Home page", url: "http://juneau.apache.org" } }</mv>
	 * </p>
	 *
	 * <p>
	 * 	The reasons why you may want to use this field include:
	 * <ul>
	 * 	<li>You want to pull in the entire Swagger JSON definition for this body from an external source such as a properties file.
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
	 * 	<ja>@MethodSwagger</ja>(<js>"{summary: 'Add pet'}"</js>)
	 * 		</p>
	 * 		<p class='bcode w800'>
	 * 	<ja>@MethodSwagger</ja>(<js>"summary: 'Add pet'"</js>)
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
