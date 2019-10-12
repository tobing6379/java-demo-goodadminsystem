package top.tobing.utils;

import top.tobing.domain.Good;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GoodMapper {
    public static Good getObject(ResultSet resultSet) throws SQLException {
        Good g = new Good();
        int gid = resultSet.getInt("gid");
        String goodname = resultSet.getString("goodname");
        String goodbrand = resultSet.getString("goodbrand");
        Date intime = resultSet.getDate("intime");
        Date saletime = resultSet.getDate("saletime");
        double inprice = resultSet.getDouble("inprice");
        double saleprice = resultSet.getDouble("saleprice");
        int stock = resultSet.getInt("stock");
        String agent = resultSet.getString("agent");
        g.setGid(gid);
        g.setGoodbrand(goodbrand);
        g.setGoodname(goodname);
        g.setIntime(intime);
        g.setSaleprice(saleprice);
        g.setSaletime(saletime);
        g.setInprice(inprice);
        g.setStock(stock);
        g.setAgent(agent);
        return g;
    }
}
