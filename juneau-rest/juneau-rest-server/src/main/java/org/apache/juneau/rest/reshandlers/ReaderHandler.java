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
package org.apache.juneau.rest.reshandlers;

import java.io.*;

import org.apache.juneau.rest.*;
import org.apache.juneau.rest.exception.*;
import org.apache.juneau.utils.*;

/**
 * Response handler for {@link Reader} objects.
 *
 * <p>
 * Simply pipes the contents of the {@link Reader} to {@link RestResponse#getNegotiatedWriter()}.
 *
 * <ul class='seealso'>
 * 	<li class='link'>{@doc juneau-rest-server.RestMethod.MethodReturnTypes}
 * </ul>
 */
public final class ReaderHandler implements ResponseHandler {

	@Override /* ResponseHandler */
	public boolean handle(RestRequest req, RestResponse res) throws IOException, NotAcceptable, RestException {
		if (res.isOutputType(Reader.class)) {
			try (Writer w = res.getNegotiatedWriter()) {
				IOPipe.create(res.getOutput(Reader.class), w).run();
			}
			return true;
		}
		return false;
	}
}

