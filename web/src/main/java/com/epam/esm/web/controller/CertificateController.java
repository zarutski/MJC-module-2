package com.epam.esm.web.controller;

import com.epam.esm.domain.dto.CertificateDTO;
import com.epam.esm.service.CertificateService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The class {@code CertificateController} handles requests to perform certificate operations
 *
 * @author Maksim Zarutski
 */
@RestController
@RequestMapping("/api")
public class CertificateController {

    private final CertificateService certificateService;

    public CertificateController(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    /**
     * Gets all certificates with tags matching search parameters in defined order.
     * Search parameters are optional
     *
     * @param tagName         tag name
     * @param certificateName part of the certificate's name
     * @param description     part of the certificate's description
     * @param sortBy          sort type
     * @param order           order type
     * @return List of certificates found
     */
    @GetMapping("/gift_certificates/search")
    public List<CertificateDTO> searchCertificates(@RequestParam(value = "tag", required = false) String tagName,
                                                   @RequestParam(value = "name", required = false) String certificateName,
                                                   @RequestParam(value = "description", required = false) String description,
                                                   @RequestParam(value = "sort", required = false) String sortBy,
                                                   @RequestParam(value = "order", required = false) String order) {
        return certificateService.searchByParameters(tagName, certificateName, description, sortBy, order);
    }

    /**
     * Gets all certificates with corresponding tags
     *
     * @return List of all certificates
     */
    @GetMapping("/gift_certificates")
    public List<CertificateDTO> readAllCertificates() {
        return certificateService.readAll();
    }

    /**
     * Gets certificate by provided ID
     *
     * @param id of the requested certificate
     * @return {@code CertificateDTO} object for provided id
     */
    @GetMapping("/gift_certificates/{id}")
    public CertificateDTO readCertificate(@PathVariable Long id) {
        return certificateService.readById(id);
    }

    /**
     * Creates new certificate using info from request body
     *
     * @param certificate parameter containing info for creating certificate record
     * @return id of the created certificate
     */
    @PostMapping("/gift_certificates")
    @ResponseStatus(HttpStatus.CREATED)
    public Long createCertificate(@RequestBody CertificateDTO certificate) {
        return certificateService.create(certificate);
    }

    /**
     * Updates certificate using id and update info from request body
     *
     * @param certificate parameter containing update info
     * @return number of db records affected
     */
    @PutMapping("/gift_certificates")
    public Integer updateCertificate(@RequestBody CertificateDTO certificate) {
        return certificateService.updateCertificate(certificate);
    }

    /**
     * Deletes certificate by provided ID
     *
     * @param id of the certificate that needs to be deleted
     * @return number of db records affected
     */
    @DeleteMapping(value = "/gift_certificates/{id}")
    public Integer delete(@PathVariable Long id) {
        return certificateService.deleteById(id);
    }
}