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
package org.apache.juneau.examples.rest.petstore.dto;

import org.apache.juneau.*;
import org.apache.juneau.http.*;
import org.apache.juneau.transform.*;

/**
 * Swap for {@link PetTag} beans.
 *
 * <ul class='seealso'>
 * 	<li class='extlink'>{@source}
 * </ul>
 */
public class PetTagNameSwap extends PojoSwap<PetTag,String> {

	/**
	 * Swap PetTag with name.
	 */
	@Override
	public String swap(BeanSession bs, PetTag o) throws Exception {
		return o.getName();
	}

	/**
	 * This is only applicable to HTML serialization.
	 */
	@Override
	public MediaType[] forMediaTypes() {
		return new MediaType[] { MediaType.HTML };
	}
}