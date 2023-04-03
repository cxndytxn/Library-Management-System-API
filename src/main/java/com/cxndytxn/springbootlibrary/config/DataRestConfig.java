package com.cxndytxn.springbootlibrary.config;

import com.cxndytxn.springbootlibrary.entity.Book;
import com.cxndytxn.springbootlibrary.entity.Review;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
public class DataRestConfig implements RepositoryRestConfigurer {

    private String allowedOrigins = "http://localhost:3000";

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        HttpMethod[] unsupportedActions = {
                HttpMethod.POST, HttpMethod.PATCH, HttpMethod.DELETE, HttpMethod.PUT
        };

        config.exposeIdsFor(Book.class);
        config.exposeIdsFor(Review.class);

        disableHttpMethods(Book.class, config, unsupportedActions);
        disableHttpMethods(Review.class, config, unsupportedActions);

        /* Configure CORS Mapping */
        cors.addMapping(config.getBasePath() + "/**").allowedOrigins(allowedOrigins);

    }

    private void disableHttpMethods(Class theClass, RepositoryRestConfiguration config, HttpMethod[] unsupportedActions) {
        config.getExposureConfiguration().forDomainType(theClass).withItemExposure((metdata, httpMethods) -> httpMethods.disable(unsupportedActions)).withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(unsupportedActions));
    }

}
