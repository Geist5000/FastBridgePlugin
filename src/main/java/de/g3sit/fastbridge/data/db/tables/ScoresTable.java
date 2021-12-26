package de.g3sit.fastbridge.data.db.tables;

import org.bukkit.Bukkit;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

public class ScoresTable extends Table{



    public ScoresTable(DataSource dataSource) {
        super(dataSource);
    }


    public void createTable() {
        try(Connection conn = this.getConnection(); Statement stmt = conn.createStatement()){
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS `scores` (" +
                    "    `id` BIGINT NOT NULL AUTO_INCREMENT," +
                    "    `player` CHAR(36) NOT NULL," +
                    "    `map` BIGINT NOT NULL," +
                    "    `time` BIGINT NOT NULL," +
                    "    `run_at` TIMESTAMP NOT NULL," +
                    "    PRIMARY KEY (`id`)," +
                    "    INDEX `Scores_map_map_id` (`map` ASC) VISIBLE," +
                    "    CONSTRAINT `Scores_map_map_id`" +
                    "    FOREIGN KEY (`map`)" +
                    "    REFERENCES `map` (`id`)" +
                    "    ON DELETE SET NULL" +
                    "    ON UPDATE CASCADE);");


        }catch (SQLException exception){
            Bukkit.getLogger().log(Level.WARNING,"Failed >creating table",exception);
        }


    }


}
