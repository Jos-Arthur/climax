package com.agitex.climax.services;

import com.agitex.climax.dtos.ClientDTO;
import com.agitex.climax.entities.Client;
import com.agitex.climax.repositories.ClientRepository;
import com.agitex.climax.utils.Constant;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FichierService {

    private final ClientRepository clientRepository;

    /**
     * Enregistre les données des clients après lecture.
     *
     * @param file Le fichier à lire.
     * @throws IOException Si une erreur d'entrée/sortie se produit lors de la lecture du fichier.
     */
    public void saveClientData(final MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        if (filename == null) {
            log.error("Le nom du fichier est null");
            throw new IllegalArgumentException("Le nom du fichier est null");
        }

        if (filename.endsWith(".csv")) {
            readCsvFile(file);
        } else if (filename.endsWith(".json")) {
            readJsonFile(file);
        } else if (filename.endsWith(".xml")) {
            readXmlFile(file);
        } else if (filename.endsWith(".txt")) {
            readTextFile(file);
        } else {
            log.error("Type de fichier non pris en charge: {}", filename);
            throw new IllegalArgumentException("Type de fichier non pris en charge: " + filename);
        }
    }

    /**
     * Methode pour lire un fichier de format CSV.
     *
     * @param file
     * @throws IOException
     */
    private void readCsvFile(final MultipartFile file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= Constant.LENGTH_INDEX) {
                    Client client = createClientFromCsvData(data);
                    clientRepository.save(client);
                } else {
                    log.warn("Données incomplètes: {}", line);
                }
            }
        }
    }

    /**
     * Methode pour storer les donnees issus du fichier CSV.
     *
     * @param data
     * @return client
     */
    private Client createClientFromCsvData(final String[] data) {
        Client client = new Client();
        client.setNom(data[Constant.NOM_INDEX]);
        client.setPrenom(data[Constant.PRENOM_INDEX]);
        try {
            String ageStr = data[Constant.AGE_INDEX].trim();
            if (!ageStr.isEmpty()) {
                client.setAge(Integer.parseInt(ageStr));
            }
            String profession = data[Constant.PROFESSION_INDEX].trim();
            if (!profession.isEmpty()) {
                client.setProfession(profession);
            }
            String salaireStr = data[Constant.SALAIRE_INDEX].trim();
            if (!salaireStr.isEmpty()) {
                client.setSalaire(BigDecimal.valueOf(Double.parseDouble(salaireStr)));
            }
        } catch (NumberFormatException e) {
            log.error("Erreur de conversion des données: {}", e.getMessage());
        }
        return client;
    }

    /**
     * Methode pour lire un fichier de format JSON.
     *
     * @param file
     * @throws IOException
     */
    private void readJsonFile(final MultipartFile file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<ClientDTO> clients = mapper.readValue(file.getInputStream(), new TypeReference<>() {
        });
        for (ClientDTO clientDTO : clients) {
            Client client = createClientFromDto(clientDTO);
            clientRepository.save(client);
        }
    }

    /**
     * Methode pour storer les information du client.
     *
     * @param clientDTO
     * @return client
     */
    private Client createClientFromDto(final ClientDTO clientDTO) {
        Client client = new Client();
        client.setNom(clientDTO.getNom());
        client.setPrenom(clientDTO.getPrenom());
        client.setAge(clientDTO.getAge());
        client.setProfession(clientDTO.getProfession());
        client.setSalaire(clientDTO.getSalaire());
        return client;
    }

    /**
     * Methode pour lire un fichier de format XML.
     *
     * @param file
     * @throws IOException
     */
    private void readXmlFile(final MultipartFile file) throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        List<ClientDTO> clients = xmlMapper.readValue(file.getInputStream(), new TypeReference<>() {
        });
        for (ClientDTO clientDTO : clients) {
            Client client = createClientFromDto(clientDTO);
            clientRepository.save(client);
        }
    }

    /**
     * Methode pour lire un fichier de format TXT.
     *
     * @param file
     * @throws IOException
     */
    private void readTextFile(final MultipartFile file) throws IOException {
        File tempFile = convertMultipartFileToFile(file);
        List<String> lines = FileUtils.readLines(tempFile, StandardCharsets.UTF_8);
        for (String line : lines) {
            String[] data = line.split(",");
            if (data.length >= Constant.LENGTH_INDEX) {
                Client client = createClientFromCsvData(data);
                clientRepository.save(client);
            } else {
                log.warn("Données incomplètes: {}", line);
            }
        }
    }

    /**
     * Conversion des fichiers.
     *
     * @param file
     * @return tempfile
     * @throws IOException
     */
    private File convertMultipartFileToFile(final MultipartFile file) throws IOException {
        File tempFile = File.createTempFile("temp", null);
        file.transferTo(tempFile);
        return tempFile;
    }
}
