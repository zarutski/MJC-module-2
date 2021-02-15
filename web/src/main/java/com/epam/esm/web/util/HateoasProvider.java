package com.epam.esm.web.util;

import com.epam.esm.domain.dto.CertificateDTO;
import com.epam.esm.domain.dto.OrderDTO;
import com.epam.esm.domain.dto.TagDTO;
import com.epam.esm.domain.dto.UserDTO;
import com.epam.esm.web.controller.CertificateController;
import com.epam.esm.web.controller.OrderController;
import com.epam.esm.web.controller.TagController;
import com.epam.esm.web.controller.UserController;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class HateoasProvider {

    private static final String RELATION_UPDATE = "update";
    private static final String RELATION_DELETE = "delete";
    private static final String RELATION_USER_ORDERS = "user orders";
    private static final String RELATION_USER = "user";
    private static final String RELATION_ALL_USER_ORDERS = "all user orders";
    private static final String RELATION_DELETE_TAG = "delete tag";
    private static final String RELATION_CREATE_TAG = "create tag";
    private static final int PAGE = 1;
    private static final int SIZE = 4;

    public CertificateDTO addLinksToCertificate(CertificateDTO certificateDTO) {
        certificateDTO.add(linkTo(methodOn(CertificateController.class).update(certificateDTO))
                .withRel(RELATION_UPDATE)
                .withType(HttpMethod.PUT.name()));
        certificateDTO.add(linkTo(methodOn(CertificateController.class).delete(certificateDTO.getId()))
                .withRel(RELATION_DELETE)
                .withType(HttpMethod.DELETE.name()));
        return certificateDTO;
    }

    public void addLinksToListCertificate(List<CertificateDTO> certificateList) {
        certificateList.forEach(certificate -> {
            certificate.add(linkTo(methodOn(CertificateController.class).read(certificate.getId()))
                    .withSelfRel()
                    .withType(HttpMethod.GET.name()));
            addLinksToListTag(certificate.getTags());
        });
    }

    public TagDTO addLinksToTag(TagDTO tagDto) {
        tagDto.add(linkTo(methodOn(TagController.class).delete(tagDto.getId()))
                .withRel(RELATION_DELETE_TAG)
                .withType(HttpMethod.DELETE.name()));
        tagDto.add(linkTo(methodOn(TagController.class).createTag(tagDto))
                .withRel(RELATION_CREATE_TAG)
                .withType(HttpMethod.POST.name()));
        return tagDto;
    }

    public void addLinksToListTag(List<TagDTO> tagDtoList) {
        tagDtoList.forEach(tag -> tag.add(linkTo(methodOn(TagController.class).readTag(tag.getId()))
                .withSelfRel()
                .withType(HttpMethod.GET.name())));
    }

    public UserDTO addLinksToUser(UserDTO user) {
        user.add(linkTo(methodOn(OrderController.class).readByUserId(user.getId(), PAGE, SIZE))
                .withRel(RELATION_ALL_USER_ORDERS)
                .withType(HttpMethod.GET.name()));
        return user;
    }

    public void addLinksToUserList(List<UserDTO> userDtoList) {
        userDtoList.forEach(userDto -> userDto.add(linkTo(methodOn(UserController.class).read(userDto.getId()))
                .withSelfRel()
                .withType(HttpMethod.GET.name())));
    }

    public OrderDTO addLinksToOrder(OrderDTO orderDto) {
        addLinksToListCertificate(orderDto.getCertificateList());
        orderDto.add(linkTo(methodOn(UserController.class).read(orderDto.getUserId()))
                .withRel(RELATION_USER)
                .withType(HttpMethod.GET.name()));
        return orderDto;
    }


    public void addLinksToOrderList(List<OrderDTO> orderDtoList) {
        orderDtoList.forEach(orderDto -> {
            orderDto.add(linkTo(methodOn(OrderController.class).readByUserId(orderDto.getUserId(), PAGE, SIZE))
                    .withRel(RELATION_USER_ORDERS)
                    .withType(HttpMethod.GET.name()));
            addLinksToListCertificate(orderDto.getCertificateList());
            orderDto.add(linkTo(methodOn(UserController.class).read(orderDto.getUserId()))
                    .withRel(RELATION_USER)
                    .withType(HttpMethod.GET.name()));
        });
    }
}
