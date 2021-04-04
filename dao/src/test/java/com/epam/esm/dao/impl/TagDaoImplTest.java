package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.config.TestConfig;
import com.epam.esm.domain.entity.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
class TagDaoImplTest {

    private static final Long RECORD_ID = 1L;
    private static final String DB_TAG_NAME = "abdullah";
    private static final String DB_TAG_NAME_NEW = "cart";

    @Autowired
    private TagDao tagDao;

    @Test
    void readByName() {
        Tag expected = createDBIdentityTag();
        Optional<Tag> actual = tagDao.readByName(DB_TAG_NAME);
        assertEquals(expected, actual.orElseThrow(RuntimeException::new));
    }

    @Test
    void create() {
        Tag expected = createTag();
        Optional<Tag> actual = tagDao.create(expected);
        assertEquals(expected, actual.orElseThrow(RuntimeException::new));
    }

    @Test
    void delete() {
        Tag expected = createTag();
        assertDoesNotThrow(() -> tagDao.delete(expected));
    }

    @Test
    void getMostUsedTagFromUserWithOrdersHighestCost() {
        Tag expected = createDBIdentityTag();
        Optional<Tag> actual = tagDao.getMostUsedTagFromUserWithOrdersHighestCost();
        assertEquals(expected, actual.orElseThrow(RuntimeException::new));
    }

    private Tag createDBIdentityTag() {
        Tag tag = new Tag();
        tag.setId(RECORD_ID);
        tag.setName(DB_TAG_NAME);
        return tag;
    }

    private Tag createTag() {
        Tag tag = new Tag();
        tag.setName(DB_TAG_NAME_NEW);
        return tag;
    }
}