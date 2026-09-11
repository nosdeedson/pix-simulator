package com.E3N.pix.soap.excptionHandler;

import com.E3N.pix.domain.validation.Notification;
import com.E3N.pix.domain.validation.Violation;
import com.E3N.pix.soap.contract.Problem;
import com.E3N.pix.soap.contract.ViolationType;
import com.E3N.pix.soap.contract.ViolationsType;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;
import org.springframework.ws.soap.SoapFault;
import org.springframework.ws.soap.SoapFaultDetail;
import org.springframework.ws.soap.server.endpoint.SoapFaultMappingExceptionResolver;

import javax.xml.transform.Result;

@Component
public class ProblemSoapFaultResolver extends SoapFaultMappingExceptionResolver {

    private final Jaxb2Marshaller marshaller;

    public ProblemSoapFaultResolver(Jaxb2Marshaller marshaller) {
        this.marshaller = marshaller;
    }

    private static Problem getDefaultProblem() {
        Problem problem = new Problem();
        problem.setTitle("Error while processing request.");
        problem.setType("https://pix.com/soap/contract");
        problem.setStatus(500);
        problem.setDetail("Unknown error.");
        return problem;
    }

    private static Problem getProblem(Notification notification) {
        if (notification == null) return getDefaultProblem();
        Problem problem = new Problem();
        if (notification.hasError()) {
            problem.setTitle(notification.getTitle());
            problem.setType(notification.getType());
            problem.setStatus(notification.getStatus());
            problem.setDetail(notification.getDetail());
            ViolationsType violationsType = new ViolationsType();
            var violations = notification.getViolations();
            for (Violation v : violations) {
                ViolationType value = new ViolationType();
                value.setProperty(v.property());
                value.setReason(v.reason());
                value.setValue(v.value());
                violationsType.getViolation().add(value);
            }
            problem.setViolations(violationsType);
        }
        return problem;
    }

    @Override
    public void customizeFault(@Nullable Object endpoint, @NonNull Exception ex, @NonNull SoapFault fault) {
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
