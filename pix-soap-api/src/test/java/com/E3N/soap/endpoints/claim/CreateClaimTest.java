package com.E3N.soap.endpoints.claim;

import com.E3N.pix.domain.modules.claim.TypeClaim;
import com.E3N.pix.domain.modules.ownership.entryKey.Reason;
import com.E3N.pix.domain.modules.ownership.owner.Owner;
import com.E3N.pix.domain.shared.TypeKey;
import com.E3N.pix.domain.shared.TypePerson;
import com.E3N.pix.infrastructure.modules.claim.ClaimRepositoryImpl;
import com.E3N.pix.infrastructure.modules.ownership.owner.OwnerRepositoryImpl;
import com.E3N.pix.soap.contract.CreateClaimResponse;
import com.E3N.pix.soap.contract.KeyType;
import com.E3N.pix.soap.contract.TypeClaims;
import com.E3N.pix.soap.endpoints.Claim;
import com.E3N.pix.soap.excptionHandler.SoapFaultException;
import com.E3N.soap.UnitTest;
import com.E3N.soap.mapper.mocks.claim.ClaimRequestMock;
import com.E3N.soap.mapper.mocks.entryKey.OwnerMock;
import com.E3N.test.Owner.RandomKeysMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateClaimTest extends UnitTest {

    public static com.E3N.pix.domain.modules.claim.Claim mockClaim(Owner claimer, Owner donor, TypeClaim typeClaim) {
        return com.E3N.pix.domain.modules.claim.Claim.getInstance(
                claimer.getAccounts().getFirst().getId().toString(),
                claimer.getAccounts().getFirst().getParticipant().getParticipant(),
                donor.getAccounts().getFirst().getParticipant().getParticipant(),
                claimer.getAccounts().getFirst().getEntryKeys().getFirst().getKey().getKey(),
                claimer.getTaxIdNumber().getTaxIdNumber(),
                typeClaim,
                donor.getAccounts().getFirst().getEntryKeys().getFirst().getKey().getType(),
                claimer.getType()
        );
    }

    @Mock
    private OwnerRepositoryImpl ownerRepository;

    @Mock
    private ClaimRepositoryImpl claimRepository;

    private Claim claim;

    @BeforeEach
    void setUp() {
        claim = new Claim(claimRepository, ownerRepository);
    }

    @Test
    void shouldBeInstantiated() {
        Assertions.assertNotNull(ownerRepository);
        Assertions.assertNotNull(claimRepository);
        Assertions.assertNotNull(claim);
    }

    @Test
    void givenInvalidRequest_whenCalling_execute_shouldReturnNotificationClaimerNotFound() {
        var request = ClaimRequestMock.createClaimRequestMock(RandomKeysMock.randomEmails(), KeyType.EMAIL, TypeClaims.PORTABILITY);
        var result = Assertions.assertThrows(SoapFaultException.class, () -> claim.createClaim(request));
        Assertions.assertNotNull(result);
        Assertions.assertEquals("Claimer not found.", result.getNotification().getDetail());
    }

    @Test
    void givenInvalidRequest_whenCalling_execute_shouldReturnDonorNotFound() {
        var owner = OwnerMock.createOwner(TypePerson.LEGAL_PERSON);
        when(ownerRepository.findByAccountNumberAndParticipantAndBranchAndTaxIdNumber(anyString(), anyString(),
                anyString(), anyString()))
                .thenReturn(Optional.of(owner));
        var request = ClaimRequestMock.from(owner, TypeClaims.PORTABILITY, RandomKeysMock.randomPhone(), TypeKey.PHONE);
        var result = Assertions.assertThrows(SoapFaultException.class, () -> claim.createClaim(request));
        Assertions.assertNotNull(result);
        Assertions.assertEquals("Donor not found", result.getNotification().getDetail());
    }

    @Test
    void givenInvalidRequest_whenCalling_execute_shouldReturnTypeClaimInvalid() {
        var owner = OwnerMock.createOwner(TypePerson.LEGAL_PERSON, Reason.USER_REQUESTED, TypeKey.PHONE);
        when(ownerRepository.findByAccountNumberAndParticipantAndBranchAndTaxIdNumber(anyString(), anyString(),
                anyString(), anyString()))
                .thenReturn(Optional.of(owner));
        var donor = OwnerMock.withDifferentParticipant(owner);
        when(ownerRepository.findByKey(anyString()))
                .thenReturn(Optional.of(donor));

        var request = ClaimRequestMock.from(
                owner, TypeClaims.PORTABILITY,
                donor.getAccounts().getFirst().getEntryKeys().getFirst().getKey().getKey(),
                donor.getAccounts().getFirst().getEntryKeys().getFirst().getKey().getType()
        );
        var result = Assertions.assertThrows(SoapFaultException.class, () -> claim.createClaim(request));
        Assertions.assertNotNull(result);
        Assertions.assertEquals("Type of claim must be Ownership", result.getNotification().getDetail());
    }

    @Test
    void givenValidRequest_whenCalling_execute_shouldCreateAClaimAsOwnership() {
        var owner = OwnerMock.createOwner(TypePerson.LEGAL_PERSON, Reason.USER_REQUESTED, TypeKey.PHONE);
        when(ownerRepository.findByAccountNumberAndParticipantAndBranchAndTaxIdNumber(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(Optional.of(owner));
        var donor = OwnerMock.withDifferentParticipant(owner);
        when(ownerRepository.findByKey(anyString())).thenReturn(Optional.of(donor));
        var claimSaved = mockClaim(owner, donor, TypeClaim.OWNERSHIP);
        when(claimRepository.save(any())).thenReturn(claimSaved);
        var request = ClaimRequestMock.from(owner, TypeClaims.OWNERSHIP, donor.getAccounts().getFirst().getEntryKeys().getFirst().getKey().getKey(),
                donor.getAccounts().getFirst().getEntryKeys().getFirst().getKey().getType());
        var result = claim.createClaim(request);
        Assertions.assertInstanceOf(CreateClaimResponse.class, result);
        Assertions.assertEquals(owner.getTaxIdNumber().getTaxIdNumber(), result.getClaim().getClaimer().getTaxIdNumber());
        Assertions.assertEquals(donor.getAccounts().getFirst().getParticipant().getParticipant(), result.getDonorParticipant());
    }

    @Test
    void givenValidRequest_whenCalling_execute_shouldCreateAClaimAsPortability() {
        var owner = OwnerMock.createOwner(TypePerson.LEGAL_PERSON, Reason.USER_REQUESTED, TypeKey.EMAIL);
        when(ownerRepository.findByAccountNumberAndParticipantAndBranchAndTaxIdNumber(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(Optional.of(owner));
        var donor = OwnerMock.withDifferentParticipant(owner);
        when(ownerRepository.findByKey(anyString())).thenReturn(Optional.of(donor));
        var claimSaved = mockClaim(owner, donor, TypeClaim.PORTABILITY);
        when(claimRepository.save(any())).thenReturn(claimSaved);
        var request = ClaimRequestMock.from(owner, TypeClaims.PORTABILITY, donor.getAccounts().getFirst().getEntryKeys().getFirst().getKey().getKey(),
                donor.getAccounts().getFirst().getEntryKeys().getFirst().getKey().getType());
        var result = claim.createClaim(request);
        Assertions.assertInstanceOf(CreateClaimResponse.class, result);
        Assertions.assertEquals(owner.getTaxIdNumber().getTaxIdNumber(), result.getClaim().getClaimer().getTaxIdNumber());
        Assertions.assertEquals(donor.getAccounts().getFirst().getParticipant().getParticipant(), result.getDonorParticipant());
    }

}
