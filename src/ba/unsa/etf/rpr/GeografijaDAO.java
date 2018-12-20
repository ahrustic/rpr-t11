package ba.unsa.etf.rpr;

import java.sql.*;
import java.util.ArrayList;

public class GeografijaDAO {

    private static GeografijaDAO instance = null;
    private Connection conn;
    private String url = "baza.db";
    private PreparedStatement preparedStatement;
    private ArrayList<Grad> gradovi;
    private ArrayList<Drzava> drzave;

    private static void initialize() {
        instance = new GeografijaDAO();
    }

    private GeografijaDAO() {
        gradovi = new ArrayList<>();
        drzave = new ArrayList<>();
        napuniPodacima();
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:" + url);
            preparedStatement = conn.prepareStatement("INSERT INTO grad VALUES (?, ?, ?, NULL)");
            for (var grad : gradovi) {
                try {
                    preparedStatement.setInt(1, grad.getId());
                    preparedStatement.setString(2, grad.getNaziv());
                    preparedStatement.setInt(3, grad.getBrojStanovnika());
                    preparedStatement.executeUpdate();
                } catch (SQLException ignored) {
                }
            }
            preparedStatement = conn.prepareStatement("INSERT  INTO drzava VALUES(?, ?, ?)");
            for (var drzava : drzave) {
                try {
                    preparedStatement.setInt(1, drzava.getId());
                    preparedStatement.setString(2, drzava.getNaziv());
                    preparedStatement.setInt(3, drzava.getGlavniGrad().getId());
                    preparedStatement.executeUpdate();
                } catch (SQLException ignored) {
                }
            }
            preparedStatement = conn.prepareStatement("UPDATE grad SET drzava = ? WHERE id = ?");
            for (var grad : gradovi) {
                try {
                    preparedStatement.setInt(1, grad.getDrzava().getId());
                    preparedStatement.setInt(2, grad.getId());
                    preparedStatement.executeUpdate();
                } catch (SQLException ignored) {
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public static void removeInstance() {
        instance = null;
    }

    private void napuniPodacima() {
        Grad pariz = new Grad(1, "Pariz", 2229621 , null);
        Grad london = new Grad(2, "London",  	7355400 , null);
        Grad bec = new Grad(3, "Beč", 1867582, null);
        Grad manchester = new Grad(4, "Manchester",  	441200, null);
        Grad graz = new Grad(5, "Graz",  	286686, null);
        Drzava francuska = new Drzava(1, "Francuska", pariz);
        Drzava engleska = new Drzava(2, "Engleska", london);
        Drzava austrija = new Drzava(3, "Austrija", bec);
        pariz.setDrzava(francuska);
        london.setDrzava(engleska);
        bec.setDrzava(austrija);
        manchester.setDrzava(engleska);
        graz.setDrzava(austrija);
        gradovi.add(pariz);
        gradovi.add(london);
        gradovi.add(bec);
        gradovi.add(manchester);
        gradovi.add(graz);
        drzave.add(francuska);
        drzave.add(engleska);
        drzave.add(austrija);

    }


    public static GeografijaDAO getInstance() {
        if(instance == null) initialize();
        return instance;
    }


    public ArrayList<Grad> gradovi() {
        return gradovi;
    }

    public Grad glavniGrad(String drzava) {

        String upit = "select g.* from grad g, drzava d where d.naziv=? and d.glavni_grad = g.id";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(upit);
            preparedStatement.setString(1, drzava);
            ResultSet resultSet = preparedStatement.executeQuery();
            int id = resultSet.getInt("g.id");
            String naziv = resultSet.getString("g.naziv");
            int brojStanovnika = resultSet.getInt("g.broj_stanovnika");
            return new Grad(id, naziv, brojStanovnika, null);

        } catch (SQLException e) {
            //e.printStackTrace();
        }

        return null;
    }

    public void obrisiDrzavu(String drzava) {

        String upit = "DELETE FROM drzava WHERE naziv = '" + drzava + "';";

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(upit);
            preparedStatement.setString(1, drzava);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            //e.printStackTrace();
        }

    }

    public Drzava nadjiDrzavu(String drzava) {

        String upit = "select * from drzava d where d.naziv=?";

        try {

            PreparedStatement preparedStatement = conn.prepareStatement(upit);
            preparedStatement.setString(1, drzava);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.isClosed())
                return null;

            int drzavaId = resultSet.getInt("d.id");
            Integer gradId = resultSet.getInt("d.glavni_grad");
            Drzava novaDrzava = new Drzava(drzavaId, drzava, null);
            Grad glavniGrad = glavniGrad(drzava);
            glavniGrad.setDrzava(novaDrzava);
            novaDrzava.setGlavniGrad(glavniGrad);
            return novaDrzava;

        } catch (SQLException e) {
            //e.printStackTrace();
        }
        return null;
    }

    public void dodajGrad(Grad grad) {
        String upit = "insert into grad values(?, ?, ?, ?);";

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(upit);
            preparedStatement.setInt(1, grad.getId());
            preparedStatement.setString(2, grad.getNaziv());
            preparedStatement.setInt(3, grad.getBrojStanovnika());
            preparedStatement.setNull(4, 0);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            //e.printStackTrace();
        }

    }

    public void dodajDrzavu(Drzava drzava) {
        String upit = "insert into drzava values(?, ?, ?);";

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(upit);
            preparedStatement.setInt(1, drzava.getId());
            preparedStatement.setString(2, drzava.getNaziv());
            preparedStatement.setInt(3, drzava.getGlavniGrad().getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            //e.printStackTrace();
        }
    }

    public void izmijeniGrad(Grad grad) {
        try{
            String upit1 = "UPDATE grad set broj_stanovnika = ? where id = ?";
            String upit2 = "UPDATE grad set name = ? where id = ?";
            String upit3 = "UPDATE grad set drzava = ? where id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(upit1);
            preparedStatement.setInt(1, grad.getBrojStanovnika());
            PreparedStatement preparedStatement2 = conn.prepareStatement(upit2);
            preparedStatement.setString(2, grad.getNaziv());
            PreparedStatement preparedStatement3 = conn.prepareStatement(upit3);
            preparedStatement.setInt(1, grad.getId());
            preparedStatement.executeUpdate();
            preparedStatement2.executeUpdate();
            preparedStatement3.executeUpdate();
        } catch (SQLException e) {
            //e.printStackTrace();
        }
    }
}
