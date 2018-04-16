package domain;

import java.util.Date;

public class Paiva {
    private int id;
    private Date paiva;
    private int asiakas_id;

    public Paiva(int id, Date paiva, int asiakas_id) {
        this.id = id;
        this.paiva = paiva;
        this.asiakas_id = asiakas_id;
    }

    public int getId() {
        return id;
    }

    public Date getPaiva() {
        return paiva;
    }

    public void setPaiva(Date paiva) {
        this.paiva = paiva;
    }

    public int getAsiakas_id() {
        return asiakas_id;
    }

    public void setAsiakas_id(int asiakas_id) {
        this.asiakas_id = asiakas_id;
    }
}
