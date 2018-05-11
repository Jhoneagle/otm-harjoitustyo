/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import companycalculator.domain.Tilaus;
import companycalculator.domain.Tuote;
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

/**
 *
 * @author Joni
 */
public class SillyUserTest {
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
        Asiakas a =  new Asiakas(0, "ask", "test", "q", "q", "123", "q", "123", "q");
        Tilaus uusi = new Tilaus(0, "tarjous", new ArrayList<>(), 1, a);

        List<String> tuotekoodit = new ArrayList<>();
        String ytunnus = a.getyTunnus();
        String paiva = "21/7/2018";
        List<Integer> tuotemaara = new ArrayList<>();
        tuotemaara.add(2);
        tuotemaara.add(2);
        
        Tuote testi = new Tuote(0, "testi12345", "testisyote", 1, 1);
        Tuote testi2 = new Tuote(0, "awq", "testisyote", 2, 2);

        try {
            testi = tuotedao.save(testi);
            testi2 = tuotedao.save(testi2);
            
            assertTrue(true);
        } catch (Exception e) {
            assertTrue(false);
        }

        try {
            asiakasdao.save(a);
            assertTrue(true);
        } catch (Exception e) {
            assertTrue(false);
        }
        
        tuotekoodit.add(testi.getTuotekoodi());
        tuotekoodit.add(testi2.getTuotekoodi());

        try {
            tilauspalvelu.addTilaus(uusi, tuotekoodit, ytunnus, paiva, tuotemaara);
            assertTrue(true);
        } catch (Exception e) {
            assertTrue(false);
        }

        try {
            this.tuotedao.delete(1);
            this.tuotedao.delete(2);
            
            assertTrue(true);
        } catch (Exception e) {
            assertTrue(false);
        }
        
        try {
            tilauspalvelu.listTilaukset();
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
