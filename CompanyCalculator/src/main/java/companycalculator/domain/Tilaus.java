package companycalculator.domain;

import java.util.List;

public class Tilaus {
    private int id;
    private String status;
    private List<Tuote> tuotteet;
    private int paivaId;
    private Asiakas asiakas;

    public Tilaus(Integer id, String status, List<Tuote> tuotteet, int paivaId, Asiakas asiakas) {
        this.id = id;
        this.status = status;
        this.tuotteet = tuotteet;
        this.paivaId = paivaId;
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

    public int getPaivaId() {
        return paivaId;
    }

    public void setPaivaId(int paivaId) {
        this.paivaId = paivaId;
    }

    public Asiakas getAsiakas() {
        return asiakas;
    }

    public void setAsiakas(Asiakas asiakas) {
        this.asiakas = asiakas;
    }
}