package Johneagle.companyCalculator.dao;

import Johneagle.companyCalculator.Dao.AsiakasDao;
import Johneagle.companyCalculator.database.Database;
import Johneagle.companyCalculator.domain.Asiakas;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileInputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.*;

public class AsiakasDaoTest {
    private AsiakasDao dao;
    private File tempFile;

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Before
    public void setUp() throws Exception {
        Properties properties = new Properties();

        properties.load(new FileInputStream("config.properties"));
        tempFile = tempFolder.newFile(properties.getProperty("testDatabaseFile"));
        String databaseAddress = "jdbc:sqlite:"+tempFile.getAbsolutePath();

        Database database = new Database(databaseAddress);

        database.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS asiakas(id INTEGER PRIMARY KEY, yritys_nimi varchar(50), ytunnus varchar(50), " +
                "nimi varchar(50), sahkoposti varchar(50), puhelinnumero varchar(50), osoite varchar(50), postinumero varchar(50), postitoimipaikka varchar(50))").execute();

        dao = new AsiakasDao(database);
    }

    @Test
    public void save() throws SQLException {
        Asiakas testi = new Asiakas(0, "ask", "w123", "q", "q", "123", "q", "123", "q");
        Asiakas uusi = dao.save(testi);

        assertTrue(testi.getYritysNimi().contains(uusi.getYritysNimi()));
        assertTrue(testi.getyTunnus().contains(uusi.getyTunnus()));
        assertTrue(testi.getNimi().contains(uusi.getNimi()));
        assertTrue(testi.getSahkoposti().contains(uusi.getSahkoposti()));
        assertTrue(testi.getPuhelinnumero().contains(uusi.getPuhelinnumero()));
        assertTrue(testi.getOsoite().contains(uusi.getOsoite()));
        assertTrue(testi.getPostinumero().contains(uusi.getPostinumero()));
        assertTrue(testi.getPostitoimipaikka().contains(uusi.getPostitoimipaikka()));
    }

    @Test
    public void findOne() throws SQLException {
        Asiakas testi = new Asiakas(0, "ask", "w123", "q", "q", "123", "q", "123", "q");
        dao.save(testi);

        Asiakas uusi = dao.findOne(1);

        assertTrue(testi.getYritysNimi().contains(uusi.getYritysNimi()));
        assertTrue(testi.getyTunnus().contains(uusi.getyTunnus()));
        assertTrue(testi.getNimi().contains(uusi.getNimi()));
        assertTrue(testi.getSahkoposti().contains(uusi.getSahkoposti()));
        assertTrue(testi.getPuhelinnumero().contains(uusi.getPuhelinnumero()));
        assertTrue(testi.getOsoite().contains(uusi.getOsoite()));
        assertTrue(testi.getPostinumero().contains(uusi.getPostinumero()));
        assertTrue(testi.getPostitoimipaikka().contains(uusi.getPostitoimipaikka()));
    }

    @Test
    public void findAll() throws SQLException {
        Asiakas testi = new Asiakas(0, "ask", "w123", "q", "q", "123", "q", "123", "q");
        Asiakas testi2 = new Asiakas(0, "ask", "w123", "q", "q", "123", "q", "123", "q");
        dao.save(testi);
        dao.save(testi2);

        List<Asiakas> uusi = dao.findAll();

        assertTrue(testi.getYritysNimi().contains(uusi.get(0).getYritysNimi()));
        assertTrue(testi.getyTunnus().contains(uusi.get(0).getyTunnus()));
        assertTrue(testi.getNimi().contains(uusi.get(0).getNimi()));
        assertTrue(testi.getSahkoposti().contains(uusi.get(0).getSahkoposti()));
        assertTrue(testi.getPuhelinnumero().contains(uusi.get(0).getPuhelinnumero()));
        assertTrue(testi.getOsoite().contains(uusi.get(0).getOsoite()));
        assertTrue(testi.getPostinumero().contains(uusi.get(0).getPostinumero()));
        assertTrue(testi.getPostitoimipaikka().contains(uusi.get(0).getPostitoimipaikka()));

        assertTrue(testi2.getYritysNimi().contains(uusi.get(1).getYritysNimi()));
        assertTrue(testi2.getyTunnus().contains(uusi.get(1).getyTunnus()));
        assertTrue(testi2.getNimi().contains(uusi.get(1).getNimi()));
        assertTrue(testi2.getSahkoposti().contains(uusi.get(1).getSahkoposti()));
        assertTrue(testi2.getPuhelinnumero().contains(uusi.get(1).getPuhelinnumero()));
        assertTrue(testi2.getOsoite().contains(uusi.get(1).getOsoite()));
        assertTrue(testi2.getPostinumero().contains(uusi.get(1).getPostinumero()));
        assertTrue(testi2.getPostitoimipaikka().contains(uusi.get(1).getPostitoimipaikka()));
    }

    @After
    public void restore() {
        tempFile.delete();
    }
}