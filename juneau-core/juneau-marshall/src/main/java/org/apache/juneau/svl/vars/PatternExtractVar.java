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

import java.util.regex.*;

import org.apache.juneau.svl.*;

/**
 * A transformational variable that returns matched regex groups by given index.
 *
 * <p>
 * The format for this var:
 * <ul>
 * 	<li><js>"$PE{arg,pattern,groupIndex}"</js>
 * </ul>
 *
 * <p>
 * The pattern can be any string optionally containing <js>'*'</js> or <js>'?'</js> representing any or one character
 * respectively.
 *
 * <h5 class='section'>Example:</h5>
 * <p class='bcode w800'>
 * 	<jc>// Create a variable resolver that resolves system properties and $SW vars.</jc>
 * 	VarResolver r = VarResolver.<jsm>create</jsm>().vars(PatternExtractVar.<jk>class</jk>, SystemPropertiesVar.<jk>class</jk>).build();
 *
 * 	<jc>// Use it!</jc>
 * 	System.<jsf>out</jsf>.println(r.resolve(<js>"Update = $PE{$P{java.version},_([0-9]+),1}!"</js>));
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
public class PatternExtractVar extends MultipartVar {

	/** The name of this variable. */
	public static final String NAME = "PE";

	/**
	 * Constructor.
	 */
	public PatternExtractVar() {
		super(NAME);
	}

	@Override /* MultipartVar */
	public String resolve(VarResolverSession session, String[] args) {
		if (args.length < 3)
			illegalArg("Invalid number of arguments passed to $PE var.  Must have 3 arguments.");

		String stringArg = args[0];
		String pattern = args[1];
		String result = "";
		int groupId = Integer.parseInt(args[2]);

		Pattern p = Pattern.compile(pattern.replace("*", ".*").replace("?", "."));
		Matcher m = p.matcher(stringArg);

		if (m.find() && groupId <= m.groupCount() && groupId >= 0) {
			result = m.group(groupId);
		}

		return result;
	}
}
