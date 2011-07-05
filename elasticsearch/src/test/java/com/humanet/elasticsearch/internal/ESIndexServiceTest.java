package com.humanet.elasticsearch.internal;

import com.humanet.elasticsearch.Document;
import com.humanet.elasticsearch.IndexService;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Hugo Marcelino
 * Date: 4/7/11
 */
public class ESIndexServiceTest {

    private ESServer esServer;
    private IndexService indexService;

    @BeforeClass
    public void setUp() throws Exception {
        Map<String, String> esConfiguration = new ESConfigurationBuilder()
                .withClusterName("es-cluster")
                .withHttp(false)
                .build();

        esServer = new ESServer(esConfiguration);
        esServer.start();
    }

    @AfterClass
    public void tearDown() throws Exception {
        esServer.stop();
    }

    @BeforeMethod
    public void instatiate() throws Exception {
        indexService = new ESIndexService(esServer);
    }

    @Test
    public void testAdd() throws Exception {
        Map<String, Object> aCpe = new HashMap<String, Object>();
        aCpe.put("name", "rt00111");
        aCpe.put("marca", "cisco");
        aCpe.put("ipv4", "127.0.0.1");
        aCpe.put("active", "false");

        indexService.add("confm", "aCpe", new Document("1", aCpe));
    }
}
