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
package org.apache.juneau.xml;

import javax.xml.stream.*;
import javax.xml.stream.util.*;

import org.apache.juneau.*;
import org.apache.juneau.parser.*;

/**
 * Parses text generated by the {@link XmlSerializer} class back into a POJO model.
 *
 * <h5 class='section'>Media types:</h5>
 *
 * Handles <code>Content-Type</code> types: <code>text/xml</code>
 *
 * <h5 class='section'>Description:</h5>
 *
 * See the {@link XmlSerializer} class for a description of Juneau-generated XML.
 */
public class XmlParser extends ReaderParser {

	//-------------------------------------------------------------------------------------------------------------------
	// Configurable properties
	//-------------------------------------------------------------------------------------------------------------------

	private static final String PREFIX = "XmlParser.";

	/**
	 * Configuration property:  XML event allocator.
	 *
	 *	<h5 class='section'>Property:</h5>
	 * <ul>
	 * 	<li><b>Name:</b>  <js>"XmlParser.eventAllocator.c"</js>
	 * 	<li><b>Data type:</b>  <code>Class&lt;? <jk>extends</jk> {@link XMLEventAllocator}&gt;</code>
	 * 	<li><b>Default:</b>  <jk>null</jk>
	 * 	<li><b>Session-overridable:</b>  <jk>true</jk>
	 * </ul>
	 *
	 *	<h5 class='section'>Description:</h5>
	 * <p>
	 * Associates an {@link XMLEventAllocator} with this parser.
	 */
	public static final String XML_eventAllocator = PREFIX + "eventAllocator.c";

	/**
	 * Configuration property:  Preserve root element during generalized parsing.
	 *
	 *	<h5 class='section'>Property:</h5>
	 * <ul>
	 * 	<li><b>Name:</b>  <js>"XmlParser.preserveRootElement.b"</js>
	 * 	<li><b>Data type:</b>  <code>Boolean</code>
	 * 	<li><b>Default:</b>  <jk>false</jk>
	 * 	<li><b>Session-overridable:</b>  <jk>true</jk>
	 * </ul>
	 *
	 *	<h5 class='section'>Description:</h5>
	 * <p>
	 * If <jk>true</jk>, when parsing into a generic {@link ObjectMap}, the map will contain a single entry whose key
	 * is the root element name.
	 *
	 *	<h5 class='section'>Example:</h5>
	 * <table class='styled'>
	 * 	<tr>
	 * 		<td>XML</td>
	 * 		<td>ObjectMap.toString(), preserveRootElement==false</td>
	 * 		<td>ObjectMap.toString(), preserveRootElement==true</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td><code><xt>&lt;root&gt;&lt;a&gt;</xt>foobar<xt>&lt;/a&gt;&lt;/root&gt;</xt></code></td>
	 * 		<td><code>{ a:<js>'foobar'</js> }</code></td>
	 * 		<td><code>{ root: { a:<js>'foobar'</js> }}</code></td>
	 * 	</tr>
	 * </table>
	 */
	public static final String XML_preserveRootElement = PREFIX + "preserveRootElement.b";

	/**
	 * Configuration property:  XML reporter.
	 *
	 *	<h5 class='section'>Property:</h5>
	 * <ul>
	 * 	<li><b>Name:</b>  <js>"XmlParser.reporter.c"</js>
	 * 	<li><b>Data type:</b>  <code>Class&lt;? <jk>extends</jk> {@link XMLReporter}&gt;</code>
	 * 	<li><b>Default:</b>  <jk>null</jk>
	 * 	<li><b>Session-overridable:</b>  <jk>true</jk>
	 * </ul>
	 *
	 *	<h5 class='section'>Description:</h5>
	 * <p>
	 * Associates an {@link XMLReporter} with this parser.
	 *
	 * <h5 class='section'>Notes:</h5>
	 * <ul>
	 * 	<li>Reporters are not copied to new parsers during a clone.
	 * </ul>
	 */
	public static final String XML_reporter = PREFIX + "reporter.c";

	/**
	 * Configuration property:  XML resolver.
	 *
	 *	<h5 class='section'>Property:</h5>
	 * <ul>
	 * 	<li><b>Name:</b>  <js>"XmlParser.resolver.c"</js>
	 * 	<li><b>Data type:</b>  <code>Class&lt;? <jk>extends</jk> {@link XMLResolver}&gt;</code>
	 * 	<li><b>Default:</b>  <jk>null</jk>
	 * 	<li><b>Session-overridable:</b>  <jk>true</jk>
	 * </ul>
	 *
	 *	<h5 class='section'>Description:</h5>
	 * <p>
	 * Associates an {@link XMLResolver} with this parser.
	 */
	public static final String XML_resolver = PREFIX + "resolver.c";

	/**
	 * Configuration property:  Enable validation.
	 *
	 *	<h5 class='section'>Property:</h5>
	 * <ul>
	 * 	<li><b>Name:</b>  <js>"XmlParser.validating.b"</js>
	 * 	<li><b>Data type:</b>  <code>Boolean</code>
	 * 	<li><b>Default:</b>  <jk>false</jk>
	 * 	<li><b>Session-overridable:</b>  <jk>true</jk>
	 * </ul>
	 *
	 *	<h5 class='section'>Description:</h5>
	 * <p>
	 * If <jk>true</jk>, XML document will be validated.
	 * See {@link XMLInputFactory#IS_VALIDATING} for more info.
	 */
	public static final String XML_validating = PREFIX + "validating.b";


	//-------------------------------------------------------------------------------------------------------------------
	// Predefined instances
	//-------------------------------------------------------------------------------------------------------------------

	/** Default parser, all default settings.*/
	public static final XmlParser DEFAULT = new XmlParser(PropertyStore.DEFAULT);


	//-------------------------------------------------------------------------------------------------------------------
	// Instance
	//-------------------------------------------------------------------------------------------------------------------

	final boolean
		validating,
		preserveRootElement;
	final XMLReporter reporter;
	final XMLResolver resolver;
	final XMLEventAllocator eventAllocator;

	/**
	 * Constructor.
	 *
	 * @param ps
	 * 	The property store containing all the settings for this object.
	 */
	public XmlParser(PropertyStore ps) {
		this(ps, "text/xml", "application/xml");
	}

	/**
	 * Constructor.
	 *
	 * @param ps
	 * 	The property store containing all the settings for this object.
	 * @param consumes
	 * 	The list of media types that this parser consumes (e.g. <js>"application/json"</js>, <js>"*&#8203;/json"</js>).
	 */
	public XmlParser(PropertyStore ps, String...consumes) {
		super(ps, consumes);
		validating = getProperty(XML_validating, boolean.class, false);
		preserveRootElement = getProperty(XML_preserveRootElement, boolean.class, false);
		reporter = getInstanceProperty(XML_reporter, XMLReporter.class, null);
		resolver = getInstanceProperty(XML_resolver, XMLResolver.class, null);
		eventAllocator = getInstanceProperty(XML_eventAllocator, XMLEventAllocator.class, null);
	}

	@Override /* Context */
	public XmlParserBuilder builder() {
		return new XmlParserBuilder(getPropertyStore());
	}

	/**
	 * Instantiates a new clean-slate {@link XmlParserBuilder} object.
	 * 
	 * <p>
	 * This is equivalent to simply calling <code><jk>new</jk> XmlParserBuilder()</code>.
	 * 
	 * <p>
	 * Note that this method creates a builder initialized to all default settings, whereas {@link #builder()} copies 
	 * the settings of the object called on.
	 * 
	 * @return A new {@link XmlParserBuilder} object.
	 */
	public static XmlParserBuilder create() {
		return new XmlParserBuilder();
	}

	@Override /* Parser */
	public ReaderParserSession createSession(ParserSessionArgs args) {
		return new XmlParserSession(this, args);
	}
	
	@Override /* Context */
	public ObjectMap asMap() {
		return super.asMap()
			.append("XmlParser", new ObjectMap()
				.append("validating", validating)
				.append("preserveRootElement", preserveRootElement)
				.append("reporter", reporter)
				.append("resolver", resolver)
				.append("eventAllocator", eventAllocator)
			);
	}
}
