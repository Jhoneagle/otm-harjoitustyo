package companycalculator.database;

import companycalculator.advancelogic.Tilaustoiminnallisuus;
import companycalculator.dao.AsiakasDao;
import companycalculator.dao.PaivaDao;
import companycalculator.dao.TilausDao;
import companycalculator.dao.TuoteDao;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * Javafx k�ytt�liitym�n tueksi tietokannan k�ytt��n otossa.
 */
public class JavafxConnectDB {
    
    private static Database database;
    private static Tilaustoiminnallisuus tt;
    
    private JavafxConnectDB() {
        
    }
    
    /**
     * antaa yhteyden tietokantaan.
     * 
     * @return  Databse olio.
     */
    public static Database getDB() {
        if (database == null) {
            Properties properties = new Properties();
            String databaseAddress;

            try {
                properties.load(new FileInputStream("config.properties"));
                databaseAddress = "jdbc:sqlite:" + properties.getProperty("mainDatabase");
            } catch(Exception e) {
                databaseAddress = "jdbc:sqlite:main.db";
            }
            
            database = new Database(databaseAddress);
        }
        
        return database;
    }
    
    public static TilausDao getTD() {
        Database db = getDB();
        AsiakasDao a = new AsiakasDao(db);
        TuoteDao te = new TuoteDao(db);
        PaivaDao p  = new PaivaDao(db);
        return new TilausDao(db, a, te, p);
    }
    
    public static Tilaustoiminnallisuus getTT() {
        if (tt != null) {
            return tt;
        } else {
            Database db = getDB();
            AsiakasDao a = new AsiakasDao(db);
            TuoteDao te = new TuoteDao(db);
            PaivaDao p  = new PaivaDao(db);
            TilausDao ti = new TilausDao(db, a, te, p);
            return new Tilaustoiminnallisuus(db, a, te, p, ti);
        }
    }
}
