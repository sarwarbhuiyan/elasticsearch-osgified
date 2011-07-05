package com.humanet.elasticsearch;

/**
 * User: Hugo Marcelino
 * Date: 4/7/11
 */
public interface IndexService {

    public Status add(String index, String type, Document document);

}
