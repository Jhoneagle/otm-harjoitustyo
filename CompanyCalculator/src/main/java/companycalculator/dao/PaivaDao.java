package companycalculator.dao;

import companycalculator.database.Database;
import companycalculator.domain.Paiva;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Luokka tarjoaa tietokannan paiva-taulun kannalta olellisia toimintoja.
 * Tällä hetkellä tuettuja ovat Lisäys, listaus ja id:llä haku.
 */
public class PaivaDao implements Dao<Paiva, Integer> {
    private Database database;

    public PaivaDao(Database database) {
        this.database = database;
    }

    /**
     * Metodi hakee parametriä vastaavan Paiva olion, jonka hakee Paiva oloiden listasta.
     *
     * @param   key   Käyttäjän tai ohjelman antama paiva_id.
     *
     * @see     PaivaDao#findAll()
     *
     * @return Löydetty Paiva olio tai null.
     */
    @Override
    public Paiva findOne(Integer key) {
        List<Paiva> kaikki = findAll();

        for (int i = 0; i < kaikki.size(); i++) {
            Paiva temp = kaikki.get(i);

            if (temp.getId() == key) {
                return temp;
            }
        }

        return null;
    }

    /**
     * Metodi kerää tietokannasta kaikki paiva tietueet ja luo niistä listan Paiva olioita.
     *
     * @return Paiva olioista muodostuva lista.
     */
    @Override
    public List<Paiva> findAll() {
        List<Paiva> paivat = new ArrayList<>();

        try (Connection conn = database.getConnection()) {
            ResultSet result = conn.prepareStatement("SELECT * FROM paiva").executeQuery();

            while (result.next()) {
                paivat.add(new Paiva(result.getInt("id"), result.getString("paiva"), result.getInt("asiakas_id")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(PaivaDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return paivat;
    }

    /**
     * Metodi luo tietokantaan uuden paiva tietueen parametrinä saadun olion tarjoamien tietojen avulla.
     *
     * @param   uusi   Käyttäjän tai ohjelman antamien tietojen perusteella luotu Paiva olio.
     *
     * @see     PaivaDao#findAll()
     *
     * @return Null tai tietokantaan lisätty Paiva olio.
     */
    @Override
    public Paiva save(Paiva uusi) {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO paiva(paiva, asiakas_id) VALUES(?, ?)");
            stmt.setString(1, uusi.getPaiva());
            stmt.setInt(2, uusi.getAsiakasId());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(PaivaDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        List<Paiva> paivat = findAll();
        return paivat.get(paivat.size() - 1);
    }

    @Override
    public Paiva update(Paiva object) {
        return null;
    }

    @Override
    public void delete(Integer key) {

    }
}
