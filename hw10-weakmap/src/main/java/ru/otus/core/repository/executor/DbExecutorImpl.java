package ru.otus.core.repository.executor;

import ru.otus.core.sessionmanager.DataBaseOperationException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;


public class DbExecutorImpl implements DbExecutor {


    @Override
    public long executeStatement(Connection connection, String sql, List<Object> params) {
        try (var pst = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            for (var idx = 0; idx < params.size(); idx++) {
                pst.setObject(idx + 1, params.get(idx));
            }
            int rowsAffected = pst.executeUpdate();

            if (rowsAffected == 0) {
                throw new DataBaseOperationException("Операция не затронула ни одной строки");
            }

            try (var rs = pst.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
                throw new DataBaseOperationException("Не удалось получить сгенерированный ключ");
            }
        } catch (SQLException ex) {
            throw new DataBaseOperationException("Ошибка при выполнении операции");
        }
    }

    @Override
    public <T> Optional<T> executeSelect(
            Connection connection, String sql, List<Object> params, Function<ResultSet, T> rsHandler) {
        try (var pst = connection.prepareStatement(sql)) {
            for (var idx = 0; idx < params.size(); idx++) {
                pst.setObject(idx + 1, params.get(idx));
            }
            try (var rs = pst.executeQuery()) {
                return Optional.ofNullable(rsHandler.apply(rs));
            }
        } catch (SQLException ex) {
            throw new DataBaseOperationException("executeSelect error");
        }
    }
}
