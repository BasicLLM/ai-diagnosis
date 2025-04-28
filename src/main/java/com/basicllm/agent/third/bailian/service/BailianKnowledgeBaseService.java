package com.basicllm.agent.third.bailian.service;

import com.aliyun.sdk.service.bailian20231229.AsyncClient;
import com.aliyun.sdk.service.bailian20231229.models.RetrieveRequest;
import com.aliyun.sdk.service.bailian20231229.models.RetrieveResponse;
import com.aliyun.sdk.service.bailian20231229.models.RetrieveResponseBody;
import com.basicllm.agent.rag.KnowledgeBaseService;
import com.basicllm.agent.third.bailian.common.ClientFactory;
import com.basicllm.agent.third.bailian.config.BailianKnowledgeBaseConfig;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class BailianKnowledgeBaseService implements KnowledgeBaseService {

    @Resource
    private ClientFactory clientFactory;

    @Resource
    private BailianKnowledgeBaseConfig bailianKnowledgeBaseConfig;

    public RetrieveResponse retrieve(String queryText) {

        // 创建请求客户端
        AsyncClient client = clientFactory.createAsyncClient();

        // 构建查询信息
        RetrieveRequest retrieveRequest = RetrieveRequest.builder()
                .query(queryText)
                .workspaceId(bailianKnowledgeBaseConfig.getWorkspaceId())
                .indexId(bailianKnowledgeBaseConfig.getIndexId())
                .build();

        // 异步请求 API
        CompletableFuture<RetrieveResponse> response = client.retrieve(retrieveRequest);

        // 同步返回结果
        RetrieveResponse resp;
        try {
            resp = response.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        } finally {
            client.close();
        }

        return resp;
    }

    @Override
    public List<String> simpleSearch(String queryText) {

        RetrieveResponse response = retrieve(queryText);
        RetrieveResponseBody body = response.getBody();

        // 检查是否查询成功，如果没有查询成功则返回 null
        if (body.getSuccess() == null || !body.getSuccess()) {
            log.error("未成功调用知识库：{}",body);
            return null;
        }

        // 获取所有查询到的结点
        List<RetrieveResponseBody.Nodes> nodes = body.getData().getNodes();

        // 检查结点情况
        if (nodes == null || nodes.isEmpty()) {
            return null;
        }

        // 将节点内容直接提取出来
        List<String> contents = new ArrayList<>();
        for (RetrieveResponseBody.Nodes node : nodes) {
            contents.add(node.getText());
        }

        return contents;
    }
}
