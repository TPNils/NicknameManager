--
-- Creating functions
--
DROP FUNCTION IF EXISTS `getPlayerIdFromName`;
DELIMITER $$
CREATE FUNCTION `getPlayerIdFromName`(name_in VARCHAR(16))
  RETURNS INT(10) UNSIGNED
  BEGIN
    DECLARE stored_id INT UNSIGNED DEFAULT NULL;

    SELECT id
    INTO stored_id
    FROM player
    WHERE last_known_name = name_in;

    RETURN stored_id;
  END $$
DELIMITER ;

DROP FUNCTION IF EXISTS `getPlayerIdFromUuid`;
DELIMITER $$
CREATE FUNCTION `getPlayerIdFromUuid`(uuid_in CHAR(36))
  RETURNS INT(10) UNSIGNED
  BEGIN
    DECLARE stored_id INT UNSIGNED DEFAULT NULL;

    SELECT id
    INTO stored_id
    FROM player
    WHERE uuid = uuid_in;

    RETURN stored_id;
  END $$
DELIMITER ;

DROP function IF EXISTS getUnformattedString;
DELIMITER $$
CREATE FUNCTION getUnformattedString(string_in VARCHAR(255)) RETURNS VARCHAR(255)
  BEGIN
    IF string_in IS NULL
    THEN
      RETURN NULL;
    END IF;

    SELECT REPLACE(string_in, '&0', '') INTO string_in;
    SELECT REPLACE(string_in, '&1', '') INTO string_in;
    SELECT REPLACE(string_in, '&2', '') INTO string_in;
    SELECT REPLACE(string_in, '&3', '') INTO string_in;
    SELECT REPLACE(string_in, '&4', '') INTO string_in;
    SELECT REPLACE(string_in, '&5', '') INTO string_in;
    SELECT REPLACE(string_in, '&6', '') INTO string_in;
    SELECT REPLACE(string_in, '&7', '') INTO string_in;
    SELECT REPLACE(string_in, '&8', '') INTO string_in;
    SELECT REPLACE(string_in, '&9', '') INTO string_in;
    SELECT REPLACE(string_in, '&a', '') INTO string_in;
    SELECT REPLACE(string_in, '&b', '') INTO string_in;
    SELECT REPLACE(string_in, '&c', '') INTO string_in;
    SELECT REPLACE(string_in, '&d', '') INTO string_in;
    SELECT REPLACE(string_in, '&e', '') INTO string_in;
    SELECT REPLACE(string_in, '&f', '') INTO string_in;
    SELECT REPLACE(string_in, '&k', '') INTO string_in;
    SELECT REPLACE(string_in, '&l', '') INTO string_in;
    SELECT REPLACE(string_in, '&m', '') INTO string_in;
    SELECT REPLACE(string_in, '&n', '') INTO string_in;
    SELECT REPLACE(string_in, '&o', '') INTO string_in;
    SELECT REPLACE(string_in, '&r', '') INTO string_in;

    SELECT REPLACE(string_in, '&A', '') INTO string_in;
    SELECT REPLACE(string_in, '&B', '') INTO string_in;
    SELECT REPLACE(string_in, '&C', '') INTO string_in;
    SELECT REPLACE(string_in, '&D', '') INTO string_in;
    SELECT REPLACE(string_in, '&E', '') INTO string_in;
    SELECT REPLACE(string_in, '&F', '') INTO string_in;
    SELECT REPLACE(string_in, '&K', '') INTO string_in;
    SELECT REPLACE(string_in, '&L', '') INTO string_in;
    SELECT REPLACE(string_in, '&M', '') INTO string_in;
    SELECT REPLACE(string_in, '&N', '') INTO string_in;
    SELECT REPLACE(string_in, '&O', '') INTO string_in;
    SELECT REPLACE(string_in, '&R', '') INTO string_in;

    RETURN string_in;
  END $$
DELIMITER ;