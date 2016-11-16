--
-- Creating PROCEDURE
--

DROP PROCEDURE IF EXISTS `savePlayer`;
DELIMITER $$
CREATE PROCEDURE `savePlayer`(IN last_known_name_in VARCHAR(16), IN uuid_in CHAR(36))
  BEGIN
    DECLARE stored_id INT UNSIGNED DEFAULT NULL;
    DECLARE stored_name VARCHAR(16) DEFAULT NULL;

    SELECT
      id,
      last_known_name
    INTO stored_id, stored_name
    FROM player
    WHERE uuid = uuid_in;

    IF stored_id IS NULL
    THEN
      INSERT INTO player (last_known_name, uuid)
      VALUES (last_known_name_in, uuid_in);
    ELSE
      UPDATE player
      SET last_known_name = last_known_name_in
      WHERE uuid = uuid_in;

      IF last_known_name_in != stored_name
      THEN
        UPDATE player
        SET name_updated = current_timestamp
        WHERE uuid = uuid_in;
      END IF;
    END IF;
  END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS `removePlayer`;
DELIMITER $$
CREATE PROCEDURE `removePlayer`(IN uuid_in CHAR(36))
  BEGIN
    DELETE FROM player
    WHERE uuid = uuid_in;
  END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS `saveNicknamePlayerData`;
DELIMITER $$
CREATE PROCEDURE `saveNicknamePlayerData`(IN uuid_in           CHAR(36), IN last_changed_in BIGINT, IN tokens_in INT,
                                          IN accepted_rules_in BOOLEAN)
  BEGIN
    DECLARE stored_id INT UNSIGNED DEFAULT NULL;

    SELECT id
    INTO stored_id
    FROM player
    WHERE uuid = uuid_in;

    IF stored_id IS NOT NULL
    THEN
      IF (SELECT player_id
          FROM nickname_data
          WHERE player_id = stored_id) IS NULL
      THEN
        INSERT INTO nickname_data (player_id, last_changed, tokens, accepted_rules)
        VALUES (stored_id, last_changed_in, tokens_in, accepted_rules_in);
      ELSE
        UPDATE nickname_data
        SET last_changed = last_changed_in, tokens = tokens_in, accepted_rules = accepted_rules_in
        WHERE player_id = stored_id;
      END IF;
    END IF;
  END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS `setPlayerLastSeen`;
DELIMITER $$
CREATE PROCEDURE `setPlayerLastSeen`(IN uuid_in CHAR(36))
  BEGIN

    UPDATE player
    SET last_seen = CURRENT_TIMESTAMP()
    WHERE uuid = uuid_in;

  END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS `setActiveNickname`;
DELIMITER $$
CREATE PROCEDURE `setActiveNickname`(IN uuid_in CHAR(36), IN nickname_in VARCHAR(255))
  BEGIN
    DECLARE stored_id INT UNSIGNED DEFAULT NULL;
    DECLARE stored_nick VARCHAR(255) DEFAULT NULL;

    SET stored_id = getPlayerIdFromUuid(uuid_in);

    SELECT nickname
    INTO stored_nick
    FROM active_nickname
    WHERE player_id = stored_id;

    IF stored_nick IS NOT NULL
    THEN
      CALL archiveNickname(uuid_in);
    END IF;

    IF (SELECT COUNT(*)
        FROM active_nickname
        WHERE player_id = stored_id) = 0
    THEN
      IF nickname_in IS NOT NULL
      THEN
        INSERT INTO active_nickname (player_id, nickname, unformatted_nickname)
        VALUES (stored_id, nickname_in, getUnformattedString(nickname_in));
      END IF;
    ELSE
      IF nickname_in IS NULL
      THEN
        DELETE FROM active_nickname
        WHERE player_id = stored_id;
      ELSE
        UPDATE active_nickname
        SET
          nickname             = nickname_in,
          unformatted_nickname = getUnformattedString(nickname_in),
          created              = CURRENT_TIMESTAMP()
        WHERE player_id = stored_id;
      END IF;
    END IF;
  END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS `archiveNickname`;
DELIMITER $$
CREATE PROCEDURE `archiveNickname`(IN uuid_in CHAR(36))
  BEGIN
    INSERT INTO archived_nickname (player_id, nickname, unformatted_nickname, created)
      SELECT
        player_id,
        nickname,
        unformatted_nickname,
        created
      FROM active_nickname
      WHERE player_id = getPlayerIdFromUuid(uuid_in);
  END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS `getNicknameDataByPlayerId`;
DELIMITER $$
CREATE PROCEDURE `getNicknameDataByPlayerId`(IN id_in INT UNSIGNED)
  BEGIN
    SELECT
      player.last_known_name                                          AS `Last known name`,
      player.uuid                                                     AS `UUID`,
      IFNULL(nickname_data.last_changed, 0)                           AS `Last changed`,
      IFNULL(nickname_data.tokens, 0)                                 AS `Tokens`,
      IFNULL(nickname_data.accepted_rules, FALSE)                     AS `Accepted rules`,
      active_nickname.nickname                                        AS `Nickname`,
      active_nickname.unformatted_nickname                            AS `Unformatted nickname`,
      group_concat(DISTINCT archived_nickname.nickname SEPARATOR ';') AS `Archived nicknames`
    FROM player
      LEFT JOIN nickname_data ON nickname_data.player_id = player.id
      LEFT JOIN active_nickname ON active_nickname.player_id = player.id
      LEFT JOIN archived_nickname ON active_nickname.player_id = player.id
    WHERE player.id = id_in
    GROUP BY player.uuid
    ORDER BY player.last_known_name, player.name_updated DESC;
  END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS `getNicknameDataByUuid`;
DELIMITER $$
CREATE PROCEDURE `getNicknameDataByUuid`(IN uuid_in CHAR(36))
  BEGIN
    DECLARE stored_id INT UNSIGNED DEFAULT NULL;

    SELECT id
    INTO stored_id
    FROM player
    WHERE uuid = uuid_in;

    IF stored_id IS NOT NULL
    THEN
      CALL getNicknameDataByPlayerId(stored_id);
    END IF;
  END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS `getNicknameDataByName`;
DELIMITER $$
CREATE PROCEDURE `getNicknameDataByName`(IN name_in VARCHAR(16))
  BEGIN
    DECLARE stored_id INT UNSIGNED DEFAULT NULL;

    SELECT id
    INTO stored_id
    FROM player
    WHERE last_known_name = name_in;

    IF stored_id IS NOT NULL
    THEN
      CALL getNicknameDataByPlayerId(stored_id);
    END IF;
  END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS `getNicknameDataByNickname`;
DELIMITER $$
CREATE PROCEDURE `getNicknameDataByNickname`(IN nickname_in VARCHAR(255), IN limit_in INT)
  BEGIN
    SELECT
      player.last_known_name                                          AS `Last known name`,
      player.uuid                                                     AS `UUID`,
      IFNULL(nickname_data.last_changed, 0)                           AS `Last changed`,
      IFNULL(nickname_data.tokens, 0)                                 AS `Tokens`,
      IFNULL(nickname_data.accepted_rules, FALSE)                     AS `Accepted rules`,
      active_nickname.nickname                                        AS `Nickname`,
      active_nickname.unformatted_nickname                            AS `Unformatted nickname`,
      group_concat(DISTINCT archived_nickname.nickname SEPARATOR ';') AS `Archived nicknames`
    FROM active_nickname
      LEFT JOIN player ON player.id = active_nickname.player_id
      LEFT JOIN nickname_data ON nickname_data.player_id = active_nickname.player_id
      LEFT JOIN archived_nickname ON archived_nickname.player_id = nickname_data.player_id
    WHERE active_nickname.unformatted_nickname LIKE CONCAT('%', nickname_in, '%')
    GROUP BY player.uuid
    ORDER BY player.last_known_name, player.name_updated DESC
    LIMIT limit_in;
  END $$
DELIMITER ;