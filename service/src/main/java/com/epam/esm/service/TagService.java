package com.epam.esm.service;

import com.epam.esm.domain.dto.TagDTO;

public interface TagService extends CRDService<TagDTO> {

    TagDTO getMostUsedTagFromUserWithOrdersHighestCost();
}