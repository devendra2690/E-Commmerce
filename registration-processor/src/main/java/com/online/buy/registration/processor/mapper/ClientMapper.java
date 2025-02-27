package com.online.buy.registration.processor.mapper;

import com.online.buy.common.code.entity.Client;
import com.online.buy.common.code.entity.OAuth2Client;
import com.online.buy.registration.processor.dto.ClientDto;
import com.online.buy.registration.processor.model.ClientModel;
import com.online.buy.registration.processor.model.ProductModel;

import java.util.List;

public class ClientMapper {

    public static OAuth2Client clientModelToOAuthClientMapper(ClientModel clientModel, OAuth2Client oAuth2Client) {

        oAuth2Client.setClientId(clientModel.getClientId());
        oAuth2Client.setRedirectUris(clientModel.getRedirectUris());
        return oAuth2Client;
    }

    public static Client clientModelToClientMapper(ClientModel clientModel, Client client) {
        client.setName(clientModel.getName());
        client.setEmail(clientModel.getEmail());
        return client;
    }

    public static ClientModel clientToClientModelMapper(Client client, ClientModel clientModel) {

        clientModel.setId(client.getId());
        clientModel.setName(client.getName());
        clientModel.setEmail(client.getEmail());
        clientModel.setUpdatedAt(client.getUpdatedAt());
        clientModel.setCreatedAt(client.getCreatedAt());
        clientModel.setRedirectUris(client.getOAuth2Client().getRedirectUris());
        clientModel.setClientId(client.getOAuth2Client().getClientId());
        clientModel.setClientSecret(client.getOAuth2Client().getClientSecret());
        List<ProductModel> productModels = client.getProducts().stream().map( product ->  ProductMapper.productToProductModel(product, new ProductModel())).toList();
        clientModel.setProductModels(productModels);

         return clientModel;
    }

    public static ClientModel clientRequestDtoToClientModelMapper(ClientDto clientRegisterDto, ClientModel clientModel) {
        clientModel.setName(clientRegisterDto.getName());
        clientModel.setEmail(clientRegisterDto.getEmail());
        clientModel.setClientSecret(clientRegisterDto.getClientSecret());
        clientModel.setRedirectUris(clientRegisterDto.getRedirectUris());
        clientModel.setClientId(clientRegisterDto.getClientId());
        return clientModel;
    }

    public static ClientDto clientModelToClientRequestDto(ClientModel clientModel, ClientDto clientRegisterDto) {

        clientRegisterDto.setId(clientModel.getId());
        clientRegisterDto.setName(clientModel.getName());
        clientRegisterDto.setEmail(clientModel.getEmail());
        clientRegisterDto.setClientSecret(clientModel.getClientSecret());
        clientRegisterDto.setRedirectUris(clientModel.getRedirectUris());
        clientRegisterDto.setClientId(clientModel.getClientId());
        return clientRegisterDto;
    }
}
