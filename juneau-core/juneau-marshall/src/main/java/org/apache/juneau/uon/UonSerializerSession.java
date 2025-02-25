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
package org.apache.juneau.uon;

import java.io.*;
import java.util.*;

import org.apache.juneau.*;
import org.apache.juneau.httppart.*;
import org.apache.juneau.internal.*;
import org.apache.juneau.serializer.*;
import org.apache.juneau.transform.*;

/**
 * Session object that lives for the duration of a single use of {@link UonSerializer}.
 *
 * <p>
 * This class is NOT thread safe.
 * It is typically discarded after one-time use although it can be reused within the same thread.
 */
public class UonSerializerSession extends WriterSerializerSession implements HttpPartSerializerSession {

	private final UonSerializer ctx;
	private final boolean plainTextParams;

	/**
	 * @param ctx
	 * 	The context creating this session object.
	 * 	The context contains all the configuration settings for this object.
	 * @param encode Override the {@link UonSerializer#UON_encoding} setting.
	 * @param args
	 * 	Runtime arguments.
	 * 	These specify session-level information such as locale and URI context.
	 * 	It also include session-level properties that override the properties defined on the bean and
	 * 	serializer contexts.
	 */
	public UonSerializerSession(UonSerializer ctx, Boolean encode, SerializerSessionArgs args) {
		super(ctx, args);
		this.ctx = ctx;
		plainTextParams = ctx.getParamFormat() == ParamFormat.PLAINTEXT;
	}

	/**
	 * Converts the specified output target object to an {@link UonWriter}.
	 *
	 * @param out The output target object.
	 * @return The output target object wrapped in an {@link UonWriter}.
	 * @throws IOException Thrown by underlying stream.
	 */
	protected final UonWriter getUonWriter(SerializerPipe out) throws IOException {
		Object output = out.getRawOutput();
		if (output instanceof UonWriter)
			return (UonWriter)output;
		UonWriter w = new UonWriter(this, out.getWriter(), isUseWhitespace(), getMaxIndent(), isEncoding(), isTrimStrings(), plainTextParams, getUriResolver());
		out.setWriter(w);
		return w;
	}

	private final UonWriter getUonWriter(Writer out) throws Exception {
		return new UonWriter(this, out, isUseWhitespace(), getMaxIndent(), isEncoding(), isTrimStrings(), plainTextParams, getUriResolver());
	}

	@Override /* Serializer */
	protected void doSerialize(SerializerPipe out, Object o) throws IOException, SerializeException {
		serializeAnything(getUonWriter(out), o, getExpectedRootType(o), "root", null);
	}

	/**
	 * Workhorse method.
	 *
	 * <p>
	 * Determines the type of object, and then calls the appropriate type-specific serialization method.
	 *
	 * @param out The writer to serialize to.
	 * @param o The object being serialized.
	 * @param eType The expected type of the object if this is a bean property.
	 * @param attrName
	 * 	The bean property name if this is a bean property.
	 * 	<jk>null</jk> if this isn't a bean property being serialized.
	 * @param pMeta The bean property metadata.
	 * @return The same writer passed in.
	 * @throws IOException Thrown by underlying stream.
	 * @throws SerializeException Generic serialization error occurred.
	 */
	@SuppressWarnings({ "rawtypes" })
	protected SerializerWriter serializeAnything(UonWriter out, Object o, ClassMeta<?> eType, String attrName, BeanPropertyMeta pMeta) throws IOException, SerializeException {

		if (o == null) {
			out.appendObject(null, false);
			return out;
		}

		if (eType == null)
			eType = object();

		ClassMeta<?> aType;			// The actual type
		ClassMeta<?> sType;			// The serialized type

		aType = push2(attrName, o, eType);
		boolean isRecursion = aType == null;

		// Handle recursion
		if (aType == null) {
			o = null;
			aType = object();
		}

		sType = aType;
		String typeName = getBeanTypeName(eType, aType, pMeta);

		// Swap if necessary
		PojoSwap swap = aType.getPojoSwap(this);
		if (swap != null) {
			o = swap(swap, o);
			sType = swap.getSwapClassMeta(this);

			// If the getSwapClass() method returns Object, we need to figure out
			// the actual type now.
			if (sType.isObject())
				sType = getClassMetaForObject(o);
		}

		// '\0' characters are considered null.
		if (o == null || (sType.isChar() && ((Character)o).charValue() == 0))
			out.appendObject(null, false);
		else if (sType.isBoolean())
			out.appendBoolean(o);
		else if (sType.isNumber())
			out.appendNumber(o);
		else if (sType.isBean())
			serializeBeanMap(out, toBeanMap(o), typeName);
		else if (sType.isUri() || (pMeta != null && pMeta.isUri()))
			out.appendUri(o);
		else if (sType.isMap()) {
			if (o instanceof BeanMap)
				serializeBeanMap(out, (BeanMap)o, typeName);
			else
				serializeMap(out, (Map)o, eType);
		}
		else if (sType.isCollection()) {
			serializeCollection(out, (Collection) o, eType);
		}
		else if (sType.isArray()) {
			serializeCollection(out, toList(sType.getInnerClass(), o), eType);
		}
		else if (sType.isReader() || sType.isInputStream()) {
			IOUtils.pipe(o, out);
		}
		else {
			out.appendObject(o, false);
		}

		if (! isRecursion)
			pop();
		return out;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private SerializerWriter serializeMap(UonWriter out, Map m, ClassMeta<?> type) throws IOException, SerializeException {

		m = sort(m);

		ClassMeta<?> keyType = type.getKeyType(), valueType = type.getValueType();

		if (! plainTextParams)
			out.append('(');

		Iterator mapEntries = m.entrySet().iterator();

		while (mapEntries.hasNext()) {
			Map.Entry e = (Map.Entry) mapEntries.next();
			Object value = e.getValue();
			Object key = generalize(e.getKey(), keyType);
			out.cr(indent).appendObject(key, false).append('=');
			serializeAnything(out, value, valueType, toString(key), null);
			if (mapEntries.hasNext())
				out.append(',');
		}

		if (m.size() > 0)
			out.cre(indent-1);

		if (! plainTextParams)
			out.append(')');

		return out;
	}

	private SerializerWriter serializeBeanMap(UonWriter out, BeanMap<?> m, String typeName) throws IOException, SerializeException {

		if (! plainTextParams)
			out.append('(');

		boolean addComma = false;

		for (BeanPropertyValue p : m.getValues(isTrimNullProperties(), typeName != null ? createBeanTypeNameProperty(m, typeName) : null)) {
			BeanPropertyMeta pMeta = p.getMeta();
			if (pMeta.canRead()) {
				ClassMeta<?> cMeta = p.getClassMeta();

				String key = p.getName();
				Object value = p.getValue();
				Throwable t = p.getThrown();
				if (t != null)
					onBeanGetterException(pMeta, t);

				if (canIgnoreValue(cMeta, key, value))
					continue;

				if (addComma)
					out.append(',');

				out.cr(indent).appendObject(key, false).append('=');

				serializeAnything(out, value, cMeta, key, pMeta);

				addComma = true;
			}
		}

		if (m.size() > 0)
			out.cre(indent-1);
		if (! plainTextParams)
			out.append(')');

		return out;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private SerializerWriter serializeCollection(UonWriter out, Collection c, ClassMeta<?> type) throws IOException, SerializeException {

		ClassMeta<?> elementType = type.getElementType();

		c = sort(c);

		if (! plainTextParams)
			out.append('@').append('(');

		for (Iterator i = c.iterator(); i.hasNext();) {
			out.cr(indent);
			serializeAnything(out, i.next(), elementType, "<iterator>", null);
			if (i.hasNext())
				out.append(',');
		}

		if (c.size() > 0)
			out.cre(indent-1);
		if (! plainTextParams)
			out.append(')');

		return out;
	}

	@Override /* HttpPartSerializer */
	public String serialize(HttpPartType type, HttpPartSchema schema, Object value) throws SerializeException, SchemaValidationException {
		try {
			// Shortcut for simple types.
			ClassMeta<?> cm = getClassMetaForObject(value);
			if (cm != null) {
				if (cm.isNumber() || cm.isBoolean())
					return ClassUtils.toString(value);
				if (cm.isString()) {
					String s = ClassUtils.toString(value);
					if (s.isEmpty() || ! UonUtils.needsQuotes(s))
						return s;
				}
			}
			StringWriter w = new StringWriter();
			serializeAnything(getUonWriter(w), value, getExpectedRootType(value), "root", null);
			return w.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override /* HttpPartSerializer */
	public String serialize(HttpPartSchema schema, Object value) throws SerializeException, SchemaValidationException {
		return serialize(null, schema, value);
	}

	//-----------------------------------------------------------------------------------------------------------------
	// Properties
	//-----------------------------------------------------------------------------------------------------------------

	/**
	 * Configuration property:  Add <js>"_type"</js> properties when needed.
	 *
	 * @see UonSerializer#UON_addBeanTypes
	 * @return
	 * 	<jk>true</jk> if <js>"_type"</js> properties will be added to beans if their type cannot be inferred
	 * 	through reflection.
	 */
	@Override
	protected final boolean isAddBeanTypes() {
		return ctx.isAddBeanTypes();
	}

	/**
	 * Configuration property:  Encode non-valid URI characters.
	 *
	 * @see UonSerializer#UON_encoding
	 * @return
	 * 	<jk>true</jk> if non-valid URI characters should be encoded with <js>"%xx"</js> constructs.
	 */
	protected final boolean isEncoding() {
		return ctx.isEncoding();
	}

	/**
	 * Configuration property:  Format to use for query/form-data/header values.
	 *
	 * @see UonSerializer#UON_paramFormat
	 * @return
	 * 	Specifies the format to use for URL GET parameter keys and values.
	 */
	protected final ParamFormat getParamFormat() {
		return ctx.getParamFormat();
	}

	//-----------------------------------------------------------------------------------------------------------------
	// Other methods
	//-----------------------------------------------------------------------------------------------------------------

	@Override /* Session */
	public ObjectMap toMap() {
		return super.toMap()
			.append("UonSerializerSession", new DefaultFilteringObjectMap()
		);
	}
}
