package biosim.server;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import m3.gwt.lang.MapX;
import m3.gwt.lang.StringX;



public final class UrlX {

	private final static char PATH_SEP = '/';

	public final static String PATH_SEP_STRING = "" + PATH_SEP;


	private URL canonicalUrl_;

	private String canonicalUrlAsString_;

	private UrlX parentUrl_;

	private boolean directory_;

    private Map<String,String> _parms;

	public UrlX(UrlX root, String subPath) {
		this(root+"/"+subPath);
	}

	public UrlX(URL url) {
		init(url, false);
	}

	public UrlX(String urlAsString) {
		try {
			init(new URL(urlAsString), false);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	public UrlX(String urlAsString, boolean alreadyCanonical) {
		try {
			init(new URL(urlAsString), alreadyCanonical);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	public UrlX(URL url, boolean alreadyCanonical) {
		init(url, alreadyCanonical);
	}

	private void init(URL url, boolean alreadyCanonical) {
		if (alreadyCanonical) {
			canonicalUrl_ = url;
		} else {
			canonicalUrl_ = resolveToCanonicalUrl(url);
		}
		canonicalUrlAsString_ = canonicalUrl_.toExternalForm();

		if (canonicalUrlAsString_.endsWith(PATH_SEP_STRING)) {
			directory_ = true;
		} else {
			directory_ = false;
		}

	}

    public UrlX child(String childPath) {
        return new UrlX(this,childPath);
    }

	public UrlX getRootUrl() {
		try {
			return new UrlX(new URL(canonicalUrl_.getProtocol(), canonicalUrl_.getHost(), canonicalUrl_.getPort(), "/"), true);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	public UrlX getParentUrl() {
		return getParentUrl(false);
	}

	public int getDepthTo(UrlX ancestor) {
		String relativePath = getPathRelativeTo(ancestor);
		return StringX.countOccurences(relativePath, PATH_SEP_STRING);
	}

    public UrlX subPath(String subPath) {
        return new UrlX(this, subPath);
    }

	public String getPathRelativeTo(UrlX ancestor) {

		if (ancestor.canonicalUrlAsString_.equals(canonicalUrlAsString_)) {
			return "";
		}

		if (!this.canonicalUrlAsString_.startsWith(ancestor.canonicalUrlAsString_)) {
			throw new RuntimeException(ancestor + "  is not a parent of " + this);
		}

		int index = ancestor.canonicalUrlAsString_.length();
		if (!ancestor.isDirectory()) {
			index++;
		}

		String relativePart = this.canonicalUrlAsString_.substring(index);

		return relativePart;

	}

	public UrlX resolveHref(String href) {

		try {
			if (href.startsWith("javascript:")) {
				return null;
			}

			URL hrefAsUrl;

			if (href.indexOf(":") > 0) {
				hrefAsUrl = new URL(href);

			} else {
				UrlX baseUrl;
				if (href.startsWith("/")) {
					baseUrl = getRootUrl();
				} else {
					baseUrl = getParentUrl(true);
				}

				hrefAsUrl = new URL(baseUrl + PATH_SEP_STRING + href);
			}

			return new UrlX(hrefAsUrl);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}

	}

	public UrlX getParentUrl(boolean returnSelfIfDirectory) {

		if (returnSelfIfDirectory && directory_) {
			return this;
		}

		if (parentUrl_ == null) {

			String path = canonicalUrl_.getPath();

			int start = path.length();
			if (directory_) {
				if (start == 1 || path.equals(PATH_SEP_STRING)) {
					return null;
				}
				start -= 2;
			}

			int lastPathSep = path.lastIndexOf(PATH_SEP, start);
			String parentPath = path.substring(0, lastPathSep + 1);
			try {
				parentUrl_ = new UrlX(new URL(canonicalUrl_.getProtocol(), canonicalUrl_.getHost(), canonicalUrl_.getPort(), parentPath), true);
			} catch (MalformedURLException e) {
				throw new RuntimeException(e);
			}

		}

		return parentUrl_;

	}

	public UrlX findCommonParent(UrlX u1) {

		UrlX parent = u1;
		while (parent != null) {
			if (this.canonicalUrlAsString_.startsWith(parent.canonicalUrlAsString_)) {
				return parent;
			}
			parent = parent.getParentUrl();
		}
		return null;
	}

	public boolean isDirectory() {
		return directory_;
	}

	@Override
	public String toString() {
		return canonicalUrlAsString_;
	}

	/**
	 * resolves all . and .. to the actual pathnames
	 */
	public static URL resolveToCanonicalUrl(URL url) {

		String fullPath = url.getPath();

		boolean poorlyFormed = false;

		List<String> pathParts = new ArrayList<String>();

		Tokenizer tokenizer = new Tokenizer(fullPath, PATH_SEP_STRING);
		while (tokenizer.next()) {

			String pathPart = tokenizer.currentToken();
			if (pathPart.length() == 0 || pathPart.equals(".")) {
				// do nothing
			} else if (pathPart.equals("..")) {
				if (pathParts.size() == 0) {
					poorlyFormed = true;
				} else {
					pathParts.remove(pathParts.size() - 1);
				}
			} else {
				pathParts.add(pathPart);
			}
		}

		StringBuffer sb = new StringBuffer();
		Iterator<String> iter = pathParts.iterator();
		while (iter.hasNext()) {
			sb.append(PATH_SEP);
			sb.append(iter.next());
		}

		if (pathParts.size() == 0 || fullPath.endsWith(PATH_SEP_STRING)) {
			sb.append(PATH_SEP);
		}

		if (url.getQuery() != null) {
			sb.append("?");
			sb.append(url.getQuery());
		}

		try {
			URL returnUrl = new URL(url.getProtocol(), url.getHost(), url.getPort(), sb.toString());
			if (poorlyFormed) {
//				logger.warn("poorly formed URL " + url.toString() + "  canonicalized to " + returnUrl.toString());
			}
			return returnUrl;
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public boolean equals(Object o) {
		UrlX u = (UrlX) o;

		if (u.canonicalUrlAsString_.equals(canonicalUrlAsString_)) {
			return true;
		} else {
			return false;
		}

	}

	public URL asUrl() {
		return canonicalUrl_;
	}

	public String asString() {
		return toString();
	}

	/**
	 * returns a relative url suitable for file:/ protocol access since the file:/ protocol doesn't have the notion of a server root the same way http://domain.com/ does. Said another way http://domain.com/dir1/ would get mapped to
	 * /myfiles/domain.com/dir1/ so all the file url's can't use an href of "/dir1/some.html" since that would translate to that absolute file dir. instead it needs to be made relative to the parent html so if "/dir1/some.html" was in "/dir2/another.html"
	 * then the getRelativeTo() would return "../dir1/some.html"
	 *
	 */
	public String getRelativeHref(UrlX hrefUrl) {

		UrlX commonParent = findCommonParent(hrefUrl);

		if (commonParent != null) {

			int depth = getDepthTo(commonParent);
			String relativePart = hrefUrl.getPathRelativeTo(commonParent);

			StringBuffer sb = new StringBuffer();
			StringX.appendRepeat(sb, ".." + PATH_SEP, depth);

			sb.append(relativePart);

			// hrefUrl is the parent of this url
			if (sb.length() == 0) {
				sb.append("./");
			}

			String relativeUrl = sb.toString();

			return relativeUrl;

		} else {
			return null;
		}

	}

	private byte[] downloadAsBytesBuilder() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		InputStream in = openStream();
		while ( true ) {
			try {
				int b = in.read();
				if ( b == -1 ) break;
				baos.write(b);
			} catch ( Exception e) {
				throw new RuntimeException(e);
			}
		}
		return baos.toByteArray();
	}

	public String getContentAsString() {
		return new String(downloadAsBytesBuilder());
	}

	public byte[] getContentAsBytes() {
		return downloadAsBytesBuilder();
	}

	public InputStream openStream() {
		try {
			return canonicalUrl_.openStream();
		} catch ( Exception e ) {
			throw new RuntimeException(e);
		}
	}

	public boolean isParentOf(UrlX url) {
		return url.canonicalUrlAsString_.startsWith(canonicalUrlAsString_);
	}

	public boolean isFileProtocol() {
		if (asString().startsWith("file:")) {
			return true;
		} else {
			return false;
		}
	}

    public int getPort() {
        int port = canonicalUrl_.getPort();
        if ( port == -1 ) {
            if ( canonicalUrl_.getProtocol().equals("http") ) {
                port = 80;
            } else if ( canonicalUrl_.getProtocol().equals("https") ) {
                port = 443;
            } else {
                throw new RuntimeException("unable to determine port for " + canonicalUrlAsString_);
            }
        }
        return port;
    }

    public Map<String,String> getParms() {
        if ( _parms == null ) {
            String query = canonicalUrl_.getQuery();
            Map<String,String> temp = MapX.create();
            for ( String p : query.split("\\?") ) {
                String[] parts = p.split("=");
                temp.put(parts[0], parts[1]);
            }
            _parms = temp;
        }
        return _parms;
    }


	public String getHostnameFromUrl() {

		int index1 = canonicalUrlAsString_.indexOf("//");
		if (index1 < 0) {
			return null;
		}

		int index2 = canonicalUrlAsString_.indexOf("/", index1 + 2);
		int index3 = canonicalUrlAsString_.indexOf(":", index1 + 2);

		String host;
		if (index3 >= 0 && (index2 < 0 || index3 < index2)) {
			host = canonicalUrlAsString_.substring(index1 + 2, index3);

		} else if (index2 > 0) {
			host = canonicalUrlAsString_.substring(index1 + 2, index2);

		} else {
			host = canonicalUrlAsString_.substring(index1 + 2);

		}

		return host;

	}

	public String getFilenamePart() {
		return getPathRelativeTo( getParentUrl() );
	}

	public static URI toURI(URL url) {
		try {
			return new URI(url.toExternalForm().replace(" ", "%20"));
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

}
