package com.online.buy.payment.processor.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.List;

@Configuration
public class RedisClusterConfig {

    /*@Value("${spring.redis.cluster.nodes}")
    private List<String> clusterNodes;*/

    /*@Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        // This bean explicitly creates the cluster configuration from your properties file,
        // ensuring that the correct settings are used.
        RedisClusterConfiguration clusterConfiguration = new RedisClusterConfiguration(clusterNodes);
        return new LettuceConnectionFactory(clusterConfiguration);
    }

    @Bean
    @Primary
    RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        // other settings...
        return template;
    }*/
}
