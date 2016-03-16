/*
 * Created by Daniel Marell 28/10/15.
 */
package se.marell.dvestagateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import se.marell.dvesta.system.BuildInfo;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Scanner;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(regex("/.*"))
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        ApiInfo apiInfo = new ApiInfo(
                "dvesta-gateway API",
                new Scanner(getClass().getResourceAsStream("/description.html"), "UTF-8").useDelimiter("\\A").next(),
                BuildInfo.getAppVersion(),
                null,
                "daniel.marell@gmail.com",
                null,
                null);
        return apiInfo;
    }
}