package de.g3sit.fastbridge.data.db.tables;

import org.bukkit.Bukkit;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.logging.Level;

public abstract class Table {

    private DataSource dataSource;

    public Table(DataSource dataSource) {
        if(Objects.isNull(dataSource))
            throw new IllegalArgumentException("dataSource cannot be null");
        this.dataSource = dataSource;
    }

    protected Connection getConnection() throws SQLException {
        return this.dataSource.getConnection();
    }
}
