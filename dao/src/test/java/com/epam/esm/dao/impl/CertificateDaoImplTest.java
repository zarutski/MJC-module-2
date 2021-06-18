package com.epam.esm.dao.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.config.TestConfig;
import com.epam.esm.domain.entity.Certificate;
import com.epam.esm.domain.entity.Tag;
import com.epam.esm.domain.util.SearchParameter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
class CertificateDaoImplTest {

    private static final Long FIRST_ID = 1L;
    private static final String FIRST_NAME = "name 1";
    private static final String FIRST_DESCRIPTION = "description 1";
    private static final BigDecimal FIRST_PRICE = BigDecimal.valueOf(200);
    private static final int FIRST_DURATION = 120;
    private static final int FIRST_MINUTE = 41;

    private static final Long SECOND_ID = 2L;
    private static final String SECOND_NAME = "name 2";
    private static final String SECOND_DESCRIPTION = "description 2";
    private static final BigDecimal SECOND_PRICE = BigDecimal.valueOf(200);
    private static final int SECOND_DURATION = 240;
    private static final int SECOND_MINUTE = 51;

    private static final int YEAR = 2021;
    private static final int MONTH = 1;
    private static final int DAY = 26;
    private static final int HOUR = 3;
    private static final int SECOND = 58;

    private static final Integer PAGE_NUMBER = 1;
    private static final Integer RECORDS_PER_PAGE = 2;
    private static final Long RECORDS_COUNT = 2L;

    private static final String DB_FIELD_NAME = "name";
    private static final String DB_FIELD_DESCRIPTION = "description";
    private static final String DB_TAG_NAME = "abdullah";
    private static final String SORT_ORDER_DESC = "DESC";

    @Autowired
    private CertificateDao certificateDao;
    @Autowired
    private TagDao tagDao;

    @Test
    void readById() {
        Certificate expected = createDBCertificateFirstID();
        Optional<Certificate> actual = certificateDao.readById(FIRST_ID);
        assertEquals(expected, actual.orElseThrow(RuntimeException::new));
    }

    @Test
    void readAll() {
        List<Certificate> expectedCertificates = createCertificateList(Boolean.FALSE);
        List<Certificate> actualGiftCertificates = certificateDao.readAll(PAGE_NUMBER, RECORDS_PER_PAGE);
        assertEquals(expectedCertificates, actualGiftCertificates);
    }

    @Test
    void getEntitiesCount() {
        Long entitiesCount = certificateDao.getEntitiesCount();
        assertEquals(RECORDS_COUNT, entitiesCount);
    }

    @Test
    void getEntitiesCountBySearchParameter() {
        SearchParameter searchParameter = new SearchParameter();
        searchParameter.setName(DB_FIELD_NAME);
        Long entitiesCount = certificateDao.getEntitiesCount(searchParameter);
        assertEquals(RECORDS_COUNT, entitiesCount);
    }

    @Test
    void create() {
        Certificate certificate = createCertificateEntity();
        certificate.setTags(Collections.singletonList(getTagById(SECOND_ID)));
        Optional<Certificate> actual = certificateDao.create(certificate);
        assertEquals(certificate, actual.orElseThrow(RuntimeException::new));
    }

    @Test
    void delete() {
        Certificate certificate = createCertificateEntity();
        assertDoesNotThrow(() -> certificateDao.delete(certificate));
    }

    @Test
    void searchByParameters() {
        List<Certificate> expectedCertificates = createCertificateList(Boolean.TRUE);
        List<String> tagNames = formTagNames();
        SearchParameter searchParameter = new SearchParameter();
        searchParameter.setName(DB_FIELD_NAME);
        searchParameter.setDescription(DB_FIELD_DESCRIPTION);
        searchParameter.setTagNames(tagNames);
        searchParameter.setSortBy(DB_FIELD_NAME);
        searchParameter.setOrder(SORT_ORDER_DESC);
        List<Certificate> actual = certificateDao.searchByParameters(searchParameter, PAGE_NUMBER, RECORDS_PER_PAGE);
        assertEquals(expectedCertificates, actual);
    }

    @Test
    void searchByParametersEmptyTagList() {
        List<Certificate> expectedCertificates = createCertificateList(Boolean.FALSE);
        SearchParameter searchParameter = new SearchParameter();
        searchParameter.setTagNames(Collections.emptyList());
        List<Certificate> actual = certificateDao.searchByParameters(searchParameter, PAGE_NUMBER, RECORDS_PER_PAGE);
        assertEquals(expectedCertificates, actual);
    }

    @Test
    void update() {
        Certificate certificate = createDBCertificateFirstID();
        certificate.setName(SECOND_NAME);
        Optional<Certificate> updatedCertificate = certificateDao.update(certificate);
        Certificate actual = updatedCertificate.orElseThrow(RuntimeException::new);
        certificate.setLastUpdateDate(actual.getLastUpdateDate());
        assertEquals(certificate, actual);
    }

    private Certificate createDBIdentityCertificate(Long id, String name, String description,
                                                    BigDecimal price, int duration, int minute) {
        Certificate certificate = new Certificate();
        certificate.setId(id);
        certificate.setName(name);
        certificate.setDescription(description);
        certificate.setPrice(price);
        certificate.setDuration(duration);
        LocalDateTime date = LocalDateTime.of(YEAR, MONTH, DAY, HOUR, minute, SECOND);
        certificate.setCreateDate(date);
        certificate.setLastUpdateDate(date);
        certificate.setTags(Collections.singletonList(getTagById(id)));
        return certificate;
    }

    private Tag getTagById(Long id) {
        Optional<Tag> tag = tagDao.readById(id);
        return tag.orElseThrow(RuntimeException::new);
    }

    private Certificate createCertificateEntity() {
        Certificate certificate = new Certificate();
        certificate.setName(CertificateDaoImplTest.FIRST_NAME);
        certificate.setDescription(CertificateDaoImplTest.FIRST_DESCRIPTION);
        certificate.setPrice(CertificateDaoImplTest.FIRST_PRICE);
        certificate.setDuration(CertificateDaoImplTest.FIRST_DURATION);
        return certificate;
    }

    private Certificate createDBCertificateFirstID() {
        return createDBIdentityCertificate(FIRST_ID, FIRST_NAME,
                FIRST_DESCRIPTION, FIRST_PRICE, FIRST_DURATION, FIRST_MINUTE);
    }

    private Certificate createDBCertificateSecondID() {
        return createDBIdentityCertificate(SECOND_ID, SECOND_NAME,
                SECOND_DESCRIPTION, SECOND_PRICE, SECOND_DURATION, SECOND_MINUTE);
    }

    private List<Certificate> createCertificateList(boolean isSingleEntryList) {
        List<Certificate> certificates = new ArrayList<>();
        certificates.add(createDBCertificateFirstID());
        if (!isSingleEntryList) {
            certificates.add(createDBCertificateSecondID());
        }
        return certificates;
    }

    private List<String> formTagNames() {
        List<String> tagNames = new ArrayList<>();
        tagNames.add(DB_TAG_NAME);
        return tagNames;
    }
}