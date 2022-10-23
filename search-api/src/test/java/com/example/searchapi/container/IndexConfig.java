package com.example.searchapi.container;

import com.google.gson.Gson;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.compress.CompressedXContent;
import org.elasticsearch.common.settings.Settings;

import org.elasticsearch.common.xcontent.XContentType;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
public class IndexConfig {

    private Logger logger = LoggerFactory.getLogger(IndexConfig.class);
    private final RestHighLevelClient client;

    public IndexConfig(RestHighLevelClient client) {
        this.client = client;
    }

    public void createIndex(String indexName) {
        CreateIndexRequest address = new CreateIndexRequest(indexName);
        Settings settings = fromPathSetting("elasticsearch/" + indexName + "/settings.json");
        String mappings = readResourceFile("elasticsearch/" + indexName + "/mappings.json");
        address.settings(settings)
            .mapping(mappings, XContentType.JSON);

        ActionListener<CreateIndexResponse> listener = new ActionListener<CreateIndexResponse>() {
            @Override
            public void onResponse(CreateIndexResponse createIndexResponse) {
                logger.info("create index : {} ", indexName);
            }

            @Override
            public void onFailure(Exception e) {
                logger.error("failed to create index : {} ::: {}", indexName, e);
            }
        };
        client.indices().createAsync(address, RequestOptions.DEFAULT, listener);
    }

    public void insertData(String indexName) {
        List<JSONObject> jsonObjects = readJsonFile("elasticsearch/" + indexName + "/data.json");

        jsonObjects
            .forEach(jsonObject -> {
                IndexRequest indexRequest = new IndexRequest(indexName)
                    .source(jsonObject.toString(), XContentType.JSON);
                try {
                    client.index(indexRequest, RequestOptions.DEFAULT);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });


    }

    private List<JSONObject> readJsonFile(String path) {
        String jsonString = readResourceFile(path);
        List<JSONObject> jsonObjects = new ArrayList<>();
        Gson gson = new Gson();
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                jsonObjects.add(jsonObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObjects;
    }

    private ActionListener<CreateIndexResponse> createListener(String indexName) {

        return new ActionListener<CreateIndexResponse>() {
            @Override
            public void onResponse(CreateIndexResponse createIndexResponse) {
                logger.info("success : {}", indexName);
            }

            @Override
            public void onFailure(Exception e) {
                logger.error("failed : {}", indexName);
            }
        };
    }

    private Settings fromPathSetting(String path) {
        String settings = readResourceFile(path);
        return Settings.builder()
            .loadFromSource(settings, XContentType.JSON)
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
