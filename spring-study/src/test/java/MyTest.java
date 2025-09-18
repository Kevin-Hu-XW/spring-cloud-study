import aop.dao.UserOracleDao;
import aop.service.UserService;
import aop.service.UserServiceImpl;

/**
 * @author Kevin
 * @date 2025/6/1 23:09
 */
public class MyTest {

    public static void main(String[] args) {

        //用户设计接触的是业务层，dao层他们不接触
        UserService  userService = new UserServiceImpl();
        //((UserServiceImpl) userService).setUserDao( new UserMysqlDao());
        ((UserServiceImpl) userService).setUserDao( new UserOracleDao());
        userService.getUser();
    }
}
