package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.exception.WrongInsertDataException;
import com.epam.esm.domain.entity.Tag;
import com.epam.esm.dao.mapper.TagMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class TagDaoImpl implements TagDao {

    private static final String SELECT_TAG_BY_NAME = "SELECT * FROM tag WHERE name=?";
    private final String SELECT_TAGS_FOR_CERTIFICATE = "SELECT t.id, t.name FROM tag AS t " +
            "INNER JOIN gift_certificate_has_tag AS ght ON t.id = ght.tag_id " +
            "INNER JOIN gift_certificate AS g ON g.id=ght.gift_certificate_id " +
            "WHERE gift_certificate_id=?";
    private static final String CREATE_TAG = "INSERT INTO tag (`name`) SELECT ? " +
            "WHERE NOT EXISTS (SELECT name FROM tag WHERE name=? LIMIT 1)";
    private static final String SELECT_TAG_BY_ID = "SELECT * FROM tag WHERE id=?";
    private static final String SELECT_ALL_TAGS = "SELECT * FROM tag";
    private static final String DELETE_TAG = "DELETE FROM tag WHERE tag.id = ? AND NOT EXISTS " +
            "(SELECT * FROM gift_certificate_has_tag AS ght WHERE ght.tag_id = ?)";
    private static final String COLUMN_ID = "id";

    private final JdbcTemplate jdbcTemplate;
    private final TagMapper tagMapper;

    public TagDaoImpl(JdbcTemplate jdbcTemplate, TagMapper tagMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.tagMapper = tagMapper;
    }

    @Override
    public Optional<Tag> readByTagName(String name) {
        return jdbcTemplate.query(SELECT_TAG_BY_NAME, tagMapper, name).stream().findFirst();
    }

    @Override
    public List<Tag> readTagsByCertificateId(Long certificateId) {
        return jdbcTemplate.query(SELECT_TAGS_FOR_CERTIFICATE, tagMapper, certificateId);
    }

    @Override
    public Long createFromEntity(Tag entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(con -> {
                PreparedStatement preparedStatement = con.prepareStatement(CREATE_TAG, new String[]{COLUMN_ID});
                String tagName = entity.getName();
                preparedStatement.setString(1, tagName);
                preparedStatement.setString(2, tagName);
                return preparedStatement;
            }, keyHolder);
        } catch (Exception e) {
            throw new WrongInsertDataException();
        }

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public Optional<Tag> readById(Long id) {
        return jdbcTemplate.query(SELECT_TAG_BY_ID, tagMapper, id).stream().findFirst();
    }

    @Override
    public List<Tag> readAll() {
        return jdbcTemplate.query(SELECT_ALL_TAGS, tagMapper);
    }

    @Override
    public Integer deleteById(Long id) {
        return jdbcTemplate.update(DELETE_TAG, id, id);
    }

}