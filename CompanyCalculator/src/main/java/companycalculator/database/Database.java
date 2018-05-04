package companycalculator.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Luokka tietokantayhteydelle. tällä hetkellä sqlite käyttöä varten.
 */
public class Database {

    private String databaseAddress;

    public Database(String databaseAddress) {
        this.databaseAddress = databaseAddress;
    }

    /**
     * Luo yhteyden tietokantaa.
     *
     * @return Connection olion kunhan saa yhteyden tietokantaan.
     */
    public Connection getConnection() {
        try {
            return DriverManager.getConnection(this.databaseAddress);
        } catch (SQLException ex) {
            Logger.getLogger(DbLauncher.class.getName()).log(Level.SEVERE, "unable to connect database.", ex);
            return null;
        }
    }
}
