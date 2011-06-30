package com.humanet.elasticsearch.internal;

import org.apache.commons.io.FileUtils;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.testng.Assert.fail;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;

@Test(sequential = true)
public class SingleESServerTest {

    @AfterMethod
    public void tearDown() throws Exception {
        FileUtils.deleteDirectory(new File("es-data"));
    }

    @Test
    public void start_server_with_default_configuration() {

        Map<String, String> properties = new HashMap<String, String>();
        properties.put("path.data", "es-data/data");
        properties.put("path.logs", "es-data/logs");
        properties.put("path.work", "es-data/work");

        ESServer server = new ESServer(properties);

        server.start();
        assertTrue(server.isRunning());

        server.stop();
        assertFalse(server.isRunning());
    }

    @Test
    public void start_server_with_conf_directory() throws IOException {

        Map<String, String> properties = new HashMap<String, String>();
        properties.put("path.conf", "elasticsearch/src/test/resources");

        ESServer server = new ESServer(properties);

        server.start();
        assertTrue(server.isRunning());

        //TODO : check that the configuration is being loaded. For example get the node name.

        server.stop();
        assertFalse(server.isRunning());

    }

    @Test
    public void start_server_with_http_module_disabled() throws IOException {
        Properties esProperties = new Properties();
        esProperties.load(new FileInputStream(
                new File("src/test/resources/elasticsearch-http-disabled.properties"))
        );

        ESServer server = new ESServer(new HashMap<String, String>((Map) esProperties));

        server.start();
        assertTrue(server.isRunning());

        assertThat(server, not(IsListeningOnPort(9200)));

        server.stop();
        assertFalse(server.isRunning());
    }

    private Matcher<ESServer> IsListeningOnPort(final int port) {

        return new TypeSafeMatcher<ESServer>() {
            @Override
            public boolean matchesSafely(ESServer esServer) {
                return canWeConnectToPort(port);
            }

            private boolean canWeConnectToPort(int port) {
                Socket socket = null;
                try {
                    InetAddress addr = InetAddress.getByName(
                            Inet4Address.getLocalHost().getHostAddress()
                    );
                    SocketAddress sockaddr = new InetSocketAddress(addr, port);

                    socket = new Socket();
                    socket.connect(sockaddr);
                } catch (IOException e) {
                    return false;

                } finally {
                    try {
                        if (socket != null) socket.close();
                    } catch (IOException e) {
                        fail("We couldn't close the connection to the socketserver", e);
                    }
                }
                return true;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("A server is listening on port " + port);
            }
        };
    }


}
