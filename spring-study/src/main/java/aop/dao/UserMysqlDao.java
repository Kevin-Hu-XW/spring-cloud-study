package aop.dao;

/**
 * @author Kevin
 * @date 2025/6/1 23:06
 */
public class UserMysqlDao implements UserDao{
    @Override
    public void getUser() {
        System.out.println( "MysqlDaoImpl");
    }
}
