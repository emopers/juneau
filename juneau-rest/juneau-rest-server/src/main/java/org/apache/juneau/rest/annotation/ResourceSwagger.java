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

import org.apache.juneau.jsonschema.annotation.ExternalDocs;
import org.apache.juneau.http.annotation.*;

/**
 * Extended annotation for {@link RestResource#swagger() @RestResource(swagger)}.
 *
 * <ul class='seealso'>
 * 	<li class='link'>{@doc juneau-rest-server.Swagger}
 * </ul>
 */
public @interface ResourceSwagger {


	/**
	 * Defines the swagger field <c>/info/title</c>.
	 *
	 * <h5 class='section'>Example:</h5>
	 * <p class='bcode w800'>
	 * 	<ja>@RestResource</ja>(
	 * 		swagger=<ja>@ResourceSwagger</ja>(
	 * 			title=<js>"Petstore application"</js>
	 * 		)
	 * 	)
	 * </p>
	 *
	 * <ul class='notes'>
	 * 	<li>
	 * 		The format is plain-text.
	 * 		<br>Multiple lines are concatenated with newlines.
	 * 	<li>
	 * 		The precedence of lookup for this field is:
	 * 		<ol>
	 * 			<li><c>{resource-class}.title</c> property in resource bundle.
	 * 			<li>{@link ResourceSwagger#title()} on this class, then any parent classes.
	 * 			<li>Value defined in Swagger JSON file.
	 * 			<li>{@link RestResource#title()} on this class, then any parent classes.
	 * 			<li>
	 * 		</ol>
	 * 	<li>
	 * 		Supports {@doc DefaultRestSvlVariables}
	 * 		(e.g. <js>"$L{my.localized.variable}"</js>).
	 * </ul>
	 */
	String[] title() default {};

	/**
	 * Defines the swagger field <c>/info/description</c>.
	 *
	 * <h5 class='section'>Example:</h5>
	 * <p class='bcode w800'>
	 * 	<ja>@RestResource</ja>(
	 * 		swagger=<ja>@ResourceSwagger</ja>(
	 * 			description={
	 * 				<js>"This is a sample server Petstore server based on the Petstore sample at Swagger.io."<js>,
	 * 				<js>"You can find out more about Swagger at &lt;a class='link' href='http://swagger.io'&gt;http://swagger.io&lt;/a&gt;."</js>
	 * 			}
	 * 		)
	 * 	)
	 * </p>
	 *
	 * <ul class='notes'>
	 * 	<li>
	 * 		The format is plain text.
	 * 		<br>Multiple lines are concatenated with newlines.
	 * 	<li>
	 * 		The precedence of lookup for this field is:
	 * 		<ol>
	 * 			<li><c>{resource-class}.description</c> property in resource bundle.
	 * 			<li>{@link ResourceSwagger#description()} on this class, then any parent classes.
	 * 			<li>Value defined in Swagger JSON file.
	 * 			<li>{@link RestResource#description()} on this class, then any parent classes.
	 * 			<li>
	 * 		</ol>
	 * 	<li>
	 * 		Supports {@doc DefaultRestSvlVariables}
	 * 		(e.g. <js>"$L{my.localized.variable}"</js>).
	 * </ul>
	 */
	String[] description() default {};

	/**
	 * Defines the swagger field <c>/info/contact</c>.
	 *
	 * <p>
	 * A {@doc juneau-marshall.JsonDetails.SimplifiedJson} string with the following fields:
	 * <p class='bcode w800'>
	 * 	{
	 * 		name: string,
	 * 		url: string,
	 * 		email: string
	 * 	}
	 * </p>
	 *
	 * <p>
	 * The default value pulls the description from the <c>contact</c> entry in the servlet resource bundle.
	 * (e.g. <js>"contact = {name:'John Smith',email:'john.smith@foo.bar'}"</js> or
	 * <js>"MyServlet.contact = {name:'John Smith',email:'john.smith@foo.bar'}"</js>).
	 *
	 * <h5 class='section'>Example:</h5>
	 * <p class='bcode w800'>
	 * 	<ja>@RestResource</ja>(
	 * 		swagger=<ja>@MethodSwagger</ja>(
	 * 			contact=<js>"{name:'John Smith',email:'john.smith@foo.bar'}"</js>
	 * 		)
	 * 	)
	 * </p>
	swagger={
		"info: {",
			"contact:{name:'Juneau Developer',email:'dev@juneau.apache.org'},",
			"license:{name:'Apache 2.0',url:'http://www.apache.org/licenses/LICENSE-2.0.html'},",
			"version:'2.0',",
			"termsOfService:'You are on your own.'",
		"},",
		"externalDocs:{description:'Apache Juneau',url:'http://juneau.apache.org'}"
	}
	swagger=@ResourceSwagger(
		contact="name:'Juneau Developer',email:'dev@juneau.apache.org'",
		license="name:'Apache 2.0',url:'http://www.apache.org/licenses/LICENSE-2.0.html'",
		version="2.0",
		termsOfService="You are on your own.",
		externalDocs="description:'Apache Juneau',url:'http://juneau.apache.org'}"
	)
	swagger=@ResourceSwagger("$F{PetStoreResource.json}"),
	 *
	 * <ul class='notes'>
	 * 	<li>
	 * 		The format is a {@doc juneau-marshall.JsonDetails.SimplifiedJson} object.
	 * 		<br>Multiple lines are concatenated with newlines.
	 * 	<li>
	 * 		Supports {@doc DefaultRestSvlVariables}
	 * 		(e.g. <js>"$L{my.localized.variable}"</js>).
	 * </ul>
	 */
	Contact contact() default @Contact;

	/**
	 * Defines the swagger field <c>/externalDocs</c>.
	 *
	 * <p>
	 * It is used to populate the Swagger external documentation field and to display on HTML pages.
	 * 	 *
	 * <p>
	 * The default value pulls the description from the <c>externalDocs</c> entry in the servlet resource bundle.
	 * (e.g. <js>"externalDocs = {url:'http://juneau.apache.org'}"</js> or
	 * <js>"MyServlet.externalDocs = {url:'http://juneau.apache.org'}"</js>).
	 *
	 * <h5 class='section'>Example:</h5>
	 * <p class='bcode w800'>
	 * 	<ja>@RestResource</ja>(
	 * 		swagger=<ja>@MethodSwagger</ja>(
	 * 			externalDocs=<ja>@ExternalDocs</ja>(url=<js>"http://juneau.apache.org"</js>)
	 * 		)
	 * 	)
	 * </p>
	 */
	ExternalDocs externalDocs() default @ExternalDocs;

	/**
	 * Defines the swagger field <c>/info/license</c>.
	 *
	 * <p>
	 * It is used to populate the Swagger license field and to display on HTML pages.
	 *
	 * <p>
	 * A {@doc juneau-marshall.JsonDetails.SimplifiedJson} string with the following fields:
	 * <p class='bcode w800'>
	 * 	{
	 * 		name: string,
	 * 		url: string
	 * 	}
	 * </p>
	 *
	 * <p>
	 * The default value pulls the description from the <c>license</c> entry in the servlet resource bundle.
	 * (e.g. <js>"license = {name:'Apache 2.0',url:'http://www.apache.org/licenses/LICENSE-2.0.html'}"</js> or
	 * <js>"MyServlet.license = {name:'Apache 2.0',url:'http://www.apache.org/licenses/LICENSE-2.0.html'}"</js>).
	 *
	 * <h5 class='section'>Example:</h5>
	 * <p class='bcode w800'>
	 * 	<ja>@RestResource</ja>(
	 * 		swagger=<ja>@MethodSwagger</ja>(
	 * 			license=<js>"{name:'Apache 2.0',url:'http://www.apache.org/licenses/LICENSE-2.0.html'}"</js>
	 * 		)
	 * 	)
	 * </p>
	 *
	 * <ul class='notes'>
	 * 	<li>
	 * 		The format is a {@doc juneau-marshall.JsonDetails.SimplifiedJson} object.
	 * 		<br>Multiple lines are concatenated with newlines.
	 * 	<li>
	 * 		Supports {@doc DefaultRestSvlVariables}
	 * 		(e.g. <js>"$L{my.localized.variable}"</js>).
	 * </ul>
	 */
	License license() default @License;

	/**
	 * Defines the swagger field <c>/tags</c>.
	 *
	 *
	 * Optional tagging information for the exposed API.
	 *
	 * <p>
	 * It is used to populate the Swagger tags field and to display on HTML pages.
	 *
	 * <p>
	 * A {@doc juneau-marshall.JsonDetails.SimplifiedJson} string with the following fields:
	 * <p class='bcode w800'>
	 * 	[
	 * 		{
	 * 			name: string,
	 * 			description: string,
	 * 			externalDocs: {
	 * 				description: string,
	 * 				url: string
	 * 			}
	 * 		}
	 * 	]
	 * </p>
	 *
	 * <p>
	 * The default value pulls the description from the <c>tags</c> entry in the servlet resource bundle.
	 * (e.g. <js>"tags = [{name:'Foo',description:'Foobar'}]"</js> or
	 * <js>"MyServlet.tags = [{name:'Foo',description:'Foobar'}]"</js>).
	 *
	 * <h5 class='section'>Example:</h5>
	 * <p class='bcode w800'>
	 * 	<ja>@RestResource</ja>(
	 * 		swagger=<ja>@MethodSwagger</ja>(
	 * 			tags=<js>"[{name:'Foo',description:'Foobar'}]"</js>
	 * 		)
	 * 	)
	 * </p>
	 *
	 * <ul class='notes'>
	 * 	<li>
	 * 		The format is a {@doc juneau-marshall.JsonDetails.SimplifiedJson} array.
	 * 		<br>Multiple lines are concatenated with newlines.
	 * 	<li>
	 * 		Supports {@doc DefaultRestSvlVariables}
	 * 		(e.g. <js>"$L{my.localized.variable}"</js>).
	 * </ul>
	 */
	Tag[] tags() default {};

	/**
	 * Defines the swagger field <c>/info/termsOfService</c>.
	 *
	 *
	 * Optional servlet terms-of-service for this API.
	 *
	 * <p>
	 * It is used to populate the Swagger terms-of-service field.
	 *
	 * <p>
	 * The default value pulls the description from the <c>termsOfService</c> entry in the servlet resource bundle.
	 * (e.g. <js>"termsOfService = foo"</js> or <js>"MyServlet.termsOfService = foo"</js>).
	 *
	 * <ul class='notes'>
	 * 	<li>
	 * 		The format is plain text.
	 * 		<br>Multiple lines are concatenated with newlines.
	 * 	<li>
	 * 		Supports {@doc DefaultRestSvlVariables}
	 * 		(e.g. <js>"$L{my.localized.variable}"</js>).
	 * </ul>
	 */
	String[] termsOfService() default {};

	/**
	 * Defines the swagger field <c>/info/version</c>.
	 *
	 *
	 *
	 * Provides the version of the application API (not to be confused with the specification version).
	 *
	 * <p>
	 * It is used to populate the Swagger version field and to display on HTML pages.
	 *
	 * <p>
	 * The default value pulls the description from the <c>version</c> entry in the servlet resource bundle.
	 * (e.g. <js>"version = 2.0"</js> or <js>"MyServlet.version = 2.0"</js>).
	 *
	 * <ul class='notes'>
	 * 	<li>
	 * 		The format is plain text.
	 * 	<li>
	 * 		Supports {@doc DefaultRestSvlVariables}
	 * 		(e.g. <js>"$L{my.localized.variable}"</js>).
	 * </ul>
	 */
	String version() default "";

	/**
	 * Free-form value for the swagger of a resource.
	 *
	 * <p>
	 * This is a {@doc juneau-marshall.JsonDetails.SimplifiedJson} object that makes up the swagger information for this resource.
	 *
	 * <p>
	 * The following are completely equivalent ways of defining the swagger description of a resource:
	 * <p class='bcode w800'>
	 * 	<jc>// Normal</jc>
	 * 	<ja>@RestResource</ja>(
	 * 		swagger=<ja>@ResourceSwagger</ja>(
	 * 			title=<js>"Petstore application"</js>,
	 * 			description={
	 * 				<js>"This is a sample server Petstore server based on the Petstore sample at Swagger.io."</js>,
	 * 				<js>"You can find out more about Swagger at &lt;a class='link' href='http://swagger.io'&gt;http://swagger.io&lt;/a&gt;."</js>
	 * 			},
	 * 			contact=<ja>@Contact</ja>(
	 * 				name=<js>"John Smith"</js>,
	 * 				email=<js>"john@smith.com"</js>
	 * 			),
	 * 			license=<ja>@License</ja>(
	 * 				name=<js>"Apache 2.0"</js>,
	 * 				url=<js>"http://www.apache.org/licenses/LICENSE-2.0.html"</js>
	 * 			),
	 * 			version=<js>"2.0"</js>,
	 * 			termsOfService=<js>"You are on your own."</js>,
	 * 			tags={
	 * 				<ja>@Tag</ja>(
	 * 					name=<js>"Java"</js>,
	 * 					description=<js>"Java utility"</js>,
	 * 					externalDocs=<ja>@ExternalDocs</ja>(
	 * 						description=<js>"Home page"</js>,
	 * 						url=<js>"http://juneau.apache.org"</js>
	 * 					)
	 * 				}
	 * 			},
	 * 			externalDocs=<ja>@ExternalDocs</ja>(
	 * 				description=<js>"Home page"</js>,
	 * 				url=<js>"http://juneau.apache.org"</js>
	 * 			)
	 * 		)
	 * 	)
	 * </p>
	 * <p class='bcode w800'>
	 * 	<jc>// Free-form</jc>
	 * 	<ja>@RestResource</ja>(
	 * 		swagger=@ResourceSwagger({
	 * 			<js>"title: 'Petstore application',"</js>,
	 * 			<js>"description: 'This is a sample server Petstore server based on the Petstore sample at Swagger.io.\nYou can find out more about Swagger at &lt;a class='link' href='http://swagger.io'&gt;http://swagger.io&lt;/a&gt;.',"</js>,
	 * 			<js>"contact:{"</js>,
	 * 				<js>"name: 'John Smith',"</js>,
	 * 				<js>"email: 'john@smith.com'"</js>,
	 * 			<js>"},"</js>,
	 * 			<js>"license:{"</js>,
	 * 				<js>"name: 'Apache 2.0',"</js>,
	 * 				<js>"url: 'http://www.apache.org/licenses/LICENSE-2.0.html'"</js>,
	 * 			<js>"},"</js>,
	 * 			<js>"version: '2.0',"</js>,
	 * 			<js>"termsOfService: 'You are on your own.',"</js>,
	 * 			<js>"tags:["</js>,
	 * 				<js>"{"</js>,
	 * 					<js>"name: 'Java',"</js>,
	 * 					<js>"description: 'Java utility',"</js>,
	 * 					<js>"externalDocs:{"</js>,
	 * 						<js>"description: 'Home page',"</js>,
	 * 						<js>"url: 'http://juneau.apache.org'"</js>,
	 * 					<js>"}"</js>,
	 * 				<js>"}"</js>,
	 * 			<js>"],"</js>,
	 * 			<js>"externalDocs:{"</js>,
	 * 				<js>"description: 'Home page',"</js>,
	 * 				<js>"url: 'http://juneau.apache.org'"</js>,
	 * 			<js>"}"</js>
	 * 		})
	 * 	)
	 * </p>
	 * <p class='bcode w800'>
	 * 	<jc>// Free-form with variables</jc>
	 * 	<ja>@RestResource</ja>(
	 * 		swagger=@ResourceSwagger(<js>"$F{MyResourceSwagger.json}"</js>)
	 * 	)
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
	 * 	<ja>@ResourceSwagger</ja>(<js>"{title:'Petstore application'}"</js>)
	 * 		</p>
	 * 		<p class='bcode w800'>
	 * 	<ja>@ResourceSwagger</ja>(<js>"title:'Petstore application'"</js>)
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
