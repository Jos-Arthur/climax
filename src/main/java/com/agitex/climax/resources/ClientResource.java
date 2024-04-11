package com.agitex.climax.resources;

import com.agitex.climax.dtos.ClientDTO;
import com.agitex.climax.dtos.ProfessionSalaireDTO;
import com.agitex.climax.services.ClientService;
import com.agitex.climax.utils.HeaderUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/v1/api/")
public class ClientResource {

    private static final String ENTITY_NAME = "Client";

    private final ClientService clientService;

    /**
     * Instantiates a new Client resource.
     *
     * @param clientService    the client service
     */
    public ClientResource(final ClientService clientService) {
        this.clientService = clientService;
    }

    /**
     * {@code POST  /clients} : Create a new client.
     *
     * @param clientDTO the clientDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new clientDTO,
     * or with status {@code 400 (Bad Request)} if the client has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/clients")
    public ResponseEntity<ClientDTO> createClient(@RequestBody final ClientDTO clientDTO) throws URISyntaxException {
        log.debug("REST request to save Client : {}", clientDTO);
        if (clientDTO.getId() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A new client cannot already have an ID");
        }
        ClientDTO result = clientService.save(clientDTO);
        return ResponseEntity
                .created(new URI("/api/clients/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * {@code PUT  /clients/:id} : Updates an existing client.
     *
     * @param id        the id of the clientDTO to save.
     * @param clientDTO the clientDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated clientDTO,
     * or with status {@code 400 (Bad Request)} if the clientDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the clientDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/clients/{id}")
    public ResponseEntity<ClientDTO> updateClient(@PathVariable(value = "id", required = false) final Long id,
                                                  @RequestBody final ClientDTO clientDTO) {
        log.debug("REST request to update Client : {}, {}", id, clientDTO);
        if (clientDTO.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Client ID is null"
            );
        }

        ClientDTO result = clientService.save(clientDTO);

        return ResponseEntity.ok()
                .headers(HeaderUtil
                        .createEntityUpdateAlert(ENTITY_NAME, clientDTO.getId().toString()))
                .body(result);
    }

    /**
     * {@code GET  /clients} : get all the clients.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of clients in body.
     */
    @GetMapping("/clients")
    public ResponseEntity<List<ClientDTO>> getAllClients() {
        log.debug("REST request to get all Clients");
        List<ClientDTO> clients = clientService.findAll();
        return ResponseEntity.ok(clients);
    }


    /**
     * {@code GET  /clients/:id} : get the "id" client.
     *
     * @param id the id of the clientDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the clientDTO,
     * or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/clients/{id}")
    public ResponseEntity<Optional<ClientDTO>> getClient(@PathVariable final Long id) {
        log.debug("REST request to get Client : {}", id);
        Optional<ClientDTO> clientDTO = clientService.findOne(id);
        return ResponseEntity.ok(clientDTO);
    }

    /**
     * {@code DELETE  /clients/:id} : delete the "id" client.
     *
     * @param id the id of the clientDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/clients/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable final Long id) {
        log.debug("REST request to delete Client : {}", id);
        clientService.delete(id);
        return ResponseEntity
                .noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString()))
                .build();
    }

    /**
     * Average salary by profession.
     *
     * @return the list.
     */
    @GetMapping("/clients/average-salary-by-profession")
    public List<ProfessionSalaireDTO> averageSalaryByProfession() {
        log.debug("REST request to get average salary by profession");
        return clientService.averageSalaireByProfession();
    }

}
