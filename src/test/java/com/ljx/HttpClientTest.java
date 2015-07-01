package com.ljx;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ljx.model.User;

public class HttpClientTest {
	
	private static Gson gson = new Gson();

	@Test
	public void testGet() throws Exception {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet("http://localhost:8800/user?op=list");
		CloseableHttpResponse response1 = httpclient.execute(httpGet);
		// The underlying HTTP connection is still held by the response object
		// to allow the response content to be streamed directly from the
		// network socket.
		// In order to ensure correct deallocation of system resources
		// the user MUST call CloseableHttpResponse#close() from a finally
		// clause.
		// Please note that if response content is not fully consumed the
		// underlying
		// connection cannot be safely re-used and will be shut down and
		// discarded
		// by the connection manager.
		try {
			System.out.println(response1.getStatusLine());
			HttpEntity entity1 = response1.getEntity();

			// do something useful with the response body
			// and ensure it is fully consumed
			EntityUtils.consume(entity1);
		} finally {
			response1.close();
		}
	}

	@Test
	public void testGet2() {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpGet httpget = new HttpGet("http://localhost:8800/user?op=list");

			System.out.println("Executing request " + httpget.getRequestLine());

			// Create a custom response handler
			ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

				@Override
				public String handleResponse(final HttpResponse response)
						throws ClientProtocolException, IOException {
					int status = response.getStatusLine().getStatusCode();
					if (status >= 200 && status < 300) {
						HttpEntity entity = response.getEntity();
						return entity != null ? EntityUtils.toString(entity)
								: null;
					} else {
						throw new ClientProtocolException(
								"Unexpected response status: " + status);
					}
				}

			};
			String responseBody = httpclient.execute(httpget, responseHandler);
			System.out.println("----------------------------------------");
			System.out.println(responseBody);
			Map<String,Object> result = gson.fromJson(responseBody, new TypeToken<Map<String,Object>>(){}.getType());
			System.out.println("----------------------------------------");
			System.out.println(result.get("flag"));
			@SuppressWarnings("unchecked")
			List<User> users = (List<User>)result.get("result");
			System.out.println(users);
			System.out.println(users.size());
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
