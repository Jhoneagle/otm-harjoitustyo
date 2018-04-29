package companycalculator.advancelogic;

import companycalculator.dao.AsiakasDao;
import companycalculator.dao.PaivaDao;
import companycalculator.dao.TilausDao;
import companycalculator.dao.TuoteDao;
import companycalculator.database.Database;
import companycalculator.domain.Asiakas;
import companycalculator.domain.Paiva;
import companycalculator.domain.Tilaus;
import companycalculator.domain.Tuote;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Varsinainen sovellus logiikka tietokannan hallintaan tilausten osalta.
 * Pääasiallisesti tilausDaon ja käyttöliittymän yhdistäminen.
 */
public class TilausToiminnallisuus {
    private Database database;
    private AsiakasDao asiakasDao;
    private TuoteDao tuoteDao;
    private PaivaDao paivaDao;
    private TilausDao tilausDao;

    public TilausToiminnallisuus(Database database, AsiakasDao asiakasDao, TuoteDao tuoteDao, PaivaDao paivaDao, TilausDao tilausDao) {
        this.database = database;
        this.asiakasDao = asiakasDao;
        this.tuoteDao = tuoteDao;
        this.paivaDao = paivaDao;
        this.tilausDao = tilausDao;
    }

    /**
     * Ensiksi etsii asiakaan ja luo päivämäärän käyttäen niille tarkoitettuja Daoita hyödyksi.
     * Tämän jälkeen selvittää tilauksen id:n luomalla tilauksen ja lisää tilaustuote liitostauluun kaikki tietueet, jotka tarvitaan.
     * Käyttäen tilauksen id:tä ja tuoteDaosta saatujen Tuote olioiden id:en avulla.
     *
     * @param   uusi    Uusi Tilaus olio, joka sisältää alussa vain tilauksen statuksen, joka on saatu käyttäjältä.
     * @param   tuotekoodit     Lista tuotekoodeja, jotka on saatu käyttäjältä.
     * @param   ytunnus     Käyttäjän tai ohjelman antama asiakaan y-tunnus.
     * @param   paiva   Käyttäjän tai ohjelman antama päivämäärä.
     * @param   tuotemaara  Tuotekoodeja vastaavien tuotteiden lukumäärä tilauksessa.
     *
     * @see     AsiakasDao#findByYtunnus(String)
     * @see     PaivaDao#save(Paiva)
     * @see     TilausDao#save(Tilaus)
     * @see     TuoteDao#findByTuotekoodi(String)
     */
    public void lisaaTilaus(Tilaus uusi, List<String> tuotekoodit, String ytunnus, String paiva, List<Integer> tuotemaara) throws SQLException {
        Asiakas asiakas = asiakasDao.findByYtunnus(ytunnus);
        Paiva paivamaara = new Paiva(0, paiva, asiakas.getId());

        paivamaara = paivaDao.save(paivamaara);
        uusi.setPaivaId(paivamaara.getId());

        int tilausId = tilausDao.save(uusi).getId();

        for (int i = 0; i < tuotekoodit.size(); i++) {
            Tuote temp = tuoteDao.findByTuotekoodi(tuotekoodit.get(i));

            try (Connection conn = database.getConnection()) {
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO tilaustuote(tuotemaara, tilaus_id, tuote_id) VALUES(?, ?, ?)");
                stmt.setInt(1, tuotemaara.get(i));
                stmt.setInt(2, tilausId);
                stmt.setInt(3, temp.getId());

                stmt.executeUpdate();
            }
        }
    }

    /**
     * Hakee kaikki tilaukset ja sitten luo niiden avulla listan merkkijonoja.
     *
     * @see     TilausDao#findAll()
     * @see     PaivaDao#findOne(Integer)
     *
     * @return lista merkkijonoja, joissa on tietoja tilauksista.
     */
    public List<String> listaaTilaukset() throws SQLException {
        List<Tilaus> tilaukset = tilausDao.findAll();
        List<String> merkkijonot = new ArrayList<>();

        for (int i = 0; i < tilaukset.size(); i++) {
            Tilaus temp = tilaukset.get(i);

            if (temp.getStatus().contains("toimitettu")) {
                continue;
            }

            Paiva paivamaara = this.paivaDao.findOne(temp.getPaivaId());

            String lisattava = new StringBuilder().append("yritys: ").append(temp.getAsiakas().getYritysNimi()).append(", paiva: ")
                    .append(paivamaara.getPaiva()).append(", status: ").append(temp.getStatus()).toString();
            merkkijonot.add(lisattava);
        }

        return merkkijonot;
    }

    /**
     * Päivittää tilauksen statuksen ja paivamaaran, jos parametrina saatu "paiva" ei ole tyhjä.
     *
     * @param   id    Käyttäjältä saatu tilauksen id.
     * @param   status  käyttäjältä saatu uusi tilauksen status.
     * @param   paiva   käyttäjältä saatu mahdollinen uusi paiva muuten tyhjä.
     *
     * @see     TilausDao#findOne(Integer)
     * @see     PaivaDao#save(Paiva)
     * @see     TilausDao#update(Tilaus)
     */
    public void muokkaaTilausta(int id, String status, String paiva) throws SQLException {
        Tilaus tilaus = this.tilausDao.findOne(id);
        tilaus.setStatus(status);

        Paiva paivamaara = new Paiva(tilaus.getPaivaId(), paiva, tilaus.getAsiakas().getId());
        if (!paiva.isEmpty()) {
            paivamaara = paivaDao.save(paivamaara);
        }

        tilaus.setPaivaId(paivamaara.getId());
        this.tilausDao.update(tilaus);
    }

    /**
     *  poistaa ensiksi tilauksen ja sen jälkeen kaikki liitostaulusta löytyvät tietueet, jotka liittyvät siihen.
     *
     * @param   id  Käyttäjältä saatu tilauksen id.
     *
     * @see     TilausDao#findOne(Integer)
     * @see     TilausDao#delete(Integer)
     */
    public void poistaTilaus(int id) throws SQLException {
        List<Tuote> tuoteet = this.tilausDao.findOne(id).getTuotteet();
        this.tilausDao.delete(id);

        for (int i = 0; i < tuoteet.size(); i++) {
            Tuote temp = tuoteet.get(i);

            try (Connection conn = database.getConnection()) {
                PreparedStatement stmt = conn.prepareStatement(
                        "DELETE FROM tilaustuote WHERE tilaus_id = ? AND tuote_id = ?");
                stmt.setInt(1, id);
                stmt.setInt(2, temp.getId());
                stmt.execute();
            }
        }
    }
}
