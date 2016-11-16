CREATE TABLE IF NOT EXISTS player
(
  id              INT UNSIGNED AUTO_INCREMENT,
  uuid            CHAR(36),
  last_known_name VARCHAR(16),
  name_updated    TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
  last_seen       TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE INDEX `UNQ_player_uuid` (uuid)

);

CREATE TABLE IF NOT EXISTS nickname_data
(
  player_id      INT UNSIGNED,
  last_changed   BIGINT  DEFAULT 0,
  tokens         INT     DEFAULT 0,
  accepted_rules BOOLEAN DEFAULT FALSE,
  PRIMARY KEY `pk_nickname_data` (player_id),
  CONSTRAINT `fk_nickname_data_player_id` FOREIGN KEY (player_id) REFERENCES player (id)
    ON DELETE CASCADE
)
  CHARSET = utf8;

CREATE TABLE IF NOT EXISTS active_nickname
(
  player_id            INT UNSIGNED,
  nickname             VARCHAR(255),
  unformatted_nickname VARCHAR(255),
  created              TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY `pk_active_nickname` (player_id),
  CONSTRAINT `fk_active_nickname_player_id` FOREIGN KEY (player_id) REFERENCES player (id)
    ON DELETE CASCADE
)
  CHARSET = utf8;

CREATE TABLE IF NOT EXISTS archived_nickname
(
  id                   INT UNSIGNED AUTO_INCREMENT,
  player_id            INT UNSIGNED,
  nickname             VARCHAR(255),
  unformatted_nickname VARCHAR(255) NOT NULL,
  created              TIMESTAMP,
  archived             TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY `pk_archived_nickname` (id),
  CONSTRAINT `fk_archived_nickname_player_id` FOREIGN KEY (player_id) REFERENCES player (id)
    ON DELETE CASCADE

)
  CHARSET = utf8;

