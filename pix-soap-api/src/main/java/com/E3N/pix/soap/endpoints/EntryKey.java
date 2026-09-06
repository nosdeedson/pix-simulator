package com.E3N.pix.soap.endpoints;

import com.E3N.pix.application.CreateEntryKeyUseCase;
import com.E3N.pix.domain.modules.owner.owner.Owner;
import com.E3N.pix.domain.validation.Notification;
import com.E3N.pix.service.Either;
//import com.E3N.pix.soap.contract.CreateEntryKeyRequest;
//import com.E3N.pix.soap.contract.CreateEntryKeyResponse;
//import com.E3N.pix.soap.mapper.EntryKeyResponseMapper;
import com.E3N.pix.soap.mapper.OwnerDtoMapper;
import org.springframework.ws.context.MessageContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class EntryKey {
    private static final String NAME_SPACE_URI = "http://pix.com/soap/contract";

//    @Autowired
//    private final CreateEntryKeyUseCase useCase;
//
//    public EntryKey(CreateEntryKeyUseCase useCase) {
//        this.useCase = useCase;
//    }

//    @PayloadRoot(namespace = NAME_SPACE_URI, localPart = "CreateEntryKeyRequest")
//    @ResponsePayload
//    public Object createEntryKey(@RequestPayload CreateEntryKeyRequest request,
//                                 MessageContext messageContext) {
//        try {
//            var dto = OwnerDtoMapper.from(request);
//            return this.useCase.createOrUpdateEntryKey(dto)
//                    .fold(
//                            notification -> EntryKeyResponseMapper.from(notification, messageContext),
//                            EntryKeyResponseMapper::from
//                    );
//        } catch (RuntimeException e) {
//            return EntryKeyResponseMapper.getErrorResponse();
//        }
//    }
}
