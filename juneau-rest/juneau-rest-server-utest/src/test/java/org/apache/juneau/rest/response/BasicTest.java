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
package org.apache.juneau.rest.response;

import static org.junit.Assert.*;
import static org.apache.juneau.rest.testutils.TestUtils.*;

import java.net.*;


import org.apache.juneau.dto.swagger.*;
import org.apache.juneau.json.*;
import org.apache.juneau.rest.annotation.*;
import org.apache.juneau.rest.mock2.*;
import org.junit.*;
import org.junit.runners.*;

@SuppressWarnings({})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BasicTest {

	//-----------------------------------------------------------------------------------------------------------------
	// Basic sanity tests
	//-----------------------------------------------------------------------------------------------------------------

	@RestResource
	public static class A {
		@RestMethod public Accepted accepted() { return new Accepted(); }
		@RestMethod public AlreadyReported alreadyReported() { return new AlreadyReported(); }
		@RestMethod(path="/continue") public Continue _continue() { return new Continue(); }
		@RestMethod public Created created() { return new Created(); }
		@RestMethod public EarlyHints earlyHints() { return new EarlyHints(); }
		@RestMethod public Found found() { return new Found(); }
		@RestMethod public IMUsed imUsed() { return new IMUsed(); }
		@RestMethod public MovedPermanently movedPermanently() { return new MovedPermanently(); }
		@RestMethod public MultipleChoices multipleChoices() { return new MultipleChoices(); }
		@RestMethod public MultiStatus multiStatus() { return new MultiStatus(); }
		@RestMethod public NoContent noContent() { return new NoContent(); }
		@RestMethod public NonAuthoritiveInformation nonAuthoritiveInformation() { return new NonAuthoritiveInformation(); }
		@RestMethod public NotModified notModified() { return new NotModified(); }
		@RestMethod public Ok ok() { return new Ok(); }
		@RestMethod public PartialContent partialContent() { return new PartialContent(); }
		@RestMethod public PermanentRedirect permanentRedirect() { return new PermanentRedirect(); }
		@RestMethod public Processing processing() { return new Processing(); }
		@RestMethod public ResetContent resetContent() { return new ResetContent(); }
		@RestMethod public SeeOther seeOther() { return new SeeOther(); }
		@RestMethod public SwitchingProtocols switchingProtocols() { return new SwitchingProtocols(); }
		@RestMethod public TemporaryRedirect temporaryRedirect() { return new TemporaryRedirect(); }
		@RestMethod public UseProxy useProxy() { return new UseProxy(); }
	}

	static MockRest a = MockRest.build(A.class, null);

	@Test
	public void a01_accepted() throws Exception {
		a.get("/accepted").execute().assertStatus(202).assertBody("Accepted");
	}
	@Test
	public void a02_alreadyReported() throws Exception {
		a.get("/alreadyReported").execute().assertStatus(208).assertBody("Already Reported");
	}
	@Test
	public void a03_continue() throws Exception {
		a.get("/continue").execute().assertStatus(100).assertBody("Continue");
	}
	@Test
	public void a04_created() throws Exception {
		a.get("/created").execute().assertStatus(201).assertBody("Created");
	}
	@Test
	public void a05_earlyHints() throws Exception {
		a.get("/earlyHints").execute().assertStatus(103).assertBody("Early Hints");
	}
	@Test
	public void a06_found() throws Exception {
		a.get("/found").execute().assertStatus(302).assertBody("Found");
	}
	@Test
	public void a07_imUsed() throws Exception {
		a.get("/imUsed").execute().assertStatus(226).assertBody("IM Used");
	}
	@Test
	public void a08_movedPermanently() throws Exception {
		a.get("/movedPermanently").execute().assertStatus(301).assertBody("Moved Permanently");
	}
	@Test
	public void a09_multipleChoices() throws Exception {
		a.get("/multipleChoices").execute().assertStatus(300).assertBody("Multiple Choices");
	}
	@Test
	public void a10_multiStatus() throws Exception {
		a.get("/multiStatus").execute().assertStatus(207).assertBody("Multi-Status");
	}
	@Test
	public void a11_noContent() throws Exception {
		a.get("/noContent").execute().assertStatus(204).assertBody("No Content");
	}
	@Test
	public void a12_nonAuthoritiveInformation() throws Exception {
		a.get("/nonAuthoritiveInformation").execute().assertStatus(203).assertBody("Non-Authoritative Information");
	}
	@Test
	public void a13_notModified() throws Exception {
		a.get("/notModified").execute().assertStatus(304).assertBody("Not Modified");
	}
	@Test
	public void a14_ok() throws Exception {
		a.get("/ok").execute().assertStatus(200).assertBody("OK");
	}
	@Test
	public void a15_partialContent() throws Exception {
		a.get("/partialContent").execute().assertStatus(206).assertBody("Partial Content");
	}
	@Test
	public void a16_permanentRedirect() throws Exception {
		a.get("/permanentRedirect").execute().assertStatus(308).assertBody("Permanent Redirect");
	}
	@Test
	public void a17_processing() throws Exception {
		a.get("/processing").execute().assertStatus(102).assertBody("Processing");
	}
	@Test
	public void a18_resetContent() throws Exception {
		a.get("/resetContent").execute().assertStatus(205).assertBody("Reset Content");
	}
	@Test
	public void a19_seeOther() throws Exception {
		a.get("/seeOther").execute().assertStatus(303).assertBody("See Other");
	}
	@Test
	public void a20_switchingProtocols() throws Exception {
		a.get("/switchingProtocols").execute().assertStatus(101).assertBody("Switching Protocols");
	}
	@Test
	public void a21_temporaryRedirect() throws Exception {
		a.get("/temporaryRedirect").execute().assertStatus(307).assertBody("Temporary Redirect");
	}
	@Test
	public void a22_useProxy() throws Exception {
		a.get("/useProxy").execute().assertStatus(305).assertBody("Use Proxy");
	}

	//-----------------------------------------------------------------------------------------------------------------
	// Statuses with URIs.
	//-----------------------------------------------------------------------------------------------------------------

	@RestResource
	public static class B {
		@RestMethod public MovedPermanently movedPermanently() { return new MovedPermanently(URI.create("servlet:/foo")); }
		@RestMethod public PermanentRedirect permanentRedirect() { return new PermanentRedirect(URI.create("servlet:/foo")); }
		@RestMethod public SeeOther seeOther() { return new SeeOther(URI.create("servlet:/foo")); }
		@RestMethod public TemporaryRedirect temporaryRedirect() { return new TemporaryRedirect(URI.create("servlet:/foo")); }
	}

	static MockRest b = MockRest.build(B.class, null);

	@Test
	public void b01_movedPermanently() throws Exception {
		b.get("/movedPermanently").execute().assertStatus(301).assertBody("Moved Permanently").assertHeader("Location", "/foo");
	}
	@Test
	public void b02_permanentRedirect() throws Exception {
		b.get("/permanentRedirect").execute().assertStatus(308).assertBody("Permanent Redirect").assertHeader("Location", "/foo");
	}
	@Test
	public void b03_seeOther() throws Exception {
		b.get("/seeOther").execute().assertStatus(303).assertBody("See Other").assertHeader("Location", "/foo");
	}
	@Test
	public void b04_temporaryRedirect() throws Exception {
		b.get("/temporaryRedirect").execute().assertStatus(307).assertBody("Temporary Redirect").assertHeader("Location", "/foo");
	}

	//-----------------------------------------------------------------------------------------------------------------
	// Overridden messages
	//-----------------------------------------------------------------------------------------------------------------

	@RestResource
	public static class C {
		@RestMethod public Accepted accepted() { return new Accepted("foo"); }
		@RestMethod public AlreadyReported alreadyReported() { return new AlreadyReported("foo"); }
		@RestMethod(path="/continue") public Continue _continue() { return new Continue("foo"); }
		@RestMethod public Created created() { return new Created("foo"); }
		@RestMethod public EarlyHints earlyHints() { return new EarlyHints("foo"); }
		@RestMethod public Found found() { return new Found("foo", null); }
		@RestMethod public IMUsed imUsed() { return new IMUsed("foo"); }
		@RestMethod public MovedPermanently movedPermanently() { return new MovedPermanently("foo", null); }
		@RestMethod public MultipleChoices multipleChoices() { return new MultipleChoices("foo"); }
		@RestMethod public MultiStatus multiStatus() { return new MultiStatus("foo"); }
		@RestMethod public NoContent noContent() { return new NoContent("foo"); }
		@RestMethod public NonAuthoritiveInformation nonAuthoritiveInformation() { return new NonAuthoritiveInformation("foo"); }
		@RestMethod public NotModified notModified() { return new NotModified("foo"); }
		@RestMethod public Ok ok() { return new Ok("foo"); }
		@RestMethod public PartialContent partialContent() { return new PartialContent("foo"); }
		@RestMethod public PermanentRedirect permanentRedirect() { return new PermanentRedirect("foo", null); }
		@RestMethod public Processing processing() { return new Processing("foo"); }
		@RestMethod public ResetContent resetContent() { return new ResetContent("foo"); }
		@RestMethod public SeeOther seeOther() { return new SeeOther("foo", null); }
		@RestMethod public SwitchingProtocols switchingProtocols() { return new SwitchingProtocols("foo"); }
		@RestMethod public TemporaryRedirect temporaryRedirect() { return new TemporaryRedirect("foo", null); }
		@RestMethod public UseProxy useProxy() { return new UseProxy("foo"); }
	}

	static MockRest c = MockRest.build(C.class, null);

	@Test
	public void c01_accepted() throws Exception {
		c.get("/accepted").execute().assertStatus(202).assertBody("foo");
	}
	@Test
	public void c02_alreadyReported() throws Exception {
		c.get("/alreadyReported").execute().assertStatus(208).assertBody("foo");
	}
	@Test
	public void c03_continue() throws Exception {
		c.get("/continue").execute().assertStatus(100).assertBody("foo");
	}
	@Test
	public void c04_created() throws Exception {
		c.get("/created").execute().assertStatus(201).assertBody("foo");
	}
	@Test
	public void c05_earlyHints() throws Exception {
		c.get("/earlyHints").execute().assertStatus(103).assertBody("foo");
	}
	@Test
	public void c06_found() throws Exception {
		c.get("/found").execute().assertStatus(302).assertBody("foo");
	}
	@Test
	public void c07_imUsed() throws Exception {
		c.get("/imUsed").execute().assertStatus(226).assertBody("foo");
	}
	@Test
	public void c08_movedPermanently() throws Exception {
		c.get("/movedPermanently").execute().assertStatus(301).assertBody("foo");
	}
	@Test
	public void c09_multipleChoices() throws Exception {
		c.get("/multipleChoices").execute().assertStatus(300).assertBody("foo");
	}
	@Test
	public void c10_multiStatus() throws Exception {
		c.get("/multiStatus").execute().assertStatus(207).assertBody("foo");
	}
	@Test
	public void c11_noContent() throws Exception {
		c.get("/noContent").execute().assertStatus(204).assertBody("foo");
	}
	@Test
	public void c12_nonAuthoritiveInformation() throws Exception {
		c.get("/nonAuthoritiveInformation").execute().assertStatus(203).assertBody("foo");
	}
	@Test
	public void c13_notModified() throws Exception {
		c.get("/notModified").execute().assertStatus(304).assertBody("foo");
	}
	@Test
	public void c14_ok() throws Exception {
		c.get("/ok").execute().assertStatus(200).assertBody("foo");
	}
	@Test
	public void c15_partialContent() throws Exception {
		c.get("/partialContent").execute().assertStatus(206).assertBody("foo");
	}
	@Test
	public void c16_permanentRedirect() throws Exception {
		c.get("/permanentRedirect").execute().assertStatus(308).assertBody("foo");
	}
	@Test
	public void c17_processing() throws Exception {
		c.get("/processing").execute().assertStatus(102).assertBody("foo");
	}
	@Test
	public void c18_resetContent() throws Exception {
		c.get("/resetContent").execute().assertStatus(205).assertBody("foo");
	}
	@Test
	public void c19_seeOther() throws Exception {
		c.get("/seeOther").execute().assertStatus(303).assertBody("foo");
	}
	@Test
	public void c20_switchingProtocols() throws Exception {
		c.get("/switchingProtocols").execute().assertStatus(101).assertBody("foo");
	}
	@Test
	public void c21_temporaryRedirect() throws Exception {
		c.get("/temporaryRedirect").execute().assertStatus(307).assertBody("foo");
	}
	@Test
	public void c22_useProxy() throws Exception {
		c.get("/useProxy").execute().assertStatus(305).assertBody("foo");
	}

	//-----------------------------------------------------------------------------------------------------------------
	// Should use Accept language for serialization.
	//-----------------------------------------------------------------------------------------------------------------

	@RestResource(serializers=SimpleJsonSerializer.class)
	public static class D {
		@RestMethod public Accepted accepted() { return new Accepted("foo"); }
	}

	static MockRest d = MockRest.build(D.class);

	@Test
	public void d01_accepted() throws Exception {
		d.get("/accepted").json().execute().assertStatus(202).assertBody("'foo'");
	}

	//-----------------------------------------------------------------------------------------------------------------
	// Test Swagger
	//-----------------------------------------------------------------------------------------------------------------

	static Swagger e = getSwagger(A.class);

	@Test
	public void e01_accepted() throws Exception {
		ResponseInfo ri = e.getPaths().get("/accepted").get("get").getResponse(Accepted.CODE);
		assertEquals(Accepted.MESSAGE, ri.getDescription());
	}
	@Test
	public void e02_alreadyReported() throws Exception {
		ResponseInfo ri = e.getPaths().get("/alreadyReported").get("get").getResponse(AlreadyReported.CODE);
		assertEquals(AlreadyReported.MESSAGE, ri.getDescription());
	}
	@Test
	public void e03_continue() throws Exception {
		ResponseInfo ri = e.getPaths().get("/continue").get("get").getResponse(Continue.CODE);
		assertEquals(Continue.MESSAGE, ri.getDescription());
	}
	@Test
	public void e04_created() throws Exception {
		ResponseInfo ri = e.getPaths().get("/created").get("get").getResponse(Created.CODE);
		assertEquals(Created.MESSAGE, ri.getDescription());
	}
	@Test
	public void e05_earlyHints() throws Exception {
		ResponseInfo ri = e.getPaths().get("/earlyHints").get("get").getResponse(EarlyHints.CODE);
		assertEquals(EarlyHints.MESSAGE, ri.getDescription());
	}
	@Test
	public void e06_found() throws Exception {
		ResponseInfo ri = e.getPaths().get("/found").get("get").getResponse(Found.CODE);
		assertEquals(Found.MESSAGE, ri.getDescription());
	}
	@Test
	public void e07_imUsed() throws Exception {
		ResponseInfo ri = e.getPaths().get("/imUsed").get("get").getResponse(IMUsed.CODE);
		assertEquals(IMUsed.MESSAGE, ri.getDescription());
	}
	@Test
	public void e08_movedPermanently() throws Exception {
		ResponseInfo ri = e.getPaths().get("/movedPermanently").get("get").getResponse(MovedPermanently.CODE);
		assertEquals(MovedPermanently.MESSAGE, ri.getDescription());
	}
	@Test
	public void e09_multipleChoices() throws Exception {
		ResponseInfo ri = e.getPaths().get("/multipleChoices").get("get").getResponse(MultipleChoices.CODE);
		assertEquals(MultipleChoices.MESSAGE, ri.getDescription());
	}
	@Test
	public void e10_multiStatus() throws Exception {
		ResponseInfo ri = e.getPaths().get("/multiStatus").get("get").getResponse(MultiStatus.CODE);
		assertEquals(MultiStatus.MESSAGE, ri.getDescription());
	}
	@Test
	public void e11_noContent() throws Exception {
		ResponseInfo ri = e.getPaths().get("/noContent").get("get").getResponse(NoContent.CODE);
		assertEquals(NoContent.MESSAGE, ri.getDescription());
	}
	@Test
	public void e12_nonAuthoritiveInformation() throws Exception {
		ResponseInfo ri = e.getPaths().get("/nonAuthoritiveInformation").get("get").getResponse(NonAuthoritiveInformation.CODE);
		assertEquals(NonAuthoritiveInformation.MESSAGE, ri.getDescription());
	}
	@Test
	public void e13_notModified() throws Exception {
		ResponseInfo ri = e.getPaths().get("/notModified").get("get").getResponse(NotModified.CODE);
		assertEquals(NotModified.MESSAGE, ri.getDescription());
	}
	@Test
	public void e14_ok() throws Exception {
		ResponseInfo ri = e.getPaths().get("/ok").get("get").getResponse(Ok.CODE);
		assertEquals(Ok.MESSAGE, ri.getDescription());
	}
	@Test
	public void e15_partialContent() throws Exception {
		ResponseInfo ri = e.getPaths().get("/partialContent").get("get").getResponse(PartialContent.CODE);
		assertEquals(PartialContent.MESSAGE, ri.getDescription());
	}
	@Test
	public void e16_permanentRedirect() throws Exception {
		ResponseInfo ri = e.getPaths().get("/permanentRedirect").get("get").getResponse(PermanentRedirect.CODE);
		assertEquals(PermanentRedirect.MESSAGE, ri.getDescription());
	}
	@Test
	public void e17_processing() throws Exception {
		ResponseInfo ri = e.getPaths().get("/processing").get("get").getResponse(Processing.CODE);
		assertEquals(Processing.MESSAGE, ri.getDescription());
	}
	@Test
	public void e18_resetContent() throws Exception {
		ResponseInfo ri = e.getPaths().get("/resetContent").get("get").getResponse(ResetContent.CODE);
		assertEquals(ResetContent.MESSAGE, ri.getDescription());
	}
	@Test
	public void e19_seeOther() throws Exception {
		ResponseInfo ri = e.getPaths().get("/seeOther").get("get").getResponse(SeeOther.CODE);
		assertEquals(SeeOther.MESSAGE, ri.getDescription());
	}
	@Test
	public void e20_switchingProtocols() throws Exception {
		ResponseInfo ri = e.getPaths().get("/switchingProtocols").get("get").getResponse(SwitchingProtocols.CODE);
		assertEquals(SwitchingProtocols.MESSAGE, ri.getDescription());
	}
	@Test
	public void e21_temporaryRedirect() throws Exception {
		ResponseInfo ri = e.getPaths().get("/temporaryRedirect").get("get").getResponse(TemporaryRedirect.CODE);
		assertEquals(TemporaryRedirect.MESSAGE, ri.getDescription());
	}
	@Test
	public void e22_useProxy() throws Exception {
		ResponseInfo ri = e.getPaths().get("/useProxy").get("get").getResponse(UseProxy.CODE);
		assertEquals(UseProxy.MESSAGE, ri.getDescription());
	}
}
