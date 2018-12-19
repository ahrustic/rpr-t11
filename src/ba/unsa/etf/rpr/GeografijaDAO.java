package ba.unsa.etf.rpr;

import java.sql.*;
import java.util.ArrayList;

public class GeografijaDAO {

    private static GeografijaDAO instance = null;
    private Connection conn;
    private String url = "baza.db";
    private Statement statement;

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

        String upit = "";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(upit);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                System.out.println(resultSet);
            }

        } catch (SQLException e) {
            //e.printStackTrace();
        }

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
        if(gradovi().contains(grad)) throw new IllegalArgumentException("Grad vec postoji");
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
