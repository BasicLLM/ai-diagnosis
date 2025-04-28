package com.basicllm.agent.third.bailian.common;

import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.bailian20231229.AsyncClient;
import com.basicllm.agent.third.bailian.config.AlibabaCloudConfig;
import darabonba.core.client.ClientOverrideConfiguration;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class ClientFactory {

    @Resource
    private AlibabaCloudConfig alibabaCloudConfig;

    @Resource
    private StaticCredentialProvider staticCredentialProvider;

    /**
     * 创建一个异步客户端
     *
     * @return 异步客户端
     */
    public AsyncClient createAsyncClient() {
        // 获取服务地域
        AlibabaCloudConfig.ServiceRegion serviceRegion = alibabaCloudConfig.getServiceRegion();

        // 配置异步客户端
        AsyncClient client = AsyncClient.builder()
                .region(serviceRegion.getRegionId()) // Region ID
                .credentialsProvider(staticCredentialProvider)
                .overrideConfiguration(
                        ClientOverrideConfiguration.create()
                                // Endpoint 请参考 https://api.aliyun.com/product/bailian
                                .setEndpointOverride(serviceRegion.getEndPoint())
                )
                .build();

        return client;
    }

}
