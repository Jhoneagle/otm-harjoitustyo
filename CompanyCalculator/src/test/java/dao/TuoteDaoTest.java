package dao;

import database.Database;
import domain.Tuote;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;

public class TuoteDaoTest {

    private TuoteDao dao;
    private int testiKey;

    @Before
    public void setUp() throws Exception {
        Database database = new Database("jdbc:sqlite:data.db");
        dao = new TuoteDao(database);
        this.testiKey = 1;
    }

    @Test
    public void saveOrUpdate() throws SQLException {
        Tuote testi = new Tuote(1, "testi12345", "testisyote", 1, 1);
        Tuote mika = dao.saveOrUpdate(testi);
        this.testiKey = mika.getId();
        assertTrue(mika.getTuotekoodi().equals(testi.getTuotekoodi()));
        assertTrue(mika.getNimi().equals(testi.getNimi()));
        assertTrue(mika.getHinta() == testi.getHinta());
        assertTrue(mika.getAlv() == testi.getAlv());
    }

    @Test
    public void findOne() throws SQLException {
        Tuote mika = dao.findOne(this.testiKey);
        assertTrue(mika.getTuotekoodi().equals("testi12345"));
        assertTrue(mika.getNimi().equals("testisyote"));
        assertTrue(mika.getHinta() == 1);
        assertTrue(mika.getAlv() == 1);
    }

    @Test
    public void delete() throws SQLException {
        dao.delete(this.testiKey);
        List<Tuote> lista = dao.findAll();
        for (int i = 0; i < lista.size(); i++) {
            Tuote temp = lista.get(i);
            assertTrue(temp.getTuotekoodi().equals("testi12345"));
            assertTrue(temp.getNimi().equals("testisyote"));
            assertTrue(temp.getHinta() == 1);
            assertTrue(temp.getAlv() == 1);
        }
    }
}