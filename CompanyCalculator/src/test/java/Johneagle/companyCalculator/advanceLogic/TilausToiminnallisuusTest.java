package Johneagle.companyCalculator.advanceLogic;

import Johneagle.companyCalculator.advancelogic.TilausToiminnallisuus;
import Johneagle.companyCalculator.Dao.AsiakasDao;
import Johneagle.companyCalculator.Dao.PaivaDao;
import Johneagle.companyCalculator.Dao.TilausDao;
import Johneagle.companyCalculator.Dao.TuoteDao;
import Johneagle.companyCalculator.database.Database;
import Johneagle.companyCalculator.domain.Asiakas;
import Johneagle.companyCalculator.domain.Tilaus;
import Johneagle.companyCalculator.domain.Tuote;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileInputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TilausToiminnallisuusTest {
    private Database database;
    private TilausDao tilausdao;
    private PaivaDao paivadao;
    private TuoteDao tuotedao;
    private AsiakasDao asiakasdao;
    private TilausToiminnallisuus tilauspalvelu;
    private File tempFile;

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Before
    public void setUp() throws Exception {
        Properties properties = new Properties();

        properties.load(new FileInputStream("config.properties"));
        tempFile = tempFolder.newFile(properties.getProperty("testDatabaseFile"));
        String databaseAddress = "jdbc:sqlite:"+tempFile.getAbsolutePath();

        this.database = new Database(databaseAddress);
        this.tuotedao = new TuoteDao(database);
        this.paivadao = new PaivaDao(database);
        this.asiakasdao = new AsiakasDao(database);
        this.tilausdao = new TilausDao(database, asiakasdao, tuotedao, paivadao);

        this.tilauspalvelu = new TilausToiminnallisuus(database, asiakasdao, tuotedao, paivadao, tilausdao);

        database.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS tuote(id INTEGER PRIMARY KEY, tuotekoodi varchar(255), nimi varchar(255), " +
                "hinta REAL, alv REAL)").execute();
        database.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS paiva(id INTEGER PRIMARY KEY, paiva varchar(144), asiakas_id INTEGER, " +
                "FOREIGN KEY (asiakas_id) REFERENCES asiakas(id))").execute();
        database.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS tilaus(id INTEGER PRIMARY KEY, status varchar(30), paiva_id INTEGER, " +
                "FOREIGN KEY (paiva_id) REFERENCES paiva(id))").execute();
        database.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS tilaustuote(tuotemaara INTEGER, tilaus_id INTEGER, tuote_id INTEGER, " +
                "FOREIGN KEY (tilaus_id) REFERENCES tilaus(id), FOREIGN KEY (tuote_id) REFERENCES tuote(id))").execute();
        database.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS asiakas(id INTEGER PRIMARY KEY, yritys_nimi varchar(144), ytunnus varchar(144), " +
                "nimi varchar(144), sahkoposti varchar(144), puhelinnumero varchar(144), osoite varchar(144), postinumero varchar(144), postitoimipaikka varchar(144))").execute();

    }

    @Test
    public void TilausLisays() throws SQLException {
        Asiakas a =  new Asiakas(0, "ask", "test", "q", "q", "123", "q", "123", "q");
        Tilaus uusi = new Tilaus(0, "tarjous", new ArrayList<>(), 1, a);

        List<String> tuotekoodit = new ArrayList<>();
        String ytunnus = a.getyTunnus();
        String paiva = "21/7/2018";
        int tuotemaara = 2;

        Tuote testi = new Tuote(0, "testi12345", "testisyote", 1, 1);
        Tuote testi2 = new Tuote(0, "awq", "testisyote", 2, 2);

        testi = tuotedao.save(testi);
        testi2 = tuotedao.save(testi2);

        asiakasdao.save(a);

        tuotekoodit.add(testi.getTuotekoodi());
        tuotekoodit.add(testi2.getTuotekoodi());

        tilauspalvelu.lisaaTilaus(uusi, tuotekoodit, ytunnus, paiva, tuotemaara);

        List<String> tilaukset = tilauspalvelu.listaaTilaukset();
        String malli = "yritys: "+a.getYritysNimi()+", paiva: "+paiva+", status: "+uusi.getStatus();

        assertTrue(malli.contains(tilaukset.get(0)));
    }

    @Test
    public void TilausListaus() throws SQLException {
        Asiakas a =  new Asiakas(0, "ask", "test", "q", "q", "123", "q", "123", "q");
        Tilaus uusi = new Tilaus(0, "tarjous", new ArrayList<>(), 1, a);

        List<String> tuotekoodit = new ArrayList<>();
        String ytunnus = a.getyTunnus();
        String paiva = "21/7/2018";
        int tuotemaara = 2;

        Tuote testi = new Tuote(0, "testi12345", "testisyote", 1, 1);
        Tuote testi2 = new Tuote(0, "awq", "testisyote", 2, 2);

        testi = tuotedao.save(testi);
        testi2 = tuotedao.save(testi2);

        asiakasdao.save(a);

        tuotekoodit.add(testi.getTuotekoodi());
        tuotekoodit.add(testi2.getTuotekoodi());

        tilauspalvelu.lisaaTilaus(uusi, tuotekoodit, ytunnus, paiva, tuotemaara);

        //tilaus 2

        Asiakas a2 =  new Asiakas(0, "test", "qwd", "q", "q", "123", "q", "123", "q");
        Tilaus uusi2 = new Tilaus(0, "tilaus", new ArrayList<>(), 1, a);

        List<String> tuotekoodit2 = new ArrayList<>();
        String ytunnus2 = a2.getyTunnus();
        String paiva2 = "21/7/2018";
        int tuotemaara2 = 2;

        Tuote testi1 = new Tuote(0, "www", "testisyote", 1, 1);
        Tuote testi12 = new Tuote(0, "aaa", "testisyote", 2, 2);

        testi1 = tuotedao.save(testi1);
        testi12 = tuotedao.save(testi12);

        asiakasdao.save(a2);

        tuotekoodit.add(testi1.getTuotekoodi());
        tuotekoodit.add(testi12.getTuotekoodi());

        tilauspalvelu.lisaaTilaus(uusi2, tuotekoodit2, ytunnus2, paiva2, tuotemaara2);

        //tarkastus

        List<String> tilaukset = tilauspalvelu.listaaTilaukset();
        String malli = "yritys: "+a.getYritysNimi()+", paiva: "+paiva+", status: "+uusi.getStatus();
        String malli2 = "yritys: "+a2.getYritysNimi()+", paiva: "+paiva2+", status: "+uusi2.getStatus();

        assertTrue(malli.contains(tilaukset.get(0)));
        assertTrue(malli2.contains(tilaukset.get(1)));
    }

    @After
    public void restore() {
        tempFile.delete();
    }
}