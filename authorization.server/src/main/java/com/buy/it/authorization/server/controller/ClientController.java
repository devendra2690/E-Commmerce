package com.buy.it.authorization.server.controller;

import com.buy.it.authorization.server.dto.ClientRegisterRequest;
import com.buy.it.authorization.server.entity.Client;
import com.buy.it.authorization.server.service.ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping("/register-client")
    public ResponseEntity<String> registerClient(@RequestBody ClientRegisterRequest request) {
        Client client = clientService.registerClient(
                request.getClientId(),
                request.getRedirectUris()
        );
        return ResponseEntity.ok("Client registered successfully with ID: " + client.getId());
    }
}
