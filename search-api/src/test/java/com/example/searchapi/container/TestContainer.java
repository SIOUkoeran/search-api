package com.example.searchapi.container;

import com.example.searchapi.address.repository.AddressQueryRepository;
import com.example.searchapi.address.repository.AddressQueryRepositoryImpl;
import com.example.searchapi.common.config.AbstractElasticsearchConfiguration;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.PutComposableIndexTemplateRequest;
import org.elasticsearch.cluster.metadata.ComposableIndexTemplate;
import org.elasticsearch.common.compress.CompressedXContent;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.cluster.metadata.Template;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@TestConfiguration
@EnableElasticsearchRepositories(basePackageClasses = {AddressQueryRepositoryImpl.class,
    AddressQueryRepository.class})
public class TestContainer extends AbstractElasticsearchConfiguration {

//    private static final String ELASTIC_IMAGE_NAME = "docker.elastic.co/elasticsearch/elasticsearch:7.15.1";
//
//
//    private static final GenericContainer container;
//
//    static {
//        container = new GenericContainer(
//                new ImageFromDockerfile()
//                        .withDockerfileFromBuilder(builder -> {
//                            builder
//                                    .from(ELASTIC_IMAGE_NAME)
//                                    .run("bin/elasticsearch-plugin install analysis-nori")
//                                    .build();
//                        })
//        ).withExposedPorts(9200, 9300)
//                .withEnv("discovery.type", "single-node");
//
//        container.start();
//    }

    @Override
    public RestHighLevelClient elasticsearchClient() {
        String hostAddress = new StringBuilder()
            .append("localhost")
            .append(":")
            .append("9200")
            .toString();

        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
            .connectedTo(hostAddress)
            .build();
        RestHighLevelClient client = RestClients.create(clientConfiguration).rest();
        return client;
    }


    private void createIndex(RestHighLevelClient client) {
        CreateIndexRequest address = new CreateIndexRequest("address");
        try {
            client.indices().create(address, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setUpIndex(RestHighLevelClient client) {
        CompressedXContent mappings = fromPathMapping("elasticsearch/address/mappings.json");
        Settings settings = fromPathSetting("elasticsearch/address/settings.json");
        Template template = new Template(settings, mappings, null);

        ComposableIndexTemplate composableIndexTemplate = new ComposableIndexTemplate(
            List.of("address*"),
            template,
            List.of(),
            null,
            1L,
            null
        );
        PutComposableIndexTemplateRequest address = new PutComposableIndexTemplateRequest().name(
            "address");
        address.indexTemplate(composableIndexTemplate);
        try {
            client.indices().putIndexTemplate(address, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException("failed to put indexTemplate");
        }
    }

    private Settings fromPathSetting(String path) {
        String settings = readResourceFile(path);
        return Settings.builder()
            .build();
    }

    private CompressedXContent fromPathMapping(String path) {
        String mappings = readResourceFile(path);
        try {
            return new CompressedXContent(mappings);
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("set up test elasticsearch container is failed");
    }

    private String readResourceFile(String path) {
        ClassPathResource classPathResource = new ClassPathResource(path);
        String mappings = null;
        try {
            mappings = IOUtils.toString(classPathResource.getInputStream(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("failed to read resource mapping json file");
        }
        return mappings;
    }
}
