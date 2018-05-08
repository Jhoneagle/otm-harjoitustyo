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

    private final String databaseAddress;

    public Database(String databaseAddress) {
        this.databaseAddress = databaseAddress;
        initializeSqlTables();
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
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, "unable to connect database.", ex);
            return null;
        }
    }
    
    private void initializeSqlTables() {
        try {
            getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS tuote(id INTEGER PRIMARY KEY, tuotekoodi varchar(255), nimi varchar(255), " +
                    "hinta REAL, alv REAL)").execute();
            getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS paiva(id INTEGER PRIMARY KEY, paiva varchar(144), asiakas_id INTEGER, " +
                    "FOREIGN KEY (asiakas_id) REFERENCES asiakas(id))").execute();
            getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS tilaus(id INTEGER PRIMARY KEY, status varchar(30), paiva_id INTEGER, " +
                    "FOREIGN KEY (paiva_id) REFERENCES paiva(id))").execute();
            getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS tilaustuote(tuotemaara INTEGER, tilaus_id INTEGER, tuote_id INTEGER, " +
                    "FOREIGN KEY (tilaus_id) REFERENCES tilaus(id), FOREIGN KEY (tuote_id) REFERENCES tuote(id))").execute();
            getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS asiakas(id INTEGER PRIMARY KEY, yritys_nimi varchar(144), ytunnus varchar(144), " +
                    "nimi varchar(144), sahkoposti varchar(144), puhelinnumero varchar(144), osoite varchar(144), postinumero varchar(144), " +
                    "postitoimipaikka varchar(144))").execute();
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, "tables not initialized.", ex);
        }
    }
}
