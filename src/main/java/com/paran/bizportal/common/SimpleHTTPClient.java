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
 * HTTP 처리 wrapper 클라이언트. 아파치 DefaultHttpClient 를 사용함 
 *
 */
public class SimpleHTTPClient implements ResponseHandler<byte[]> 
{
	/**
	 * 로거
	 */
	private Log log = LogFactory.getLog(this.getClass());
	
	/**
	 * 파라미터 리스트
	 */
	private ArrayList<NameValuePair> paramList = new ArrayList<NameValuePair>();
	
	/**
	 * 헤더 리스트
	 */
	private ArrayList<Header> headerList = new ArrayList<Header>();
	
	/**
	 * URL 스트링
	 */
	private String url = null;
	
	/**
	 * HTTP 메소드 (GET/POST)
	 */
	private String method = HttpGet.METHOD_NAME;
	
	/**
	 * HTTP 메소드 GET 여부 (디폴트 true)
	 */
	private boolean isGet = true;
	
	/**
	 * HTTP 처리 응답시간 ms
	 */
	private long responseTime = 0;
	
	/**
	 * HTTP 처리 결과 상태 코드 (디폴트 0, 실패한 경우)
	 */
	private int statusCode = 0;
	
	/**
	 * 커넥션 타임 아웃 ms
	 */
	private int connectionTimeoutMillis = 1500;
	
	/**
	 * 소켓 read 타임 아웃
	 */
	private int socketReadTimeoutMillis = 1500;
	
	/**
	 * 클래스 생성자
	 * 
	 * @param  url URL 스트링
	 */
	public SimpleHTTPClient(String url)
	{
		setURL(url);
	}	
	
	/**
	 * 클래스 생성자
	 * 
	 * @param  url URL 스트링 
	 * @param  isGet 메소드 GET 여부
	 */
	public SimpleHTTPClient(String url, boolean isGet)
	{
		setURL(url, isGet);
	}
	
	/**
	 * 클라이언트 설정 정보를 제거한다.
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
	 * URL 스트링 setter 메쏘드
	 * 
	 * @param url
	 */
	public void setURL(String url)
	{
		setURL(url, true);
	}	
	
	/**
	 * URL 스트링 setter 메쏘드
	 * 
	 * @param  url URL 스트링
	 * @param  isGet 메쏘드 GET 여부
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
	 * 커넥션 타임아웃을 설정한다.
	 * 
	 * @param  connectionTimeoutMillis 커넥션 타임아웃 ms
	 */
	public void setConnectionTimeoutMillis(int connectionTimeoutMillis)
	{
		if(connectionTimeoutMillis > 0)
			this.connectionTimeoutMillis = connectionTimeoutMillis;
		else
			log.warn("invalid connectionTimeoutMillis[" + connectionTimeoutMillis + "]");
	}
	
	/**
	 * 소켓 read 타임아웃을 설정한다.
	 * 
	 * @param  socketReadTimeoutMillis 소켓 read 타임아웃
	 */
	public void setSocketReadTimeoutMillis(int socketReadTimeoutMillis)
	{
		if(socketReadTimeoutMillis > 0)
			this.socketReadTimeoutMillis = socketReadTimeoutMillis;
		else
			log.warn("invalid socketReadTimeoutMillis[" + socketReadTimeoutMillis + "]");
	}
	
	/**
	 * HTTP 헤더를 추가한다.
	 * 
	 * @param  name 헤더 이름
	 * @param  val 헤더 값
	 */
	public void addHeader(String name, String val)
	{
		if(StringUtils.isEmpty(name))
			return;
		
		if(val != null)
			headerList.add(new BasicHeader(name, val));
	}

	/**
	 * HTTP 파라미터를 추가한다.
	 * 
	 * @param  name 파라미터 이름
	 * @param  val 파라미터 값
	 */
	public void addParam(String name, String val)
	{
		if(StringUtils.isEmpty(name))
			return;
		
		if(val != null)
			paramList.add(new BasicNameValuePair(name, val));
	}
	
	/**
	 * HTTP 메쏘드를 POST 설정한다.
	 */
	public void setPost()
	{
		isGet = false;
	}
	
	/**
	 * HTTP 메쏘드를 GET 설정한다.
	 */
	public void setGet()
	{
		isGet = true;
	}
	
	/**
	 * HTTP 요청을 실행한다.
	 * 
	 * @param  reqCharset 요청 파라미터 인코딩 캐릭터셋 
	 * @return 응답 결과 바이트
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
	 * HTTP 실행 결과를 로깅한다.
	 * 
	 * @param  resultBytes 응답 결과 바이트
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
	 * HTTP 요청을 실행한다.
	 * 
	 * @param  reqCharset 요청 파라미터 인코딩 캐릭터셋
	 * @param  rspCharset 응답 결과 디코딩 캐릭터셋
	 * @return 응답 결과 스트링
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
	 * HTTP 요청을 실행한다. 
	 * 요청 파라미터 인코딩과 응답 결과 디코딩 캐릭터셋은 VM 설정 값을 따른다. 
	 * 
	 * @return 응답 결과 스트링
	 */
	public String exec()
	{
		return exec(SystemUtils.FILE_ENCODING, SystemUtils.FILE_ENCODING);
	}
	
	/**
	 * HTTP 요청을 실행하고 그 결과를 JSONObject 객체로 변환해 리턴한다.
	 * 
	 * @param  reqCharset 요청 파라미터 인코딩 캐릭터셋
	 * @param  rspCharset 응답 결과 디코딩 캐릭터셋
	 * @return 응답 결과 JSONObject 객체
	 */
	public JSONObject execReturnJSONObject(String reqCharset, String rspCharset)
	{
		String resultString = exec(reqCharset, rspCharset);
		return JSONObject.fromObject(resultString);
	}
	
	/**
	 * HTTP 요청을 실행하고 그 결과를 JSONObject 객체로 변환해 리턴한다.
	 * 
	 * @param  charset 요청 파리미터 인코딩, 응답 결과 디코딩 캐릭터셋
	 * @return 응답 결과 JSONObject 객체
	 */
	public JSONObject execReturnJSONObject(String charset)
	{
		return execReturnJSONObject(charset, charset);
	}
	
	/**
	 * HTTP 요청을 실행하고 그 결과를 JSONObject 객체로 변환해 리턴한다.
	 * 요청 파라미터 인코딩과 응답 결과 디코딩 캐릭터셋은 VM 설정 값을 따른다.
	 * 
	 * @return 응답 결과 JSONObject 객체
	 */
	public JSONObject execReturnJSONObject()
	{
		return execReturnJSONObject(SystemUtils.FILE_ENCODING);
	}
	
	/**
	 * HTTP 요청을 실행하고 그 결과를 JSONArray 객체로 변환해 리턴한다.
	 * 
	 * @param reqCharset 요청 파라미터 인코딩 캐릭터셋
	 * @param rspCharset 응답 결과 디코딩 캐릭터셋
	 * @return 응답 결과 JSONArray 객체
	 */
	public JSONArray execReturnJSONArray(String reqCharset, String rspCharset)
	{
		String resultString = exec(reqCharset, rspCharset);
		return JSONArray.fromObject(resultString);
	}
	
	/**
	 * HTTP 요청을 실행하고 그 결과를 JSONArray 객체로 변환해 리턴한다.
	 * 
	 * @param  charset 요청 파라미터 인코딩 캐릭터셋
	 * @return 응답 결과 JSONArray 객체
	 */
	public JSONArray execReturnJSONArray(String charset)
	{
		return execReturnJSONArray(charset, charset);
	}
	
	/**
	 * HTTP 요청을 실행하고 그 결과를 JSONArray 객체로 변환해 리턴한다.
	 * 요청 파라미터 인코딩과 응답 결과 디코딩 캐릭터셋은 VM 설정 값을 따른다.
	 * 
	 * @return 응답 결과 JSONArray 객체
	 */
	public JSONArray execReturnJSONArray()
	{
		return execReturnJSONArray(SystemUtils.FILE_ENCODING);
	}	
	
	/**
	 * HTTP 결과 처리 핸들러. ResponseHandler 인터페이스 구현 메쏘드 
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
	 * HTTP 처리 응답시간을 리턴한다. 
	 * 
	 * @return HTTP 처리 응답시간
	 */
	public long getLastResponseTime()
	{
		return responseTime;
	}
}
