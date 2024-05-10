package io.praecepta.rest.client.builder;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

import javax.net.ssl.SSLContext;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import io.praecepta.rest.client.config.PraeceptaSecureWebServiceClientConfig;

public class PraeceptaSecureRestClientBuilder extends PraeceptaRestClientBuilder<PraeceptaSecureWebServiceClientConfig> {
	
	private final static Logger logger = LoggerFactory.getLogger(PraeceptaSecureRestClientBuilder.class);
	
	public PraeceptaSecureRestClientBuilder(PraeceptaSecureWebServiceClientConfig config) {
		super(config);
	}
	
	@Override
	protected HttpClient getHttpClient() {
		
		logger.info("Returning Secure Http Client for Wrapper");
		
		 char[] password = getWsClientConfig().getCertPassword().toCharArray();
		
		SSLContext sslContext = null;
		
		try {
			
			sslContext = SSLContextBuilder.create()
			        .loadKeyMaterial(
			        		keyStore(getWsClientConfig().getCertPath(), password)
			        		, password)
			        .loadTrustMaterial(null, new TrustSelfSignedStrategy())
			        .build();
			
		} catch (KeyManagementException e) {
			logger.error(" KeyManagementException while building SSL Context for Secure Rest Client ", e);
		} catch (UnrecoverableKeyException e) {
			logger.error(" UnrecoverableKeyException while building SSL Context for Secure Rest Client ", e);
		} catch (NoSuchAlgorithmException e) {
			logger.error(" NoSuchAlgorithmException while building SSL Context for Secure Rest Client ", e);
		} catch (KeyStoreException e) {
			logger.error(" KeyStoreException while building SSL Context for Secure Rest Client ", e);
		} catch (Exception e) {
			logger.error(" Exception while building SSL Context for Secure Rest Client ", e);
		}

		logger.info("Wrapping SSL Context to Secure Http Client");
		
	    return HttpClients.custom()
	    		.setSSLContext(sslContext)
	    		.build();
	}

	private KeyStore keyStore(String file, char[] password) throws Exception {
		
		logger.info(" Preparing Key Store ");
		
	    KeyStore keyStore = KeyStore.getInstance("PKCS12");
	    
	    logger.info(" Getting the Cert File using Resource Utils. ");
	    File key = ResourceUtils.getFile(file);
	    
	    if(key != null) {
	    	logger.info(" Able to get the Cert file from the Location Specified"); 
	    	
		    try (InputStream in = new FileInputStream(key)) {
		        keyStore.load(in, password);
		    }
	    }
	    return keyStore;
	}
}
