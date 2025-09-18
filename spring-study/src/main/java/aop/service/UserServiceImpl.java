package aop.service;

import aop.dao.UserDao;
import aop.dao.UserDaoImpl;

/**
 * @author Kevin
 * @date 2025/6/1 23:08
 */
public class UserServiceImpl implements UserService{


    /**
     * 控制反转
     * 1、之前，程序员主动实现创建对象！控制权在程序员手上
     * 2、使用了set注入之后，程序不在具有主动性，而是变成了被动的接收对象
     * 这种思想，从本质上解决了问题，程序员不需要再取管理对象的创建了。系统耦合性大大降低，更加专注于业务实现上！
     */

    //private UserDao userDao = new UserMysqlDao();

    private UserDao userDao;

    //利用set实现动态值的注入
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
    @Override
    public Void getUser() {
        userDao.getUser();
        return null;
    }
}
