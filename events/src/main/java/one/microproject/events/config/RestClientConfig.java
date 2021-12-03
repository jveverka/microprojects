package one.microproject.events.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchCustomConversions;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@Configuration
public class RestClientConfig extends AbstractElasticsearchConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(RestClientConfig.class);

    @Value("${app.elastic-host}")
    private String elasticHost;

    @Value("${app.elastic-port}")
    private Integer elasticPort;

    @Value("${app.elastic-user}")
    private String elasticUser;

    @Value("${app.elastic-pass}")
    private String elasticPass;

    @PostConstruct
    public void init() {
        LOG.info("#ElasticHost: {}", elasticHost);
        LOG.info("#ElasticPort: {}", elasticPort);
        LOG.info("#ElasticUser: {}", elasticUser);
    }

    @Override
    @Bean
    public RestHighLevelClient elasticsearchClient() {
        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo(elasticHost + ":" + elasticPort)
                .withBasicAuth(elasticUser, elasticPass)
                .build();
        return RestClients.create(clientConfiguration).rest();
    }

}
