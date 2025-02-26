package com.online.buy.registration.processor.controller;

import com.online.buy.registration.processor.dto.ClientDto;
import com.online.buy.registration.processor.mapper.ClientMapper;
import com.online.buy.registration.processor.model.ClientModel;
import com.online.buy.registration.processor.service.ClientService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client")
@AllArgsConstructor
public class RegisterClientController {

    private final ClientService clientService;

    @PostMapping("/register-client")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<String> registerClient(@RequestBody @Valid ClientDto clientDto) {
        ClientModel client = clientService.registerClient(ClientMapper.clientRequestDtoToClientModelMapper(clientDto, new ClientModel()));
        return ResponseEntity.ok("Client registered successfully with ID: " + client.getId());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDto> getClient(@PathVariable @NotNull Long id) {
        ClientModel client = clientService.findById(id);
        return ResponseEntity.ok(ClientMapper.clientModelToClientRequestDto(client, new ClientDto()));
    }

}
