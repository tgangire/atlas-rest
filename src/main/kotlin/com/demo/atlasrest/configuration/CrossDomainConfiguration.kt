package com.demo.atlasrest.configuration

import org.apache.atlas.AtlasClientV2
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

@Configuration
class CrossDomainConfiguration {

    @Bean
    fun corsFilter(): FilterRegistrationBean<*>?{
        val source= UrlBasedCorsConfigurationSource()
        val config = CorsConfiguration()
        config.allowCredentials = true
        config.addAllowedOrigin("*")
        config.addAllowedHeader("*")
        config.addAllowedMethod("*")
        config.addExposedHeader("Content-disposition")
        source.registerCorsConfiguration("/**",config)
        val bean = FilterRegistrationBean(CorsFilter(source))
        bean.order = -100000000
        return bean
    }

    /*@Bean
    fun createClient(baseUrls: Array<String>): AtlasClientV2 {
        return AtlasClientV2(baseUrls, arrayOf("user", "password"))
    }*/

}