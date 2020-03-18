package com.demo.atlasrest.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@EnableWebSecurity
class OAuth2ResourceConfiguration {

    @Configuration
     class  SsoSecurity : WebSecurityConfigurerAdapter() {

        override fun configure(web: WebSecurity){
            web.ignoring().antMatchers("/**")
        }

    }

}