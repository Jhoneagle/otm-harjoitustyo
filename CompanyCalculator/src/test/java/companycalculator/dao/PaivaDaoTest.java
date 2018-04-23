package companycalculator.dao;

import companycalculator.database.Database;
import companycalculator.domain.Paiva;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
        assertEquals(testi.getAsiakasId(), save.getAsiakasId());
    }

    @Test
    public void findOne() throws SQLException {
        Paiva testi = new Paiva(0, "2014-02-10", 1);
        dao.save(testi);

        Paiva save = dao.findOne(1);

        assertTrue(testi.getPaiva().contains(save.getPaiva()));
        assertEquals(testi.getAsiakasId(), save.getAsiakasId());
    }

    @Test
    public void findAll() throws SQLException {
        Paiva testi = new Paiva(0, "2014-02-10", 1);
        Paiva testi2 = new Paiva(0, "2014-06-10", 2);
        dao.save(testi);
        dao.save(testi2);

        List<Paiva> save = dao.findAll();

        assertTrue(testi.getPaiva().contains(save.get(0).getPaiva()));
        assertEquals(testi.getAsiakasId(), save.get(0).getAsiakasId());
        assertTrue(testi2.getPaiva().contains(save.get(1).getPaiva()));
        assertEquals(testi2.getAsiakasId(), save.get(1).getAsiakasId());
    }

    @After
    public void restore() {
        tempFile.delete();
    }
}