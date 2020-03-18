package com.demo.atlasrest.configuration

import feign.auth.BasicAuthRequestInterceptor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FeignClientConfiguration {

    @Bean
    fun basicAuthRequestInterceptor():BasicAuthRequestInterceptor{
        return BasicAuthRequestInterceptor("admin","R=W/2ndfAkRI")
    }

}