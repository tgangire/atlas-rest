package com.demo.atlasrest

import org.apache.atlas.AtlasClientV2
import org.bouncycastle.cms.RecipientId.password
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Bean


@SpringBootApplication
@EnableFeignClients
class AtlasRestApplication

fun main(args: Array<String>) {
	runApplication<AtlasRestApplication>(*args)
}

/*@Bean
fun createClient(baseUrls: Array<String>): AtlasClientV2 {
	return AtlasClientV2(baseUrls, arrayOf("user", "password"))
}*/

