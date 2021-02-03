package com.epam.esm.dao.parameters;

public interface SearchQueryProvider {

    String getCertificateSearchQuery(String tagName, String certificateName, String description, String sortBy, String order);

}