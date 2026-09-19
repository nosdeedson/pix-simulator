package com.E3N.pix.soap.endpoints;

import com.E3N.pix.application.ownership.CreateEntryKeyUseCase;
import com.E3N.pix.application.ownership.DeleteEntryKeyUseCase;
import com.E3N.pix.application.ownership.GetEntryKeyUseCase;
import com.E3N.pix.application.ownership.UpdateEntryKeyUseCase;
import com.E3N.pix.domain.modules.ownership.entryKey.Reason;
import com.E3N.pix.domain.modules.ownership.owner.Owner;
import com.E3N.pix.domain.validation.Notification;
import com.E3N.pix.infrastructure.modules.ownership.owner.OwnerRepositoryImpl;
import com.E3N.pix.service.Either;
import com.E3N.pix.soap.contract.*;
import com.E3N.pix.soap.excptionHandler.HandleError;
import com.E3N.pix.soap.excptionHandler.SoapFaultException;
import com.E3N.pix.soap.mapper.entryKey.CreateEntryKeyRequestToDtoMapper;
import com.E3N.pix.soap.mapper.entryKey.OwnerToEntryKeyResponseMapper;
import com.E3N.pix.soap.mapper.entryKey.UpdateEntryKeyRequestToDtoMapper;
import com.E3N.pix.soap.validation.entryKey.ValidationEntryKeyRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.*;
import org.springframework.ws.soap.server.endpoint.annotation.SoapAction;
import org.springframework.ws.soap.server.endpoint.annotation.SoapHeader;


@Endpoint("entries")
public class EntryKey {
    private static final String NAME_SPACE_URI = "http://pix.com/soap/contract";
    private static final String SOAP_ACTION_PREFIX = NAME_SPACE_URI + "/";
    private final OwnerRepositoryImpl ownerRepository;

    @Autowired
    public EntryKey(OwnerRepositoryImpl ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    @PayloadRoot(namespace = NAME_SPACE_URI, localPart = "CreateEntryKeyRequest")
    @ResponsePayload
    @SoapAction(SOAP_ACTION_PREFIX + "CreateEntryKey")
    public CreateEntryKeyResponse createEntryKey(@RequestPayload CreateEntryKeyRequest request) {
        try {
            CreateEntryKeyUseCase useCase = new CreateEntryKeyUseCase(ownerRepository);
            var dto = CreateEntryKeyRequestToDtoMapper.from(request);
            Either<Notification, Owner> result = useCase.createOrUpdateEntryKey(dto);
            return result.fold(
                    notification -> {
                        throw new SoapFaultException("Invalid request", notification);
                    },
                    OwnerToEntryKeyResponseMapper::from
            );
        } catch (Exception e) {
            throw HandleError.handleError(e);
        }
    }

    @PayloadRoot(namespace = NAME_SPACE_URI, localPart = "GetEntryKeyRequest")
    @ResponsePayload
    @SoapAction(SOAP_ACTION_PREFIX + "GetEntryKey")
    public GetEntryKeyResponse getEntryKey(
            @XPathParam("/key") String key,
            @XPathParam("/IncludesStatistics") Boolean includesStatistics,
            @SoapHeader("{" + NAME_SPACE_URI + "}PI-RequestingParticipant") String piRequestingParticipant,
            @SoapHeader("{" + NAME_SPACE_URI + "}PI-PayerId") String piPayerId,
            @SoapHeader("{" + NAME_SPACE_URI + "}PI-EndToEndId") String piEndToEndId
    ) {
        try {
            ValidationEntryKeyRequest.validateHeaderGetKey(piRequestingParticipant, piPayerId, piEndToEndId);
            var getUseCase = new GetEntryKeyUseCase(ownerRepository);
            Either<Notification, Owner> result = getUseCase.getEntryKey(key, includesStatistics);
            return result.fold(
                    notification -> {
                        throw new SoapFaultException("Could not get Key", notification);
                    },
                    OwnerToEntryKeyResponseMapper::getEntryKeyResponse
            );
        } catch (Exception e) {
            throw HandleError.handleError(e);
        }
    }

    @PayloadRoot(namespace = NAME_SPACE_URI, localPart = "UpdateEntryKeyRequest")
    @ResponsePayload
    @SoapAction(SOAP_ACTION_PREFIX + "UpdateEntryKey")
    public UpdateEntryKeyResponse updateEntryKey(
            @XPathParam("/key") String key,
            @RequestPayload UpdateEntryKeyRequest request
    ) {
        try {
            UpdateEntryKeyUseCase useCase = new UpdateEntryKeyUseCase(ownerRepository);
            var dto = UpdateEntryKeyRequestToDtoMapper.getDto(request);
            Either<Notification, Owner> result = useCase.execute(dto, key);
            return result.fold(
                    notification -> {
                        throw new SoapFaultException("Invalid request", notification);
                    },
                    OwnerToEntryKeyResponseMapper::getUpdateEntryKeyResponse
            );
        } catch (Exception e) {
            throw HandleError.handleError(e);
        }
    }

    @PayloadRoot(namespace = NAME_SPACE_URI, localPart = "DeleteEntryKeyRequest")
    @ResponsePayload
    @SoapAction(SOAP_ACTION_PREFIX + "DeleteEntryKey")
    public DeleteEntryKeyResponse deleteEntryKey(
            @XPathParam("/key") String key,
            @RequestPayload DeleteEntryKeyRequest request
    ) {
        try {
            var deleteKeyUseCase = new DeleteEntryKeyUseCase(ownerRepository);
            if (!request.getKey().equals(key)){
                var notification = Notification.create("Conflict", 400, "Key in the path does not match the key in body.");
                throw new SoapFaultException("Invalid request", notification);
            }
            Either<Notification, Void>  result = deleteKeyUseCase.deleteEntryKey(request.getKey(), request.getParticipant(), Reason.valueOf(request.getReason().name()));
            return result.fold(
                    notification -> {
                        throw new SoapFaultException("Invalid request", notification);
                    },
                    aVoid -> OwnerToEntryKeyResponseMapper.getDeletedKeyResponse(request.getKey())
            );
        } catch (Exception e) {
            throw HandleError.handleError(e);
        }

    }


}
