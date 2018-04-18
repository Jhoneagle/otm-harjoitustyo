package Johneagle.companyCalculator.domain;

public class Paiva {
    private int id;
    private String paiva;
    private int asiakas_id;

    public Paiva(int id, String paiva, int asiakas_id) {
        this.id = id;
        this.paiva = paiva;
        this.asiakas_id = asiakas_id;
    }

    public int getId() {
        return id;
    }

    public String getPaiva() {
        return paiva;
    }

    public void setPaiva(String paiva) {
        this.paiva = paiva;
    }

    public int getAsiakas_id() {
        return asiakas_id;
    }

    public void setAsiakas_id(int asiakas_id) {
        this.asiakas_id = asiakas_id;
    }
}
