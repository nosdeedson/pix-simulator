package com.E3N.soap.exceptionHandler;


import com.E3N.pix.domain.validation.Notification;
import com.E3N.pix.domain.validation.Violation;
import com.E3N.pix.soap.contract.Problem;
import com.E3N.pix.soap.contract.ViolationType;
import com.E3N.pix.soap.excptionHandler.ProblemSoapFaultResolver;
import com.E3N.pix.soap.excptionHandler.SoapFaultException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.soap.SoapFault;
import org.springframework.ws.soap.SoapFaultDetail;

import javax.xml.transform.Result;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProblemSoapFaultResolverTest {

    @Mock
    private Jaxb2Marshaller marshaller;

    @Mock
    private SoapFault soapFault;

    @Mock
    private SoapFaultDetail soapFaultDetail;

    @Mock
    private Result result;

    private ProblemSoapFaultResolver resolver;

    @BeforeEach
    void setUp() {
        resolver = new ProblemSoapFaultResolver(marshaller);
    }

    @Test
    public void should_resolver_beInstantiated() {
        assertNotNull(resolver);
    }

    @Test
    void customizeFault_WithStandardException_ShouldOnlySetFaultActor() {
        Exception e = new RuntimeException("Run time error");
        resolver.customizeFault(null, e, soapFault);
        verify(soapFault).setFaultActorOrRole("Run time error");
        Mockito.verifyNoInteractions(marshaller);
    }

    @Test
    void customizeFault_WithSoapFaultException_ShouldMarshalProblemDetail() {
        // Arrange
        String exceptionMessage = "Invalid entry provided";

        // Mocking custom notification and violations
        Notification notification = Notification.create(
                "https://pix.com/soap/contract",
                "Entry is invalid",
                400,
                "Entry has invalid values");
        Violation violation = new Violation("Invalid taxIdNumber.", "12345678912", "EntryKey");
        notification.getViolations().add(violation);


        SoapFaultException ex = new SoapFaultException(exceptionMessage, notification);

        when(soapFault.addFaultDetail()).thenReturn(soapFaultDetail);
        when(soapFaultDetail.getResult()).thenReturn(result);

        // Act
        resolver.customizeFault(null, ex, soapFault);

        // Assert
        verify(soapFault).setFaultActorOrRole(exceptionMessage);
        verify(soapFault).addFaultDetail();

        // Capture the mapped Problem object sent to the marshaller
        ArgumentCaptor<Problem> problemCaptor = ArgumentCaptor.forClass(Problem.class);
        verify(marshaller).marshal(problemCaptor.capture(), eq(result));

        // Verify the properties mapped inside the Problem object
        Problem capturedProblem = problemCaptor.getValue();
        assertNotNull(capturedProblem);
        assertEquals("Entry is invalid", capturedProblem.getTitle());
        assertEquals("https://pix.com/soap/contract", capturedProblem.getType());
        assertEquals(400, capturedProblem.getStatus());
        assertEquals("Entry has invalid values", capturedProblem.getDetail());

        // Verify violations mapping
        assertEquals(1, capturedProblem.getViolations().getViolation().size());
        ViolationType capturedViolation = capturedProblem.getViolations().getViolation().get(0);
        assertEquals("EntryKey", capturedViolation.getProperty());
        assertEquals("Invalid taxIdNumber.", capturedViolation.getReason());
        assertEquals("12345678912", capturedViolation.getValue());
    }

    @Test
    void customizeFault_WithSoapFaultExceptionAndNoViolations_ShouldMarshalProblemWithoutViolations() {
        // Arrange
        SoapFaultException ex = new SoapFaultException("Error", null); // null notification

        when(soapFault.addFaultDetail()).thenReturn(soapFaultDetail);
        when(soapFaultDetail.getResult()).thenReturn(result);

        // Act
        resolver.customizeFault(null, ex, soapFault);

        // Assert
        ArgumentCaptor<Problem> problemCaptor = ArgumentCaptor.forClass(Problem.class);
        verify(marshaller).marshal(problemCaptor.capture(), eq(result));

        Problem capturedProblem = problemCaptor.getValue();
        assertNotNull(capturedProblem);
        assertNull(capturedProblem.getViolations());

        assertEquals("Error while processing request.", capturedProblem.getTitle());
        assertEquals("https://pix.com/soap/contract", capturedProblem.getType());
        assertEquals(500, capturedProblem.getStatus());
        assertEquals("Unknown error.", capturedProblem.getDetail());
    }

}
