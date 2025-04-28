package com.basicllm.agent.third.bailian.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "alibaba-cloud")
public class AlibabaCloudConfig {

    /**
     * 安全验证
     */
    private Secret samSecret;

    @Data
    public static class Secret {

        private String accessKeyId;

        private String accessKeySecret;

        private String securityToken;

    }

    /**
     * 服务区域
     */
    private ServiceRegion serviceRegion;

    @Data
    public static class ServiceRegion {

        private String regionId;

        private String endPoint;

    }

}
