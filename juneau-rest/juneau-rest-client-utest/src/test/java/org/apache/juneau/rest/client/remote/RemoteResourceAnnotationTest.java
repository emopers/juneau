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
package org.apache.juneau.rest.client.remote;

import static org.junit.Assert.*;

import org.apache.juneau.rest.annotation.*;
import org.apache.juneau.rest.client.*;
import org.apache.juneau.rest.mock2.*;
import org.apache.juneau.rest.mock2.MockRemoteResource;
import org.junit.*;
import org.junit.runners.*;

/**
 * Tests the @RemoteResource annotation.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RemoteResourceAnnotationTest {

	//=================================================================================================================
	// @RemoteResource(path), relative paths
	//=================================================================================================================

	@RestResource
	public static class A {

		@RestMethod
		public String a01() {
			return "foo";
		}

		@RestMethod(path="/A/a02")
		public String a02() {
			return "foo";
		}

		@RestMethod(path="/A/A/a03")
		public String a03() {
			return "foo";
		}
	}

	@RemoteResource
	public static interface A01a {
		@RemoteMethod
		public String a01();
		@RemoteMethod(path="a01")
		public String a01a();
		@RemoteMethod(path="/a01/")
		public String a01b();
	}

	@Test
	public void a01_noPath() throws Exception {
		A01a t = MockRemoteResource.build(A01a.class, A.class, null);
		assertEquals("foo", t.a01());
		assertEquals("foo", t.a01a());
		assertEquals("foo", t.a01b());
	}

	@RemoteResource(path="A")
	public static interface A02a {
		@RemoteMethod
		public String a02();
		@RemoteMethod(path="a02")
		public String a02a();
		@RemoteMethod(path="/a02/")
		public String a02b();
	}

	@Test
	public void a02a_normalPath() throws Exception {
		A02a t = MockRemoteResource.build(A02a.class, A.class, null);
		assertEquals("foo", t.a02());
		assertEquals("foo", t.a02a());
		assertEquals("foo", t.a02b());
	}

	@RemoteResource(path="/A/")
	public static interface A02b {
		@RemoteMethod
		public String a02();
		@RemoteMethod(path="a02")
		public String a02a();
		@RemoteMethod(path="/a02/")
		public String a02b();
	}

	@Test
	public void a02b_normalPathWithSlashes() throws Exception {
		A02b t = MockRemoteResource.build(A02b.class, A.class, null);
		assertEquals("foo", t.a02());
		assertEquals("foo", t.a02a());
		assertEquals("foo", t.a02b());
	}

	@RemoteResource
	public static interface A02c {
		@RemoteMethod
		public String a02();
		@RemoteMethod(path="a02")
		public String a02a();
		@RemoteMethod(path="/a02/")
		public String a02b();
	}

	@Test
	public void a02c_pathOnClient() throws Exception {
		try (RestClient rc = MockRestClient.create(A.class, null).rootUrl("http://localhost/A").build()) {
			A02c t = rc.getRemoteResource(A02c.class);
			assertEquals("foo", t.a02());
			assertEquals("foo", t.a02a());
			assertEquals("foo", t.a02b());
		}
	}

	@RemoteResource(path="A/A")
	public static interface A03a {
		@RemoteMethod
		public String a03();
		@RemoteMethod(path="a03")
		public String a03a();
		@RemoteMethod(path="/a03/")
		public String a03b();
	}

	@Test
	public void a03a_normalPath() throws Exception {
		A03a t = MockRemoteResource.build(A03a.class, A.class, null);
		assertEquals("foo", t.a03());
		assertEquals("foo", t.a03a());
		assertEquals("foo", t.a03b());
	}

	@RemoteResource(path="/A/A/")
	public static interface A03b {
		@RemoteMethod
		public String a03();
		@RemoteMethod(path="a03")
		public String a03a();
		@RemoteMethod(path="/a03/")
		public String a03b();
	}

	@Test
	public void a03b_normalPathWithSlashes() throws Exception {
		A03b t = MockRemoteResource.build(A03b.class, A.class, null);
		assertEquals("foo", t.a03());
		assertEquals("foo", t.a03a());
		assertEquals("foo", t.a03b());
	}

	@RemoteResource(path="A")
	public static interface A03c {
		@RemoteMethod
		public String a03();
		@RemoteMethod(path="a03")
		public String a03a();
		@RemoteMethod(path="/a03/")
		public String a03b();
	}

	@Test
	public void a03c_partialPath() throws Exception {
		try (RestClient rc = MockRestClient.create(A.class, null).rootUrl("http://localhost/A").build()) {
			A03c t = rc.getRemoteResource(A03c.class);
			assertEquals("foo", t.a03());
			assertEquals("foo", t.a03a());
			assertEquals("foo", t.a03b());
		}
	}

	@RemoteResource(path="/A/")
	public static interface A03d {
		@RemoteMethod
		public String a03();
		@RemoteMethod(path="a03")
		public String a03a();
		@RemoteMethod(path="/a03/")
		public String a03b();
	}

	@Test
	public void a03d_partialPathExtraSlashes() throws Exception {
		try (RestClient rc = MockRestClient.create(A.class, null).rootUrl("http://localhost/A/").build()) {
			A03d t = rc.getRemoteResource(A03d.class);
			assertEquals("foo", t.a03());
			assertEquals("foo", t.a03a());
			assertEquals("foo", t.a03b());
		}
	}

	//=================================================================================================================
	// @RemoteResource(path), absolute paths
	//=================================================================================================================

	@RestResource
	public static class B {

		@RestMethod(path="B/b01")
		public String b01() {
			return "foo";
		}

		@RestMethod(path="/B/b02")
		public String b02() {
			return "foo";
		}

		@RestMethod(path="/B/b03")
		public String b03() {
			return "foo";
		}
	}
	private static RestClient rb = MockRestClient.create(B.class, null).rootUrl("http://localhost/B").build();

	@RemoteResource
	public static interface B01 {
		@RemoteMethod
		public String b01();
		@RemoteMethod(path="b01")
		public String b01a();
		@RemoteMethod(path="/b01/")
		public String b01b();
	}

	@Test
	public void b01_noPath() throws Exception {
		B01 t = rb.getRemoteResource(B01.class);
		assertEquals("foo", t.b01());
		assertEquals("foo", t.b01a());
		assertEquals("foo", t.b01b());
	}

	@RemoteResource(path="http://localhost/B")
	public static interface B02 {
		@RemoteMethod
		public String b01();
		@RemoteMethod(path="b01")
		public String b01a();
		@RemoteMethod(path="/b01/")
		public String b01b();
	}

	@Test
	public void b02_absolutePathOnClass() throws Exception {
		B02 t = rb.getRemoteResource(B02.class);
		assertEquals("foo", t.b01());
		assertEquals("foo", t.b01a());
		assertEquals("foo", t.b01b());
	}

	@RemoteResource
	public static interface B03 {
		@RemoteMethod
		public String b01();
		@RemoteMethod(path="http://localhost/B/b01")
		public String b01a();
		@RemoteMethod(path="http://localhost/B/b01/")
		public String b01b();
	}

	@Test
	public void b03_absolutePathsOnMethods() throws Exception {
		B03 t = rb.getRemoteResource(B03.class);
		assertEquals("foo", t.b01());
		assertEquals("foo", t.b01a());
		assertEquals("foo", t.b01b());
	}
}
