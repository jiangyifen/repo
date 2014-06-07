/**
  2 *
  3 *
  4 * apache 出的 struts hibernate 字符编码处理过滤器
  5 *
  6 *
  7 */
  package com.jiangyifen.ec.filter;
  /*
 10* Copyright 2004 The Apache Software Foundation
 11*
 12* Licensed under the Apache License, Version 2.0 (the "License");
 13* you may naot use this file except in compliance with the License.
 14* You may obtain a copy of the License at
 15*
 16*     http://www.apache.org/licenses/LICENSE-2.0
 17*
 18* Unless required by applicable law or agreed to in writing, software
 19* distributed under the License is distributed on an "AS IS" BASIS,
 20* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 21* See the License for the specific language governing permissions and
 22* limitations under the License.
 23*/

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;


/**
 36 * <p>Example filter that sets the character encoding to be used in parsing the
 37 * incoming request, either unconditionally or only if the client did not
 38 * specify a character encoding.  Configuration of this filter is based on
 39 * the following initialization parameters:</p>
 40 * <ul>
 41 * <li><strong>encoding</strong> - The character encoding to be configured
 42 *     for this request, either conditionally or unconditionally based on
 43 *     the <code>ignore</code> initialization parameter.  This parameter
 44 *     is required, so there is no default.</li>
 45 * <li><strong>ignore</strong> - If set to "true", any character encoding
 46 *     specified by the client is ignored, and the value returned by the
 47 *     <code>selectEncoding()</code> method is set.  If set to "false,
 48 *     <code>selectEncoding()</code> is called <strong>only</strong> if the
 49 *     client has not already specified an encoding.  By default, this
 50 *     parameter is set to "true".</li>
 51 * </ul>
 52 *
 53 * <p>Although this filter can be used unchanged, it is also easy to
 54 * subclass it and make the <code>selectEncoding()</code> method more
 55 * intelligent about what encoding to choose, based on characteristics of
 56 * the incoming request (such as the values of the <code>Accept-Language</code>
 57 * and <code>User-Agent</code> headers, or a value stashed in the current
 58 * user's session.</p>
 59 *
 60 * @author Craig McClanahan
 61 * @version $Revision: 1.1 $ $Date: 2012/08/23 13:55:34 $
 62 */

public class SetCharacterEncodingFilter implements Filter {


    // ----------------------------------------------------- Instance Variables


   /**
 71     * The default character encoding to set for requests that pass through
 72     * this filter.
 73     */
   protected String encoding = null;

    /**
 77     * The filter configuration object we are associated with.  If this value
 78     * is null, this filter instance is not currently configured.
 79     * 
 80     * @uml.property name="filterConfig"
 81     * @uml.associationEnd multiplicity="(0 1)"
 82     */
    protected FilterConfig filterConfig = null;

    /**
 88     * Should a character encoding specified by the client be ignored?
 89     */
    protected boolean ignore = true;

   // --------------------------------------------------------- Public Methods

    /**
 97     * Take this filter out of service.
 98     */
   public void destroy() {

        this.encoding = null;
        this.filterConfig = null;

    }
   /**
108     * Select and set (if specified) the character encoding to be used to
109     * interpret request parameters for this request.
110     *
111     * @param request The servlet request we are processing
112     * @param result The servlet response we are creating
113     * @param chain The filter chain we are processing
114     *
115     * @exception IOException if an input/output error occurs
116     * @exception ServletException if a servlet error occurs
117     */
   public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain)
    throws IOException, ServletException {

        // Conditionally select and set the character encoding to be used
        if (ignore || (request.getCharacterEncoding() == null)) {
            String encoding = selectEncoding(request);
            if (encoding != null)
                request.setCharacterEncoding(encoding);
        }

    // Pass control on to the next filter
        chain.doFilter(request, response);

    }

    /**
135     * Place this filter into service.
136     * 
137     * @param filterConfig The filter configuration object
138     * 
139     * @uml.property name="filterConfig"
140     */
    public void init(FilterConfig filterConfig) throws ServletException {

        this.filterConfig = filterConfig;
        this.encoding = filterConfig.getInitParameter("encoding");
        String value = filterConfig.getInitParameter("ignore");
        if (value == null)
            this.ignore = true;
        else if (value.equalsIgnoreCase("true"))
            this.ignore = true;
        else if (value.equalsIgnoreCase("yes"))
            this.ignore = true;
        else
            this.ignore = false;

    }

    // ------------------------------------------------------ Protected Methods
    /**
161     * Select an appropriate character encoding to be used, based on the
162     * characteristics of the current request and/or filter initialization
163     * parameters.  If no character encoding should be set, return
164     * <code>null</code>.
165     * <p>
166     * The default implementation unconditionally returns the value configured
167     * by the <strong>encoding</strong> initialization parameter for this
168     * filter.
169     *
170     * @param request The servlet request we are processing
171     */
    protected String selectEncoding(ServletRequest request) {

        return (this.encoding);

    }


}