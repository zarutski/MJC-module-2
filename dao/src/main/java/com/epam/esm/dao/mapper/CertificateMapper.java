package com.epam.esm.dao.mapper;

import com.epam.esm.domain.entity.Certificate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;

@Component
public class CertificateMapper implements RowMapper<Certificate> {

    @Override
    public Certificate mapRow(ResultSet rs, int rowNum) throws SQLException {
        Certificate certificate = new Certificate();
        certificate.setId(rs.getLong(ColumnName.COLUMN_ID));
        certificate.setName(rs.getString(ColumnName.COLUMN_NAME));
        certificate.setDescription(rs.getString(ColumnName.COLUMN_DESCRIPTION));
        certificate.setPrice(rs.getBigDecimal(ColumnName.COLUMN_PRICE));
        certificate.setDuration(rs.getInt(ColumnName.COLUMN_DURATION));
        certificate.setCreateDate(rs.getTimestamp(ColumnName.COLUMN_CREATE_DATE).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        certificate.setLastUpdateDate(rs.getTimestamp(ColumnName.COLUMN_LAST_UPDATE_DATE).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        return certificate;
    }
}