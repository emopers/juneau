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
package org.apache.juneau.svl;

import static org.apache.juneau.internal.StringUtils.*;

/**
 * Interface for the resolution of vars with a default value if the <c>resolve()</c> method returns <jk>null</jk>.
 *
 * <p>
 * For example, to resolve the system property <js>"myProperty"</js> but resolve to <js>"not found"</js> if the
 * property doesn't exist: <js>"$S{myProperty,not found}"</js>
 *
 * <p>
 * Subclasses must implement the following method:
 * <ul class='javatree'>
 * 	<li class='jm'>{@link #resolve(VarResolverSession, String)}
 * </ul>
 *
 * <ul class='seealso'>
 * 	<li class='link'>{@doc juneau-svl.SvlVariables}
 * </ul>
 */
public abstract class DefaultingVar extends SimpleVar {

	/**
	 * Constructor.
	 *
	 * @param name The name of this variable.
	 */
	public DefaultingVar(String name) {
		super(name);
	}

	@Override /* Var*/
	public String doResolve(VarResolverSession session, String s) throws Exception {
		int i = s.indexOf(',');
		if (i == -1)
			return resolve(session, s.trim());
		String[] s2 = split(s);
		String v = resolve(session, s2[0]);
		if (v == null)
			v = s2[1];
		return v;
	}
}
