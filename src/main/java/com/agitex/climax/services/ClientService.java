package com.agitex.climax.services;

import com.agitex.climax.dtos.ClientDTO;
import com.agitex.climax.dtos.ProfessionSalaireDTO;
import com.agitex.climax.entities.Client;
import com.agitex.climax.mapper.ClientMapper;
import com.agitex.climax.repositories.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    /**
     * Save a Client.
     *
     * @param clientDTO the entity to save.
     * @return the persisted entity.
     */
    public ClientDTO save(final ClientDTO clientDTO) {
        log.debug("Request to save a client : {}", clientDTO);
        Client client = clientMapper.toEntity(clientDTO);
        client = clientRepository.save(client);
        return clientMapper.toDto(client);
    }

    /**
     * Get all the clients.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ClientDTO> findAll() {
        log.debug("Request to get all clients");
        List<Client> clients = clientRepository.findAllByDeletedFalse();
        return clients.stream().map(clientMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Get one Client by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ClientDTO> findOne(final Long id) {
        log.debug("Request to get Client : {}", id);
        return clientRepository.findById(id)
                .map(clientMapper::toDto);
    }

    /**
     * Delete the Client by id.
     *
     * @param id the id of the entity.
     */
    public void delete(final Long id) {
        log.debug("Request to delete Client : {}", id);
        Client client = clientRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Aucun Client avec cet identifiant existe dans la base de données"));
        client.setDeleted(true);
        clientRepository.save(client);
    }

    /**
     * Get Average of salary by profession.
     *
     * @return ProfessionDTO.
     */
    public List<ProfessionSalaireDTO> averageSalaireByProfession() {
        log.debug("Request to get all average of salary by profession");
        return clientRepository.findAverageSalaryByProfession().stream()
                .map(object -> {
                    String profession = object[0].toString();
                    BigDecimal salaireMoyen =
                            (object[1] != null) ? new BigDecimal(object[1].toString()) : BigDecimal.ZERO;
                    return new ProfessionSalaireDTO(profession, salaireMoyen);
                })
                .collect(Collectors.toList());
    }
}
