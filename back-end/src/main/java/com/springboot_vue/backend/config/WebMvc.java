package com.springboot_vue.backend.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.alibaba.fastjson.support.spring.FastJsonViewResponseBodyAdvice;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

import com.springboot_vue.backend.common.interceptor.ClearToken;

@Configuration
public class WebMvc implements WebMvcConfigurer {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {

		registry.addInterceptor(clearTokenInterceptor()).addPathPatterns("/**");
	}

	@Bean
	public ClearToken clearTokenInterceptor() {
		return new ClearToken();
	}


	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {

		FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();

		FastJsonConfig fastJsonConfig = new FastJsonConfig();

		fastJsonConfig.setSerializerFeatures(
				SerializerFeature.PrettyFormat, SerializerFeature.WriteNullStringAsEmpty,
				SerializerFeature.DisableCircularReferenceDetect
		);


		List<MediaType> fastMediaTypes = new ArrayList<>();
		fastMediaTypes.add(MediaType.APPLICATION_JSON);

		fastConverter.setFastJsonConfig(fastJsonConfig);
		fastConverter.setSupportedMediaTypes(fastMediaTypes);

		converters.add(fastConverter);
	}


	@Bean
	public FastJsonViewResponseBodyAdvice FastJsonViewResponseBodyAdvice() {
		return new FastJsonViewResponseBodyAdvice();
	}

}
