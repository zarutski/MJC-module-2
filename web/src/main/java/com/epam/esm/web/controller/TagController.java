package com.epam.esm.web.controller;

import com.epam.esm.domain.dto.TagDTO;
import com.epam.esm.service.TagService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The class {@code TagController} handles requests to perform tag operations
 *
 * @author Maksim Zarutski
 */
@RestController
@RequestMapping("/api/tags")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    /**
     * Creates new tag using info from request body
     *
     * @param tag parameter containing info for creating tag record
     * @return id of the created tag
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long createTag(@RequestBody TagDTO tag) {
        return tagService.create(tag);
    }

    /**
     * Gets all tags
     *
     * @return List of all tag
     */
    @GetMapping
    public List<TagDTO> readAllTags() {
        return tagService.readAll();
    }

    /**
     * Gets tag by provided ID
     *
     * @param id of the requested tag
     * @return {@code TagDTO} object for provided id
     */
    @GetMapping("/{id}")
    public TagDTO readTag(@PathVariable long id) {
        return tagService.readById(id);
    }

    /**
     * Deletes tag by provided ID
     *
     * @param id of the tag that needs to be deleted
     * @return number of db records affected
     */
    @DeleteMapping(value = "/{id}")
    public Integer delete(@PathVariable Long id) {
        return tagService.deleteById(id);
    }
}
