package com.E3N.pix.soap.endpoints;

import com.E3N.pix.application.ownership.CreateEntryKeyUseCase;
import com.E3N.pix.application.ownership.GetEntryKeyUseCase;
import com.E3N.pix.domain.modules.ownership.owner.Owner;
import com.E3N.pix.domain.validation.Notification;
import com.E3N.pix.infrastructure.modules.ownership.owner.OwnerRepositoryImpl;
import com.E3N.pix.service.Either;
import com.E3N.pix.soap.contract.CreateEntryKeyRequest;
import com.E3N.pix.soap.contract.CreateEntryKeyResponse;
import com.E3N.pix.soap.contract.GetEntryKeyResponse;
import com.E3N.pix.soap.excptionHandler.SoapFaultException;
import com.E3N.pix.soap.mapper.entryKey.EntryKeyResponseMapper;
import com.E3N.pix.soap.mapper.entryKey.OwnerDtoMapper;
import com.E3N.pix.soap.validation.entryKey.ValidationEntryKeyRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.*;
import org.springframework.ws.soap.server.endpoint.annotation.SoapHeader;


@Endpoint("entries")
public class EntryKey {
    private static final String NAME_SPACE_URI = "https://pix.com/soap/contract";
    private final OwnerRepositoryImpl ownerRepository;

    @Autowired
    public EntryKey(OwnerRepositoryImpl ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    @PayloadRoot(namespace = NAME_SPACE_URI, localPart = "CreateEntryKeyRequest")
    @ResponsePayload
    public CreateEntryKeyResponse createEntryKey(@RequestPayload CreateEntryKeyRequest request) {
        try {
            CreateEntryKeyUseCase useCase = new CreateEntryKeyUseCase(ownerRepository);
            var dto = OwnerDtoMapper.from(request);
            Either<Notification, Owner> result = useCase.createOrUpdateEntryKey(dto);
            return result.fold(
                    notification -> {
                        throw new SoapFaultException("Invalid request", notification);
                    },
                    EntryKeyResponseMapper::from
            );
        } catch (Exception e) {
            if (e instanceof SoapFaultException) {
                throw e;
            }
            var message = e.getMessage() != null ? e.getMessage() : e.getLocalizedMessage() != null ? e.getLocalizedMessage() : "Unknown error";
            Notification notification = Notification.create(e.getMessage(), request.getEntry().getKey(), "EntryKey.creation");
            throw new SoapFaultException(message, notification);
        }
    }

    @PayloadRoot(namespace = NAME_SPACE_URI, localPart = "GetEntryKeyResponse")
    @ResponsePayload
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
                    EntryKeyResponseMapper::getEntryKeyResponse
            );
        } catch (Exception e) {
            if (e instanceof SoapFaultException) {
                throw e;
            }
            Notification notification = Notification.create("Could not process the request.", 400, "Unknow error while processing the request.");
            throw new SoapFaultException("Failer", notification);
        }
    }
}
