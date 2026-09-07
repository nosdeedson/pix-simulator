package com.E3N.pix.soap.endpoints;

import com.E3N.pix.application.CreateEntryKeyUseCase;
import com.E3N.pix.domain.modules.owner.owner.Owner;
import com.E3N.pix.domain.validation.Notification;
import com.E3N.pix.service.Either;
import com.E3N.pix.soap.contract.CreateEntryKeyRequest;
import com.E3N.pix.soap.contract.CreateEntryKeyResponse;
import com.E3N.pix.soap.excptionHandler.SoapFaultException;
import com.E3N.pix.soap.mapper.EntryKeyResponseMapper;
import com.E3N.pix.soap.mapper.OwnerDtoMapper;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;


@Endpoint
public class EntryKey {
    private static final String NAME_SPACE_URI = "http://pix.com/soap/contract";

    @PayloadRoot(namespace = NAME_SPACE_URI, localPart = "CreateEntryKeyRequest")
    @ResponsePayload
    public CreateEntryKeyResponse createEntryKey(@RequestPayload CreateEntryKeyRequest request) {
        try {
            CreateEntryKeyUseCase useCase = new CreateEntryKeyUseCase(null);
            var dto = OwnerDtoMapper.from(request);
            Either<Notification, Owner> result = useCase.createOrUpdateEntryKey(dto);
            return result.fold(
                    notification -> {
                        throw new SoapFaultException("Invalid request", notification);
                    },
                    EntryKeyResponseMapper::from
            );
        } catch (RuntimeException e) {
            throw new SoapFaultException("Error while processing the request", null);
        }
    }
}
