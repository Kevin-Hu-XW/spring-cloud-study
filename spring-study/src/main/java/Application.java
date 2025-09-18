import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Kevin
 * @date 2024/12/21 11:10
 */
public class Application {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);

    }
}
