package com.basicllm.agent.third.bailian.config;

import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BailianConfig {

    @Resource
    private AlibabaCloudConfig alibabaCloudConfig;

    @Bean
    public StaticCredentialProvider staticCredentialProvider() {

        // 安全信息配置
        Credential.Builder builder = Credential.builder()
                .accessKeyId(alibabaCloudConfig.getSamSecret().getAccessKeyId())
                .accessKeySecret(alibabaCloudConfig.getSamSecret().getAccessKeySecret());

        String securityToken = alibabaCloudConfig.getSamSecret().getSecurityToken();
        if (securityToken != null) {
            builder.securityToken(securityToken);
        }

        StaticCredentialProvider provider = StaticCredentialProvider.create(
                builder.build()
        );
        return provider;

    }

}
