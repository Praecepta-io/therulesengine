package io.praecepta.rest.api.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class TestPostClientWithPayload {

public static void main(String[] args) throws Exception{
		
		CloseableHttpClient client = HttpClients.createDefault();
//	    HttpPost httpPost = new HttpPost("http://localhost:4567/myTestController/testPostWithoutQueryParams");
	    HttpPost httpPost = new HttpPost("http://localhost:8080/myTestController/testPostWithoutQueryParams");
	 
	    String json = "{\"userName\":\"rajasrikar\",\"age\":41}";
	    StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
	    httpPost.setEntity(entity);
	    
	    httpPost.setHeader("accpet", ContentType.APPLICATION_JSON.getMimeType());
		
	    HttpResponse httpResponse = client.execute(httpPost);
	    
	    System.out.println(httpResponse.getStatusLine().getStatusCode());
	    System.out.println(httpResponse.getEntity().getContent().toString());//toString());
	    
	    if (httpResponse != null && httpResponse.getEntity() != null) {
			BufferedReader br;
			try {
				br = new BufferedReader(new InputStreamReader((httpResponse.getEntity().getContent())));
				
				String output;
				System.out.println("Output from Server .... \n");
				while ((output = br.readLine()) != null) {
					System.out.println(output);
				}
				
			} catch (UnsupportedOperationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	    client.close();
	}
}
