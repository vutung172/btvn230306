package com.ra.repository.Impl;

import com.ra.repository.Repository;
import com.ra.util.Annotation.*;
import com.ra.util.MySqlConnect.MySQLConnect;

import java.lang.reflect.Field;
import java.sql.*;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.stream.Collectors;

public class RepositoryImpl<T> implements Repository<T> {
    @Override
    public List<T> findAll(Class<T> entityClass) {
        List<T> result = new ArrayList<>();
        try (Connection conn = new MySQLConnect().getConnection()) {
            String sql = MessageFormat.format("SELECT * FROM {0}", tblName(entityClass));
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                T entity = readResultSet(rs, entityClass);
                result.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public T findId(Class<T> entityClass, Object... keys) {
        try (Connection conn = new MySQLConnect().getConnection()) {
            List<Field> keysField = getKey(entityClass);
            String keysName = keysField.stream().map(f -> colName(f) + " = ?").collect(Collectors.joining(" , "));
            String sql = MessageFormat.format("SELECT * FROM {0} WHERE {1}", tblName(entityClass), keysName);

            PreparedStatement ps = conn.prepareStatement(sql);
            int index = 1;
            for (Object key : keys)
                ps.setObject(index++, key);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                T entity = readResultSet(rs, entityClass);
                return entity;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public T add(T entity) {
        try (Connection conn = new MySQLConnect().getConnection()) {
            List<Field> fields = getColumns((Class<T>) entity.getClass());
            String columns = fields.stream().map(this::colName).collect(Collectors.joining(","));
            String values = fields.stream().map(f -> "?").collect(Collectors.joining(","));
            String sql = MessageFormat.format("INSERT INTO {0}({1}) VALUES ({2})", tblName((Class<T>) entity.getClass()), columns, values);
            PreparedStatement ps = conn.prepareStatement(sql);
            int index = 1;
            for (Field f : fields) {
                f.setAccessible(true);
                ps.setObject(index++, f.get(entity));
            }
            ps.executeUpdate();

            return entity;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public T addIgnoreId(T entity) {
        try (Connection connection = new MySQLConnect().getConnection()) {
            List<Field> fields = getColumnsIgnoreKey((Class<T>) entity.getClass());
            List<Field> keyfields = getKey((Class<T>) entity.getClass());
            String columns = fields.stream().map(this::colName).collect(Collectors.joining(","));
            String values = fields.stream().map(f -> "?").collect(Collectors.joining(","));
            String sql = MessageFormat.format("INSERT INTO {0}({1}) VALUES ({2})", tblName((Class<T>) entity.getClass()), columns, values);

            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            int index = 1;
            for (Field f : fields) {
                f.setAccessible(true);
                ps.setObject(index++, f.get(entity));
            }
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            while (rs.next()) {
                for (Field field : keyfields) {
                    field.setAccessible(true);
                    field.set(entity, rs.getInt(1));
                }
            }
            return entity;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public T edit(T entity) {
        try (Connection conn = new MySQLConnect().getConnection()) {
            List<Field> updateFields = getColumnsIgnoreKey((Class<T>) entity.getClass());
            List<Field> keyFields = getKey((Class<T>) entity.getClass());
            String columns = updateFields.stream().map(f -> colName(f) + " = ?").collect(Collectors.joining(","));
            String key = keyFields.stream().map(f -> colName(f) + " = ?").collect(Collectors.joining(","));
            String sql = MessageFormat.format("UPDATE {0} SET {1} WHERE {2}", tblName((Class<T>) entity.getClass()), columns, key);
            PreparedStatement ps = conn.prepareStatement(sql);
            int index = 1;
            for (Field f : updateFields) {
                f.setAccessible(true);
                ps.setObject(index++, f.get(entity));
            }
            for (Field f : keyFields) {
                f.setAccessible(true);
                ps.setObject(index++, f.get(entity));
            }
            ps.executeUpdate();
            return entity;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean remove(Class<T> entityClass, Object... keys) {
        try (Connection conn = new MySQLConnect().getConnection()) {
            List<Field> keysField = getKey(entityClass);
            String keysName = keysField.stream().map(f -> colName(f) + " = ?").collect(Collectors.joining(","));
            String sql = MessageFormat.format("DELETE FROM {0} WHERE {1}", tblName(entityClass), keysName);

            PreparedStatement ps = conn.prepareStatement(sql);
            int index = 1;
            for (Object key : keys)
                ps.setObject(index++, key);
            ps.executeUpdate();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public List<T> findAllByPagination(Class<T> entityClass, Integer pageNumber) {
        List<T> result = new ArrayList<>();
        try (Connection conn = new MySQLConnect().getConnection()) {
            if (pageNumber > 0) {
                int offset = (pageNumber - 1) * 10;
                String sql = MessageFormat.format("SELECT * FROM {0} LIMIT 10 OFFSET {1}", tblName(entityClass), offset);

                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                rs.getFetchSize();
                while (rs.next()) {
                    T entity = readResultSet(rs, entityClass);
                    result.add(entity);
                }
            } else {
                System.err.println("page error");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<T> findRelativeByIndexes(Class<T> entityClass, Object... anys) {
        List<T> result = new ArrayList<>();
        try (Connection conn = new MySQLConnect().getConnection()) {
            List<Field> keysField = getIndexes(entityClass);
            String keysName = keysField.stream().map(f -> colName(f) + " LIKE concat('%',?,'%')").collect(Collectors.joining(" OR "));
            String sql = MessageFormat.format("SELECT * FROM {0} WHERE {1}", tblName(entityClass), keysName);

            PreparedStatement ps = conn.prepareStatement(sql);
            int index = 1;
            for (Object any : anys) {
                ps.setObject(index++, any);
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                T entity = readResultSet(rs, entityClass);
                result.add(entity);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<T> findAbsoluteByIndexes(Class<T> entityClass, Object... anys) {
        List<T> result = new ArrayList<>();
        try (Connection conn = new MySQLConnect().getConnection()) {
            List<Field> keysField = getIndexes(entityClass);
            String keysName = keysField.stream().map(f -> colName(f) + " = ?").collect(Collectors.joining(" OR "));
            String sql = MessageFormat.format("SELECT * FROM {0} WHERE {1}", tblName(entityClass), keysName);

            PreparedStatement ps = conn.prepareStatement(sql);
            int index = 1;
            for (Object any : anys) {
                ps.setObject(index++, any);
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                T entity = readResultSet(rs, entityClass);
                result.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<T> findByIndexesPagination(Class<T> entityClass, String any, Integer pageNumber) {
        List<T> result = new ArrayList<>();
        try (Connection conn = new MySQLConnect().getConnection()) {
            List<Field> keysField = getIndexes(entityClass);
            String keysName = keysField.stream().map(f -> colName(f) + " LIKE concat('%',?,'%')").collect(Collectors.joining(" OR "));
            if (pageNumber > 0) {
                int offset = (pageNumber - 1) * 10;
                String sql = MessageFormat.format("SELECT * FROM {0} WHERE {1} LIMIT 10 OFFSET {2}", tblName(entityClass), keysName, offset);

                PreparedStatement ps = conn.prepareStatement(sql);
                for (int i = 1; i <= keysField.size(); i++)
                    ps.setObject(i, any);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    T entity = readResultSet(rs, entityClass);
                    result.add(entity);
                }
            } else {
                System.err.println("page error");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    @Override
    public List<T> findByMark(Class<T> entityClass, Object... targets) {
        List<T> result = new ArrayList<>();
        try (Connection conn = new MySQLConnect().getConnection()) {
            List<Field> keysField = getMark(entityClass);
            String keysName = keysField.stream().map(f -> colName(f) + " = ?").collect(Collectors.joining(" AND "));
            String sql = MessageFormat.format("SELECT * FROM {0} WHERE {1}", tblName(entityClass), keysName);

            PreparedStatement ps = conn.prepareStatement(sql);
            int index = 1;
            for (Object key : targets)
                ps.setObject(index++, key);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                T entity = readResultSet(rs, entityClass);
                result.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public T authenticator(Class<T> entityClass, Object... keys) {
        try (Connection conn = new MySQLConnect().getConnection()) {
            List<Field> authsField = getAuth(entityClass);
            String authsName = authsField.stream().map(f -> colName(f) + " = ?").collect(Collectors.joining(" AND "));
            String sql = MessageFormat.format("SELECT * FROM {0} WHERE {1}", tblName(entityClass), authsName);

            PreparedStatement ps = conn.prepareStatement(sql);
            int index = 1;
            for (Object key : keys)
                ps.setObject(index++, key);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                T entity = readResultSet(rs, entityClass);
                return entity;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //Editor
    private T readResultSet(ResultSet rs, Class<T> clazz) throws Exception {
        T entity = clazz.getDeclaredConstructor().newInstance();
        List<Field> fields = getColumns(clazz);
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.getType().equals(Date.class)) {
                SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                field.set(entity, fmt.parse(rs.getString(colName(field))));
            } else {
                field.set(entity, rs.getObject(colName(field)));
            }
        }
        return entity;
    }

    private String colName(Field field) {
        Column column = field.getAnnotation(Column.class);
        if (Objects.nonNull(column))
            return column.name();
        return null;
    }

    private String tblName(Class<T> entityClass) {
        Table table = entityClass.getAnnotation(Table.class);
        if (Objects.nonNull(table))
            return table.name();
        return null;
    }

    private List<Field> getColumns(Class<T> entityClass) {
        Field[] fields = entityClass.getDeclaredFields();
        return Arrays.stream(fields)
                .filter(f -> Objects.nonNull(f.getAnnotation(Column.class)))
                .collect(Collectors.toList());
    }

    private List<Field> getIndexes(Class<T> entityClass) {
        Field[] fields = entityClass.getDeclaredFields();
        return Arrays.stream(fields)
                .filter(f -> Objects.nonNull(f.getAnnotation(Index.class)))
                .collect(Collectors.toList());
    }

    private String indexName(Field field) {
        Index index = field.getAnnotation(Index.class);
        if (Objects.nonNull(index))
            return index.name();
        return null;
    }

    private List<Field> getColumnsIgnoreKey(Class<T> entityClass) {
        List<Field> fields = getColumns(entityClass);
        return fields.stream()
                .filter(f -> Objects.isNull(f.getAnnotation(Id.class)))
                .collect(Collectors.toList());
    }

    private List<Field> getKey(Class<T> entityClass) {
        List<Field> fields = getColumns(entityClass);
        return fields.stream()
                .filter(f -> Objects.nonNull(f.getAnnotation(Id.class)))
                .collect(Collectors.toList());
    }

    private List<Field> getAuth(Class<T> entityClass) {
        List<Field> fields = getColumns(entityClass);
        return fields.stream()
                .filter(f -> Objects.nonNull(f.getAnnotation(Auth.class)))
                .collect(Collectors.toList());
    }

    private List<Field> getMark(Class<T> entityClass) {
        List<Field> fields = getColumns(entityClass);
        return fields.stream()
                .filter(f -> Objects.nonNull(f.getAnnotation(Mark.class)))
                .collect(Collectors.toList());
    }
    //End-Editor
}
