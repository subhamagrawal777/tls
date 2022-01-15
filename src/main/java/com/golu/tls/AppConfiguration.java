package com.golu.tls;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.appform.dropwizard.discovery.bundle.ServiceDiscoveryConfiguration;
import io.dropwizard.Configuration;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AppConfiguration extends Configuration {
    @NotNull
    @NotEmpty
    private String serviceName;

    @NotNull
    @NotEmpty
    private String serviceUrl;

    @Valid
    @NotNull
    private ServiceDiscoveryConfiguration serviceDiscovery;

    @Valid
    @NotNull
    private SwaggerBundleConfiguration swagger;
}
