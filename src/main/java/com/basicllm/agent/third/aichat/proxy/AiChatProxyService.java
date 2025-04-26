package com.basicllm.agent.third.aichat.proxy;

import com.basicllm.agent.third.aichat.model.config.AiChatConfig;
import com.basicllm.agent.third.aichat.model.config.AiProxyServiceConfig;
import com.basicllm.agent.third.aichat.model.config.AuthenticationConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class AiChatProxyService {

    @Autowired
    private AiChatConfig aiChatConfig;

    /**
     * 根据 ID 获取代理服务信息
     * @param id 代理ID
     * @return 代理服务配置实体类
     */
    public AiProxyServiceConfig getAiProxyServiceConfigById(String id) {
        Optional<AiProxyServiceConfig> aiProxyServiceConfigOptional =
                aiChatConfig.getProxies().stream().filter(config -> config.getId().equals(id)).findFirst();
        return aiProxyServiceConfigOptional.orElse(null);

    }

    /**
     * 根据 ID 获取一个随机认证密钥
     * @param id 代理ID
     * @return 随机的认证密钥
     */
    public String getRandomKeyById(String id) {
        AiProxyServiceConfig aiProxyServiceConfig = getAiProxyServiceConfigById(id);
        if (aiProxyServiceConfig != null) {
            return getRandomKey(aiProxyServiceConfig.getAuthentications());
        }
        return null;
    }

    /**
     * 获取一个随机的认证密钥
     * @param authentications 认证列表
     * @return 随机的认证密钥
     */
    public String getRandomKey(List<AuthenticationConfig> authentications) {
        int randomIndex = ThreadLocalRandom.current().nextInt(authentications.size());
        AuthenticationConfig randomAuth = authentications.get(randomIndex);
        return randomAuth.getKey();
    }

}
