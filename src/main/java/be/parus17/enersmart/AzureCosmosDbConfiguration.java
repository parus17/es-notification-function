package be.parus17.enersmart;

import com.azure.core.credential.AzureKeyCredential;
import com.azure.cosmos.CosmosClientBuilder;
import com.azure.spring.data.cosmos.config.AbstractCosmosConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class AzureCosmosDbConfiguration { //extends AbstractCosmosConfiguration {
//    @Value("${spring.cloud.azure.cosmos.endpoint}")
//    private String endpoint;
//
//    @Value("${spring.cloud.azure.cosmos.key}")
//    private String key;
//
//    @Value("${spring.cloud.azure.cosmos.database}")
//    private String database;
//
//    @Bean
//    public CosmosClientBuilder getCosmosClientBuilder() {
//        AzureKeyCredential azureKeyCredential = new AzureKeyCredential(key);
//        return new CosmosClientBuilder()
//                .endpoint(endpoint)
//                .credential(azureKeyCredential)
//                .directMode();
//    }
//
//    @Override
//    protected String getDatabaseName() {
//        return database;
//    }
}
