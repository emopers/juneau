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
import static org.apache.juneau.internal.StringUtils.*;
import static org.apache.juneau.rest.annotation.HookEvent.*;

import java.io.*;
import java.util.*;

import org.apache.juneau.annotation.*;
import org.apache.juneau.dto.*;
import org.apache.juneau.html.annotation.*;
import org.apache.juneau.http.annotation.Body;
import org.apache.juneau.http.annotation.Path;
import org.apache.juneau.http.annotation.Response;
import org.apache.juneau.jsonschema.annotation.*;
import org.apache.juneau.rest.*;
import org.apache.juneau.rest.annotation.*;
import org.apache.juneau.rest.exception.*;
import org.apache.juneau.rest.helper.*;
import org.apache.juneau.utils.*;

/**
 * REST resource that allows access to a file system directory.
 *
 * <p>
 * The root directory is specified in one of two ways:
 * <ul class='spaced-list'>
 * 	<li>
 * 		Specifying the location via a <l>DirectoryResource.rootDir</l> property.
 * 	<li>
 * 		Overriding the {@link #getRootDir()} method.
 * </ul>
 *
 * <p>
 * Read/write access control is handled through the following properties:
 * <ul class='spaced-list'>
 * 	<li>
 * 		<l>DirectoryResource.allowViews</l> - If <jk>true</jk>, allows view and download access to files.
 * 	<li>
 * 		<l>DirectoryResource.allowUploads</l> - If <jk>true</jk>, allows files to be created or overwritten.
 * 	<li>
 * 		<l>DirectoryResource.allowDeletes</l> - If <jk>true</jk>, allows files to be deleted.
 * </ul>
 */
@RestResource(
	title="File System Explorer",
	messages="nls/DirectoryResource",
	allowedMethodParams="*"
)
@HtmlDocConfig(
	navlinks={
		"up: request:/..",
		"options: servlet:/?method=OPTIONS"
	}
)
@HtmlConfig(uriAnchorText="PROPERTY_NAME")
@SuppressWarnings("javadoc")
public class DirectoryResource extends BasicRestServlet {
	private static final long serialVersionUID = 1L;

	//-------------------------------------------------------------------------------------------------------------------
	// Configurable properties
	//-------------------------------------------------------------------------------------------------------------------

	private static final String PREFIX = "DirectoryResource.";

	/**
	 * Configuration property:  Root directory.
	 */
	public static final String DIRECTORY_RESOURCE_rootDir = PREFIX + "rootDir.s";

	/**
	 * Configuration property:  Allow view and downloads on files.
	 */
	public static final String DIRECTORY_RESOURCE_allowViews = PREFIX + "allowViews.b";

	/**
	 * Configuration property:  Allow deletes on files.
	 */
	public static final String DIRECTORY_RESOURCE_allowDeletes = PREFIX + "allowDeletes.b";

	/**
	 * Configuration property:  Allow uploads on files.
	 */
	public static final String DIRECTORY_RESOURCE_allowUploads = PREFIX + "allowUploads.b";


	//-------------------------------------------------------------------------------------------------------------------
	// Instance
	//-------------------------------------------------------------------------------------------------------------------

	private File rootDir;     // The root directory

	// Settings enabled through servlet init parameters
	boolean allowDeletes, allowUploads, allowViews;

	@RestHook(INIT)
	public void init(RestContextBuilder b) throws Exception {
		RestContextProperties p = b.getProperties();
		rootDir = new File(p.getString(DIRECTORY_RESOURCE_rootDir));
		allowViews = p.getBoolean(DIRECTORY_RESOURCE_allowViews, false);
		allowDeletes = p.getBoolean(DIRECTORY_RESOURCE_allowDeletes, false);
		allowUploads = p.getBoolean(DIRECTORY_RESOURCE_allowUploads, false);
	}

	@RestMethod(
		name=GET,
		path="/*",
		summary="View information on file or directory",
		description="Returns information about the specified file or directory."
	)
	@HtmlDocConfig(
		nav={"<h5>Folder:  $RA{fullPath}</h5>"}
	)
	public FileResource getFile(RestRequest req, @Path("/*") String path) throws NotFound, Exception {

		File dir = getFile(path);
		req.setAttribute("fullPath", dir.getAbsolutePath());

		return new FileResource(dir, path, true);
	}

	@RestMethod(
		name="VIEW",
		path="/*",
		summary="View contents of file",
		description="View the contents of a file.\nContent-Type is set to 'text/plain'."
	)
	public FileContents viewFile(RestResponse res, @Path("/*") String path) throws NotFound, MethodNotAllowed {
		if (! allowViews)
			throw new MethodNotAllowed("VIEW not enabled");

		res.setContentType("text/plain");
		try {
			return new FileContents(getFile(path));
		} catch (FileNotFoundException e) {
			throw new NotFound("File not found");
		}
	}

	@RestMethod(
		name="DOWNLOAD",
		path="/*",
		summary="Download file",
		description="Download the contents of a file.\nContent-Type is set to 'application/octet-stream'."
	)
	public FileContents downloadFile(RestResponse res, @Path("/*") String path) throws NotFound, MethodNotAllowed {
		if (! allowViews)
			throw new MethodNotAllowed("DOWNLOAD not enabled");

		res.setContentType("application/octet-stream");
		try {
			return new FileContents(getFile(path));
		} catch (FileNotFoundException e) {
			throw new NotFound("File not found");
		}
	}

	@RestMethod(
		name=DELETE,
		path="/*",
		summary="Delete file",
		description="Delete a file on the file system."
	)
	public RedirectToRoot deleteFile(@Path("/*") String path) throws MethodNotAllowed {
		deleteFile(getFile(path));
		return new RedirectToRoot();
	}

	@RestMethod(
		name=PUT,
		path="/*",
		summary="Add or replace file",
		description="Add or overwrite a file on the file system."
	)
	public RedirectToRoot updateFile(
		@Body(schema=@Schema(type="string",format="binary")) InputStream is,
		@Path("/*") String path
	) throws InternalServerError {

		if (! allowUploads)
			throw new MethodNotAllowed("PUT not enabled");

		File f = getFile(path);

		try (OutputStream os = new BufferedOutputStream(new FileOutputStream(f))) {
			IOPipe.create(is, os).run();
		} catch (IOException e) {
			throw new InternalServerError(e);
		}

		return new RedirectToRoot();
	}

	//-----------------------------------------------------------------------------------------------------------------
	// Helper beans
	//-----------------------------------------------------------------------------------------------------------------

	@Response(schema=@Schema(type="string",format="binary"), description="Contents of file")
	static class FileContents extends FileInputStream {
		public FileContents(File file) throws FileNotFoundException {
			super(file);
		}
	}

	@Response(description="Redirect to root page on success")
	static class RedirectToRoot extends SeeOtherRoot {}

	@Response(description="File action")
	public static class Action extends LinkString {
		public Action(String name, String uri, Object...uriArgs) {
			super(name, uri, uriArgs);
		}
	}

	@Response(description="File or directory details")
	@Bean(properties="type,name,size,lastModified,actions,files")
	public class FileResource {
		private final File f;
		private final String path;
		private final String uri;
		private final boolean includeChildren;

		public FileResource(File f, String path, boolean includeChildren) {
			this.f = f;
			this.path = path;
			this.uri = "servlet:/"+(path == null ? "" : path);
			this.includeChildren = includeChildren;
		}

		public String getType() {
			return (f.isDirectory() ? "dir" : "file");
		}

		public LinkString getName() {
			return new LinkString(f.getName(), uri);
		}

		public long getSize() {
			return f.isDirectory() ? f.listFiles().length : f.length();
		}

		public Date getLastModified() {
			return new Date(f.lastModified());
		}

		@Html(format=HtmlFormat.HTML_CDC)
		public List<Action> getActions() throws Exception {
			List<Action> l = new ArrayList<>();
			if (allowViews && f.canRead() && ! f.isDirectory()) {
				l.add(new Action("view", uri + "?method=VIEW"));
				l.add(new Action("download", uri + "?method=DOWNLOAD"));
			}
			if (allowDeletes && f.canWrite() && ! f.isDirectory())
				l.add(new Action("delete", uri + "?method=DELETE"));
			return l;
		}

		public Set<FileResource> getFiles() {
			if (f.isFile() || ! includeChildren)
				return null;
			Set<FileResource> s = new TreeSet<>(new FileResourceComparator());
			for (File fc : f.listFiles())
				s.add(new FileResource(fc, (path != null ? (path + '/') : "") + urlEncode(fc.getName()), false));
			return s;
		}
	}

	static final class FileResourceComparator implements Comparator<FileResource>, Serializable {
		private static final long serialVersionUID = 1L;
		@Override /* Comparator */
		public int compare(FileResource o1, FileResource o2) {
			int c = o1.getType().compareTo(o2.getType());
			return c != 0 ? c : o1.getName().compareTo(o2.getName());
		}
	}

	//-----------------------------------------------------------------------------------------------------------------
	// Helper methods
	//-----------------------------------------------------------------------------------------------------------------

	/**
	 * Returns the root directory.
	 *
	 * @return The root directory.
	 */
	protected File getRootDir() {
		return rootDir;
	}

	private File getFile(String path) throws NotFound {
		if (path == null)
			return rootDir;
		File f = new File(rootDir.getAbsolutePath() + '/' + path);
		if (f.exists())
			return f;
		throw new NotFound("File not found.");
	}

	private void deleteFile(File f) {
		if (! allowDeletes)
			throw new MethodNotAllowed("DELETE not enabled");
		if (f.isDirectory()) {
			File[] files = f.listFiles();
			if (files != null) {
				for (File fc : files)
					deleteFile(fc);
			}
		}
		if (! f.delete())
			throw new Forbidden("Could not delete file {0}", f.getAbsolutePath()) ;
	}
}
