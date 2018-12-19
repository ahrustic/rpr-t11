package ba.unsa.etf.rpr;

public class Grad {
    private Grad drzava;
    private String naziv;
    private int brojStanovnika;


    public Grad getDrzava() {
        return drzava;
    }

    public void setDrzava(Grad drzava) {
        this.drzava = drzava;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public void setBrojStanovnika(int brojStanovnika) {
        this.brojStanovnika = brojStanovnika;
    }

    public int getBrojStanovnika() {
        return brojStanovnika;
    }

    public void setDrzava(Drzava francuska) {

    }
}
