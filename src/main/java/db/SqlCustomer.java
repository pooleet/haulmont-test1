package db;

import com.haulmont.model.Customer;
import com.haulmont.model.Role;
import com.haulmont.model.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SqlCustomer {
    private String sql = "";

    public SqlCustomer() {

    }

    public ArrayList<Customer> loadCustomerList() throws SQLException {

        ArrayList<Customer> cList = new ArrayList<Customer>();
        Connection conn = null;
        sql = "select a1.ID, a1.NAME, a1.SURNAME,a1.FNAME,a2.PHONEC, a1.ROLE, case when b1.IDO is null then 0 else b1.IDO end IDO from user a1\n" +
                "    inner join CUSTOMER a2 on a1.ID=a2.IDC\n" +
                "    left join (select IDC, count(ido) ido from ORDER1 group by IDC) b1 on a2.IDC=b1.IDC";
        try {
            conn = ConnectSql.getMySQLConnection();
            Statement stmnt = conn.createStatement();
            ResultSet res = stmnt.executeQuery(sql);
            while (res.next()) {

                User user = new User();
                // System.out.println(res.getString("SURNAME"));
                user.setId(res.getLong("ID"));
                user.setFirstName(res.getString("NAME"));
                user.setLastName(res.getString("SURNAME"));
                user.setFatherName(res.getString("FNAME"));
                user.setRole(Role.valueOf(res.getString("ROLE")));


                cList.add(new Customer(user, res.getString("PHONEC"), res.getInt("IDO")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("ошибки при заполнении " + sql + "      ");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        conn.close();

        return cList;

    }

    public boolean UpdateCustomer(Customer customer) {

        Connection conn = null;
        sql = "UPDATE  USER set NAME='" + customer.getName().getFirstName() + "', " +
                "SURNAME='" + customer.getName().getLastName() + "',FNAME='" + customer.getName().getFatherName() + "' " +
                "where id='" + customer.getName().getId() + "' ";

        String sql2 = "UPDATE CUSTOMER set PHONEC='" + customer.getPhone() + "' where IDC='" + customer.getName().getId() + "'";
        try {
            conn = ConnectSql.getMySQLConnection();
            Statement stmnt = conn.createStatement();

            conn.setAutoCommit(false);
            stmnt.executeUpdate(sql);
            stmnt.executeUpdate(sql2);
            conn.commit();

            conn.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("ошибки при заполнении " + sql + "\n      " + sql2);
            return false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }


    }

    public boolean CreateCustomer(Customer customer) {
        Connection conn = null;
        sql = " INSERT into USER (NAME,SURNAME,FNAME,ROLE) " +
                "   values ('" + customer.getName().getFirstName() + "'," +
                "'" + customer.getName().getLastName() + "'," +
                "'" + customer.getName().getFatherName() + "','клиент')";
// поиск ид
        String sql2 = "Insert INTO CUSTOMER  (IDC,PHONEC) values ((select  max(a1.ID) from user a1\n" +
                "left join CUSTOMER a2 on a1.ID=a2.IDC \n" +
                "where a1.role like  'клиент' and PHONEC is null),'" + customer.getPhone() + "')";


        try {
            conn = ConnectSql.getMySQLConnection();
            Statement stmnt = conn.createStatement();


            conn.setAutoCommit(false);
            stmnt.executeUpdate(sql);
            stmnt.executeUpdate(sql2);
            conn.commit();

            conn.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("ошибки при заполнении " + sql + "\n      " + sql2);
            return false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }


    }

    public boolean deleteCustomer(Customer customer) {

        Connection conn = null;
        sql = "delete from user where id= " + customer.getName().getId() + "";
// поиск ид


        try {
            conn = ConnectSql.getMySQLConnection();
            Statement stmnt = conn.createStatement();
            stmnt.executeUpdate(sql);
            conn.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("ошибки при заполнении " + sql);
            return false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
