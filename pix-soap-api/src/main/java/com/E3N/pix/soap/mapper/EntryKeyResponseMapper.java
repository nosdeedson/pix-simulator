package com.E3N.pix.soap.mapper;

import com.E3N.pix.domain.modules.owner.owner.Owner;
import com.E3N.pix.domain.validation.Notification;
import com.E3N.pix.domain.validation.Violation;
import com.E3N.pix.soap.contract.*;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.SoapBody;
import org.springframework.ws.soap.SoapFault;
import org.springframework.ws.soap.SoapFaultDetail;
import org.springframework.ws.soap.SoapMessage;

import javax.xml.transform.Result;
import java.util.Locale;

public abstract class EntryKeyResponseMapper {

    public static CreateEntryKeyResponse from(Owner owner) {
        var response = new CreateEntryKeyResponse();
        var entryKey = new EntryResponseType();
        response.setEntry(entryKey);
        return response;
    }

    public static MessageContext from(Notification notification, MessageContext context) {
        try {

            Problem problem = getProblem(notification);
            final SoapMessage response = (SoapMessage) context.getResponse();
            final SoapBody body = response.getSoapBody();
            final SoapFault soapFault = body.addClientOrSenderFault(
                    problem.getDetail(),
                    Locale.ENGLISH
            );
            final SoapFaultDetail detail = soapFault.addFaultDetail();
            Result result = detail.getResult();
            JAXBContext jaxbContext = JAXBContext.newInstance(Problem.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.marshal(problem, result);
            return context;
        } catch (JAXBException e) {

            // treat the exception returning appropriate error
            return null;
        }
    }

    private static Problem getProblem(Notification notification) {
        Problem problem = new Problem();
        problem.setTitle("Entry is invalid");
        problem.setType("http://pix.com/soap/contract");
        problem.setStatus(400);
        problem.setDetail("Entry has invalid values");
        notification.getViolations()
                .stream().map(it -> {
                    ViolationType value = new ViolationType(;
                }).forEach(problem.getViolations().getViolation()::add);
        return problem;
    }

    public static Problem getErrorResponse() {
        var errorGeneric = new Problem();
        errorGeneric.setTitle("Generic Error");
        errorGeneric.setType("http://pix.com/soap/contract");
        errorGeneric.setStatus(500);
        errorGeneric.setDetail("Error while processing request.");
        return errorGeneric;
    }
}
