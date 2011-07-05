package com.humanet.elasticsearch;

import java.util.Map;

/**
 * User: Hugo Marcelino
 * Date: 4/7/11
 */
public class Document {

    public String docId;
    public Map<String,Object> content;

    public Document(String docId, Map<String, Object> content) {
        this.docId = docId;
        this.content = content;
    }

}
