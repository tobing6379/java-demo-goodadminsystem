package top.tobing.view;

import top.tobing.dao.GoodDao;
import top.tobing.domain.Good;
import top.tobing.domain.User;

import java.util.List;
import java.util.Scanner;

public class Menu {
    User user = null;
    public Menu(User user){
        this.user = user;
    }
    GoodDao goodDao = new GoodDao();
    /**
     * 通用商品查询
     */
    public void findGoods(){
        Good g = new Good();
        while(true){
            System.out.println("-----------商品查询-----------");
            System.out.println("-1. 根据商品名称");
            System.out.println("-2. 根据商品品牌");
            System.out.println("-3. 根据商品经办人");
            System.out.println("-0. 退出");
            System.out.print("-请输入你的选择：");
            Scanner sc = new Scanner(System.in);
            int i = sc.nextInt();
            List<Good> goods =null;
            switch (i){
                case 1:
                    System.out.print("请输入商品名称：");
                    String name = sc.next();
                    goods = goodDao.findByName(name);
                    System.out.println("--------------------------------------------");
                    System.out.println("商品名称\t\t商品品牌\t\t商品进价\t\t商品剩余\t\t商品经办人");
                    for (Good good : goods) {
                        showGood(good);
                    }
                    break;
                case 2:
                    System.out.print("请输入商品品牌：");
                    String brand = sc.next();
                    goods = goodDao.findByBrand(brand);
                    System.out.println("--------------------------------------------");
                    System.out.println("商品名称\t\t商品品牌\t\t商品进价\t\t商品剩余\t\t商品经办人");
                    for (Good good : goods) {
                        showGood(good);
                    }
                    break;
                case 3:
                    System.out.print("请输入商品经办人：");
                    String agent = sc.next();
                    goods = goodDao.findByAgent(agent);
                    System.out.println("--------------------------------------------");
                    System.out.println("商品名称\t\t商品品牌\t\t商品进价\t\t商品剩余\t\t商品经办人");
                    for (Good good : goods) {
                        showGood(good);
                    }
                    break;
                case 0:
                    System.out.println("退出");
                    return;
                default:
                    System.out.println("输入有误请重新输入");
                    break;
            }
        }
    }

    /**
     * 展示商品
     * @param good
     */
    public void showGood(Good good){
        System.out.println(good.getGoodname()+"\t\t"+good.getGoodbrand()+"\t\t"+good.getInprice()+"\t\t"+good.getStock()+"\t\t"+good.getAgent());
    }
}
