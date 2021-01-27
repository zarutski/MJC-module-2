package com.epam.esm.dao.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.config.TestConfig;
import com.epam.esm.dao.exception.WrongInsertDataException;
import com.epam.esm.dao.mapper.TagMapper;
import com.epam.esm.domain.entity.Certificate;
import com.epam.esm.domain.entity.Tag;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CertificateDaoImplTest {

    private static final long FIRST_ID = 1L;
    private static final String FIRST_NAME = "first name";
    private static final String FIRST_DESCRIPTION = "first description";
    private static final BigDecimal FIRST_PRICE = BigDecimal.valueOf(100);
    private static final int FIRST_DURATION = 120;
    private static final int FIRST_MINUTE = 40;

    private static final long SECOND_ID = 2L;
    private static final String SECOND_NAME = "second name";
    private static final String SECOND_DESCRIPTION = "second description";
    private static final BigDecimal SECOND_PRICE = BigDecimal.valueOf(200);
    private static final int SECOND_DURATION = 240;
    private static final int SECOND_MINUTE = 50;

    private static final int YEAR = 2021;
    private static final int MONTH = 1;
    private static final int DAY = 25;
    private static final int HOUR = 7;
    private static final int SECOND = 10;

    private static final String DB_FIELD_NAME = "name";
    private static final String DB_FIELD_DESCRIPTION = "description";
    private static final int RECORD_AFFECTED_ONE = 1;
    private static final int RECORD_AFFECTED_NONE = 0;
    private static final long THIRD_ID = 3L;

    private static final String SEARCH_CERTIFICATE_QUERY_PART = "WHERE c.name LIKE concat('%', ?, '%') " +
            "AND c.description LIKE concat('%', ?, '%') order by c.name ASC";

    @Autowired
    private CertificateDao certificateDao;
    @Autowired
    private EmbeddedDatabase embeddedDatabase;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @AfterEach
    public void tearDown() {
        embeddedDatabase.shutdown();
    }

    @Test
    void updateCertificatePositive() {
        Certificate certificate = new Certificate();
        certificate.setId(SECOND_ID);
        certificate.setName(FIRST_NAME);
        certificate.setDescription(FIRST_DESCRIPTION);

        Integer integer = certificateDao.update(certificate);
        assertEquals(RECORD_AFFECTED_ONE, integer);
    }

    @Test
    void updateCertificateNegative() {
        Certificate first = new Certificate();
        first.setId(THIRD_ID);
        first.setDescription(FIRST_DESCRIPTION);

        Integer integer = certificateDao.update(first);
        assertEquals(RECORD_AFFECTED_NONE, integer);
    }

    @Test
    void createCertificateHasTag() {
        Certificate certificate = createCertificateEntity();
        Long certificateID = certificateDao.create(certificate);

        Tag tag = new Tag();
        tag.setName(FIRST_NAME);
        TagDao tagDao = new TagDaoImpl(jdbcTemplate, tagMapper);
        Long tagID = tagDao.create(tag);

        Integer actual = certificateDao.createCertificateHasTag(certificateID, tagID);
        assertEquals(RECORD_AFFECTED_ONE, actual);
    }

    @Test
    void searchByParameters() {
        List<String> parameters = Stream.of(DB_FIELD_NAME, DB_FIELD_DESCRIPTION).collect(Collectors.toList());
        List<Certificate> certificates = certificateDao.searchByParameters(SEARCH_CERTIFICATE_QUERY_PART, parameters);

        Certificate entityFirst = createDBIdentityCertificate(FIRST_ID, FIRST_NAME,
                FIRST_DESCRIPTION, FIRST_PRICE, FIRST_DURATION, FIRST_MINUTE);
        Certificate entitySecond = createDBIdentityCertificate(SECOND_ID, SECOND_NAME,
                SECOND_DESCRIPTION, SECOND_PRICE, SECOND_DURATION, SECOND_MINUTE);

        List<Certificate> expectedGiftCertificates = new ArrayList<>();
        expectedGiftCertificates.add(entityFirst);
        expectedGiftCertificates.add(entitySecond);

        assertEquals(expectedGiftCertificates, certificates);
    }

    @Test
    void createFromEntityPositive() {
        Certificate certificate = createCertificateEntity();
        assertEquals(THIRD_ID, certificateDao.create(certificate));
    }

    @Test
    void createFromEntityNegative() {
        Certificate certificate = createCertificateEntity();
        certificate.setPrice(null);

        assertThrows(WrongInsertDataException.class, () -> certificateDao.create(certificate));
    }

    @Test
    void readByIdPositive() {
        Certificate expected = createDBIdentityCertificate(FIRST_ID, FIRST_NAME,
                FIRST_DESCRIPTION, FIRST_PRICE, FIRST_DURATION, FIRST_MINUTE);
        Optional<Certificate> certificate = certificateDao.readById(FIRST_ID);
        assertEquals(expected, certificate.get());
    }

    @Test
    void readByIdNegative() {
        Optional<Certificate> certificate = certificateDao.readById(THIRD_ID);
        assertFalse(certificate.isPresent());
    }

    @Test
    void readAll() {
        Certificate entityFirst = createDBIdentityCertificate(FIRST_ID, FIRST_NAME,
                FIRST_DESCRIPTION, FIRST_PRICE, FIRST_DURATION, FIRST_MINUTE);
        Certificate entitySecond = createDBIdentityCertificate(SECOND_ID, SECOND_NAME,
                SECOND_DESCRIPTION, SECOND_PRICE, SECOND_DURATION, SECOND_MINUTE);

        List<Certificate> expectedCertificates = new ArrayList<>();
        expectedCertificates.add(entityFirst);
        expectedCertificates.add(entitySecond);

        List<Certificate> actualGiftCertificates = certificateDao.readAll();
        assertEquals(expectedCertificates, actualGiftCertificates);
    }

    @Test
    void deleteById() {
        assertEquals(RECORD_AFFECTED_ONE, certificateDao.deleteById(FIRST_ID));
    }


    private Certificate createDBIdentityCertificate(Long id, String name, String description, BigDecimal price, int duration, int minute) {
        Certificate certificate = new Certificate();
        certificate.setId(id);
        certificate.setName(name);
        certificate.setDescription(description);
        certificate.setPrice(price);
        certificate.setDuration(duration);
        LocalDateTime date = LocalDateTime.of(YEAR, MONTH, DAY, HOUR, minute, SECOND);
        certificate.setCreateDate(date);
        certificate.setLastUpdateDate(date);
        return certificate;
    }

    private Certificate createCertificateEntity() {
        Certificate certificate = new Certificate();
        certificate.setName(CertificateDaoImplTest.FIRST_NAME);
        certificate.setDescription(CertificateDaoImplTest.FIRST_DESCRIPTION);
        certificate.setPrice(CertificateDaoImplTest.FIRST_PRICE);
        certificate.setDuration(CertificateDaoImplTest.FIRST_DURATION);
        return certificate;
    }

}