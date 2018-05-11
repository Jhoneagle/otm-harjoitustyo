package crashtries;

import companycalculator.advanceLogic.TilausToiminnallisuusTest;
import companycalculator.advancelogic.Tilaustoiminnallisuus;
import companycalculator.dao.AsiakasDao;
import companycalculator.dao.PaivaDao;
import companycalculator.dao.TilausDao;
import companycalculator.dao.TuoteDao;
import companycalculator.database.Database;
import companycalculator.domain.Asiakas;
import companycalculator.domain.Paiva;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class WrongTyposTest {
    private Database database;
    private File tempFile;
    private TilausDao tilausdao;
    private PaivaDao paivadao;
    private TuoteDao tuotedao;
    private AsiakasDao asiakasdao;
    private Tilaustoiminnallisuus tilauspalvelu;
    
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
                Logger.getLogger(TilausToiminnallisuusTest.class.getName()).log(Level.SEVERE, null, ex);
            }
            databaseAddress = "jdbc:sqlite:"+tempFile.getAbsolutePath();
        }
        
        this.database = new Database(databaseAddress);
        
        this.tuotedao = new TuoteDao(database);
        this.paivadao = new PaivaDao(database);
        this.asiakasdao = new AsiakasDao(database);
        this.tilausdao = new TilausDao(database, asiakasdao, tuotedao, paivadao);

        this.tilauspalvelu = new Tilaustoiminnallisuus(database, asiakasdao, tuotedao, paivadao, tilausdao);
    }
    
    /**
     * simulations test four situation but with miss typed tilaus id on edit
     * 
     * @see     usersimulation.FreshUserTest#testFour() 
     */
    @Test
    public void testOne() {
        Asiakas one = new Asiakas(0, "Google inc", "aarni123", "DROP TABLE tuote;", "hack@edu.fi", "DROP TABLE tilaus;", "mannerhiemintie 7", "troll", "troll");
        
        try {
            this.asiakasdao.save(one);
            assertTrue(true);
        } catch (Exception e) {
            assertTrue(false);
        }
        
        List<String> numbers = new ArrayList<>();
        List<Integer> amounts = new ArrayList<>();
        
        Tilaustoiminnallisuus.prepareAdd("aarni123", "tarjous", "23/7/2018");
        
        try {
            this.tilauspalvelu.executeAdd(numbers, amounts);
            this.tilauspalvelu.executeAdd(numbers, amounts);
            assertTrue(true);
        } catch (Exception e) {
            assertTrue(false);
        }
        
        String get = this.tilauspalvelu.listTilaukset().get(1);
        
        try {
            // meant id=1 :3 ups...
            this.tilauspalvelu.editTilausta(11, "tilaus", "23/7/2018");
            
            assertTrue(!this.tilauspalvelu.listTilaukset().get(1).contains(get));
            assertTrue(false);
        } catch (Exception e) {
            // problem :/
            assertTrue(true);
        }
    }
    
    /**
     * paivadao findone tests situation but with miss typed id
     * 
     * @see     companycalculator.dao.PaivaDaoTest#findOne() 
     */
    @Test
    public void testTwo() {
        Paiva testi = new Paiva(0, "2014-02-10", 1);
        this.paivadao.save(testi);

        try {
            Paiva save = this.paivadao.findOne(3);
            assertTrue(true);
        } catch (Exception e) {
            assertTrue(false);
        }
    }
    
    @After
    public void restore() {
        tempFile.delete();
    }
}
