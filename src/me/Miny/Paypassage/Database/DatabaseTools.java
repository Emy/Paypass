
package me.Miny.Paypassage.Database;

import java.sql.SQLException;
import me.Miny.Paypassage.Paypassage;
import me.Miny.Paypassage.logger.LoggerUtility;

/**
 *
 * @author ibhh
 */
public class DatabaseTools {
     private static void SQLErrorHandler(Paypassage plugin, SQLException ex) {
        do {
            try {
                plugin.getLoggerUtility().log("Exception Message: " + ex.getMessage(), LoggerUtility.Level.ERROR);
                plugin.getLoggerUtility().log("DBMS Code: " + ex.getErrorCode(), LoggerUtility.Level.ERROR);
                ex.printStackTrace();
            } catch (Exception ne) {
                plugin.getLoggerUtility().log(ne.getMessage(), LoggerUtility.Level.ERROR);
            }
        } while ((ex = ex.getNextException()) != null);
    }
}
