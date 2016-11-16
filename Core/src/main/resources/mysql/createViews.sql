CREATE OR REPLACE VIEW player_view AS
  SELECT
    player.last_known_name                                          AS `Last known name`,
    player.uuid                                                     AS `UUID`,
    IFNULL(nickname_data.last_changed, 0)                           AS `Last changed`,
    IFNULL(nickname_data.tokens, 0)                                 AS `Tokens`,
    IFNULL(nickname_data.accepted_rules, FALSE)                     AS `Accepted rules`,
    active_nickname.nickname                                        AS `Nickname`,
    active_nickname.unformatted_nickname                            AS `Unformatted nickname`,
    GROUP_CONCAT(DISTINCT archived_nickname.nickname SEPARATOR ';') AS `Archived nicknames`
  FROM player
    LEFT JOIN nickname_data ON nickname_data.player_id = player.id
    LEFT JOIN active_nickname ON active_nickname.player_id = player.id
    LEFT JOIN archived_nickname ON active_nickname.player_id = player.id
  GROUP BY player.uuid
  ORDER BY player.last_known_name ASC, player.name_updated DESC;

CREATE OR REPLACE VIEW nickname_view AS
  SELECT
    player.last_known_name               AS `Last known name`,
    player.uuid                          AS `UUID`,
    active_nickname.nickname             AS `Nickname`,
    active_nickname.unformatted_nickname AS `Unformatted nickname`
  FROM active_nickname
    JOIN player ON player.id = active_nickname.player_id
  GROUP BY player.uuid
  ORDER BY player.last_known_name ASC, player.name_updated DESC;