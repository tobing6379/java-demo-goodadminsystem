package top.tobing.dao;

import top.tobing.domain.Good;
import top.tobing.utils.GoodMapper;
import top.tobing.utils.JDBCUtils;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GoodDao {
    /**
     * 根据姓名查询商品
     * @param name
     * @return
     * @throws Exception
     */
    public List<Good> findByName(String name)  {
        Connection connection = null;
        ResultSet resultSet =null;
        Statement statement = null;
        List<Good> list =null;
        try {
            connection = JDBCUtils.getConnection();
            statement = connection.createStatement();
            String sql = "select * from good where goodname = '" + name + "'";
            resultSet = statement.executeQuery(sql);
            list = new ArrayList<>();
            while (resultSet.next()) {
                Good good = GoodMapper.getObject(resultSet);
                list.add(good);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 根据品牌查询
     * @param brand
     * @return
     */
    public List<Good> findByBrand(String brand) {
        Connection connection = null;
        ResultSet resultSet =null;
        Statement statement = null;
        List<Good> list =null;
        try {
            connection = JDBCUtils.getConnection();
            statement = connection.createStatement();
            String sql = "select * from good where goodbrand = '" + brand + "'";
            resultSet = statement.executeQuery(sql);
            list = new ArrayList<>();
            while (resultSet.next()) {
                Good good = GoodMapper.getObject(resultSet);
                list.add(good);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 根据经办人查询
     * @param agent
     * @return
     */
    public List<Good> findByAgent(String agent) {
        Connection connection = null;
        ResultSet resultSet =null;
        Statement statement = null;
        List<Good> list =null;
        try {
            connection = JDBCUtils.getConnection();
            statement = connection.createStatement();
            String sql = "select * from good where agent = '" + agent + "'";
            resultSet = statement.executeQuery(sql);
            list = new ArrayList<>();
            while (resultSet.next()) {
                Good good = GoodMapper.getObject(resultSet);
                list.add(good);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 添加新商品
     * @param good
     */
    public void addNewGoods(Good good){
        Connection connection = null;
        ResultSet resultSet =null;
        Statement statement = null;
        List<Good> list =null;
        try {
            connection = JDBCUtils.getConnection();
            statement = connection.createStatement();
            String sql = "insert into good(goodname,goodbrand,intime,inprice,stock,agent) " +
                    "values('"+good.getGoodname()+"','"+good.getGoodbrand()+"','"+
                    new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()) +
                    "','"+good.getInprice()+"','"+good.getStock()+"','"+good.getAgent()+"')" ;
            statement.execute(sql);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 更新已经存在的商品数量
     * @param name
     * @param num
     */
    public void updateByName(String name,int num) {
        Connection connection = null;
        ResultSet resultSet =null;
        Statement statement = null;
        List<Good> list =null;
        try {
            connection = JDBCUtils.getConnection();
            statement = connection.createStatement();
            String sql = "update good set stock = "+num+" where goodname ='"+name+"'";
            System.out.println(sql);
            statement.execute(sql);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
