package com.epam.esm.web.controller;

import com.epam.esm.domain.dto.TagDTO;
import com.epam.esm.domain.validation.CreateGroup;
import com.epam.esm.service.TagService;
import com.epam.esm.web.util.HateoasProvider;
import com.epam.esm.web.util.PaginationProvider;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.List;

/**
 * The class {@code TagController} handles requests to perform tag operations
 *
 * @author Maksim Zarutski
 */
@RestController
@RequestMapping("/api/v1.3/tags")
public class TagController {

    private final TagService tagService;
    private final HateoasProvider hateoas;
    private final PaginationProvider<TagDTO> pagination;

    public TagController(TagService tagService, HateoasProvider hateoas, PaginationProvider<TagDTO> pagination) {
        this.tagService = tagService;
        this.hateoas = hateoas;
        this.pagination = pagination;
    }

    /**
     * Creates new tag using info from request body
     *
     * @param tag parameter containing info for creating tag record
     * @return {@code TagDTO} object for created tag
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TagDTO createTag(@Validated(CreateGroup.class) @RequestBody TagDTO tag) {
        return tagService.create(tag);
    }

    /**
     * Gets all tags
     *
     * @param page number of the displayed page
     * @param size count of records displayed per page
     * @return List of all tags
     */
    @GetMapping
    public PagedModel<TagDTO> readAllTags(@RequestParam(value = "page", required = false, defaultValue = "1") @Min(1) int page,
                                          @RequestParam(value = "size", required = false, defaultValue = "8") @Min(1) int size) {
        List<TagDTO> tagList = tagService.readAll(page, size);
        hateoas.addLinksToListTag(tagList);
        return pagination.addPagination(tagList, page, size, tagService.getEntitiesCount());
    }

    /**
     * Gets tag by provided ID
     *
     * @param id of the requested tag
     * @return {@code TagDTO} object for provided id
     */
    @GetMapping("/{id}")
    public TagDTO readTag(@PathVariable long id) {
        return hateoas.addLinksToTag(tagService.readById(id));
    }

    /**
     * Deletes tag by provided ID
     *
     * @param id of the tag that needs to be deleted
     * @return empty response body
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tagService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Gets the most widely used tag of a user with the highest cost of all orders
     *
     * @return most widely used tag
     */
    @GetMapping("/mostUsedTag")
    public TagDTO getMostUsedTagFromUserWithOrdersHighestCost() {
        return tagService.getMostUsedTagFromUserWithOrdersHighestCost();
    }
}
