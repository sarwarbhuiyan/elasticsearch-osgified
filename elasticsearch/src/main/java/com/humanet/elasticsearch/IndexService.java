package com.humanet.elasticsearch;

public interface IndexService {

    public Status add(String index, String type, Document document);

}
