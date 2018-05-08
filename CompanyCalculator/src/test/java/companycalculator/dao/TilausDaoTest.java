package companycalculator.dao;

import companycalculator.database.Database;
import companycalculator.domain.Asiakas;
import companycalculator.domain.Paiva;
import companycalculator.domain.Tilaus;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TilausDaoTest {
    private TilausDao dao;
    private File tempFile;

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Before
    public void setUp() {
        Properties properties = new Properties();
        String databaseAddress = "";
        
        try {
            properties.load(new FileInputStream("config.properties"));
            tempFile = tempFolder.newFile(properties.getProperty("testDatabaseFile"));
            databaseAddress = "jdbc:sqlite:"+tempFile.getAbsolutePath();
        } catch(Exception e) {
            try {
                tempFile = tempFolder.newFile("test.db");
            } catch (IOException ex) {
                Logger.getLogger(TilausDaoTest.class.getName()).log(Level.SEVERE, null, ex);
            }
            databaseAddress = "jdbc:sqlite:"+tempFile.getAbsolutePath();
        }
        
        Database database = new Database(databaseAddress);

        try {
            database.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS tuote(id INTEGER PRIMARY KEY, tuotekoodi varchar(255), nimi varchar(255), " +
                    "hinta REAL, alv REAL)").execute();
            database.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS paiva(id INTEGER PRIMARY KEY, paiva varchar(144), asiakas_id INTEGER, " +
                "FOREIGN KEY (asiakas_id) REFERENCES asiakas(id))").execute();
            database.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS tilaus(id INTEGER PRIMARY KEY, status varchar(30), paiva_id INTEGER, " +
                    "FOREIGN KEY (paiva_id) REFERENCES paiva(id))").execute();
            database.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS asiakas(id INTEGER PRIMARY KEY, yritys_nimi varchar(144), ytunnus varchar(144), " +
                    "nimi varchar(144), sahkoposti varchar(144), puhelinnumero varchar(144), osoite varchar(144), postinumero varchar(144), postitoimipaikka varchar(144))").execute();
            database.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS tilaustuote(tuotemaara INTEGER, tilaus_id INTEGER, tuote_id INTEGER, " +
                    "FOREIGN KEY (tilaus_id) REFERENCES tilaus(id), FOREIGN KEY (tuote_id) REFERENCES tuote(id))").execute();
        } catch (SQLException ex) {
            Logger.getLogger(TilausDaoTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        AsiakasDao a = new AsiakasDao(database);
        TuoteDao t = new TuoteDao(database);
        PaivaDao p = new PaivaDao(database);

        a.save(new Asiakas(0, "ask", "w123", "q", "q", "123", "q", "123", "q"));
        p.save(new Paiva(0, "2014-02-10", 1));

        dao = new TilausDao(database, a, t, p);
    }

    @Test
    public void save() {
        Asiakas testiAsiakas = new Asiakas(1, "ask", "w123", "q", "q", "123", "q", "123", "q");
        Tilaus testi = new Tilaus(0, "tarjous", new ArrayList<>(), 1, testiAsiakas);

        Tilaus lisatty = dao.save(testi);

        assertEquals(testi.getPaivaId(), lisatty.getPaivaId());
        assertTrue(testi.getStatus().contains(lisatty.getStatus()));
    }

    @Test
    public void findOne() {
        Asiakas testiAsiakas = new Asiakas(0, "ask", "w123", "q", "q", "123", "q", "123", "q");
        Tilaus testi = new Tilaus(0, "tarjous", new ArrayList<>(), 1, testiAsiakas);

        dao.save(testi);
        Tilaus lisatty = dao.findOne(1);

        assertEquals(testi.getPaivaId(), lisatty.getPaivaId());
        assertTrue(testi.getStatus().contains(lisatty.getStatus()));
    }

    @Test
    public void findAll() {
        Asiakas testiAsiakas = new Asiakas(0, "ask", "w123", "q", "q", "123", "q", "123", "q");
        Tilaus testi = new Tilaus(0, "tarjous", new ArrayList<>(), 1, testiAsiakas);
        Asiakas testiAsiakas2 = new Asiakas(0, "ask", "w123", "q", "q", "123", "q", "123", "q");
        Tilaus testi2 = new Tilaus(0, "pooilla", new ArrayList<>(), 1, testiAsiakas2);

        dao.save(testi);
        dao.save(testi2);
        List<Tilaus> lisatyt = dao.findAll();

        assertEquals(testi.getPaivaId(), lisatyt.get(0).getPaivaId());
        assertTrue(testi.getStatus().contains(lisatyt.get(0).getStatus()));
        assertEquals(testi2.getPaivaId(), lisatyt.get(1).getPaivaId());
        assertTrue(testi2.getStatus().contains(lisatyt.get(1).getStatus()));
    }

    @After
    public void restore() {
        tempFile.delete();
    }
}