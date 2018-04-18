package companycalculator.domain;

public class Asiakas {

    private int id;
    private String yritysNimi;
    private String yTunnus;
    private String nimi;
    private String sahkoposti;
    private String puhelinnumero;
    private String osoite;
    private String postinumero;
    private String postitoimipaikka;

    public Asiakas(int id, String yritysNimi, String yTunnus, String nimi, String sahkoposti, String puhelinnumero, String osoite, String postinumero, String postitoimipaikka) {
        this.id = id;
        this.yritysNimi = yritysNimi;
        this.yTunnus = yTunnus;
        this.nimi = nimi;
        this.sahkoposti = sahkoposti;
        this.puhelinnumero = puhelinnumero;
        this.osoite = osoite;
        this.postinumero = postinumero;
        this.postitoimipaikka = postitoimipaikka;
    }

    public Asiakas() {

    }

    public int getId() {
        return id;
    }

    public String getYritysNimi() {
        return yritysNimi;
    }

    public void setYritysNimi(String yritysNimi) {
        this.yritysNimi = yritysNimi;
    }

    public String getyTunnus() {
        return yTunnus;
    }

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public String getSahkoposti() {
        return sahkoposti;
    }

    public void setSahkoposti(String sahkoposti) {
        this.sahkoposti = sahkoposti;
    }

    public String getPuhelinnumero() {
        return puhelinnumero;
    }

    public void setPuhelinnumero(String puhelinnumero) {
        this.puhelinnumero = puhelinnumero;
    }

    public String getOsoite() {
        return osoite;
    }

    public void setOsoite(String osoite) {
        this.osoite = osoite;
    }

    public String getPostinumero() {
        return postinumero;
    }

    public void setPostinumero(String postinumero) {
        this.postinumero = postinumero;
    }

    public String getPostitoimipaikka() {
        return postitoimipaikka;
    }

    public void setPostitoimipaikka(String postitoimipaikka) {
        this.postitoimipaikka = postitoimipaikka;
    }
}
