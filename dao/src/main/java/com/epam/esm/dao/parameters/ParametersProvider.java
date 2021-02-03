package com.epam.esm.dao.parameters;

import java.util.List;

public interface ParametersProvider {

    List<String> getCertificateParameters(String tagName, String certificateName, String description);

}