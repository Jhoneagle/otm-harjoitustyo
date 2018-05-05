package companycalculator.dao;

import companycalculator.database.Database;
import companycalculator.domain.Tuote;
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

import static org.junit.Assert.assertTrue;

public class TuoteDaoTest {

    private TuoteDao dao;
    private File tempFile;

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Before
    public void setUp() throws Exception {
        Properties properties = new Properties();
        String databaseAddress = "";
        
        try {
            properties.load(new FileInputStream("config.properties"));
            tempFile = tempFolder.newFile(properties.getProperty("testDatabaseFile"));
            databaseAddress = "jdbc:sqlite:"+tempFile.getAbsolutePath();
        } catch(Exception e) {
            tempFile = tempFolder.newFile("test.db");
            databaseAddress = "jdbc:sqlite:"+tempFile.getAbsolutePath();
        }
        
        Database database = new Database(databaseAddress);

        database.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS tuote(id INTEGER PRIMARY KEY, tuotekoodi varchar(255), nimi varchar(255), " +
                "hinta REAL, alv REAL)").execute();

        dao = new TuoteDao(database);
    }

    @Test
    public void save() throws SQLException {
        Tuote testi = new Tuote(0, "testi12345", "testisyote", 1, 1);
        Tuote mika = dao.save(testi);
        assertTrue(mika.getTuotekoodi().equals(testi.getTuotekoodi()));
        assertTrue(mika.getNimi().equals(testi.getNimi()));
        assertTrue(mika.getHinta() == testi.getHinta());
        assertTrue(mika.getAlv() == testi.getAlv());
    }

    @Test
    public void findOne() throws SQLException {
        Tuote testi = new Tuote(0, "testi12345", "testisyote", 1, 1);
        dao.save(testi);
        Tuote mika = dao.findOne(1);
        assertTrue(mika.getTuotekoodi().equals("testi12345"));
        assertTrue(mika.getNimi().equals("testisyote"));
        assertTrue(mika.getHinta() == 1);
        assertTrue(mika.getAlv() == 1);
    }

    @Test
    public void update() throws SQLException {
        Tuote testi = new Tuote(1, "muutos", "muutos", 1, 1);
        Tuote mika = dao.update(testi);
        assertTrue(mika.getTuotekoodi().equals(testi.getTuotekoodi()));
        assertTrue(mika.getNimi().equals(testi.getNimi()));
        assertTrue(mika.getHinta() == testi.getHinta());
        assertTrue(mika.getAlv() == testi.getAlv());
    }

    @Test
    public void delete() throws SQLException {
        dao.delete(1);
        List<Tuote> lista = dao.findAll();
        for (int i = 0; i < lista.size(); i++) {
            Tuote temp = lista.get(i);
            assertTrue(temp.getTuotekoodi().equals("muutos"));
            assertTrue(temp.getNimi().equals("muutos"));
            assertTrue(temp.getHinta() == 1);
            assertTrue(temp.getAlv() == 1);
        }
    }

    @After
    public void restore() {
        tempFile.delete();
    }
}