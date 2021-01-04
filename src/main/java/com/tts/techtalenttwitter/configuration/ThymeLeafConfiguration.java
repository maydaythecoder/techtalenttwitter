package com.tts.techtalenttwitter.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;

@Configuration
public class ThymeLeafConfiguration 
{
    @Bean
    public SpringSecurityDialect springSecurityDialect()
    {
        return new SpringSecurityDialect();
    }
    
}
