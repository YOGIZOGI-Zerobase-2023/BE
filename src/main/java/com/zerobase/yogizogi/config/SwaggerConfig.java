package com.zerobase.yogizogi.config;

import com.fasterxml.classmate.TypeResolver;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.AlternateTypeRule;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@RequiredArgsConstructor
public class SwaggerConfig {
  private final TypeResolver typeResolver;
  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
        .alternateTypeRules(AlternateTypeRules.newRule(
            typeResolver.resolve(Pageable.class),
            typeResolver.resolve(Page.class)))
        .select()
        .apis(RequestHandlerSelectors.basePackage("com.zerobase.yogizogi"))
        .paths(PathSelectors.any()) // ant("/company/**) 로 특정 api만 호출 가능
        .build().apiInfo(apiInfo());
  }

  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title("요기조기")
        .description("숙박 예약 서비스")
        .version("2.0")
        .build();
  }

  @Getter
  @Setter
  @ApiModel
  static class Page {
    @ApiModelProperty(value = "페이지 번호(0..N)")
    private Integer page;

    @ApiModelProperty(value = "페이지 크기", allowableValues = "range[0,100]")
    private Integer size;

    @ApiModelProperty(value = "정렬(사용법: 컬럼명,ASC|DESC)")
    private List<String> sort;
  }
}