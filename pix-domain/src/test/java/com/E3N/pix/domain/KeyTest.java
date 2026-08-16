package com.E3N.pix.domain;

import com.E3N.pix.domain.key.TypeKey;
import com.E3N.pix.domain.key.Key;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.stream.Stream;

public class KeyTest extends UnitTest {
    // phone validation
    @ParameterizedTest
    @ValueSource(strings = {
            "+5511987654321",
            "+5521998765432",
            "+5531976543210",
            "+5541965432109",
            "+5551954321098",
            "+5561943210987",
            "+5571932109876",
            "+5581921098765",
            "+5585910987654",
            "+5591909876543",
    })
    public void validatePhone(final String phone) {
        var key = Key.validatePhone(phone);
        Assertions.assertInstanceOf(Key.class, key);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "+551198765432",     // missing one digit (too short)
            "+55119876543210",   // one digit too many (too long)
            "5511987654321",     // missing the + sign
            "+5511887654321",    // missing the mandatory 9 prefix
            "+55119876543",      // 9 present but only 7 digits after (too short)
            "+5500987654321",    // invalid DDD (00 doesn't exist)
            "+5511a87654321",    // contains a letter
            "+55 11 98765-4321", // has spaces and a dash (not normalized)
            "+1511987654321",    // wrong country code (should be 55, not 15+1)
            "++5511987654321",   // double plus sign
    })
    @NullSource
    public void invalidatePhone(final String phone) {
        var key = Key.validatePhone(phone);
        Assertions.assertNull(key);
    }

    // validation e-mail
    @ParameterizedTest
    @ValueSource(strings = {
            "joao.silva@gmail.com",
            "maria_santos@outlook.com",
            "carlos-oliveira@empresa.com.br",
            "ana.paula123@yahoo.com",
            "pedro.costa+newsletter@gmail.com",
            "juliana.lima@dominio.co.uk",
            "rafael_souza99@hotmail.com",
            "fernanda.alves@meusite.com.br",
            "lucas.pereira@icloud.com",
            "beatriz.rocha@teste-empresa.org",
    })
    public void validateEmail(final String email) {
        var key = Key.validateEmail(email);
        Assertions.assertInstanceOf(Key.class, key);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "usuario@dominio",
            "@dominio.com",
            "usuario@",
            "usuario domínio@dominio.com",
            "usuario@@dominio.com",
            "usuario@.com",
            "usuario@dominio..com",
            "usuário@dominio.com",
            ".usuario@dominio.com",
            "usuario@dominio.c",
    })
    @NullSource
    public void invalidateEmail(final String email) {
        var key = Key.validateEmail(email);
        Assertions.assertNull(key);
    }

    //validate cnpj
    @ParameterizedTest
    @ValueSource(strings = {
            "41639206000143",
            "52898357000132",
            "87716166000197",
            "74196838000139",
            "89041353000143",
            "04052208000105",
            "22267631000104",
            "69419257000189",
            "26331327000130",
            "47065452000115",
    })
    public void validateAlphaNumericCnpj(final String cnpj) {
        var key = Key.validateCnpj(cnpj);
        Assertions.assertInstanceOf(Key.class, key);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "41636206000143",
            "52896357000132",
            "87715166000197",
            "74186838000139",
            "89046353000143",
            "04056208000105",
            "22266631000104",
            "69416257000189",
            "26331327000131",
            "47065452000105",
    })
    @NullSource
    public void invalidateAlphaNumericCnpj(final String cnpj) {
        var key = Key.validateCnpj(cnpj);
        Assertions.assertNull(key);
    }

    // validate key EVP
    @ParameterizedTest
    @ValueSource(strings = {
            "f47ac10b-58cc-4372-a567-0e02b2c3d479",
            "9b2e1a3c-6f4d-4e8a-b1c2-3d4e5f6a7b8c",
            "c3d4e5f6-a7b8-49c0-91d2-e3f4a5b6c7d8",
            "1a2b3c4d-5e6f-4789-a0b1-c2d3e4f5a6b7",
            "7e8f9a0b-1c2d-4e3f-8a9b-0c1d2e3f4a5b",
            "d0e1f2a3-b4c5-46d7-98e9-f0a1b2c3d4e5",
            "5f6a7b8c-9d0e-41f2-a3b4-c5d6e7f8a9b0",
            "e2f3a4b5-c6d7-48e9-90f1-a2b3c4d5e6f7",
            "3b4c5d6e-7f8a-49b0-81c2-d3e4f5a6b7c8",
            "a9b0c1d2-e3f4-4506-97a8-b9c0d1e2f3a4",
    })
    public void validateEVP(final String EVP) {
        var key = Key.validateEVP(EVP);
        Assertions.assertInstanceOf(Key.class, key);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "f47ac10b-58cc-4372-a567-0e02b2c3d47",      // missing one character (too short)
            "f47ac10b-58cc-4372-a567-0e02b2c3d4799",    // one character too many
            "f47ac10b58cc-4372-a567-0e02b2c3d479",      // missing a dash
            "f47ac10b-58cc-4372-a567-0e02b2c3d47g",     // contains invalid character 'g'
            "f47ac10b_58cc_4372_a567_0e02b2c3d479",     // underscores instead of dashes
            "f47ac10b-58cc-4372-a567",                  // missing last segment
            "-58cc-4372-a567-0e02b2c3d479",             // missing first segment
            "f47ac10b--58cc-4372-a567-0e02b2c3d479",    // double dash
            "F47AC10B-58CC-4372-A567-0E02B2C3D479Z",    // extra trailing character
            "not-a-uuid-at-all-1234",                   // completely wrong format
    })
    @NullSource
    public void invalidateEVP(final String EVP) {
        var key = Key.validateEVP(EVP);
        Assertions.assertNull(key);
    }

    // validate cpf
    @ParameterizedTest
    @ValueSource(strings = {
            "87249573055",
            "82771651297",
            "81886028761",
            "25972336189",
            "54382243408",
            "14266386125",
            "19015950482",
            "40997630302",
            "79147993510",
            "06372974827",
    })
    public void validateCpf(final String cpf){
        var key = Key.validateCpf(cpf);
        Assertions.assertInstanceOf(Key.class, key);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "87249573054",
            "82771651287",
            "81889028761",
            "25979336189",
            "54389243408",
            "14269386125",
            "19019950482",
            "40999630302",
            "79149993510",
            "06379974827",
    })
    public void invalidateCpf(final String cpf){
        var key = Key.validateCpf(cpf);
        Assertions.assertNull(key);
    }

    @ParameterizedTest
    @MethodSource("provider")
    public void givenAValidTypeOfKey_shouldReturnAKeys(final String value, final TypeKey type){
        var k = Key.getInstance(value, type);
        Assertions.assertInstanceOf(Key.class, k);
        Assertions.assertEquals(type, k.getType());
    }

    static Stream<Arguments> provider() {
        return Stream.of(
                Arguments.of("06372974827", TypeKey.CPF),
                Arguments.of("84686187000119", TypeKey.CNPJ),
                Arguments.of("S46Y7RD0000125", TypeKey.CNPJ),
                Arguments.of("+5535998697235", TypeKey.PHONE),
                Arguments.of("3b4c5d6e-7f8a-49b0-81c2-d3e4f5a6b7c8", TypeKey.EVP),
                Arguments.of("carlos-oliveira@empresa.com.br", TypeKey.EMAIL)
        );
    }
}
