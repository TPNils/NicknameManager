package be.spyproof.nickmanager.performace;

import be.spyproof.nickmanager.util.DateUtil;

import java.io.File;
import java.sql.Connection;

/**
 * Created by Spyproof on 03/11/2016.
 */
public class Performance
{
    public void mySql() throws Exception
    {
        // Reset the database
        try (Connection connection = TestReference.getLocalSqlConnection())
        {
            connection.prepareStatement("DELETE FROM player;").execute();

            connection.prepareStatement("ALTER TABLE nickname_test.nickname_data DROP FOREIGN KEY fk_nickname_data_player_id;").execute();
            connection.prepareStatement("ALTER TABLE nickname_test.archived_nickname DROP FOREIGN KEY fk_archived_nickname_player_id;").execute();
            connection.prepareStatement("ALTER TABLE nickname_test.active_nickname DROP FOREIGN KEY fk_active_nickname_player_id;").execute();
            connection.prepareStatement("DROP TABLE nickname_test.nickname_data;").execute();
            connection.prepareStatement("DROP TABLE nickname_test.archived_nickname;").execute();
            connection.prepareStatement("DROP TABLE nickname_test.active_nickname;").execute();
            connection.prepareStatement("DROP TABLE nickname_test.player;").execute();

            connection.prepareStatement("DROP PROCEDURE IF EXISTS nickname_test.setPlayerLastSeen;").execute();
            connection.prepareStatement("DROP PROCEDURE IF EXISTS nickname_test.archiveNickname;").execute();
            connection.prepareStatement("DROP PROCEDURE IF EXISTS nickname_test.getNicknameDataByName;").execute();
            connection.prepareStatement("DROP PROCEDURE IF EXISTS nickname_test.getNicknameDataByUuid;").execute();
            connection.prepareStatement("DROP PROCEDURE IF EXISTS nickname_test.getNicknameDataByNickname;").execute();
            connection.prepareStatement("DROP PROCEDURE IF EXISTS nickname_test.getNicknameDataByPlayerId;").execute();
            connection.prepareStatement("DROP PROCEDURE IF EXISTS nickname_test.archiveNickname;").execute();
            connection.prepareStatement("DROP FUNCTION IF EXISTS nickname_test.getPlayerIdFromName;").execute();
            connection.prepareStatement("DROP FUNCTION IF EXISTS nickname_test.getPlayerIdFromUuid;").execute();
            connection.prepareStatement("DROP FUNCTION IF EXISTS nickname_test.getUnformattedString;").execute();
            connection.prepareStatement("DROP PROCEDURE IF EXISTS nickname_test.removePlayer;").execute();
            connection.prepareStatement("DROP PROCEDURE IF EXISTS nickname_test.saveNicknamePlayerData;").execute();
            connection.prepareStatement("DROP PROCEDURE IF EXISTS nickname_test.savePlayer;").execute();
            connection.prepareStatement("DROP PROCEDURE IF EXISTS nickname_test.setActiveNickname;").execute();
        }

        // Start the test
        long timestamp = System.currentTimeMillis();
        System.out.print("initializing MySQL: ");
        PerformanceTester tester = new PerformanceTester(TestReference.getLocalSqlPlayerStorage());
        System.out.println(DateUtil.timeformat(System.currentTimeMillis() - timestamp));

        tester.createPlayers(2000);
        tester.createAndUpdatePlayers(10);
        tester.close();
        tester.setPlayerStorage(TestReference.getLocalSqlPlayerStorage());
        tester.getByUuid(1);
        tester.getByName(1);
        tester.getByNickname(1);
        tester.createAndUpdatePlayers(1);
        tester.close();

        // Remove all create players
        try (Connection connection = TestReference.getLocalSqlConnection())
        {
            //connection.prepareStatement("DELETE FROM player;").execute();
        }
    }

    public void gson() throws Exception
    {
        // Delete all files that are currently available
        File dir = TestReference.getTempDir();
        {
            File[] files = dir.listFiles();
            if (files != null)
                for (File file : files)
                    file.delete();
        }

        // Start the test
        long timestamp = System.currentTimeMillis();
        System.out.print("initializing JSON: ");
        PerformanceTester tester = new PerformanceTester(TestReference.getGsonPlayerStorage());
        System.out.println(DateUtil.timeformat(System.currentTimeMillis() - timestamp));

        tester.createPlayers(100);
        tester.createAndUpdatePlayers(10);
        tester.getByUuid(1);
        tester.getByName(1);
        tester.getByNickname(1);
        tester.createAndUpdatePlayers(1);
        tester.close();

        // Delete all files that were generated
        {
            File[] files = dir.listFiles();
            if (files != null)
                for (File file : files)
                    file.delete();
            dir.delete();
        }
    }
}
