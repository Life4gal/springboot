package com.springboot.eft;

import com.spring4all.swagger.EnableSwagger2Doc;
import com.springboot.eft.additional_utils.FileExecutor;
import com.springboot.eft.additional_utils.MailSender;
import com.springboot.eft.additional_utils.ReflectUtils;
import com.springboot.eft.additional_utils.config.JsonParser;
import com.springboot.eft.config.TokenConfig;
import com.springboot.eft.modules.constant.ConfigConstants;
import com.springboot.eft.modules.constant.DefaultValues;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;

@SpringBootApplication
@EnableSwagger2Doc
@MapperScan("com.springboot.eft.dao")
@EnableTransactionManagement
public class EftApplication {

	public static JsonParser settings = new JsonParser();

	public static List<Class<?>> controllers;

	public static Hashtable<String, Integer> tokens;

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		settings.setJsonObject(FileExecutor.read(EftApplication.class.getResourceAsStream(DefaultValues.SETTING_PATH)));
		MailSender.config(settings.getObjectUseEval(ConfigConstants.EMAIL_CONFIG_OF_SETTINGS));
		controllers = ReflectUtils.getClasses(DefaultValues.CONTROLLER_PACKAGE);
		tokens = TokenConfig.loadToken();
		SpringApplication.run(EftApplication.class, args);
	}

}
