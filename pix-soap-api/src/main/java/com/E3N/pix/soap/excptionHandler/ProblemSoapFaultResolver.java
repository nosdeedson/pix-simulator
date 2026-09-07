package com.E3N.pix.soap.excptionHandler;

import com.E3N.pix.domain.validation.Notification;
import com.E3N.pix.soap.contract.Problem;
import com.E3N.pix.soap.contract.ViolationType;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;
import org.springframework.ws.soap.SoapFault;
import org.springframework.ws.soap.SoapFaultDetail;
import org.springframework.ws.soap.server.endpoint.SoapFaultMappingExceptionResolver;

import javax.xml.transform.Result;

@Component
public class ProblemSoapFaultResolver extends SoapFaultMappingExceptionResolver {

    @Autowired
    private Jaxb2Marshaller marshaller;

    private static Problem getProblem(Notification notification) {
        Problem problem = new Problem();
        problem.setTitle("Entry is invalid");
        problem.setType("https://pix.com/soap/contract");
        problem.setStatus(400);
        problem.setDetail("Entry has invalid values");
        if (notification != null && notification.hasError()) {

            notification.getViolations()
                    .stream().map(it -> {
                        ViolationType value = new ViolationType();
                        value.setProperty(it.property());
                        value.setReason(it.reason());
                        value.setValue(it.value());
                        return value;
                    }).forEach(problem.getViolations().getViolation()::add);
        }
        return problem;
    }

    @Override
    protected void customizeFault(@Nullable Object endpoint, @NonNull Exception ex, SoapFault fault) {
        if (ex instanceof SoapFaultException faultException) {
            fault.setFaultActorOrRole(faultException.getMessage());
            SoapFaultDetail detail = fault.addFaultDetail();
            Problem problem = getProblem(faultException.getNotification());
            Result result = detail.getResult();
            marshaller.marshal(problem, result);
        } else {
            fault.setFaultActorOrRole(ex.getMessage());
        }
    }
}
