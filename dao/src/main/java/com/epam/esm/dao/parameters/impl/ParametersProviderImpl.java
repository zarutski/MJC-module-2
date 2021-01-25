package com.epam.esm.dao.parameters.impl;

import com.epam.esm.dao.parameters.ParametersProvider;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class ParametersProviderImpl implements ParametersProvider {

    @Override
    public List<String> getCertificateParameters(String tagName, String certificateName, String description) {
        List<String> params = new ArrayList<>();
        if (Objects.nonNull(tagName)) {
            params.add(tagName);
        }
        if (Objects.nonNull(certificateName)) {
            params.add(certificateName);
        }
        if (Objects.nonNull(description)) {
            params.add(description);
        }

        return params;
    }

}