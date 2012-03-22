package com.paran.bizportal.common;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

/**
 * HTTP ó�� wrapper Ŭ���̾�Ʈ. ����ġ DefaultHttpClient �� ����� 
 *
 */
public class SimpleHTTPClient implements ResponseHandler<byte[]> 
{
	/**
	 * �ΰ�
	 */
	private Log log = LogFactory.getLog(this.getClass());
	
	/**
	 * �Ķ���� ����Ʈ
	 */
	private ArrayList<NameValuePair> paramList = new ArrayList<NameValuePair>();
	
	/**
	 * ��� ����Ʈ
	 */
	private ArrayList<Header> headerList = new ArrayList<Header>();
	
	/**
	 * URL ��Ʈ��
	 */
	private String url = null;
	
	/**
	 * HTTP �޼ҵ� (GET/POST)
	 */
	private String method = HttpGet.METHOD_NAME;
	
	/**
	 * HTTP �޼ҵ� GET ���� (����Ʈ true)
	 */
	private boolean isGet = true;
	
	/**
	 * HTTP ó�� ����ð� ms
	 */
	private long responseTime = 0;
	
	/**
	 * HTTP ó�� ��� ���� �ڵ� (����Ʈ 0, ������ ���)
	 */
	private int statusCode = 0;
	
	/**
	 * Ŀ�ؼ� Ÿ�� �ƿ� ms
	 */
	private int connectionTimeoutMillis = 1500;
	
	/**
	 * ���� read Ÿ�� �ƿ�
	 */
	private int socketReadTimeoutMillis = 1500;
	
	/**
	 * Ŭ���� ������
	 * 
	 * @param  url URL ��Ʈ��
	 */
	public SimpleHTTPClient(String url)
	{
		setURL(url);
	}	
	
	/**
	 * Ŭ���� ������
	 * 
	 * @param  url URL ��Ʈ�� 
	 * @param  isGet �޼ҵ� GET ����
	 */
	public SimpleHTTPClient(String url, boolean isGet)
	{
		setURL(url, isGet);
	}
	
	/**
	 * Ŭ���̾�Ʈ ���� ������ �����Ѵ�.
	 * 
	 */
	public void clear()
	{
		paramList.clear();
		headerList.clear();
		
		url = null;
		method = HttpGet.METHOD_NAME;
		isGet = true;
		responseTime = 0;
		statusCode = 0;
	}
	
	/**
	 * URL ��Ʈ�� setter �޽��
	 * 
	 * @param url
	 */
	public void setURL(String url)
	{
		setURL(url, true);
	}	
	
	/**
	 * URL ��Ʈ�� setter �޽��
	 * 
	 * @param  url URL ��Ʈ��
	 * @param  isGet �޽�� GET ����
	 */
	public void setURL(String url, boolean isGet)
	{
		clear();
		this.url = url;
		this.isGet = isGet;
		
		if(isGet)
			method = HttpGet.METHOD_NAME;
		else
			method = HttpPost.METHOD_NAME;
	}
	
	/**
	 * Ŀ�ؼ� Ÿ�Ӿƿ��� �����Ѵ�.
	 * 
	 * @param  connectionTimeoutMillis Ŀ�ؼ� Ÿ�Ӿƿ� ms
	 */
	public void setConnectionTimeoutMillis(int connectionTimeoutMillis)
	{
		if(connectionTimeoutMillis > 0)
			this.connectionTimeoutMillis = connectionTimeoutMillis;
		else
			log.warn("invalid connectionTimeoutMillis[" + connectionTimeoutMillis + "]");
	}
	
	/**
	 * ���� read Ÿ�Ӿƿ��� �����Ѵ�.
	 * 
	 * @param  socketReadTimeoutMillis ���� read Ÿ�Ӿƿ�
	 */
	public void setSocketReadTimeoutMillis(int socketReadTimeoutMillis)
	{
		if(socketReadTimeoutMillis > 0)
			this.socketReadTimeoutMillis = socketReadTimeoutMillis;
		else
			log.warn("invalid socketReadTimeoutMillis[" + socketReadTimeoutMillis + "]");
	}
	
	/**
	 * HTTP ����� �߰��Ѵ�.
	 * 
	 * @param  name ��� �̸�
	 * @param  val ��� ��
	 */
	public void addHeader(String name, String val)
	{
		if(StringUtils.isEmpty(name))
			return;
		
		if(val != null)
			headerList.add(new BasicHeader(name, val));
	}

	/**
	 * HTTP �Ķ���͸� �߰��Ѵ�.
	 * 
	 * @param  name �Ķ���� �̸�
	 * @param  val �Ķ���� ��
	 */
	public void addParam(String name, String val)
	{
		if(StringUtils.isEmpty(name))
			return;
		
		if(val != null)
			paramList.add(new BasicNameValuePair(name, val));
	}
	
	/**
	 * HTTP �޽�带 POST �����Ѵ�.
	 */
	public void setPost()
	{
		isGet = false;
	}
	
	/**
	 * HTTP �޽�带 GET �����Ѵ�.
	 */
	public void setGet()
	{
		isGet = true;
	}
	
	/**
	 * HTTP ��û�� �����Ѵ�.
	 * 
	 * @param  reqCharset ��û �Ķ���� ���ڵ� ĳ���ͼ� 
	 * @return ���� ��� ����Ʈ
	 */
	public byte[] exec(String reqCharset)
	{
		byte[] resultBytes = null;
		DefaultHttpClient client = new DefaultHttpClient();		
		HttpRequestBase httpRequest = null;
		
		client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, connectionTimeoutMillis);
		client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, socketReadTimeoutMillis);

		try
		{
			if(isGet)
			{
				URI uri = new URI(url);
				uri = URIUtils.createURI(uri.getScheme(), uri.getHost(), uri.getPort(), uri.getPath(), 
					    URLEncodedUtils.format(paramList, reqCharset), uri.getFragment());
				
				httpRequest = new HttpGet(uri);
			}
			else
			{
				httpRequest = new HttpPost(url);
				
				if(paramList != null && !paramList.isEmpty())
				{
					UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList, reqCharset);
					((HttpPost)httpRequest).setEntity(entity);
				}
				
				if(headerList != null && !headerList.isEmpty())
					httpRequest.setHeaders((Header[])headerList.toArray(new Header[0]));
			}
			
			if(headerList != null && !headerList.isEmpty())
				httpRequest.setHeaders((Header[])headerList.toArray(new Header[0]));

			responseTime = System.currentTimeMillis();
			resultBytes = client.execute(httpRequest, this);
		}
		catch(Exception e)
		{
			responseTime = System.currentTimeMillis() - responseTime;
			log.warn(e, e);
		}
		
		logExecResult(resultBytes);
		
		return resultBytes;
	}
	
	/**
	 * HTTP ���� ����� �α��Ѵ�.
	 * 
	 * @param  resultBytes ���� ��� ����Ʈ
	 */
	private void logExecResult(byte[] resultBytes)
	{
		StringBuffer logBuffer = new StringBuffer();
		StringBuffer paramBuffer = new StringBuffer();
		
		int paramCount = paramList.size();
		
		for(int i=0;i<paramCount;i++)
		{
			NameValuePair nvp = paramList.get(i);
			paramBuffer.append(nvp.getName()).append("=>").append(nvp.getValue()).append(",");
		}
		
		String paramString = StringUtils.removeEnd(paramBuffer.toString(), ",");
		logBuffer.append("SimpleHTTPClient [").append(method).append("][").append(url).append("][").append(paramString).append("]");
		logBuffer.append("[").append(statusCode).append("][").append(responseTime).append(" ms]");
		
		if(statusCode != 200)
		{
			String errResult = null;
			
			if(resultBytes == null)
				errResult = "result is null";
			else
				errResult = new String(resultBytes);
			
			logBuffer.append(SystemUtils.LINE_SEPARATOR).append("---------------------------------------------").append(SystemUtils.LINE_SEPARATOR).append(errResult);
		}
		
		log.info(logBuffer);
	}

	/**
	 * HTTP ��û�� �����Ѵ�.
	 * 
	 * @param  reqCharset ��û �Ķ���� ���ڵ� ĳ���ͼ�
	 * @param  rspCharset ���� ��� ���ڵ� ĳ���ͼ�
	 * @return ���� ��� ��Ʈ��
	 */
	public String exec(String reqCharset, String rspCharset)
	{
		byte[] resultBytes = exec(reqCharset);
		String resultString = null;
		
		if(!ArrayUtils.isEmpty(resultBytes))
		{
			try
			{
				resultString = new String(resultBytes, rspCharset); 
			}
			catch(Exception e)
			{
				log.warn(e, e);
			}
		}
		
		return StringUtils.defaultString(resultString);
	}
	
	/**
	 * HTTP ��û�� �����Ѵ�. 
	 * ��û �Ķ���� ���ڵ��� ���� ��� ���ڵ� ĳ���ͼ��� VM ���� ���� ������. 
	 * 
	 * @return ���� ��� ��Ʈ��
	 */
	public String exec()
	{
		return exec(SystemUtils.FILE_ENCODING, SystemUtils.FILE_ENCODING);
	}
	
	/**
	 * HTTP ��û�� �����ϰ� �� ����� JSONObject ��ü�� ��ȯ�� �����Ѵ�.
	 * 
	 * @param  reqCharset ��û �Ķ���� ���ڵ� ĳ���ͼ�
	 * @param  rspCharset ���� ��� ���ڵ� ĳ���ͼ�
	 * @return ���� ��� JSONObject ��ü
	 */
	public JSONObject execReturnJSONObject(String reqCharset, String rspCharset)
	{
		String resultString = exec(reqCharset, rspCharset);
		return JSONObject.fromObject(resultString);
	}
	
	/**
	 * HTTP ��û�� �����ϰ� �� ����� JSONObject ��ü�� ��ȯ�� �����Ѵ�.
	 * 
	 * @param  charset ��û �ĸ����� ���ڵ�, ���� ��� ���ڵ� ĳ���ͼ�
	 * @return ���� ��� JSONObject ��ü
	 */
	public JSONObject execReturnJSONObject(String charset)
	{
		return execReturnJSONObject(charset, charset);
	}
	
	/**
	 * HTTP ��û�� �����ϰ� �� ����� JSONObject ��ü�� ��ȯ�� �����Ѵ�.
	 * ��û �Ķ���� ���ڵ��� ���� ��� ���ڵ� ĳ���ͼ��� VM ���� ���� ������.
	 * 
	 * @return ���� ��� JSONObject ��ü
	 */
	public JSONObject execReturnJSONObject()
	{
		return execReturnJSONObject(SystemUtils.FILE_ENCODING);
	}
	
	/**
	 * HTTP ��û�� �����ϰ� �� ����� JSONArray ��ü�� ��ȯ�� �����Ѵ�.
	 * 
	 * @param reqCharset ��û �Ķ���� ���ڵ� ĳ���ͼ�
	 * @param rspCharset ���� ��� ���ڵ� ĳ���ͼ�
	 * @return ���� ��� JSONArray ��ü
	 */
	public JSONArray execReturnJSONArray(String reqCharset, String rspCharset)
	{
		String resultString = exec(reqCharset, rspCharset);
		return JSONArray.fromObject(resultString);
	}
	
	/**
	 * HTTP ��û�� �����ϰ� �� ����� JSONArray ��ü�� ��ȯ�� �����Ѵ�.
	 * 
	 * @param  charset ��û �Ķ���� ���ڵ� ĳ���ͼ�
	 * @return ���� ��� JSONArray ��ü
	 */
	public JSONArray execReturnJSONArray(String charset)
	{
		return execReturnJSONArray(charset, charset);
	}
	
	/**
	 * HTTP ��û�� �����ϰ� �� ����� JSONArray ��ü�� ��ȯ�� �����Ѵ�.
	 * ��û �Ķ���� ���ڵ��� ���� ��� ���ڵ� ĳ���ͼ��� VM ���� ���� ������.
	 * 
	 * @return ���� ��� JSONArray ��ü
	 */
	public JSONArray execReturnJSONArray()
	{
		return execReturnJSONArray(SystemUtils.FILE_ENCODING);
	}	
	
	/**
	 * HTTP ��� ó�� �ڵ鷯. ResponseHandler �������̽� ���� �޽�� 
	 */
	public byte[] handleResponse(HttpResponse response)
	throws ClientProtocolException, IOException
	{
		responseTime = System.currentTimeMillis() - responseTime;
		this.statusCode = response.getStatusLine().getStatusCode();
		HttpEntity entity = response.getEntity();
		
		if(entity == null)
			return null;
		else
			return EntityUtils.toByteArray(entity);
    }

	/**
	 * HTTP ó�� ����ð��� �����Ѵ�. 
	 * 
	 * @return HTTP ó�� ����ð�
	 */
	public long getLastResponseTime()
	{
		return responseTime;
	}
}
