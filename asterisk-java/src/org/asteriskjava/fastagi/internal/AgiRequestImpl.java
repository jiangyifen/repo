/*
 *  Copyright 2004-2006 Stefan Reuter
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.asteriskjava.fastagi.internal;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.asteriskjava.fastagi.AgiRequest;
import org.asteriskjava.util.AstUtil;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;


/**
 * Default implementation of the AGIRequest interface.
 * 
 * @author srt
 * @version $Id: AgiRequestImpl.java,v 1.2 2011/05/24 03:48:02 bruce Exp $
 */
public class AgiRequestImpl implements Serializable, AgiRequest
{
    private final Log logger = LogFactory.getLog(getClass());
    private static final Pattern SCRIPT_PATTERN = Pattern
            .compile("^([^\\?]*)\\?(.*)$");
    private static final Pattern PARAMETER_PATTERN = Pattern
            .compile("^(.*)=(.*)$");

    private String rawCallerId;

    /**
     * Serial version identifier.
     */
    private static final long serialVersionUID = 3257001047145789496L;

    private Map<String, String> request;

    /**
     * A map assigning the values of a parameter (an array of Strings) to the
     * name of the parameter.
     */
    private Map<String, String[]> parameterMap;

    private String parameters;
    private String script;
    private boolean callerIdCreated;
    private InetAddress localAddress;
    private int localPort;
    private InetAddress remoteAddress;
    private int remotePort;

    /**
     * Creates a new AGIRequestImpl.
     * 
     * @param environment the first lines as received from Asterisk containing
     *            the environment.
     */
    AgiRequestImpl(final Collection<String> environment)
    {
        if (environment == null)
        {
            throw new IllegalArgumentException("Environment must not be null.");
        }
        request = buildMap(environment);

        script = (String) request.get("network_script");
        if (script != null)
        {
            Matcher scriptMatcher = SCRIPT_PATTERN.matcher(script);
            if (scriptMatcher.matches())
            {
                script = scriptMatcher.group(1);
                parameters = scriptMatcher.group(2);
            }
        }
    }

    /**
     * Builds a map containing variable names as key (with the "agi_" prefix
     * stripped) and the corresponding values.<p>
     * Syntactically invalid and empty variables are skipped.
     * 
     * @param lines the environment to transform.
     * @return a map with the variables set corresponding to the given
     *         environment.
     */
    private Map<String, String> buildMap(final Collection<String> lines)
    {
        Map<String, String> map;

        map = new HashMap<String, String>();

        for (String line : lines)
        {
            int colonPosition;
            String key;
            String value;

            colonPosition = line.indexOf(':');

            // no colon on the line?
            if (colonPosition < 0)
            {
                continue;
            }

            // key doesn't start with agi_?
            if (!line.startsWith("agi_"))
            {
                continue;
            }

            // first colon in line is last character -> no value present?
            if (line.length() < colonPosition + 2)
            {
                continue;
            }

            key = line.substring(4, colonPosition).toLowerCase(Locale.ENGLISH);
            value = line.substring(colonPosition + 2);

            if (value.length() != 0)
            {
                map.put(key, value);
            }
        }

        return map;
    }

    public Map<String, String> getRequest()
    {
        return request;
    }

    /**
     * Returns the name of the script to execute.
     * 
     * @return the name of the script to execute.
     */
    public synchronized String getScript()
    {
        return script;
    }

    /**
     * Returns the full URL of the request in the form
     * agi://host[:port][/script].
     * 
     * @return the full URL of the request in the form
     *         agi://host[:port][/script].
     */
    public String getRequestURL()
    {
        return (String) request.get("request");
    }

    /**
     * Returns the name of the channel.
     * 
     * @return the name of the channel.
     */
    public String getChannel()
    {
        return (String) request.get("channel");
    }

    /**
     * Returns the unqiue id of the channel.
     * 
     * @return the unqiue id of the channel.
     */
    public String getUniqueId()
    {
        return (String) request.get("uniqueid");
    }

    public String getType()
    {
        return (String) request.get("type");
    }

    public String getLanguage()
    {
        return (String) request.get("language");
    }

    public String getCallerId()
    {
        return getCallerIdNumber();
    }

    public String getCallerIdNumber()
    {
        String callerIdName;
        String callerId;

        callerIdName = (String) request.get("calleridname");
        callerId = (String) request.get("callerid");
        if (callerIdName != null)
        {
            // Asterisk 1.2
            if (callerId == null || "unknown".equals(callerId))
            {
                return null;
            }

            return callerId;
        }
        else
        {
            // Asterisk 1.0
            return getCallerId10();
        }
    }

    public String getCallerIdName()
    {
        String callerIdName;

        callerIdName = (String) request.get("calleridname");
        if (callerIdName != null)
        {
            // Asterisk 1.2
            if ("unknown".equals(callerIdName))
            {
                return null;
            }

            return callerIdName;
        }
        else
        {
            // Asterisk 1.0
            return getCallerIdName10();
        }
    }

    /**
     * Returns the Caller*ID number using Asterisk 1.0 logic.
     * 
     * @return the Caller*ID number
     */
    private synchronized String getCallerId10()
    {
        final String[] parsedCallerId;

        if (!callerIdCreated)
        {
            rawCallerId = (String) request.get("callerid");
            callerIdCreated = true;
        }

        parsedCallerId = AstUtil.parseCallerId(rawCallerId);
        if (parsedCallerId[1] == null)
        {
            return parsedCallerId[0];
        }
        else
        {
            return parsedCallerId[1];
        }
    }

    /**
     * Returns the Caller*ID name using Asterisk 1.0 logic.
     * 
     * @return the Caller*ID name
     */
    private synchronized String getCallerIdName10()
    {
        if (!callerIdCreated)
        {
            rawCallerId = (String) request.get("callerid");
            callerIdCreated = true;
        }

        if (!callerIdCreated)
        {
            rawCallerId = (String) request.get("callerid");
            callerIdCreated = true;
        }

        return AstUtil.parseCallerId(rawCallerId)[0];
    }

    public String getDnid()
    {
        String dnid;
        
        dnid = (String) request.get("dnid");
        
        if (dnid == null || "unknown".equals(dnid))
        {
            return null;
        }
        
        return dnid; 
    }

    public String getRdnis()
    {
        String rdnis;
        
        rdnis = (String) request.get("rdnis");
        
        if (rdnis == null || "unknown".equals(rdnis))
        {
            return null;
        }
        
        return rdnis; 
    }

    public String getContext()
    {
        return (String) request.get("context");
    }

    public String getExtension()
    {
        return (String) request.get("extension");
    }

    public Integer getPriority()
    {
        if (request.get("priority") != null)
        {
            return Integer.valueOf((String) request.get("priority"));
        }
        return null;
    }

    public Boolean getEnhanced()
    {
        if (request.get("enhanced") != null)
        {
            if ("1.0".equals((String) request.get("enhanced")))
            {
                return Boolean.TRUE;
            }
            else
            {
                return Boolean.FALSE;
            }
        }
        return null;
    }

    public String getAccountCode()
    {
        return request.get("accountcode");
    }

    public Integer getCallingAni2()
    {
        if (request.get("callingani2") == null)
        {
            return null;
        }

        try
        {
            return Integer.valueOf(request.get("callingani2"));
        }
        catch (NumberFormatException e)
        {
            return null;
        }
    }

    public Integer getCallingPres()
    {
        if (request.get("callingpres") == null)
        {
            return null;
        }
        
        try
        {
            return Integer.valueOf(request.get("callingpres"));
        }
        catch (NumberFormatException e)
        {
            return null;
        }
    }

    public Integer getCallingTns()
    {
        if (request.get("callingtns") == null)
        {
            return null;
        }
        
        try
        {
            return Integer.valueOf(request.get("callingtns"));
        }
        catch (NumberFormatException e)
        {
            return null;
        }
    }

    public Integer getCallingTon()
    {
        if (request.get("callington") == null)
        {
            return null;
        }
        
        try
        {
            return Integer.valueOf(request.get("callington"));
        }
        catch (NumberFormatException e)
        {
            return null;
        }
    }

    public String getParameter(String name)
    {
        String[] values;

        values = getParameterValues(name);

        if (values == null || values.length == 0)
        {
            return null;
        }

        return values[0];
    }

    public String[] getParameterValues(String name)
    {
        if (getParameterMap().isEmpty())
        {
            return null;
        }

        return parameterMap.get(name);
    }

    @SuppressWarnings("rawtypes")
	public synchronized Map getParameterMap()
    {
        if (parameterMap == null)
        {
            parameterMap = parseParameters(parameters);
        }
        return parameterMap;
    }

    /**
     * Parses the given parameter string and caches the result.
     * 
     * @param s the parameter string to parse
     * @return a Map made up of parameter names their values
     */
    private synchronized Map<String, String[]> parseParameters(String s)
    {
        Map<String, List<String>> parameterMap;
        Map<String, String[]> result;
        StringTokenizer st;

        parameterMap = new HashMap<String, List<String>>();
        result = new HashMap<String, String[]>();

        if (s == null)
        {
            return result;
        }

        st = new StringTokenizer(s, "&");
        while (st.hasMoreTokens())
        {
            String parameter;
            Matcher parameterMatcher;
            String name;
            String value;
            List<String> values;

            parameter = st.nextToken();
            parameterMatcher = PARAMETER_PATTERN.matcher(parameter);
            if (parameterMatcher.matches())
            {
                try
                {
                    name = URLDecoder
                            .decode(parameterMatcher.group(1), "UTF-8");
                    value = URLDecoder.decode(parameterMatcher.group(2),
                            "UTF-8");
                }
                catch (UnsupportedEncodingException e)
                {
                    logger.error("Unable to decode parameter '" + parameter
                            + "'", e);
                    continue;
                }
            }
            else
            {
                try
                {
                    name = URLDecoder.decode(parameter, "UTF-8");
                    value = "";
                }
                catch (UnsupportedEncodingException e)
                {
                    logger.error("Unable to decode parameter '" + parameter
                            + "'", e);
                    continue;
                }
            }

            if (parameterMap.get(name) == null)
            {
                values = new ArrayList<String>();
                values.add(value);
                parameterMap.put(name, values);
            }
            else
            {
                values = parameterMap.get(name);
                values.add(value);
            }
        }

        for (String name : parameterMap.keySet())
        {
            List<String> values;
            String[] valueArray;

            values = parameterMap.get(name);
            valueArray = new String[values.size()];
            result.put(name, values.toArray(valueArray));
        }

        return result;
    }

    public InetAddress getLocalAddress()
    {
        return localAddress;
    }

    void setLocalAddress(InetAddress localAddress)
    {
        this.localAddress = localAddress;
    }

    public int getLocalPort()
    {
        return localPort;
    }

    void setLocalPort(int localPort)
    {
        this.localPort = localPort;
    }

    public InetAddress getRemoteAddress()
    {
        return remoteAddress;
    }

    void setRemoteAddress(InetAddress remoteAddress)
    {
        this.remoteAddress = remoteAddress;
    }

    public int getRemotePort()
    {
        return remotePort;
    }

    void setRemotePort(int remotePort)
    {
        this.remotePort = remotePort;
    }

    @Override
   public String toString()
    {
        StringBuffer sb;

        sb = new StringBuffer("AgiRequest[");
        sb.append("script='" + getScript() + "',");
        sb.append("requestURL='" + getRequestURL() + "',");
        sb.append("channel='" + getChannel() + "',");
        sb.append("uniqueId='" + getUniqueId() + "',");
        sb.append("type='" + getType() + "',");
        sb.append("language='" + getLanguage() + "',");
        sb.append("callerId='" + getCallerId() + "',");
        sb.append("callerIdName='" + getCallerIdName() + "',");
        sb.append("dnid='" + getDnid() + "',");
        sb.append("rdnis='" + getRdnis() + "',");
        sb.append("context='" + getContext() + "',");
        sb.append("extension='" + getExtension() + "',");
        sb.append("priority='" + getPriority() + "',");
        sb.append("enhanced='" + getEnhanced() + "',");
        sb.append("accountCode='" + getAccountCode() + "',");
        sb.append("systemHashcode=" + System.identityHashCode(this));
        sb.append("]");

        return sb.toString();
    }
}
