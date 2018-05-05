package companycalculator.dao;

import companycalculator.database.Database;
import companycalculator.domain.Asiakas;
import companycalculator.domain.Paiva;
import companycalculator.domain.Tilaus;
import companycalculator.domain.Tuote;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Luokka tarjoaa tietokannan tilaus-taulun kannalta olellisia toimintoja.
 * Tällä hetkellä tuettuja ovat Lisäys, listaus, id:llä haku, poisto ja muokkaus.
 */
public class TilausDao implements Dao<Tilaus, Integer> {
    private Database database;
    private AsiakasDao asiakasDao;
    private TuoteDao tuoteDao;
    private PaivaDao paivaDao;

    public TilausDao(Database database, AsiakasDao asiakasDao, TuoteDao tuoteDao, PaivaDao paivaDao) {
        this.database = database;
        this.asiakasDao = asiakasDao;
        this.tuoteDao = tuoteDao;
        this.paivaDao = paivaDao;
    }

    /**
     * Metodi hakee parametriä vastaavan Tilaus olion, joka haetaan Tilaus oloiden listasta.
     *
     * @param   key   Käyttäjän tai ohjelman antama tilaus_id.
     *
     * @see     TilausDao#findAll()
     *
     * @return Löydetty Tilaus olio tai null.
     */
    @Override
    public Tilaus findOne(Integer key) {
        List<Tilaus> kaikki = findAll();

        for (int i = 0; i < kaikki.size(); i++) {
            Tilaus temp = kaikki.get(i);

            if (temp.getId() == key) {
                return temp;
            }
        }

        return null;
    }

    /**
     * Metodi hakee kaikki tietueet tilaus-taulusta ja luo niistä Tilaus olioita.
     * Joihin lisätään samalla tilaustuote-liitostaulun ja tuote-taulun avulla kaikki niihin liittyvät tuotteet Tuote olioina.
     *
     * @return lista Tilaus olioita.
     */
    @Override
    public List<Tilaus> findAll() {
        List<Tilaus> tilaukset = new ArrayList<>();

        try (Connection conn = database.getConnection()) {
            ResultSet result = conn.prepareStatement("SELECT * FROM tilaus").executeQuery();

            while (result.next()) {
                List<Tuote> tuotteet = new ArrayList<>();
                Paiva paiva = this.paivaDao.findOne(result.getInt("paiva_id"));
                Asiakas asiakas = asiakasDao.findOne(paiva.getAsiakasId());

                try (Connection conn2 = database.getConnection()) {
                    PreparedStatement stmt = conn2.prepareStatement("SELECT * FROM tilaustuote WHERE tilaus_id = ?");
                    stmt.setInt(1, result.getInt("id"));
                    ResultSet result2 = stmt.executeQuery();

                    while (result2.next()) {
                        tuotteet.add(tuoteDao.findOne(result2.getInt("tuote_id")));
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(TilausDao.class.getName()).log(Level.SEVERE, null, ex);
                }

                tilaukset.add(new Tilaus(result.getInt("id"), result.getString("status"), tuotteet, result.getInt("paiva_id"), asiakas));
            }
        } catch (SQLException ex) {
            Logger.getLogger(TilausDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return tilaukset;
    }

    /**
     * Metodi luo tilaus-tauluun uuden tietueen Tilaus oliosta saatujen tietojen status ja paiva_id avulla.
     *
     * @param   uusi   Käyttäjän tai ohjelman antamien tietojen avulla luotu Tilaus olio.
     *
     * @see     TilausDao#findAll()
     *
     * @return Luotu tieto tai null.
     */
    @Override
    public Tilaus save(Tilaus uusi) {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO tilaus(status, paiva_id) VALUES(?, ?)");
            stmt.setString(1, uusi.getStatus());
            stmt.setInt(2, uusi.getPaivaId());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(TilausDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        List<Tilaus> tilaukset = findAll();
        return tilaukset.get(tilaukset.size() - 1);
    }

    /**
     * Metodi Muokkaa tilaus id:tä vastaavaa tietuetta tilaus-taulussa Tilaus oliosta saatujen tietojen status ja paiva_id avulla.
     *
     * @param   paivitys   Käyttäjän tai ohjelman antamien tietojen avulla luotu Tilaus olio.
     *
     * @see     TilausDao#findAll()
     *
     * @return Luotu tieto tai null.
     */
    @Override
    public Tilaus update(Tilaus paivitys) {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("UPDATE tilaus SET status = ?, paiva_id = ? WHERE id = ?");
            stmt.setString(1, paivitys.getStatus());
            stmt.setInt(2, paivitys.getPaivaId());
            stmt.setInt(3, paivitys.getId());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(TilausDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        List<Tilaus> tilaukset = findAll();
        return tilaukset.get(tilaukset.size() - 1);
    }

    /**
     * Metodi poistaa tilaus-taulusta parametrina saatua id:tä vastaavan tietueen.
     *
     * @param   key   Käyttäjän tai ohjelman antama id.
     */
    @Override
    public void delete(Integer key) {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM tilaus WHERE id = ?");
            stmt.setInt(1, key);
            boolean execute = stmt.execute();
        } catch (SQLException ex) {
            Logger.getLogger(TilausDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
