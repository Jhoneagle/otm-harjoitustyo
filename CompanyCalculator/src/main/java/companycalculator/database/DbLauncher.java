package companycalculator.database;

import companycalculator.advancelogic.TilausToiminnallisuus;
import companycalculator.dao.AsiakasDao;
import companycalculator.dao.PaivaDao;
import companycalculator.dao.TilausDao;
import companycalculator.dao.TuoteDao;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DbLauncher {
    private Database database;
    private TuoteDao tuotedao;
    private PaivaDao paivadao;
    private AsiakasDao asiakasdao;
    private TilausDao tilausdao;
    private TilausToiminnallisuus tilauspalvelu;

    public DbLauncher(String address) {
        this.database = new Database(address);
    }

    public void intializeAllDao() {
        this.tuotedao = new TuoteDao(this.database);
        this.paivadao = new PaivaDao(this.database);
        this.asiakasdao = new AsiakasDao(this.database);
        this.tilausdao = new TilausDao(this.database, this.asiakasdao, this.tuotedao, this.paivadao);
        this.tilauspalvelu = new TilausToiminnallisuus(this.database, this.asiakasdao, this.tuotedao, this.paivadao, this.tilausdao);
    }

    public void initializeSqlTables() {
        try {
            this.database.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS tuote(id INTEGER PRIMARY KEY, tuotekoodi varchar(255), nimi varchar(255), " +
                    "hinta REAL, alv REAL)").execute();
            this.database.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS paiva(id INTEGER PRIMARY KEY, paiva varchar(144), asiakas_id INTEGER, " +
                    "FOREIGN KEY (asiakas_id) REFERENCES asiakas(id))").execute();
            this.database.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS tilaus(id INTEGER PRIMARY KEY, status varchar(30), paiva_id INTEGER, " +
                    "FOREIGN KEY (paiva_id) REFERENCES paiva(id))").execute();
            this.database.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS tilaustuote(tuotemaara INTEGER, tilaus_id INTEGER, tuote_id INTEGER, " +
                    "FOREIGN KEY (tilaus_id) REFERENCES tilaus(id), FOREIGN KEY (tuote_id) REFERENCES tuote(id))").execute();
            this.database.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS asiakas(id INTEGER PRIMARY KEY, yritys_nimi varchar(144), ytunnus varchar(144), " +
                    "nimi varchar(144), sahkoposti varchar(144), puhelinnumero varchar(144), osoite varchar(144), postinumero varchar(144), " +
                    "postitoimipaikka varchar(144))").execute();
        } catch (SQLException ex) {
            Logger.getLogger(DbLauncher.class.getName()).log(Level.SEVERE, "tables not initialized.", ex);
        }
    }

    public TuoteDao getTuotedao() {
        return this.tuotedao;
    }

    public PaivaDao getPaivadao() {
        return this.paivadao;
    }

    public AsiakasDao getAsiakasdao() {
        return this.asiakasdao;
    }

    public TilausDao getTilausdao() {
        return this.tilausdao;
    }

    public TilausToiminnallisuus getTilauspalvelu() {
        return this.tilauspalvelu;
    }
}
