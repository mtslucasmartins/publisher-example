package br.com.lucasmartins.publisher.config;

import org.directwebremoting.spring.DwrSpringServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;


/**
 * Add spring.xml to configure dwr scanning.
 * Springboot needs to load spring.xml and do the following configuration
 * 
 */
@Configuration
@ImportResource("classpath:spring/spring.xml")
public class DwrConfiguration {
     
    /**
     * Join the DWR servlet, which is equivalent to configuring in xml
     */
    @Bean
    public ServletRegistrationBean<DwrSpringServlet> servletRegistrationBean() {
        
        DwrSpringServlet servlet = new DwrSpringServlet();

        ServletRegistrationBean<DwrSpringServlet> registrationBean = new ServletRegistrationBean<DwrSpringServlet>(servlet, "/dwr/*");
        
        // Set to true to enable DWR to debug and enter the test page.
        registrationBean.setName("dwr");
        // registrationBean.addInitParameter("debug", "true");


        //pollAndCometEnabled set to true to increase the loadability of the server, 
        // although DWR has a mechanism to protect the server from overload.
        registrationBean.addInitParameter("pollAndCometEnabled", "true");
        
        registrationBean.addInitParameter("activeReverseAjaxEnabled", "true");
        registrationBean.addInitParameter("maxWaitAfterWrite", "60");

        return registrationBean;
    }

    /* @Bean
    public ServletRegistrationBean servletRegistrationBean() {
        ServletRegistrationBean registration = new ServletRegistrationBean(new DwrSpringServlet());
        registration.setName("dwr");
        registration.setEnabled(true);
        registration.addUrlMappings("/dwr/*");
        Map<String, String> initParameters = new HashMap<>(1);
        initParameters.put("debug", "true");
        registration.setInitParameters(initParameters);
        return registration;
    } */

}
