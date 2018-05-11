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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Varsinainen sovellus logiikka tietokannan hallintaan tilausten osalta.
 * P‰‰asiallisesti tilausDaon ja k‰yttˆliittym‰n yhdist‰minen.
 */
public class Tilaustoiminnallisuus {
    private Database database;
    private AsiakasDao asiakasDao;
    private TuoteDao tuoteDao;
    private PaivaDao paivaDao;
    private TilausDao tilausDao;

    public Tilaustoiminnallisuus(Database database, AsiakasDao asiakasDao, TuoteDao tuoteDao, PaivaDao paivaDao, TilausDao tilausDao) {
        this.database = database;
        this.asiakasDao = asiakasDao;
        this.tuoteDao = tuoteDao;
        this.paivaDao = paivaDao;
        this.tilausDao = tilausDao;
    }

    /**
     * Ensiksi etsii asiakaan ja luo p‰iv‰m‰‰r‰n k‰ytt‰en niille tarkoitettuja Daoita hyˆdyksi.
     * T‰m‰n j‰lkeen selvitt‰‰ tilauksen id:n luomalla tilauksen ja lis‰‰ tilaustuote liitostauluun kaikki tietueet, jotka tarvitaan.
     * K‰ytt‰en tilauksen id:t‰ ja tuoteDaosta saatujen Tuote olioiden id:en avulla.
     *
     * @param   created    Uusi Tilaus olio, joka sis‰lt‰‰ alussa vain tilauksen statuksen, joka on saatu k‰ytt‰j‰lt‰.
     * @param   productNumbers     Lista tuotekoodeja, jotka on saatu k‰ytt‰j‰lt‰.
     * @param   ytunnus     K‰ytt‰j‰n tai ohjelman antama asiakaan y-tunnus.
     * @param   date   K‰ytt‰j‰n tai ohjelman antama p‰iv‰m‰‰r‰.
     * @param   productAmounts  Tuotekoodeja vastaavien tuotteiden lukum‰‰r‰ tilauksessa.
     *
     * @see     AsiakasDao#findByYtunnus(String)
     * @see     PaivaDao#save(Paiva)
     * @see     TilausDao#save(Tilaus)
     * @see     TuoteDao#findByTuotekoodi(String)
     */
    public void addTilaus(Tilaus created, List<String> productNumbers, String ytunnus, String date, List<Integer> productAmounts) {
        Asiakas customer = this.asiakasDao.findByYtunnus(ytunnus);
        Paiva calenter = new Paiva(0, date, customer.getId());

        calenter = this.paivaDao.save(calenter);
        created.setPaivaId(calenter.getId());

        int orderId = this.tilausDao.save(created).getId();
        
        for (int i = 0; i < productNumbers.size(); i++) {
            Tuote temp = this.tuoteDao.findByTuotekoodi(productNumbers.get(i));

            try (Connection conn = this.database.getConnection()) {
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO tilaustuote(tuotemaara, tilaus_id, tuote_id) VALUES(?, ?, ?)");
                stmt.setInt(1, productAmounts.get(i));
                stmt.setInt(2, orderId);
                stmt.setInt(3, temp.getId());

                stmt.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(Tilaustoiminnallisuus.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Hakee kaikki orders ja sitten luo niiden avulla listan merkkijonoja.
     *
     * @see     TilausDao#findAll()
     * @see     PaivaDao#findOne(Integer)
     *
     * @return lista merkkijonoja, joissa on tietoja tilauksista.
     */
    public List<String> listTilaukset() {
        List<Tilaus> orders = this.tilausDao.findAll();
        List<String> string = new ArrayList<>();

        for (int i = 0; i < orders.size(); i++) {
            Tilaus temp = orders.get(i);

            if (temp.getStatus().contains("toimitettu")) {
                continue;
            }

            Paiva paivamaara = this.paivaDao.findOne(temp.getPaivaId());

            String added = new StringBuilder().append(temp.getId()).append(". ")
                    .append("yritys: ").append(temp.getAsiakas().getYritysNimi()).append(", paiva: ")
                    .append(paivamaara.getPaiva()).append(", status: ").append(temp.getStatus()).toString();

            string.add(added);
        }

        return string;
    }

    /**
     * P‰ivitt‰‰ tilauksen statuksen ja p‰iv‰m‰‰r‰n, jos parametrina saatu "day" ei ole tyhj‰.
     *
     * @param   id    K‰ytt‰j‰lt‰ saatu tilauksen id.
     * @param   state  k‰ytt‰j‰lt‰ saatu uusi tilauksen state.
     * @param   day   k‰ytt‰j‰lt‰ saatu mahdollinen uusi day muuten tyhj‰.
     *
     * @see     TilausDao#findOne(Integer)
     * @see     PaivaDao#save(Paiva)
     * @see     TilausDao#update(Tilaus)
     */
    public void editTilausta(int id, String state, String day) {
        Tilaus order = this.tilausDao.findOne(id);
        order.setStatus(state);

        Paiva date = new Paiva(order.getPaivaId(), day, order.getAsiakas().getId());
        if (!day.isEmpty()) {
            date = paivaDao.save(date);
        }

        order.setPaivaId(date.getId());
        this.tilausDao.update(order);
    }

    /**
     *  poistaa ensiksi tilauksen ja sen j‰lkeen kaikki liitostaulusta lˆytyv‰t tietueet, jotka liittyv‰t siihen.
     *
     * @param   id  K‰ytt‰j‰lt‰ saatu tilauksen id.
     *
     * @see     TilausDao#findOne(Integer)
     * @see     TilausDao#delete(Integer)
     */
    public void removeTilaus(int id) {
        List<Tuote> orders = this.tilausDao.findOne(id).getTuotteet();
        this.tilausDao.delete(id);

        for (int i = 0; i < orders.size(); i++) {
            Tuote temp = orders.get(i);

            try (Connection conn = database.getConnection()) {
                PreparedStatement stmt = conn.prepareStatement(
                        "DELETE FROM tilaustuote WHERE tilaus_id = ? AND tuote_id = ?");
                stmt.setInt(1, id);
                stmt.setInt(2, temp.getId());
                stmt.execute();
            } catch (SQLException ex) {
                Logger.getLogger(Tilaustoiminnallisuus.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * k‰ytt‰‰ valmistelussa saatuja arvoja ja parametrina saatuja vikoja tietoja ja suorittaa nill‰ varsinaisen lis‰yksen.
     *
     * @param   products    tuoteiden tuotekoodit.
     * @param   amounts     tuoteiden lukumaara tilauksessa.
     *
     * @see     Tilaustoiminnallisuus#prepareAdd(java.lang.String, java.lang.String, java.lang.String)
     */

    public void executeAdd(List<String> products, List<Integer> amounts) {
        addTilaus(preOrder, products, preYtunnus, preDay, amounts);
    }

    private static Tilaus preOrder;
    private static String preYtunnus;
    private static String preDay;

    /**
     * Metodi valmistelee tilauksen lis‰yksen.
     * Mik‰ mahdollistaa tilauksen lis‰yksen on k‰ytt‰liittym‰ millainen tahansa.
     *
     * @param   ytunnus asiakaan y tunnus
     * @param   state   tilauksen status
     * @param   day     paivamaara
     */

    public static void prepareAdd(String ytunnus, String state, String day) {
        preOrder = new Tilaus(0, state, new ArrayList<>(), 0, new Asiakas());
        preYtunnus = ytunnus;
        preDay = day;
    }
}
