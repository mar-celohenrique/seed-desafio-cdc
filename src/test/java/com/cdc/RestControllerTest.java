package com.cdc;

import net.jqwik.spring.JqwikSpringSupport;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@JqwikSpringSupport
@SpringBootTest
@AutoConfigureMockMvc
public @interface RestControllerTest {

}
