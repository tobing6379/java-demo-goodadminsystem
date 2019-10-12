package top.tobing.view;

import top.tobing.dao.GoodDao;
import top.tobing.domain.Good;
import top.tobing.domain.User;

import java.util.List;
import java.util.Scanner;

/**
 * 管理员管理菜单
 */
public class AdminMenu extends Menu{

    public AdminMenu(User user){
        super(user);
        this.show();
    }
    GoodDao goodDao = new GoodDao();
    private void show() {
        while(true){
            System.out.println("------------欢迎光临!"+user.getUsername()+"------------");
            System.out.println("-1. 商品入库");
            System.out.println("-2. 查看商品");
            System.out.println("-0. 退出");
            System.out.print("-请输入：");
            Scanner sc = new Scanner(System.in);
            int i = sc.nextInt();
            if(i==1){
                newGoods(); // 添加商品（商品入库）
            }else if(i==2){
                findGoods();// 查找商品
            }else if(i==0){
                return;
            }else{
                System.out.println("输入数字有误！");
            }
        }
    }

    /**
     * 商品入库
     */
    private void newGoods(){

        Scanner sc = new Scanner(System.in);
        System.out.println("-----------商品进货-----------");
        System.out.print("-商品名称：");
        String name = sc.next();
        System.out.print("-商品品牌：");
        String brand = sc.next();
        System.out.print("-商品数量：");
        int num = sc.nextInt();
        System.out.print("-商品进价：");
        double price = sc.nextDouble();
        List<Good> list = goodDao.findByName(name);
        if(list!=null&&(!list.isEmpty())){// 已经存在商品
            Good good = list.get(0);
            int temp = good.getStock();
            goodDao.updateByName(name,num+temp);
        }else{
            Good g = new Good();
            g.setGoodname(name);
            g.setGoodbrand(brand);
            g.setStock(num);
            g.setInprice(price);
            g.setAgent(user.getName());
            goodDao.addNewGoods(g);
        }
    }
}
