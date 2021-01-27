package com.epam.esm.dao.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.exception.WrongInsertDataException;
import com.epam.esm.dao.mapper.ColumnName;
import com.epam.esm.domain.entity.Certificate;
import com.epam.esm.dao.mapper.CertificateMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class CertificateDaoImpl implements CertificateDao {

    private static final String UPDATE_CERTIFICATE = "UPDATE gift_certificate  SET " +
            "name = COALESCE(?, name), description = COALESCE(?, description), " +
            "price = COALESCE(?, price), duration = COALESCE(?, duration) WHERE (`id` = ?)";
    private static final String INSERT_INTO_CERT_HAS_TAG = "INSERT INTO gift_certificate_has_tag " +
            "(`gift_certificate_id`, `tag_id`) VALUES (?, ?)";
    private static final String SELECT_BY_SEARCH_PARAMETERS = "SELECT c.id, c.name, c.description, c.price, " +
            "c.duration, c.create_date, c.last_update_date " +
            "FROM gift_certificate AS c " +
            "JOIN gift_certificate_has_tag AS cht ON c.id = cht.gift_certificate_id " +
            "JOIN tag AS t on cht.tag_id=t.id ";
    private static final String INSERT_CERTIFICATE = "INSERT INTO gift_certificate " +
            "(`name`, `description`, `price`, `duration`) VALUES (?, ?, ?, ?)";
    private static final String SELECT_CERTIFICATE_BY_ID = "SELECT c.id, c.name, c.description, c.price, " +
            "c.duration, c.create_date, c.last_update_date FROM gift_certificate AS c WHERE id=?";
    private static final String SELECT_ALL_CERTIFICATES = "SELECT c.id, c.name, c.description, c.price, " +
            "c.duration, c.create_date, c.last_update_date FROM gift_certificate AS c";
    private static final String DELETE_CERTIFICATE = "DELETE FROM gift_certificate WHERE id = ?";

    private final JdbcTemplate jdbcTemplate;
    private final CertificateMapper certificateMapper;

    public CertificateDaoImpl(CertificateMapper certificateMapper, DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.certificateMapper = certificateMapper;
    }

    @Override
    public Integer update(Certificate certificate) {
        return jdbcTemplate.update(UPDATE_CERTIFICATE,
                certificate.getName(),
                certificate.getDescription(),
                certificate.getPrice(),
                certificate.getDuration(),
                certificate.getId());
    }

    @Override
    public Integer createCertificateHasTag(Long certificateId, Long tagId) {
        return jdbcTemplate.update(INSERT_INTO_CERT_HAS_TAG, certificateId, tagId);
    }

    @Override
    public List<Certificate> searchByParameters(String query, List<String> parameters) {
        String searchQuery = SELECT_BY_SEARCH_PARAMETERS.concat(query);
        return jdbcTemplate.query(searchQuery, certificateMapper, parameters.toArray());
    }

    @Override
    public Long create(Certificate entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(INSERT_CERTIFICATE, new String[]{ColumnName.COLUMN_ID});
                ps.setString(1, entity.getName());
                ps.setString(2, entity.getDescription());
                ps.setBigDecimal(3, entity.getPrice());
                ps.setInt(4, entity.getDuration());
                return ps;
            }, keyHolder);
        } catch (Exception e) {
            throw new WrongInsertDataException();
        }
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public Optional<Certificate> readById(Long id) {
        return jdbcTemplate.query(SELECT_CERTIFICATE_BY_ID, certificateMapper, id)
                .stream()
                .findFirst();
    }

    @Override
    public List<Certificate> readAll() {
        return jdbcTemplate.query(SELECT_ALL_CERTIFICATES, certificateMapper);
    }

    @Override
    public Integer deleteById(Long id) {
        return jdbcTemplate.update(DELETE_CERTIFICATE, id);
    }
}