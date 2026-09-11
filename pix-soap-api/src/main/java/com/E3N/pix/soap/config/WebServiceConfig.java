package com.E3N.pix.soap.config;

import com.E3N.pix.soap.excptionHandler.ProblemSoapFaultResolver;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.XsdSchemaCollection;
import org.springframework.xml.xsd.commons.CommonsXsdSchemaCollection;

@EnableWs
@Configuration
public class WebServiceConfig {

    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
        jaxb2Marshaller.setContextPath("com.E3N.pix.soap.contract");
        return jaxb2Marshaller;
    }

    @Bean
    public ProblemSoapFaultResolver problemSoapFaultResolver(Jaxb2Marshaller marshaller) {
        ProblemSoapFaultResolver resolver = new ProblemSoapFaultResolver(marshaller);
        resolver.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return resolver;
    }

    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcher(
            ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean<>(servlet, "/ws/*");
    }

    @Bean(name = "pix")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchemaCollection pixSchema) {
        DefaultWsdl11Definition defaultWsdl11Definition = new DefaultWsdl11Definition();
        defaultWsdl11Definition.setPortTypeName("PixPort");
        defaultWsdl11Definition.setLocationUri("/ws");
        defaultWsdl11Definition.setTargetNamespace("https://pix.com/soap/contract");
        defaultWsdl11Definition.setSchemaCollection(pixSchema);
        return defaultWsdl11Definition;
    }


    @Bean
    public XsdSchemaCollection pixSchema() {
        CommonsXsdSchemaCollection commonsXsdSchemaCollection = new CommonsXsdSchemaCollection(new ClassPathResource("xsd/pix.xsd"));
        commonsXsdSchemaCollection.setInline(true);
        return commonsXsdSchemaCollection;
    }

}
