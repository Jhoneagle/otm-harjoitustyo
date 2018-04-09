package domain;

public class Tuote {
    private int id;
    private String tuotekoodi;
    private String nimi;
    private double hinta;
    private double alv;

    public Tuote(int id, String tuotekoodi, String nimi, double hinta, double alv) {
        this.id = id;
        this.tuotekoodi = tuotekoodi;
        this.nimi = nimi;
        this.hinta = hinta;
        this.alv = alv;
    }

    public int getId() {
        return id;
    }

    public String getTuotekoodi() {
        return tuotekoodi;
    }

    public void setTuotekoodi(String tuotekoodi) {
        this.tuotekoodi = tuotekoodi;
    }

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public double getHinta() {
        return hinta;
    }

    public void setHinta(double hinta) {
        this.hinta = hinta;
    }

    public double getAlv() {
        return alv;
    }

    public void setAlv(double alv) {
        this.alv = alv;
    }
}
