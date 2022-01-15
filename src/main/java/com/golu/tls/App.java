package com.golu.tls;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.golu.tls.exception.AppExceptionMapper;
import com.golu.tls.mapper.MapperType;
import com.golu.tls.mapper.MapperUtils;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Stage;
import io.appform.dropwizard.discovery.bundle.ServiceDiscoveryBundle;
import io.appform.dropwizard.discovery.bundle.ServiceDiscoveryConfiguration;
import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.oor.OorBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import lombok.val;
import org.zapodot.hystrix.bundle.HystrixBundle;
import ru.vyarus.dropwizard.guice.GuiceBundle;

import java.util.List;

public class App extends Application<AppConfiguration> {
    private static final List<String> PACKAGE_NAME_LIST = ImmutableList.of("com.golu.tls");

    public static void main(String[] args) throws Exception {
        val app = new App();
        app.run(args);
    }


    @Override
    public void run(AppConfiguration appConfiguration, Environment environment) {
        environment.jersey().register(AppExceptionMapper.class);
    }

    @Override
    public void initialize(Bootstrap<AppConfiguration> bootstrap) {
        setMapperProperties(bootstrap.getObjectMapper());
        val objectMapper = Jackson.newObjectMapper();
        setMapperProperties(objectMapper);
        MapperUtils.setup(ImmutableMap.of(MapperType.JSON, objectMapper, MapperType.XML, getXmlMapper(), MapperType.YAML, getYamlMapper()));

        bootstrap.setConfigurationSourceProvider(configSourceProvider(bootstrap));

        val serviceDiscoveryBundle = serviceDiscoveryBundle();
        bootstrap.addBundle(serviceDiscoveryBundle);


        GuiceBundle guiceBundle = GuiceBundle.builder().enableAutoConfig(PACKAGE_NAME_LIST.toArray(new String[0])).modules(new ConfigurationModule()).build(Stage.PRODUCTION);

        bootstrap.addBundle(guiceBundle);

        bootstrap.addBundle(swaggerBundle());

        bootstrap.addBundle(oorBundle());

    }

    private ObjectMapper getXmlMapper() {
        val xmlMapper = new XmlMapper();
        setMapperProperties(xmlMapper);
        return xmlMapper;
    }

    private ObjectMapper getYamlMapper() {
        val yamlMapper = new YAMLMapper();
        setMapperProperties(yamlMapper);
        return yamlMapper;
    }

    private void setMapperProperties(ObjectMapper mapper) {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        mapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);
        mapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES);
    }

    private OorBundle<AppConfiguration> oorBundle() {
        return new OorBundle<AppConfiguration>() {
            public boolean withOor() {
                return false;
            }
        };
    }

    private HystrixBundle hystrixBundle() {
        return HystrixBundle.builder().disableStreamServletInAdminContext().withApplicationStreamPath("/hystrix.stream").build();
    }

    private SwaggerBundle<AppConfiguration> swaggerBundle() {
        return new SwaggerBundle<AppConfiguration>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(AppConfiguration configuration) {
                return configuration.getSwagger();
            }
        };
    }

    private ServiceDiscoveryBundle<AppConfiguration> serviceDiscoveryBundle() {
        return new ServiceDiscoveryBundle<AppConfiguration>() {
            @Override
            protected ServiceDiscoveryConfiguration getRangerConfiguration(AppConfiguration config) {
                return config.getServiceDiscovery();
            }

            @Override
            protected String getServiceName(AppConfiguration config) {
                return config.getServiceName();
            }

            @Override
            protected int getPort(AppConfiguration config) {
                return config.getServiceDiscovery().getPublishedPort();
            }
        };
    }

    private SubstitutingSourceProvider configSourceProvider(Bootstrap<AppConfiguration> bootstrap) {
        return new SubstitutingSourceProvider(bootstrap.getConfigurationSourceProvider(), new EnvironmentVariableSubstitutor());
    }
}
