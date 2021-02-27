package com.epam.esm.web.controller;

import com.epam.esm.domain.dto.CertificateDTO;
import com.epam.esm.domain.util.SearchParameter;
import com.epam.esm.domain.validation.CreateGroup;
import com.epam.esm.domain.validation.UpdateGroup;
import com.epam.esm.service.CertificateService;
import com.epam.esm.web.util.HateoasProvider;
import com.epam.esm.web.util.PaginationProvider;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * The class {@code CertificateController} handles requests to perform certificate operations
 *
 * @author Maksim Zarutski
 */
@RestController
@RequestMapping("/api/v1.3/gift_certificates")
@Validated
public class CertificateController {

    private final CertificateService certificateService;
    private final HateoasProvider hateoas;
    private final PaginationProvider<CertificateDTO> pagination;

    public CertificateController(CertificateService certificateService, HateoasProvider hateoas,
                                 PaginationProvider<CertificateDTO> pagination) {
        this.certificateService = certificateService;
        this.hateoas = hateoas;
        this.pagination = pagination;
    }

    /**
     * Gets all certificates with corresponding tags
     *
     * @param page number of the displayed page
     * @param size count of records displayed per page
     * @return List of all certificates
     */
    @GetMapping
    public PagedModel<CertificateDTO> readAll(
            @RequestParam(value = "page", required = false, defaultValue = "1") @Min(1) int page,
            @RequestParam(value = "size", required = false, defaultValue = "4") @Min(1) int size) {
        List<CertificateDTO> certificateList = certificateService.readAll(page, size);
        hateoas.addLinksToListCertificate(certificateList);
        return pagination.addPagination(certificateList, page, size, certificateService.getEntitiesCount());
    }

    /**
     * Gets all certificates with tags matching search parameters in defined order.
     *
     * @param name        is a part or a full name of the searched certificate
     * @param description is a part of certificate's description
     * @param tagNames    is a list of tag names of the searched certificate
     * @param sortBy      sort type
     * @param order       order type
     * @param page        number of the displayed page
     * @param size        count of records displayed per page
     * @return List of certificates found
     */
    @GetMapping("/search")
    public PagedModel<CertificateDTO> searchByParameters(
            @RequestParam(value = "name", required = false) @Size(min = 2, max = 45) String name,
            @RequestParam(value = "description", required = false) @Size(min = 3, max = 45) String description,
            @RequestParam(value = "tag-name", required = false) List<String> tagNames,
            @RequestParam(value = "sort-by", required = false) String sortBy,
            @RequestParam(value = "order", required = false) String order,
            @RequestParam(value = "page", required = false, defaultValue = "1") @Min(1) int page,
            @RequestParam(value = "size", required = false, defaultValue = "4") @Min(1) int size) {
        SearchParameter searchParameter = formSearchParameter(name, description, tagNames, sortBy, order);
        List<CertificateDTO> certificateList = certificateService.searchByParameters(searchParameter, page, size);
        hateoas.addLinksToListCertificate(certificateList);
        return pagination
                .addPagination(certificateList, page, size, certificateService.getEntitiesCount(searchParameter));
    }

    /**
     * Gets certificate by provided ID
     *
     * @param id of the requested certificate
     * @return {@code CertificateDTO} object for provided id
     */
    @GetMapping("/{id}")
    public CertificateDTO read(@PathVariable Long id) {
        return hateoas.addLinksToCertificate(certificateService.readById(id));
    }

    /**
     * Creates new certificate using info from request body
     *
     * @param certificate parameter containing info for creating certificate record
     * @return {@code CertificateDTO} object for created certificate
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CertificateDTO create(@Validated(CreateGroup.class) @RequestBody CertificateDTO certificate) {
        return hateoas.addLinksToCertificate(certificateService.create(certificate));
    }

    /**
     * Updates certificate using id and update info from request body
     *
     * @param certificate certificate parameter containing update info
     * @param id          of the certificate that needs to be updated
     * @return {@code CertificateDTO} object for updated certificate
     */
    @PutMapping("/{id}")
    public CertificateDTO update(@Validated(UpdateGroup.class) @RequestBody CertificateDTO certificate,
                                 @PathVariable @Min(1) Long id) {
        certificate.setId(id);
        return hateoas.addLinksToCertificate(certificateService.update(certificate));
    }

    /**
     * Deletes certificate by provided ID
     *
     * @param id of the certificate that needs to be deleted
     * @return empty response body
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        certificateService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private SearchParameter formSearchParameter(String name, String description, List<String> tagNames,
                                                String sortBy, String order) {
        SearchParameter searchParameter = new SearchParameter();
        searchParameter.setName(name);
        searchParameter.setDescription(description);
        searchParameter.setTagNames(tagNames);
        searchParameter.setSortBy(sortBy);
        searchParameter.setOrder(order);
        return searchParameter;
    }
}