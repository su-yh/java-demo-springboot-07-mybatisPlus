/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2020. All rights reserved.
 */

package com.suyh.mp.dynamic.config;

import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceAutoConfiguration;
import com.suyh.mp.dynamic.config.properties.DynamicDataSourceProviderProperties;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@AutoConfigureAfter(value = DynamicDataSourceAutoConfiguration.class)
@Configuration
public class FlywayDbConfiguration {

    // DynamicRoutingDataSource

    @Bean("flyway001")
    public FlywayMigrationInitializer flyway001(DynamicDataSourceProviderProperties providerProperties) {
        // 参考：DynamicDataSourceAutoConfiguration

        DataSource dataSource001 = providerProperties.getDs001();
        FluentConfiguration masterFlywayConfig = new FluentConfiguration();
        masterFlywayConfig.baselineOnMigrate(true)
                .dataSource(dataSource001)
                // mysql 没有schemas 一定不能设置，设置了之后就不会建表了。
                // .schemas("master")
                .locations("db/migration/001")
                .validateOnMigrate(true)
                .ignoreFutureMigrations(false);
        Flyway flyway001 = masterFlywayConfig.load();
        return new FlywayMigrationInitializer(flyway001, null);
    }


    @Bean("flyway002")
    public FlywayMigrationInitializer flyway002(DynamicDataSourceProviderProperties providerProperties) {
        // 参考：DynamicDataSourceAutoConfiguration

        DataSource dataSource002 = providerProperties.getDs002();
        FluentConfiguration slaveFlywayConfig = new FluentConfiguration();
        slaveFlywayConfig.baselineOnMigrate(true)
                .dataSource(dataSource002)
                // .schemas("slave")
                .locations("db/migration/002")
                .validateOnMigrate(true)
                .ignoreFutureMigrations(false);

        Flyway slaveFlyway = slaveFlywayConfig.load();
        return new FlywayMigrationInitializer(slaveFlyway, null);
    }
}
