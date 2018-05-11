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