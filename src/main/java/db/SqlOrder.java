package db;


import com.haulmont.model.Order;
import com.haulmont.model.WorkStatus;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SqlOrder {

    private String error = "Ошибка sql SqlOrder ";
    private String sql = "";
    private Connection conn = null;

    public SqlOrder() {

    }

    public boolean deleteOrder(Order order) {
        return true;
    }

    public ArrayList<Order> loadOrderList() {
        ArrayList<Order> oList = new ArrayList<Order>();
        sql = " select IDO,    IDM, a2.SURNAME+' '+ Substring(a2.NAME,0,2)+'.'+ Substring(a2.FNAME,0,2)+'.' mm, \n" +
                "       IDC        ,    IDM, a3.SURNAME+' '+ Substring(a3.NAME,0,2)+'.'+ Substring(a3.FNAME,0,2)+'.' cc,    \n" +
                "       DESCRIPTION ,    DATECR     ,    DATEFIN    ,    STATUSO     ,    COST  \n" +
                "   from ORDER1 a1  \n" +
                "   left join USER a2 on a1.IDM=a2.ID and a2.ROLE like 'механик'    \n" +
                "   left join USER a3 on a1.IDC=a3.ID and a3.ROLE like 'клиент' ";

        try {
            conn = ConnectSql.getMySQLConnection();
            Statement stmnt = conn.createStatement();
            ResultSet res = stmnt.executeQuery(sql);
            while (res.next()) {

                Order ord = new Order();
                ord.setId(res.getLong("IDO"));
                ord.setIdcM(res.getLong("IDM"));
                ord.setNameM(res.getString("mm"));
                ord.setIdc(res.getLong("IDC"));
                ord.setNameC(res.getString("cc"));
                ord.setDescription(res.getString("DESCRIPTION"));
                ord.setDateStart(res.getDate("DATECR"));
                ord.setDateStop(res.getDate("DATEFIN"));
                ord.setPrice(res.getDouble("COST"));
                ord.setWorkStatus(WorkStatus.valueOf(res.getString("STATUSO")));

                System.out.println(res.getDate("DATEFIN"));
                oList.add(ord);


            }
            return oList;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println(error + "\n" + sql + "      ");
            return null;
        }

    }
}