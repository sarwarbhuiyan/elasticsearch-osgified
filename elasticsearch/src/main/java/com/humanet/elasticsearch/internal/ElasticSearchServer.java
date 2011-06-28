package com.humanet.elasticsearch.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.io.FileSystemUtils;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * User: Hugo Marcelino
 * Date: 28/6/11
 * <p/>
 * This class was build based on https://gist.github.com/977580
 */
public class ElasticSearchServer {

    public static final Log log = LogFactory.getLog(ElasticSearchServer.class);

    private Map<String, String> configuration;

    private Node node;

    public ElasticSearchServer(Map<String, String> configuration) {
        this.configuration = configuration;
    }

    public void setUp() {
        final ImmutableSettings.Builder settingsBuilder = ImmutableSettings.settingsBuilder().put(configuration);

        node = NodeBuilder.nodeBuilder()
                .settings(settingsBuilder.build())
                .loadConfigSettings(false)
                .build();

        //TODO - It would be cool if this parameter could also be added to the configuration file instead comming
        // from system properties to take advantage of the osgi bundle property loader.
        if ("true".equalsIgnoreCase(System.getProperty("es.max.files"))) {
            final String workPath = node.settings().get("path.work");
            final int maxOpen = FileSystemUtils.maxOpenFiles(new File(workPath));
            log.info("The maximum number of open files for user " + System.getProperty("user.name") + " is " + maxOpen);
        }

        if (log.isInfoEnabled()) {
            log.info("Starting the Elastic Search server node with these settings:");
            final Map<String, String> map = node.settings().getAsMap();
            final List<String> keys = new ArrayList<String>(map.keySet());
            Collections.sort(keys);
            for (String key : keys) {
                log.info(" " + key + " : " + getValue(map, key));
            }
        }
    }

    public void start() {
        log.info("Starting FullTextSearch Server with ElasticSearchServer");
        node.start();
    }

    public void stop() {
        log.info("Starting FullTextSearch Server with ElasticSearchServer");
        node.stop();
    }

    public Client getClient() {
        return node.client();
    }

    protected static String getValue(Map<String, String> map, String key) {
        if (key.startsWith("cloud.aws.secret")) return "<HIDDEN>";
        return map.get(key);
    }

}
