package com.epam.esm.dao.mapper;

import com.epam.esm.domain.entity.Certificate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;

@Component
public class CertificateMapper implements RowMapper<Certificate> {

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String PRICE = "price";
    private static final String DURATION = "duration";
    private static final String CREATE_DATE = "create_date";
    private static final String LAST_UPDATE_DATE = "last_update_date";

    @Override
    public Certificate mapRow(ResultSet rs, int rowNum) throws SQLException {
        Certificate certificate = new Certificate();
        certificate.setId(rs.getLong(ID));
        certificate.setName(rs.getString(NAME));
        certificate.setDescription(rs.getString(DESCRIPTION));
        certificate.setPrice(rs.getBigDecimal(PRICE));
        certificate.setDuration(rs.getInt(DURATION));
        certificate.setCreateDate(rs.getTimestamp(CREATE_DATE).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        certificate.setLastUpdateDate(rs.getTimestamp(LAST_UPDATE_DATE).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        return certificate;
    }
}