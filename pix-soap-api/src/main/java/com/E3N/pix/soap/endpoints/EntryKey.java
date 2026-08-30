package com.E3N.pix.soap.endpoints;

import com.E3N.pix.soap.contract.CreateEntryKeyResponse;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import com.E3N.pix.soap.contract.CreateEntryKeyRequest;
@Endpoint
public class EntryKey {
    private static final String NAME_SPACE_URI = "http://pix.com/soap/contract";

    @PayloadRoot(namespace = NAME_SPACE_URI, localPart = "CreateEntryKeyRequest")
    @ResponsePayload
    public Object createEntryKey(@RequestPayload CreateEntryKeyRequest request){
        System.out.println(request);

        return new CreateEntryKeyResponse();
    }
}
