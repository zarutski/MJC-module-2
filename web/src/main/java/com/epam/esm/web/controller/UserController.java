package com.epam.esm.web.controller;

import com.epam.esm.domain.dto.UserDTO;
import com.epam.esm.service.UserService;
import com.epam.esm.web.util.HateoasProvider;
import com.epam.esm.web.util.PaginationProvider;
import org.springframework.hateoas.PagedModel;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import java.util.List;

/**
 * The class {@code UserController} handles requests to perform user operations
 *
 * @author Maksim Zarutski
 */
@RestController
@RequestMapping("/api/v1.3/users")
@Validated
public class UserController {

    private final UserService userService;
    private final HateoasProvider hateoas;
    private final PaginationProvider<UserDTO> pagination;

    public UserController(UserService userService, HateoasProvider hateoas, PaginationProvider<UserDTO> pagination) {
        this.userService = userService;
        this.hateoas = hateoas;
        this.pagination = pagination;
    }

    /**
     * Gets user by provided ID
     *
     * @param id of the requested user
     * @return {@code UserDTO} object for provided id
     */
    @GetMapping("/{id}")
    public UserDTO read(@PathVariable Long id) {
        return hateoas.addLinksToUser(userService.readById(id));
    }

    /**
     * Gets all users
     *
     * @param page number of the displayed page
     * @param size count of records displayed per page
     * @return List of all tags users
     */
    @GetMapping
    public PagedModel<UserDTO> readAll(
            @RequestParam(value = "page", required = false, defaultValue = "1") @Min(1) int page,
            @RequestParam(value = "size", required = false, defaultValue = "4") @Min(1) int size) {
        List<UserDTO> userList = userService.readAll(page, size);
        hateoas.addLinksToUserList(userList);
        return pagination.addPagination(userList, page, size, userService.getEntitiesCount());
    }
}
