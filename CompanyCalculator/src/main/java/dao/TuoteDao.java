package dao;

import database.Database;
import domain.Tuote;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class TuoteDao implements Dao<Tuote, Integer> {

    private Database database;

    public TuoteDao(Database database) {
        this.database = database;
    }

    @Override
    public Tuote findOne(Integer key) throws SQLException {
        return findAll().stream().filter(t -> t.getId() == key).findFirst().get();
    }

    @Override
    public List<Tuote> findAll() throws SQLException {
        List<Tuote> tuotteet = new ArrayList<>();

        try (Connection conn = database.getConnection();
             ResultSet result = conn.prepareStatement("SELECT * FROM tuote").executeQuery()) {

            while (result.next()) {
                tuotteet.add(new Tuote(result.getInt("id"), result.getString("tuotekoodi"), result.getString("nimi"), result.getDouble("hinta"), result.getDouble("alv")));
            }
        }

        return tuotteet;
    }

    @Override
    public Tuote saveOrUpdate(Tuote uusi) throws SQLException {
        Tuote on = findByTuotekoodi(uusi.getTuotekoodi());

        if (on != null) {
            return on;
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

    private Tuote findByTuotekoodi(String tuotekoodi) throws SQLException {
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
