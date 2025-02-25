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
package org.apache.juneau.transform;

import static org.junit.Assert.*;
import static org.apache.juneau.testutils.TestUtils.*;

import java.util.*;

import org.apache.juneau.*;
import org.apache.juneau.annotation.*;
import org.apache.juneau.parser.*;
import org.apache.juneau.reflect.*;
import org.apache.juneau.serializer.*;
import org.apache.juneau.utils.*;
import org.junit.*;
import org.junit.runners.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({"unchecked","rawtypes"})
public class AutoListSwapTest {

	private static final List<String> STRINGLIST = AList.create("foo");
	private static final ObjectList OBJECTLIST = new ObjectList().append("foo");

	private static PojoSwap find(Class<?> c) {
		return AutoListSwap.find(ClassInfo.of(c));
	}

	//------------------------------------------------------------------------------------------------------------------
	// Swap methods
	//------------------------------------------------------------------------------------------------------------------

	public static class A01 {
		public List<String> toList() {
			return STRINGLIST;
		}
	}
	public static class A02 {
		public ObjectList toObjectList() {
			return OBJECTLIST;
		}
	}
	public static class A03 {
		public ObjectList toObjectList() throws SerializeException {
			throw new SerializeException("foo");
		}
	}
	public static class A04 {
		public ObjectList toObjectList() {
			throw new RuntimeException("foo");
		}
	}

	@Test
	public void a01_swap_toList() throws Exception {
		assertObjectEquals("['foo']", find(A01.class).swap(null, new A01()));
	}

	@Test
	public void a02_swap_toObjectList() throws Exception {
		assertObjectEquals("['foo']", find(A02.class).swap(null, new A02()));
	}

	@Test(expected = SerializeException.class)
	public void a03_swap_serializeException() throws Exception {
		find(A03.class).swap(null, null);
	}

	@Test(expected = SerializeException.class)
	public void a04_swap_runtimeException() throws Exception {
		find(A04.class).swap(null, null);
	}

	//------------------------------------------------------------------------------------------------------------------
	// Unswap methods
	//------------------------------------------------------------------------------------------------------------------

	public static class B01 {
		public List<String> toList() {
			return STRINGLIST;
		}
		public static B01 fromList(List<String> o) {
			assertObjectEquals("['foo']", o);
			return new B01();
		}
	}
	public static class B02 {
		public ObjectList toObjectList() {
			return OBJECTLIST;
		}
		public static B02 fromObjectList(ObjectList o) {
			assertObjectEquals("['foo']", o);
			return new B02();
		}
	}
	public static class B03 {
		public List<String> toList() {
			return STRINGLIST;
		}
		public static B03 create(List<String> o) {
			assertObjectEquals("['foo']", o);
			return new B03();
		}
	}
	public static class B04 {
		public List<String> toList() {
			return STRINGLIST;
		}
	}

	@Test
	public void b01_unswap_fromList() throws Exception {
		assertNotNull(find(B01.class).unswap(null, STRINGLIST, null));
	}

	@Test
	public void b02_unswap_fromObjectList() throws Exception {
		assertNotNull(find(B02.class).unswap(null, OBJECTLIST, null));
	}

	@Test
	public void b03_unswap_create() throws Exception {
		assertNotNull(find(B03.class).unswap(null, STRINGLIST, null));
	}

	@Test(expected = ParseException.class)
	public void b04_unswap_noMethod() throws Exception {
		find(B04.class).unswap(null, STRINGLIST, null);
	}

	//------------------------------------------------------------------------------------------------------------------
	// Unswap constructor
	//------------------------------------------------------------------------------------------------------------------

	public static class C01 {
		public C01() {}
		public C01(List<String> o) {
			assertObjectEquals("['foo']", o);
		}
		public List<String> toList() {
			return STRINGLIST;
		}
	}

	@Test
	public void c01_unswap_constructor() throws Exception {
		assertNotNull(find(C01.class).unswap(null, STRINGLIST, null));
	}

	//------------------------------------------------------------------------------------------------------------------
	// Ignore class
	//------------------------------------------------------------------------------------------------------------------

	@BeanIgnore
	public static class D01 {
		public List<String> toList() {
			return STRINGLIST;
		}
	}
	public static class D02 {
		public class D02A {
			public List<String> toList() {
				return STRINGLIST;
			}
		}
	}

	@Test
	public void d01_ignoreClass_beanIgnore() throws Exception {
		assertNull(find(D01.class));
	}

	@Test
	public void d02_ignoreClass_memberClass() throws Exception {
		assertNull(find(D02.D02A.class));
	}

	//------------------------------------------------------------------------------------------------------------------
	// Ignore swap method
	//------------------------------------------------------------------------------------------------------------------

	public static class E01 {
		@BeanIgnore
		public List<String> toList() {
			return STRINGLIST;
		}
	}
	public static class E02 {
		@Deprecated
		public List<String> toList() {
			return STRINGLIST;
		}
	}
	public static class E03 {
		public Object toList() {
			return STRINGLIST;
		}
	}
	public static class E04 {
		public List<String> toList(List<String> foo) {
			return STRINGLIST;
		}
	}
	public static class E05 {
		public static List<String> toList() {
			return STRINGLIST;
		}
	}

	@Test
	public void e01_ignoreSwapMethod_beanIgnore() throws Exception {
		assertNull(find(E01.class));
	}

	@Test
	public void e02_ignoreSwapMethod_deprecated() throws Exception {
		assertNull(find(E02.class));
	}

	@Test
	public void e03_ignoreSwapMethod_wrongReturnType() throws Exception {
		assertNull(find(E03.class));
	}

	@Test
	public void e04_ignoreSwapMethod_wrongParameters() throws Exception {
		assertNull(find(E04.class));
	}

	@Test
	public void e05_ignoreSwapMethod_notStatic() throws Exception {
		assertNull(find(E05.class));
	}

	//------------------------------------------------------------------------------------------------------------------
	// Ignore unswap method
	//------------------------------------------------------------------------------------------------------------------

	public static class F01 {
		public List<String> toList() {
			return STRINGLIST;
		}
		@BeanIgnore
		public static F01 create(List<String> o) {
			return null;
		}
	}
	public static class F02 {
		public List<String> toList() {
			return STRINGLIST;
		}
		@Deprecated
		public static F02 create(List<String> o) {
			return null;
		}
	}
	public static class F03 {
		public List<String> toList() {
			return STRINGLIST;
		}
		public static Object create(List<String> o) {
			return null;
		}
	}
	public static class F04 {
		public List<String> toList() {
			return STRINGLIST;
		}
		public static F04 create(Map<String,String> o) {
			return null;
		}
	}
	public static class F05 {
		public List<String> toList() {
			return STRINGLIST;
		}
		public F05 create(List<String> o) {
			return null;
		}
	}
	public static class F06 {
		public List<String> toList() {
			return STRINGLIST;
		}
		public static F06 createx(List<String> o) {
			return null;
		}
	}

	@Test(expected = ParseException.class)
	public void f01_ignoreUnswapMethod_beanIgnore() throws Exception {
		find(F01.class).unswap(null, null, null);
	}

	@Test(expected = ParseException.class)
	public void f02_ignoreUnswapMethod_deprecated() throws Exception {
		find(F02.class).unswap(null, null, null);
	}

	@Test(expected = ParseException.class)
	public void f03_ignoreUnswapMethod_wrongReturnType() throws Exception {
		find(F03.class).unswap(null, null, null);
	}

	@Test(expected = ParseException.class)
	public void f04_ignoreUnswapMethod_wrongParameters() throws Exception {
		find(F04.class).unswap(null, null, null);
	}

	@Test(expected = ParseException.class)
	public void f05_ignoreUnswapMethod_notStatic() throws Exception {
		find(F05.class).unswap(null, null, null);
	}

	@Test(expected = ParseException.class)
	public void f06_ignoreUnswapMethod_wrongName() throws Exception {
		find(F06.class).unswap(null, null, null);
	}

	//------------------------------------------------------------------------------------------------------------------
	// Ignore constructor
	//------------------------------------------------------------------------------------------------------------------

	public static class G01 {
		@BeanIgnore
		public G01(List<String> o) {}
		public List<String> toList() {
			return STRINGLIST;
		}
	}

	public static class G02 {
		@Deprecated
		public G02(List<String> o) {}
		public List<String> toList() {
			return STRINGLIST;
		}
	}

	@Test(expected = ParseException.class)
	public void g01_ignoreUnswapConstructor_beanIgnore() throws Exception {
		find(G01.class).unswap(null, null, null);
	}

	@Test(expected = ParseException.class)
	public void g02_ignoreUnswapConstructor_deprecated() throws Exception {
		find(G02.class).unswap(null, null, null);
	}
}
