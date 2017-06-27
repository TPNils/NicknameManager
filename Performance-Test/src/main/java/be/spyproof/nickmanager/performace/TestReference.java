package be.spyproof.nickmanager.performace;

import be.spyproof.nickmanager.da.player.GsonPlayerStorage;
import be.spyproof.nickmanager.da.player.MySqlPlayerStorage;
import be.spyproof.nickmanager.model.NicknameData;

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
    private static String host, database, user, password, tempDir;
    private static int port;

    public static void Init(String host_, int port_, String database_, String user_, String password_, String dir)
    {
        host = host_;
        port = port_;
        database = database_;
        user = user_;
        password = password_;
        tempDir = dir;
    }

    public static MySqlPlayerStorage getLocalSqlPlayerStorage() throws IOException, SQLException
    {
        return new MySqlPlayerStorage(host, port, database, user, password);
    }

    public static Connection getLocalSqlConnection() throws SQLException
    {
        return DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, user, password);
    }

    public static GsonPlayerStorage getGsonPlayerStorage() throws IOException, SQLException
    {
        return new GsonPlayerStorage(getTempDir());
    }

    public static File getTempDir()
    {
        return new File(tempDir);
    }

    public static NicknameData getPlayerData()
    {
        return new NicknameData("NotTP", UUID.fromString("75002d64-cc35-3541-b6a4-c70e6aab5883"));
    }
}
