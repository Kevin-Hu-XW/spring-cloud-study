package aop.dao;

/**
 * @author Kevin
 * @date 2025/6/1 23:28
 */
public class UserOracleDao implements UserDao{
    @Override
    public void getUser() {
        System.out.println( "UserOracleMysql");
    }
}
