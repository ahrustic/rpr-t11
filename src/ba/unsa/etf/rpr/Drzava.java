package ba.unsa.etf.rpr;

public class Drzava {

    private String naziv;
    private Grad glavniGrad;

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setGlavniGrad(Grad glavniGrad) {
        this.glavniGrad = glavniGrad;
    }

    public Grad getGlavniGrad() {
        return glavniGrad;
    }
}
