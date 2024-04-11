package com.agitex.climax.mapper;

import com.agitex.climax.dtos.ClientDTO;
import com.agitex.climax.entities.Client;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Client} and its DTO {@link ClientDTO}.
 *
 * @author jos-arthur
 */
@Mapper(componentModel = "spring", uses = {})
public interface ClientMapper extends EntityMapper<ClientDTO, Client> {

    /**
     * @param client
     * @return
     */
    @Override
    ClientDTO toDto(Client client);

    /**
     * @param clientDTO
     * @return
     */
    @Override
    Client toEntity(ClientDTO clientDTO);

    /**
     * @param id
     * @return Client
     */
    default Client fromId(Long id) {
        if (id == null) {
            return null;
        }
        Client client = new Client();
        client.setId(id);
        return client;
    }
}
