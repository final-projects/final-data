package org.finalframework.sharding.config;

import java.util.Properties;

import lombok.Getter;

/**
 * @author likly
 * @version 1.0.0
 * @since 1.0.0
 */
@Getter
public class ShardingAlgorithmRegistration {

    private final String type;

    private final String name;

    private final Properties properties;

    public ShardingAlgorithmRegistration(final String type, final String name, final Properties properties) {

        this.type = type;
        this.name = name;
        this.properties = properties;
    }

}
