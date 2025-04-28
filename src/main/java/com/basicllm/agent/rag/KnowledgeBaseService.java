package com.basicllm.agent.rag;

import java.util.List;

public interface KnowledgeBaseService {

    List<String> simpleSearch(String queryText);

}
