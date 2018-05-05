package companycalculator.dao;

import companycalculator.database.Database;
import companycalculator.domain.Asiakas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Luokka tarjoaa tietokannan asiakas-taulun kannalta olellisia toimintoja.
 * Tällä hetkellä tuettuja ovat Lisäys, listaus, id:llä haku ja y-tunnuksella haku.
 */
public class AsiakasDao implements Dao<Asiakas, Integer> {

    private Database database;

    public AsiakasDao(Database database) {
        this.database = database;
    }

    /**
     * Metodi Hakee id:tä vastaavan asiakaan tietokannasta käyttäen kaikkien etsintää hyväkseen.
     *
     * @param   key   Käyttäjän antama tai toiselta toiminnallisuudelta saatu asiakas id.
     *
     * @see     AsiakasDao#findAll()
     *
     * @return Ei mitään, jos asiakasta ei löydy tai asiakaan sisältävän Asiakas olion, jos löytyy.
     */
    @Override
    public Asiakas findOne(Integer key) {
        List<Asiakas> kaikki = findAll();

        for (int i = 0; i < kaikki.size(); i++) {
            Asiakas temp = kaikki.get(i);

            if (temp.getId() == key) {
                return temp;
            }
        }

        return null;
    }

    /**
     * Metodi Hakee tietokannasta kaikki asiakaat, jotka löytää ja luo niistä listan Asiakas olioita..
     *
     * @return Palauttaa listan Asiakas olioita, jotka sisältävät kaikki tietokannan asiakas tietueet.
     */
    @Override
    public List<Asiakas> findAll() {
        List<Asiakas> asiakaat = new ArrayList<>();

        try (Connection conn = database.getConnection()) {
            ResultSet result = conn.prepareStatement("SELECT * FROM asiakas").executeQuery();

            while (result.next()) {
                asiakaat.add(new Asiakas(result.getInt("id"), result.getString("yritys_nimi"), result.getString("ytunnus"),
                        result.getString("nimi"), result.getString("sahkoposti"), result.getString("puhelinnumero"),
                        result.getString("osoite"), result.getString("postinumero"), result.getString("postitoimipaikka")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(AsiakasDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return asiakaat;
    }

    /**
     * Metodi Lisää tietokantaan uuden asiakas tietueen Asiakas olion antamien arvojen perusteella.
     *
     * @param   uusi   Käyttäjän söyteistä luotu Asiakas olio.
     *
     * @see     AsiakasDao#findByYtunnus(String)
     *
     * @return Palauttaa lisätyn asiakaan hakemalla sen tietokannasta y-tunnuksen avulla.
     */
    @Override
    public Asiakas save(Asiakas uusi) {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO asiakas(yritys_nimi, ytunnus, nimi, sahkoposti, puhelinnumero, osoite, " +
                    "postinumero, postitoimipaikka) VALUES(?, ?, ?, ?, ?, ?, ?, ?)");
            stmt.setString(1, uusi.getYritysNimi());
            stmt.setString(2, uusi.getyTunnus());
            stmt.setString(3, uusi.getNimi());
            stmt.setString(4, uusi.getSahkoposti());
            stmt.setString(5, uusi.getPuhelinnumero());
            stmt.setString(6, uusi.getOsoite());
            stmt.setString(7, uusi.getPostinumero());
            stmt.setString(8, uusi.getPostitoimipaikka());

            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(AsiakasDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return findByYtunnus(uusi.getyTunnus());
    }

    @Override
    public Asiakas update(Asiakas object) {
        return null;
    }

    @Override
    public void delete(Integer key) {

    }

    /**
     * Metodi hakee tietokannasta y-tunnusta vastaavan tietueen ja luo sen avulla uuden Asiakas olion.
     *
     * @param   yTunnus   Käyttäjän tai ohjelman määrittelemä yrityksen y-tunnus.
     *
     * @return Palauttaa löydetyn asiakaan Asiakas oliona.
     */
    public Asiakas findByYtunnus(String yTunnus) {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM asiakas WHERE ytunnus = ?");
            stmt.setString(1, yTunnus);

            ResultSet result = stmt.executeQuery();
            if (!result.next()) {
                return null;
            }

            return new Asiakas(result.getInt("id"), result.getString("yritys_nimi"), result.getString("ytunnus"),
                    result.getString("nimi"), result.getString("sahkoposti"), result.getString("puhelinnumero"),
                    result.getString("osoite"), result.getString("postinumero"), result.getString("postitoimipaikka"));
        } catch (SQLException ex) {
            Logger.getLogger(AsiakasDao.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
