package companycalculator.dao;

import companycalculator.database.Database;
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
 * Luokka tarjoaa tietokannan tuote-taulun kannalta olellisia toimintoja.
 * Tällä hetkellä tuettuja ovat Lisäys, listaus, id:llä ja tuotekoodilla haku, poisto ja muokkaus.
 */
public class TuoteDao implements Dao<Tuote, Integer> {

    private Database database;

    public TuoteDao(Database database) {
        this.database = database;
    }

    /**
     * Metodi hakee parametrin perusteella Tuote olion, Tuote olioiden listasta.
     *
     * @param   key   Käyttäjän tai ohjelman antama tuote_id.
     *
     * @see     TuoteDao#findAll()
     *
     * @return Löydetty Tuote olio tai null.
     */
    @Override
    public Tuote findOne(Integer key) {
        List<Tuote> kaikki = findAll();

        for (int i = 0; i < kaikki.size(); i++) {
            Tuote temp = kaikki.get(i);

            if (temp.getId() == key) {
                return temp;
            }
        }

        return null;
    }

    /**
     * Metodi hakee kaikki tietueet tuote-taulusta ja luo niistä listan Tuote olioita.
     *
     * @return Lista Tuote olioita.
     */
    @Override
    public List<Tuote> findAll() {
        List<Tuote> tuotteet = new ArrayList<>();

        try (Connection conn = database.getConnection()) {
            ResultSet result = conn.prepareStatement("SELECT * FROM tuote").executeQuery();

            while (result.next()) {
                tuotteet.add(new Tuote(result.getInt("id"), result.getString("tuotekoodi"), result.getString("nimi"), result.getDouble("hinta"), result.getDouble("alv")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(TuoteDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return tuotteet;
    }

    /**
     * Metodi Muokkaa tuote id:tä vastaavaa tietuetta tuote-taulussa Tuote olion antamien tietojen avulla.
     *
     * @param   paivitys   Käyttäjän tai ohjelman antamien tietojen avulla luotu Tuote olio.
     *
     * @see     TuoteDao#findByTuotekoodi(String)
     *
     * @return Palauttaa saadun olion tai null, jos jo olemassa tuotekoodi jossakin tietueessa.
     */
    @Override
    public Tuote update(Tuote paivitys) {
        Tuote onko = findByTuotekoodi(paivitys.getTuotekoodi());
        if (onko != null) {
            return null;
        }

        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("UPDATE tuote SET tuotekoodi = ?, nimi = ?, hinta = ?, alv = ? WHERE id = ?");
            stmt.setString(1, paivitys.getTuotekoodi());
            stmt.setString(2, paivitys.getNimi());
            stmt.setDouble(3, paivitys.getHinta());
            stmt.setDouble(4, paivitys.getAlv());
            stmt.setInt(5, paivitys.getId());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(TuoteDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return paivitys;
    }

    /**
     * Metodi luo tuote-tauluun uuden tietueen saadun Tuote olion avulla.
     *
     * @param   uusi   Käyttäjän tai ohjelman antamien tietojen avulla luotu Tuote olio.
     *
     * @see     TuoteDao#findByTuotekoodi(String)
     *
     * @return Luotu tieto Tuote oliona tai null.
     */
    @Override
    public Tuote save(Tuote uusi) {
        Tuote onko = findByTuotekoodi(uusi.getTuotekoodi());
        if (onko != null) {
            return null;
        }

        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO tuote(tuotekoodi, nimi, hinta, alv) VALUES(?, ?, ?, ?)");
            stmt.setString(1, uusi.getTuotekoodi());
            stmt.setString(2, uusi.getNimi());
            stmt.setDouble(3, uusi.getHinta());
            stmt.setDouble(4, uusi.getAlv());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(TuoteDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return findByTuotekoodi(uusi.getTuotekoodi());
    }

    /**
     * Metodi poistaa tuote-taulusta parametrina saatua id:tä vastaavan tietueen.
     *
     * @param   key   Käyttäjän tai ohjelman antama id.
     */
    @Override
    public void delete(Integer key) {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM tuote WHERE id = ?");
            stmt.setInt(1, key);
            boolean execute = stmt.execute();
        } catch (SQLException ex) {
            Logger.getLogger(TuoteDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Metodi etsii tietokannasta tuote-taulusta parametrin arvon perusteella tietueen ja luo siitä Tuote olion.
     *
     * @param   tuotekoodi   Käyttäjän tai ohjelman antma tuotekoodi.
     *
     * @return Löydetty Tuote olio tai null.
     */
    public Tuote findByTuotekoodi(String tuotekoodi) {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM tuote WHERE tuotekoodi = ?");
            stmt.setString(1, tuotekoodi);

            ResultSet result = stmt.executeQuery();
            if (!result.next()) {
                return null;
            }

            return new Tuote(result.getInt("id"), result.getString("tuotekoodi"), result.getString("nimi"), result.getDouble("hinta"), result.getDouble("alv"));
        } catch (SQLException ex) {
            Logger.getLogger(TuoteDao.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
