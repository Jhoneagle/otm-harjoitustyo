package dao;

import database.Database;
import domain.Paiva;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileInputStream;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.*;

public class PaivaDaoTest {
    private PaivaDao dao;
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

        database.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS paiva(id INTEGER PRIMARY KEY, paiva DATE, asiakas_id INTEGER, " +
                "FOREIGN KEY (asiakas_id) REFERENCES asiakas(id))").execute();

        dao = new PaivaDao(database);
    }

    @Test
    public void save() throws SQLException {
        Paiva testi = new Paiva(0, "2014-02-10", 1);
        Paiva save = dao.save(testi);

        assertEquals(testi.getPaiva(), save.getPaiva());
        assertEquals(testi.getAsiakas_id(), save.getAsiakas_id());
    }

    @Test
    public void findOne() throws SQLException {
        Paiva testi = new Paiva(0, "2014-02-10", 1);
        dao.save(testi);

        Paiva save = dao.findOne(1);

        assertTrue(testi.getPaiva().contains(save.getPaiva()));
        assertEquals(testi.getAsiakas_id(), save.getAsiakas_id());
    }

    @Test
    public void findAll() throws SQLException {
        Paiva testi = new Paiva(0, "2014-02-10", 1);
        Paiva testi2 = new Paiva(0, "2014-06-10", 2);
        dao.save(testi);
        dao.save(testi2);

        List<Paiva> save = dao.findAll();

        assertTrue(testi.getPaiva().contains(save.get(0).getPaiva()));
        assertEquals(testi.getAsiakas_id(), save.get(0).getAsiakas_id());
        assertTrue(testi2.getPaiva().contains(save.get(1).getPaiva()));
        assertEquals(testi2.getAsiakas_id(), save.get(1).getAsiakas_id());
    }

    @After
    public void restore() {
        tempFile.delete();
    }
}