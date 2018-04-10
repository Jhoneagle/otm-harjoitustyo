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

        Tekstikayttoliittyma liittyma = new Tekstikayttoliittyma(lukija, tuotedao);
        liittyma.start();
    }
}
