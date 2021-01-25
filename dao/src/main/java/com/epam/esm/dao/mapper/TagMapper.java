package com.epam.esm.dao.mapper;

import com.epam.esm.domain.entity.Tag;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TagMapper implements RowMapper<Tag> {

    private static final String ID = "id";
    private static final String NAME = "name";

    @Override
    public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
        Tag tag = new Tag();
        tag.setId(rs.getLong(ID));
        tag.setName(rs.getString(NAME));
        return tag;
    }
}