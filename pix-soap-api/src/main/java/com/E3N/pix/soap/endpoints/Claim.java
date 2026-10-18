package com.E3N.pix.soap.endpoints;

import com.E3N.pix.application.claim.CreateClaimUseCase;
import com.E3N.pix.application.claim.dto.createClaim.ClaimOutputDto;
import com.E3N.pix.domain.validation.Notification;
import com.E3N.pix.infrastructure.modules.claim.ClaimRepositoryImpl;
import com.E3N.pix.infrastructure.modules.ownership.owner.OwnerRepositoryImpl;
import com.E3N.pix.service.Either;
import com.E3N.pix.soap.contract.CreateClaimRequest;
import com.E3N.pix.soap.contract.CreateClaimResponse;
import com.E3N.pix.soap.excptionHandler.HandleError;
import com.E3N.pix.soap.excptionHandler.SoapFaultException;
import com.E3N.pix.soap.mapper.claim.CreateClaimRequestToDtoMapper;
import com.E3N.pix.soap.mapper.claim.DtoToClaimResponseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.ws.soap.server.endpoint.annotation.SoapAction;

@Endpoint("claims")
public class Claim {
    private static final String NAME_SPACE_URI = "http://pix.com/soap/contract";
    private static final String SOAP_ACTION_PREFIX = NAME_SPACE_URI + "/";

    private final ClaimRepositoryImpl claimRepository;
    private final OwnerRepositoryImpl ownerRepository;

    @Autowired
    public Claim(ClaimRepositoryImpl claimRepository, OwnerRepositoryImpl ownerRepository) {
        this.claimRepository = claimRepository;
        this.ownerRepository = ownerRepository;
    }

    @PayloadRoot(namespace = NAME_SPACE_URI, localPart = "CreateClaimRequest")
    @ResponsePayload
    @SoapAction(SOAP_ACTION_PREFIX + "CreateClaimRequest")
    public CreateClaimResponse createClaim(@RequestPayload CreateClaimRequest request){
        try {
            CreateClaimUseCase useCase = new CreateClaimUseCase(claimRepository, ownerRepository);
            var dto = CreateClaimRequestToDtoMapper.from(request.getClaim());
            Either<Notification, ClaimOutputDto> result = useCase.execute(dto);
            return result.fold(
                    notification -> {
                        throw  new SoapFaultException("Invalid request", notification);
                    },
                    DtoToClaimResponseMapper::toResponse
            );
        } catch (Exception e){
            throw HandleError.handleError(e);
        }
    }
}
