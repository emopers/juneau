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
package org.apache.juneau.microservice.resources;

import static org.apache.juneau.http.HttpMethodName.*;

import org.apache.juneau.rest.*;
import org.apache.juneau.rest.annotation.*;

/**
 * Provides the capability to shut down this REST microservice through a REST call.
 */
@RestResource(
	path="/shutdown",
	title="Shut down this resource"
)
public class ShutdownResource extends BasicRestServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * [GET /] - Shutdown this resource.
	 *
	 * @return The string <js>"OK"</js>.
	 */
	@RestMethod(name=GET, path="/", description="Show contents of config file.")
	public String shutdown() {
		new Thread(
			new Runnable() {
				@Override /* Runnable */
				public void run() {
					try {
						Thread.sleep(1000);
						System.exit(0);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		).start();
		return "OK";
	}
}
