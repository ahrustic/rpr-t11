package ba.unsa.etf.rpr;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class GeografijaDAO {

    private static GeografijaDAO instance = null;
    private Connection conn;
    private String url = "baza.db";

    private static void initialize() {
        instance = new GeografijaDAO();
    }

    private GeografijaDAO() {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(url);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void removeInstance() {
        instance = null;
    }

    public static GeografijaDAO getInstance() {
        if(instance == null) initialize();
        return instance;
    }


    public ArrayList<Grad> gradovi() {
        ArrayList<Grad> gradovi = new ArrayList<>();



        return gradovi;
    }

    public Grad glavniGrad(String austrija) {
        return null;
    }

    public void obrisiDrzavu(String kina) {

    }

    public Drzava nadjiDrzavu(String francuska) {
        return null;
    }

    public void dodajGrad(Grad grad) {

    }

    public void dodajDrzavu(Drzava bih) {

    }

    public void izmijeniGrad(Grad bech) {

    }
}
