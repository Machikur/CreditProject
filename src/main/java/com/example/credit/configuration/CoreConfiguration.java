package com.example.credit.configuration;

import com.example.credit.bank.CurrencyConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class CoreConfiguration extends WebMvcConfigurationSupport {

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Override
    public FormattingConversionService mvcConversionService() {
        FormattingConversionService f = super.mvcConversionService();
        f.addConverter(new CurrencyConverter());
        return f;
    }
}
