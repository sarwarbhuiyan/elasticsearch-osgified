package com.humanet.elasticsearch.internal;

import com.humanet.elasticsearch.Document;
import com.humanet.elasticsearch.IndexService;
import com.humanet.elasticsearch.Status;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.elasticsearch.ElasticSearchException;
import org.elasticsearch.client.Client;

public class ESIndexService implements IndexService {

    private static final Log log = LogFactory.getLog(ESIndexService.class);

    private ESServer server;

    public ESIndexService(ESServer server) {
        this.server = server;
    }

    @Override
    public Status add(String index, String type, Document document) {
        Client client = server.getClient();

        try {
            client.prepareIndex(index, type, document.docId)
                .setSource(document.content)
                .execute()
                .actionGet();

        } catch (ElasticSearchException e) {
            log.error(e.getMessage(), e);
            return Status.failure(e.getMessage());
        }

        return Status.success();
    }

}
