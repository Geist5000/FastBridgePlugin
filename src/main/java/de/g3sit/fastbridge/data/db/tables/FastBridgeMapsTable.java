package de.g3sit.fastbridge.data.db.tables;

import de.g3sit.fastbridge.data.db.models.FastBridgeMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;

import javax.sql.DataSource;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class FastBridgeMapsTable extends Table{

    private static final String SELECT_QUERY = "SELECT * FROM `map`";

    private static final String SELECT_BY_ID_QUERY = SELECT_QUERY + " WHERE id = ?";



    private Server server;



    public FastBridgeMapsTable(DataSource dataSource, Server server) {
        super(dataSource);
        this.server = server;
    }


    public void createTable() {
        try(Connection conn = this.getConnection(); Statement stmt = conn.createStatement()){
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS `map` (" +
                    "     `id` BIGINT NOT NULL AUTO_INCREMENT," +
                    "     `name` VARCHAR(50) NOT NULL," +
                    "     `min_x` INT NOT NULL," +
                    "     `min_y` INT NOT NULL," +
                    "     `min_z` INT NOT NULL," +
                    "     `max_x` INT NOT NULL," +
                    "     `max_y` INT NOT NULL," +
                    "     `max_z` INT NOT NULL," +
                    "     `spawn_x` DECIMAL(9,2) NOT NULL," +
                    "     `spawn_y` DECIMAL(9,2) NOT NULL," +
                    "     `spawn_z` DECIMAL(9,2) NOT NULL," +
                    "     `spawn_yaw` DECIMAL(3,2) NOT NULL," +
                    "     `spawn_pitch` DECIMAL(3,2) NOT NULL," +
                    "     `world` CHAR(36) NOT NULL," +
                    "     PRIMARY KEY (`id`)," +
                    "     UNIQUE INDEX (`name` ASC) VISIBLE);");
        }catch (SQLException exception){
            Bukkit.getLogger().log(Level.WARNING,exception,() -> "Failed creating table");
        }
    }

    /**
     * retrieves all map entries form the database
     * @return is null if an error occurred a list instance with all map entries
     */
    public List<FastBridgeMap> getAll(){
        List<FastBridgeMap> maps = new LinkedList<>();
        try(Connection conn = this.getConnection(); PreparedStatement statement = conn.prepareStatement(SELECT_QUERY)){
            ResultSet set = statement.executeQuery();
            while(set.next()){
                maps.add(this.mapFromResultSet(set));
            }
        }catch (SQLException exception){
            Bukkit.getLogger().log(Level.WARNING,"Failed retrieving all map entries",exception);
            return null;
        }
        return maps;

    }

    /**
     * Retrieves the map entry identified by the given id from the datasource.
     * @param id the id of the map database entrie
     * @return null if an error occurred or no entry with the given id exists, a {@link FastBridgeMap} instance otherwise
     */
    public FastBridgeMap get(long id){
        try(Connection conn = this.getConnection(); PreparedStatement statement = conn.prepareStatement(SELECT_BY_ID_QUERY)){
            statement.setLong(0,id);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next())
                return this.mapFromResultSet(resultSet);
            else
                return null;
        }catch (SQLException exception){
            Bukkit.getLogger().log(Level.WARNING,"Failed retrieving map entry",exception);
            return null;
        }
    }


    public FastBridgeMap mapFromResultSet(ResultSet resultSet) throws SQLException {
        long id = resultSet.getLong("id");
        String name = resultSet.getString("name");
        UUID worldUUID = UUID.fromString(resultSet.getString("world"));
        double minX = resultSet.getInt("min_x");
        double minY = resultSet.getInt(("min_y"));
        double minZ = resultSet.getInt(("min_z"));

        double maxX = resultSet.getInt("max_x");
        double maxY = resultSet.getInt("max_y");
        double maxZ = resultSet.getInt("max_z");

        double spawnX = resultSet.getDouble("spawn_x");
        double spawnY = resultSet.getDouble("spawn_y");
        double spawnZ = resultSet.getDouble("spawn_z");
        float spawnYaw = resultSet.getFloat("spawn_yaw");
        float spawnPitch = resultSet.getFloat("spawn_pitch");

        World world = this.server.getWorld(worldUUID);
        return new FastBridgeMap(id,name,
                new Location(world,minX,minY,minZ),
                new Location(world,maxX,maxY,maxZ),
                new Location(world,spawnX,spawnY,spawnZ,spawnYaw,spawnPitch));
    }


}
