package Johneagle.companyCalculator.domain;

import java.util.List;

public class Tilaus {
    private int id;
    private String status;
    private List<Tuote> tuotteet;
    private int paiva_id;
    private Asiakas asiakas;

    public Tilaus(Integer id, String status, List<Tuote> tuotteet, int paiva_id, Asiakas asiakas) {
        this.id = id;
        this.status = status;
        this.tuotteet = tuotteet;
        this.paiva_id = paiva_id;
        this.asiakas = asiakas;

    }

    public Integer getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Tuote> getTuotteet() {
        return tuotteet;
    }

    public void setTuotteet(List<Tuote> tuotteet) {
        this.tuotteet = tuotteet;
    }

    public int getPaiva_id() {
        return paiva_id;
    }

    public void setPaiva_id(int paiva_id) {
        this.paiva_id = paiva_id;
    }

    public Asiakas getAsiakas() {
        return asiakas;
    }

    public void setAsiakas(Asiakas asiakas) {
        this.asiakas = asiakas;
    }
}
