package com.gec.test;

import com.gec.bean.PageModel;
import com.gec.bean.User;
import com.gec.dao.UserDao;
import com.gec.dao.impl.UserDaoImpl;

import java.util.List;

/**
 * @ClassName test1
 * @Description TODO
 * @Author 钟岩龙
 * @Date 2023/2/24 19:52
 * @Version 1.0
 */

public class UserTest {
    static UserDao dao=new UserDaoImpl();

    public static void main(String[] args) {
        User user = new User();
//        user.setLoginname("张");
        PageModel page=new PageModel();
        page.setPageIndex(1);

        List<User>usersByPage =dao.findUsersByPage(user,page);
        usersByPage.forEach(u -> {
            System.out.println(u);
        });
    }
}
