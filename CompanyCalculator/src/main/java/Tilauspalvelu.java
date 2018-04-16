import dao.TuoteDao;
import database.Database;
import kayttoliittyma.Tekstikayttoliittyma;

import java.io.FileInputStream;
import java.util.Properties;
import java.util.Scanner;

public class Tilauspalvelu {
    public static void main(String[] args) throws Exception {
        Properties properties = new Properties();

        properties.load(new FileInputStream("config.properties"));
        String databaseAddress = properties.getProperty("mainDatabase");

        Database database = new Database(databaseAddress);
        TuoteDao tuotedao = new TuoteDao(database);
        Scanner lukija = new Scanner(System.in);

        database.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS tuote(id INTEGER PRIMARY KEY, tuotekoodi varchar(255), nimi varchar(255), " +
                "hinta REAL, alv REAL)").execute();
        database.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS paiva(id INTEGER PRIMARY KEY, paiva DATE, asiakas_id INTEGER, " +
                "FOREIGN KEY (asiakas_id) REFERENCES asiakas(id))").execute();
        database.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS tilaus(id INTEGER PRIMARY KEY, status varchar(30), paiva_id INTEGER, " +
                "FOREIGN KEY (paiva_id) REFERENCES paiva(id))").execute();
        database.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS tilaustuote(tuotemaara INTEGER, tilaus_id INTEGER, tuote_id INTEGER, " +
                "FOREIGN KEY (tilaus_id) REFERENCES tilaus(id)), FOREIGN KEY (tuote_id) REFERENCES tuote(id)").execute();
        database.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS asiakas(id INTEGER PRIMARY KEY, yritys_nimi varchar(144), y-tunnus varchar(144), " +
                "nimi varchar(144), sahkoposti varchar(144), puhelinnumero varchar(144), osoite varchar(144), postinumero varchar(144), postitoimipaikka varchar(144))").execute();

        Tekstikayttoliittyma liittyma = new Tekstikayttoliittyma(lukija, tuotedao);
        liittyma.start();
    }
}
