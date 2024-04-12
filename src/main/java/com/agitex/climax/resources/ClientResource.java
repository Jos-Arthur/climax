package com.agitex.climax.resources;

import com.agitex.climax.dtos.ClientDTO;
import com.agitex.climax.dtos.ProfessionSalaireDTO;
import com.agitex.climax.services.ClientService;
import com.agitex.climax.services.FichierService;
import com.agitex.climax.utils.HeaderUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
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
    private final FichierService fichierService;

    /**
     * Instantiates a new Client resource.
     *
     * @param clientService    the client service
     * @param  fichierService the file service
     */
    public ClientResource(final ClientService clientService,
                          final FichierService fichierService) {
        this.clientService = clientService;
        this.fichierService = fichierService;
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
    @Operation(summary = "Endpoint permettant d'enregistrer un client.", tags = {"Clients"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success|OK"),
                    @ApiResponse(responseCode = "401", description = "not authorized!"),
                    @ApiResponse(responseCode = "403", description = "forbidden!!!"),
                    @ApiResponse(responseCode = "404", description = "not found!!!"),
                    @ApiResponse(responseCode = "204", description = "empty List"),
                    @ApiResponse(responseCode = "500", description = "Internal Error")})
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
    @Operation(summary = "Endpoint permettant de mettre à jour un client.", tags = {"Clients"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success|OK"),
                    @ApiResponse(responseCode = "401", description = "not authorized!"),
                    @ApiResponse(responseCode = "403", description = "forbidden!!!"),
                    @ApiResponse(responseCode = "404", description = "not found!!!"),
                    @ApiResponse(responseCode = "204", description = "empty List"),
                    @ApiResponse(responseCode = "500", description = "Internal Error")})
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
    @Operation(summary = "Endpoint permettant de retourner la liste des clients.", tags = {"Clients"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success|OK"),
                    @ApiResponse(responseCode = "401", description = "not authorized!"),
                    @ApiResponse(responseCode = "403", description = "forbidden!!!"),
                    @ApiResponse(responseCode = "404", description = "not found!!!"),
                    @ApiResponse(responseCode = "204", description = "empty List"),
                    @ApiResponse(responseCode = "500", description = "Internal Error")})
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
    @Operation(summary = "Endpoint permettant de recuperer un client par son identifiant.", tags = {"Clients"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success|OK"),
                    @ApiResponse(responseCode = "401", description = "not authorized!"),
                    @ApiResponse(responseCode = "403", description = "forbidden!!!"),
                    @ApiResponse(responseCode = "404", description = "not found!!!"),
                    @ApiResponse(responseCode = "204", description = "empty List"),
                    @ApiResponse(responseCode = "500", description = "Internal Error")})
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
    @Operation(summary = "Endpoint permettant de supprimer un client par son identifiant.", tags = {"Clients"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success|OK"),
                    @ApiResponse(responseCode = "401", description = "not authorized!"),
                    @ApiResponse(responseCode = "403", description = "forbidden!!!"),
                    @ApiResponse(responseCode = "404", description = "not found!!!"),
                    @ApiResponse(responseCode = "204", description = "empty List"),
                    @ApiResponse(responseCode = "500", description = "Internal Error")})
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
    @Operation(summary = "Endpoint permettant de calculer le salaire moyen des clients par profession.",
            tags = {"Clients"}, responses = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
            @ApiResponse(responseCode = "401", description = "not authorized!"),
            @ApiResponse(responseCode = "403", description = "forbidden!!!"),
            @ApiResponse(responseCode = "404", description = "not found!!!"),
            @ApiResponse(responseCode = "204", description = "empty List"),
            @ApiResponse(responseCode = "500", description = "Internal Error")})
    public ResponseEntity<List<ProfessionSalaireDTO>> averageSalaryByProfession() {
        log.debug("REST request to get average salary by profession");
        return ResponseEntity.ok(clientService.averageSalaireByProfession());
    }

    /**
     * Save client data using upload file.
     *
     * @param file
     * @return clients.
     */
    @PostMapping(value = "/clients/upload-save-clients", consumes = {"multipart/form-data"})
    @Operation(summary = "Endpoint permettant de charger un fichier contenant les données d'un client.",
            tags = {"Clients"}, responses = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
            @ApiResponse(responseCode = "401", description = "not authorized!"),
            @ApiResponse(responseCode = "403", description = "forbidden!!!"),
            @ApiResponse(responseCode = "404", description = "not found!!!"),
            @ApiResponse(responseCode = "204", description = "empty List"),
            @ApiResponse(responseCode = "500", description = "Internal Error")})
    public ResponseEntity<String> createClients(@RequestParam("file") final MultipartFile file) {
        try {
            fichierService.saveClientData(file);
            return ResponseEntity.ok("File uploaded successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload file: " + e.getMessage());
        }
    }


}
