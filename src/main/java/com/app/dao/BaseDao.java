package com.app.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface BaseDao<K, E> {

    E save(E entity, Connection connection) throws SQLException;
    List<E> findAll(Connection connection) throws SQLException;
}
