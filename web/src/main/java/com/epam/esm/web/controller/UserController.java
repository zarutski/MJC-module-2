package com.epam.esm.web.controller;

import com.epam.esm.domain.dto.CreateOrderDTO;
import com.epam.esm.domain.dto.OrderDTO;
import com.epam.esm.domain.dto.UserDTO;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import com.epam.esm.web.security.verifier.PermissionVerifier;
import com.epam.esm.web.util.HateoasProvider;
import com.epam.esm.web.util.PaginationProvider;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
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
    private final OrderService orderService;
    private final HateoasProvider hateoas;
    private final PaginationProvider<UserDTO> pagination;
    private final PaginationProvider<OrderDTO> orderPagination;
    private final PermissionVerifier permissionVerifier;

    public UserController(UserService userService, OrderService orderService,
                          HateoasProvider hateoas, PaginationProvider<UserDTO> pagination,
                          PaginationProvider<OrderDTO> orderPagination, PermissionVerifier permissionVerifier) {
        this.userService = userService;
        this.orderService = orderService;
        this.hateoas = hateoas;
        this.pagination = pagination;
        this.orderPagination = orderPagination;
        this.permissionVerifier = permissionVerifier;
    }

    /**
     * Gets user by provided ID
     *
     * @param id of the requested user
     * @return {@code UserDTO} object for provided id
     */
    @GetMapping("/{id}")
    public UserDTO read(@PathVariable Long id) {
        permissionVerifier.verifyPermission(id);
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
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public PagedModel<UserDTO> readAll(
            @RequestParam(value = "page", required = false, defaultValue = "1") @Min(1) int page,
            @RequestParam(value = "size", required = false, defaultValue = "4") @Min(1) int size) {
        List<UserDTO> userList = userService.readAll(page, size);
        hateoas.addLinksToUserList(userList);
        return pagination.addPagination(userList, page, size, userService.getEntitiesCount());
    }

    /**
     * Creates order for certificates for a user
     *
     * @param userId         id of the user
     * @param certificateIds certificates ids user wants to order
     * @return {@code OrderDTO} object for created order
     */
    @PostMapping("/{userId}/order")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDTO create(@PathVariable @Min(1) Long userId, @RequestBody List<Long> certificateIds) {
        permissionVerifier.verifyPermission(userId);
        CreateOrderDTO createOrderDTO = formCreateOrder(userId, certificateIds);
        return hateoas.addLinksToOrder(orderService.create(createOrderDTO));
    }

    /**
     * Gets orders of the certain user
     *
     * @param userId id of a user
     * @param page   number of the displayed page
     * @param size   count of records displayed per page
     * @return List of a user's orders
     */
    @GetMapping("/{userId}/orders")
    public PagedModel<OrderDTO> readByUserId(@PathVariable Long userId,
                                             @RequestParam(value = "page", required = false, defaultValue = "1") @Min(1) int page,
                                             @RequestParam(value = "size", required = false, defaultValue = "4") @Min(1) int size) {
        permissionVerifier.verifyPermission(userId);
        List<OrderDTO> orderList = orderService.readOrdersByUserId(userId, page, size);
        hateoas.addLinksToOrderList(orderList);
        return orderPagination.addPagination(orderList, page, size, orderService.getUserOrderCount(userId));
    }

    /**
     * Gets order by provided ID
     *
     * @param orderId of the requested order
     * @return {@code OrderDTO} object for provided id
     */
    @GetMapping({"/{userId}/orders/{orderId}"})
    public OrderDTO readOrder(@PathVariable Long userId, @PathVariable Long orderId) {
        permissionVerifier.verifyPermission(userId);
        return hateoas.addLinksToOrder(orderService.readUserOrder(userId, orderId));
    }

    private CreateOrderDTO formCreateOrder(Long userId, List<Long> certificateIds) {
        CreateOrderDTO createOrderDTO = new CreateOrderDTO();
        createOrderDTO.setUserId(userId);
        createOrderDTO.setCertificateIds(certificateIds);
        return createOrderDTO;
    }
}
