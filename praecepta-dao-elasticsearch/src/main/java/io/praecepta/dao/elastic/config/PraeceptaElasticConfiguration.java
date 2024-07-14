package io.praecepta.dao.elastic.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.ClientConfiguration.MaybeSecureClientConfigurationBuilder;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

@Configuration
@PropertySource("classpath:praecepta-es-${envTarget:local}.properties")
public class PraeceptaElasticConfiguration extends AbstractElasticsearchConfiguration{

	
	@Value("${praecepta.es.hostName}")
	private String hostName;
	
	@Value("${praecepta.es.subjectName}")
	private String connectionSubjectName;
	
	@Value("${praecepta.es.subjectSupport}")
	private String connectionSubjectSupport;
	
	@Value("${praecepta.es.useSsl}")
	private boolean useSSL;

	@Value("${praecepta.es.useSubject}")
	private boolean useSubject;
	
	@Override
	@Bean(destroyMethod = "close")
	public RestHighLevelClient elasticsearchClient() {
		
		ClientConfiguration clientConfiguration = null;
		
		MaybeSecureClientConfigurationBuilder secureClientConfigurationBuilder =  ClientConfiguration.builder()           
				.connectedTo(hostName)
				//.
				//.build()
				;
		
		if(useSSL) {
			secureClientConfigurationBuilder.usingSsl();
		}
		
		if(useSubject) {
			secureClientConfigurationBuilder.withBasicAuth(connectionSubjectName, connectionSubjectSupport);
		}
		
		clientConfiguration = secureClientConfigurationBuilder.build();
		
		return RestClients.create(clientConfiguration).rest();
		
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getConnectionSubjectName() {
		return connectionSubjectName;
	}

	public void setConnectionSubjectName(String connectionSubjectName) {
		this.connectionSubjectName = connectionSubjectName;
	}

	public String getConnectionSubjectSupport() {
		return connectionSubjectSupport;
	}

	public void setConnectionSubjectSupport(String connectionSubjectSupport) {
		this.connectionSubjectSupport = connectionSubjectSupport;
	}

	public boolean isUseSSL() {
		return useSSL;
	}

	public void setUseSSL(boolean useSSL) {
		this.useSSL = useSSL;
	}

	public boolean isUseSubject() {
		return useSubject;
	}

	public void setUseSubject(boolean useSubject) {
		this.useSubject = useSubject;
	}
	
	

}
