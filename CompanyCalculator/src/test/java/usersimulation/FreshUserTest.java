package usersimulation;

import companycalculator.advanceLogic.TilausToiminnallisuusTest;
import companycalculator.advancelogic.Tilaustoiminnallisuus;
import companycalculator.dao.AsiakasDao;
import companycalculator.dao.PaivaDao;
import companycalculator.dao.TilausDao;
import companycalculator.dao.TuoteDao;
import companycalculator.database.Database;
import companycalculator.domain.Asiakas;
import companycalculator.domain.Paiva;
import companycalculator.domain.Tilaus;
import companycalculator.domain.Tuote;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.ResultSet;
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

public class FreshUserTest {
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
    
    @Test
    public void testOne() {
        Tuote frist = new Tuote(-1, "utc123", "kahvakuula", 5.50, 2);
        Tuote second = new Tuote(-2, "kep12kka", "keppi", 1, 0.33);
        Tuote third = new Tuote(-3, "utc123", "jumppapallo", 20.15, 7.75);
        Tuote forth = new Tuote(-4, "mailvoima", "pesismaila", 6, 0);
        
        Tuote success1 = this.tuotedao.save(frist);
        Tuote success2 = this.tuotedao.save(second);
        Tuote fail = this.tuotedao.save(third);
        Tuote success3 = this.tuotedao.save(forth);
        
        assertTrue(success1.getId() != frist.getId());
        assertTrue(success2.getId() != second.getId());
        assertTrue(fail == null);
        assertTrue(success3.getId() != forth.getId());
        
        assertTrue(this.tuotedao.findAll().size() == 3);
    }
    
    @Test
    public void testTwo() {
        // no sql querys here :3
        Asiakas one = new Asiakas(0, "Google inc", "aarni123", "DROP TABLE tuote;", "hack@edu.fi", "DROP TABLE tilaus;", "mannerhiemintie 7", "troll", "troll");
        
        //legal one
        Asiakas two = new Asiakas(1, "a", "a", "a", "a", "a", "a", "a", "a");
        
        try {
            this.asiakasdao.save(one);
            this.asiakasdao.save(two);
            
            assertTrue(true);
        } catch (Exception e) {
            assertTrue(false);
        }
        
        assertTrue(this.asiakasdao.findAll().size() == 2);
        
        try {
            this.database.getConnection().prepareStatement("SELECT * FROM tuote").executeQuery();
            assertTrue(true);
        } catch (Exception e) {
            assertTrue(false);
        }
    }
    
    @Test
    public void testThree() {
        Asiakas one = new Asiakas(0, "Google inc", "aarni123", "DROP TABLE tuote;", "hack@edu.fi", "DROP TABLE tilaus;", "mannerhiemintie 7", "troll", "troll");
        
        try {
            this.asiakasdao.save(one);
            assertTrue(true);
        } catch (Exception e) {
            assertTrue(false);
        }
        
        Tuote third = new Tuote(-3, "utc123", "jumppapallo", 20.15, 7.75);
        Tuote forth = new Tuote(-4, "mailvoima", "pesismaila", 6, 0);
        
        try {
            this.tuotedao.save(third);
            this.tuotedao.save(forth);
            
            assertTrue(true);
        } catch (Exception e) {
            assertTrue(false);
        }
        
        List<String> numbers = new ArrayList<>();
        List<Integer> amounts = new ArrayList<>();
        
        numbers.add("utc123");
        numbers.add("mailvoima");
        
        amounts.add(5);
        amounts.add(3 / 2);
        
        Tilaustoiminnallisuus.prepareAdd("aarni123", "tarjous", "23/7/2018");
        
        try {
            this.tilauspalvelu.executeAdd(numbers, amounts);
            assertTrue(true);
        } catch (Exception e) {
            assertTrue(false);
        }
    }
    
    @Test
    public void testFour() {
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
        
        try {
            this.tilauspalvelu.editTilausta(2, "tilaus", "23/7/2018");
            assertTrue(true);
        } catch (Exception e) {
            assertTrue(false);
        }
    }
    
    @Test
    public void testFive() {
        Asiakas two = new Asiakas(1, "a", "a", "a", "a", "a", "a", "a", "a");
        
        try {
            this.asiakasdao.save(two);
            assertTrue(true);
        } catch (Exception e) {
            assertTrue(false);
        }
        
        Tuote second = new Tuote(-2, "kep12kka", "keppi", 1, 0.33);
        
        try {
            this.tuotedao.save(second);
            assertTrue(true);
        } catch (Exception e) {
            assertTrue(false);
        }
        
        List<String> numbers = new ArrayList<>();
        List<Integer> amounts = new ArrayList<>();
        
        numbers.add("kep12kka");
        
        amounts.add(5);
        
        Tilaustoiminnallisuus.prepareAdd("a", "tilaus", "23/7/2018");
        
        try {
            this.tilauspalvelu.executeAdd(numbers, amounts);
            assertTrue(true);
        } catch (Exception e) {
            assertTrue(false);
        }
        
        assertTrue(this.tilauspalvelu.listTilaukset().size() == 1);
    }
    
    @After
    public void restore() {
        tempFile.delete();
    }
}
