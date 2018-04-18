package Johneagle.companyCalculator.Dao;

import Johneagle.companyCalculator.Dao.Dao;
import Johneagle.companyCalculator.database.Database;
import Johneagle.companyCalculator.domain.Tuote;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TuoteDao implements Dao<Tuote, Integer> {

    private Database database;

    public TuoteDao(Database database) {
        this.database = database;
    }

    @Override
    public Tuote findOne(Integer key) throws SQLException {
        List<Tuote> kaikki = findAll();

        for (int i = 0; i < kaikki.size(); i++) {
            Tuote temp = kaikki.get(i);

            if (temp.getId() == key) {
                return temp;
            }
        }

        return null;
    }

    @Override
    public List<Tuote> findAll() throws SQLException {
        List<Tuote> tuotteet = new ArrayList<>();

        try ( Connection conn = database.getConnection() ) {
            ResultSet result = conn.prepareStatement("SELECT * FROM tuote").executeQuery();

            while (result.next()) {
                tuotteet.add(new Tuote(result.getInt("id"), result.getString("tuotekoodi"), result.getString("nimi"), result.getDouble("hinta"), result.getDouble("alv")));
            }
        }

        return tuotteet;
    }

    @Override
    public Tuote update(Tuote paivitys) throws SQLException {
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
        }

        return paivitys;
    }

    @Override
    public Tuote save(Tuote uusi) throws SQLException {
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
        }

        return findByTuotekoodi(uusi.getTuotekoodi());
    }

    @Override
    public void delete(Integer key) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM tuote WHERE id = ?");
            stmt.setInt(1, key);
            boolean execute = stmt.execute();
        }
    }

    public Tuote findByTuotekoodi(String tuotekoodi) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM tuote WHERE tuotekoodi = ?");
            stmt.setString(1, tuotekoodi);

            ResultSet result = stmt.executeQuery();
            if (!result.next()) {
                return null;
            }

            return new Tuote(result.getInt("id"), result.getString("tuotekoodi"), result.getString("nimi"), result.getDouble("hinta"), result.getDouble("alv"));
        }
    }
}
