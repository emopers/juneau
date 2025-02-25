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
package org.apache.juneau.svl.vars;

import static org.apache.juneau.internal.ThrowableUtils.*;

import org.apache.juneau.svl.*;

/**
 * A basic if-else logic variable resolver.
 *
 * <p>
 * The format for this var is one of the following:
 * <ul>
 * 	<li><js>"$IF{booleanArg,thenValue}"</js>
 * 	<li><js>"$IF{booleanArg,thenValue,elseValue}"</js>
 * </ul>
 *
 * <p>
 * The boolean argument is any string.
 * <br>The following values are interpreted as <jk>true</jk>:  <js>"true"</js>,<js>"TRUE"</js>,<js>"t"</js>,
 * <js>"T"</js>,<js>"1"</js>.
 * <br>All else are interpreted as <jk>false</jk>
 *
 * <h5 class='section'>Example:</h5>
 * <p class='bcode w800'>
 * 	<jc>// Create a variable resolver that resolves system properties and $IF vars.</jc>
 * 	VarResolver r = VarResolver.<jsm>create</jsm>().vars(IfVar.<jk>class</jk>, SystemPropertiesVar.<jk>class</jk>).build();
 *
 * 	<jc>// Use it!</jc>
 * 	System.<jsf>out</jsf>.println(r.resolve(<js>"Property $IF{$S{someBooleanFlag},IS,IS NOT} set!"</js>));
 * </p>
 *
 * <p>
 * Since this is a {@link MultipartVar}, any variables contained in the result will be recursively resolved.
 * <br>Likewise, if the arguments contain any variables, those will be resolved before they are passed to this var.
 *
 * <ul class='seealso'>
 * 	<li class='link'>{@doc juneau-svl.SvlVariables}
 * </ul>
 */
public class IfVar extends MultipartVar {

	/** The name of this variable. */
	public static final String NAME = "IF";

	/**
	 * Constructor.
	 */
	public IfVar() {
		super(NAME);
	}

	@Override /* MultipartVar */
	public String resolve(VarResolverSession session, String[] args) {
		if (args.length < 2 || args.length > 3)
			illegalArg("Invalid number of arguments passed to $IF var.  Must be either $IF{booleanArg,thenValue} or $IF{booleanArg,thenValue,elseValue}");

		String b = args[0].toLowerCase();
		if ("1".equals(b) || "t".equals(b) || "true".equals(b))
			return args[1];
		return args.length == 2 ? "" : args[2];
	}
}
