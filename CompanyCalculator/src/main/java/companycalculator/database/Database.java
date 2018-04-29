package companycalculator.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Luokka tietokantayhteydelle. tällä hetkellä sqlite käyttöä varten.
 */
public class Database {

    private String databaseAddress;

    public Database(String databaseAddress) throws ClassNotFoundException {
        this.databaseAddress = databaseAddress;
    }

    /**
     * Luo yhteyden tietokantaa.
     *
     * @return Connection olion kunhan saa yhteyden tietokantaan.
     */
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(databaseAddress);
    }
}
