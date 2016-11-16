package be.spyproof.nickmanager.test;

import be.spyproof.nickmanager.da.player.GsonPlayerStorage;
import be.spyproof.nickmanager.da.player.MySqlPlayerStorage;
import be.spyproof.nickmanager.model.PlayerData;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Created by Spyproof on 28/10/2016.
 */
public class TestReference
{
    public static MySqlPlayerStorage getLocalSqlPlayerStorage() throws IOException, SQLException
    {
        return new MySqlPlayerStorage("localhost", 3306, "nickname_test", "root", "");
    }

    public static Connection getSqlConnection() throws SQLException
    {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/nickname_test", "root", "");
    }

    public static GsonPlayerStorage getGsonPlayerStorage() throws IOException, SQLException
    {
        return new GsonPlayerStorage(new File(System.getProperty("java.io.tmpdir"), "nickname-test"));
    }

    public static PlayerData getPlayerData()
    {
        return new PlayerData("NotTP", UUID.fromString("75002d64-cc35-3541-b6a4-c70e6aab5883"));
    }
}
