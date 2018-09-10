package be.spyproof.nickmanager.da.player;

import java.sql.SQLException;

interface AsyncMysqlExecution extends Runnable {

  @Override
  default void run() {
    try {
      execute();
    } catch (SQLException e1) {
      try {
        execute();
      } catch (SQLException e2) {
        e2.printStackTrace();
      }
    }
  }

  void execute() throws SQLException;
}
