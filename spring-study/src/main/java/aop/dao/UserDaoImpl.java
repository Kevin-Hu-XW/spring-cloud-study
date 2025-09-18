package aop.dao;

/**
 * @author Kevin
 * @date 2025/6/1 23:05
 */
public class UserDaoImpl implements UserDao{
    @Override
    public void getUser() {
        System.out.println("UserDaoImpl.getUser()");
    }
}
