package com.epam.esm.web.controller;

import com.epam.esm.domain.dto.OrderDTO;
import com.epam.esm.service.OrderService;
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
 * The class {@code OrderController} handles requests to perform order operations
 *
 * @author Maksim Zarutski
 */
@RestController
@RequestMapping("/api/v1.3/orders")
@Validated
public class OrderController {

    private final OrderService orderService;
    private final HateoasProvider hateoas;
    private final PaginationProvider<OrderDTO> pagination;

    public OrderController(OrderService orderService, HateoasProvider hateoas,
                           PaginationProvider<OrderDTO> pagination) {
        this.orderService = orderService;
        this.hateoas = hateoas;
        this.pagination = pagination;
    }

    /**
     * Gets all orders
     *
     * @param page number of the displayed page
     * @param size count of records displayed per page
     * @return List of all orders
     */
    @GetMapping
    public PagedModel<OrderDTO> readAll(
            @RequestParam(value = "page", required = false, defaultValue = "1") @Min(1) int page,
            @RequestParam(value = "size", required = false, defaultValue = "4") @Min(1) int size) {
        List<OrderDTO> orderList = orderService.readAll(page, size);
        hateoas.addLinksToOrderList(orderList);
        return pagination.addPagination(orderList, page, size, orderService.getEntitiesCount());
    }

    /**
     * Gets order by provided ID
     *
     * @param id of the requested order
     * @return {@code OrderDTO} object for provided id
     */
    @GetMapping({"{id}"})
    public OrderDTO read(@PathVariable Long id) {
        return hateoas.addLinksToOrder(orderService.readById(id));
    }

}