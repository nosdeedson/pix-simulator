package com.E3N.pix.soap.endpoints;

import com.E3N.pix.application.claim.CreateClaimUseCase;
import com.E3N.pix.application.claim.GetClaimUseCase;
import com.E3N.pix.application.claim.ListClaimUseCase;
import com.E3N.pix.application.claim.dto.createClaim.ClaimOutputDto;
import com.E3N.pix.domain.validation.Notification;
import com.E3N.pix.infrastructure.modules.claim.ClaimRepositoryImpl;
import com.E3N.pix.infrastructure.modules.ownership.owner.OwnerRepositoryImpl;
import com.E3N.pix.service.Either;
import com.E3N.pix.soap.contract.*;
import com.E3N.pix.soap.excptionHandler.HandleError;
import com.E3N.pix.soap.excptionHandler.SoapFaultException;
import com.E3N.pix.soap.mapper.claim.CreateClaimRequestToDtoMapper;
import com.E3N.pix.soap.mapper.claim.DtoToCreateClaimResponseMapper;
import com.E3N.pix.soap.mapper.claim.DtoToListClaimResponseMapper;
import com.E3N.pix.soap.mapper.claim.ListClaimRequestToDtoMapper;
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
    public CreateClaimResponse createClaim(@RequestPayload CreateClaimRequest request) {
        try {
            CreateClaimUseCase useCase = new CreateClaimUseCase(claimRepository, ownerRepository);
            var dto = CreateClaimRequestToDtoMapper.from(request.getClaim());
            Either<Notification, ClaimOutputDto> result = useCase.execute(dto);
            return result.fold(
                    notification -> {
                        throw new SoapFaultException("Invalid request", notification);
                    },
                    DtoToCreateClaimResponseMapper::toResponse
            );
        } catch (Exception e) {
            throw HandleError.handleError(e);
        }
    }

    @PayloadRoot(namespace = NAME_SPACE_URI, localPart = "ListClaimsRequest")
    @ResponsePayload
    @SoapAction(SOAP_ACTION_PREFIX + "ListClaimsRequest")
    public ListClaimsResponse listClaim(@RequestPayload ListClaimsRequest request) {
        try {
            ListClaimUseCase useCase = new ListClaimUseCase(claimRepository, ownerRepository);
            var dto = ListClaimRequestToDtoMapper.from(request);
            var result = useCase.execute(dto);
            return  result.fold(
                    notification -> {throw new SoapFaultException("Invalid request", notification);},
                    DtoToListClaimResponseMapper::getResponse
            );
        } catch (Exception e) {
            throw HandleError.handleError(e);
        }
    }

    @PayloadRoot(namespace = NAME_SPACE_URI, localPart = "GetClaimRequest")
    @ResponsePayload
    @SoapAction(SOAP_ACTION_PREFIX + "GetClaimRequest")
    public GetClaimResponse getClaim(@RequestPayload GetClaimRequest request){
        try {
            GetClaimUseCase getClaimUseCase = new GetClaimUseCase(claimRepository, ownerRepository);
            var result = getClaimUseCase.execute(request.getClaimId());
            return  result.fold(
                    notification -> {throw new SoapFaultException("Invalid request", notification);},
                    DtoToListClaimResponseMapper::getClaimResponse
            );
        } catch (Exception e) {
            throw HandleError.handleError(e);
        }
    }

}
