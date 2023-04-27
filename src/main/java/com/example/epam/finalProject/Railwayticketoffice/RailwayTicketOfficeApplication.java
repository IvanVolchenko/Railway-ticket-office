package com.example.epam.finalProject.Railwayticketoffice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class RailwayTicketOfficeApplication implements WebMvcConfigurer {

	private final LocaleChangeInterceptor localeChangeInterceptor;

	public RailwayTicketOfficeApplication(LocaleChangeInterceptor localeChangeInterceptor) {
		this.localeChangeInterceptor = localeChangeInterceptor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor);
	}

	public static void main(String[] args) {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasenames("lang/messages");
		messageSource.setDefaultEncoding("UTF-8");
		SpringApplication.run(RailwayTicketOfficeApplication.class, args);
	}

}
