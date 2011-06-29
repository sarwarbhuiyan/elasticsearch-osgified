package com.humanet.elasticsearch.internal;

import org.testng.annotations.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;

@Test(sequential = true)
public class SingleESServerTest {

    @Test
    public void start_server_with_default_configuration() {
        ESServer server = new ESServer();

        server.start();
        assertTrue(server.isRunning());

        server.stop();
        assertFalse(server.isRunning());
    }

    @Test(enabled = false)
    public void start_server_with_http_module_disabled() throws IOException {
        Properties esProperties = new Properties();
        esProperties.load(new FileInputStream(
                new File("src/test/resources/elasticsearch-http-disabled.properties"))
        );

        ESServer server = new ESServer(new HashMap<String, String>((Map) esProperties));

        server.start();
        assertTrue(server.isRunning());

        server.stop();
        assertFalse(server.isRunning());
    }

}
