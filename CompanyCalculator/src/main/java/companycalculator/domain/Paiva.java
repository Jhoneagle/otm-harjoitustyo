package companycalculator.domain;

public class Paiva {
    private int id;
    private String paiva;
    private int asiakasId;

    public Paiva(int id, String paiva, int asiakasId) {
        this.id = id;
        this.paiva = paiva;
        this.asiakasId = asiakasId;
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

    public int getAsiakasId() {
        return asiakasId;
    }

    public void setAsiakasId(int asiakasId) {
        this.asiakasId = asiakasId;
    }
}
