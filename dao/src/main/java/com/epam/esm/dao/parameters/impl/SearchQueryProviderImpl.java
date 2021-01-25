package com.epam.esm.dao.parameters.impl;

import com.epam.esm.dao.parameters.SearchQueryProvider;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class SearchQueryProviderImpl implements SearchQueryProvider {

    private static final String PART_WHERE = "WHERE ";
    private static final String PART_HAS_TAG = "t.name = ? AND ";
    private static final String PART_NAME_LIKE = "c.name LIKE ";
    private static final String PARAMETER_PREPARED_LIKE = "concat('%', ?, '%') ";
    private static final String PARAMETER_LIKE_ANY = "'%%' ";
    private static final String PART_DESCRIPTION_LIKE = "AND c.description LIKE ";
    private static final String ORDER_BY_CERTIFICATE = "ORDER BY c.";

    private static final String NAME = "name";
    private static final String SORT_TYPE_DEFAULT = "create_date ";
    private static final String SORT_TYPE_NAME = "name ";
    private static final String ORDER_TYPE_DEFAULT = "ASC";
    private static final String ORDER_TYPE_DESC = "DESC";

    @Override
    public String getCertificateSearchQuery(String tagName, String certificateName, String description, String sortBy, String order) {
        return tagQuery(tagName)
                .concat(nameQuery(certificateName))
                .concat(descriptionQuery(description))
                .concat(sortOrDefault(sortBy))
                .concat(orderOrDefault(order));
    }

    private String tagQuery(String tagName) {
        if (Objects.nonNull(tagName)) {
            return PART_WHERE.concat(PART_HAS_TAG);
        }
        return PART_WHERE;
    }

    private String nameQuery(String namePart) {
        if (Objects.nonNull(namePart)) {
            return PART_NAME_LIKE.concat(PARAMETER_PREPARED_LIKE);
        }
        return PART_NAME_LIKE.concat(PARAMETER_LIKE_ANY);
    }

    private String descriptionQuery(String descriptionPart) {
        if (Objects.nonNull(descriptionPart)) {
            return PART_DESCRIPTION_LIKE.concat(PARAMETER_PREPARED_LIKE);
        }
        return PART_DESCRIPTION_LIKE.concat(PARAMETER_LIKE_ANY);
    }

    private String sortOrDefault(String sortBy) {
        if (Objects.nonNull(sortBy) && sortBy.equalsIgnoreCase(NAME)) {
            return ORDER_BY_CERTIFICATE.concat(SORT_TYPE_NAME);
        }
        return ORDER_BY_CERTIFICATE.concat(SORT_TYPE_DEFAULT);
    }

    private String orderOrDefault(String orderBy) {
        if (Objects.nonNull(orderBy) && orderBy.equalsIgnoreCase(ORDER_TYPE_DESC)) {
            return ORDER_TYPE_DESC;
        }
        return ORDER_TYPE_DEFAULT;
    }

}