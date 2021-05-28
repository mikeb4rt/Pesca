import java.io.File;
import java.io.IOException;

public class test {
    public static void main(String[] args) throws Exception {
        Admin admin = new Admin();
        boolean flag = false;
        while (!flag)
            try {
                admin.menu();
                flag = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
}
