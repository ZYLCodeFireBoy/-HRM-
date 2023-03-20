package com.gec.dao;



import com.gec.bean.PageModel;
import com.gec.bean.User;

import java.util.List;

/**
 * @ClassName UserDao
 * @Description TODO
 * @Author 钟岩龙
 * @Date 2023/2/23 20:11
 * @Version 1.0
 */

public interface UserDao {

    /**
     * @description 用户登录
     * @return
     * @author 钟岩龙
     * @since 2023/2/23 20:13
     */
    User login(String loginname, String password);
    /**
     * @description 添加用户
     * @return
     * @author 钟岩龙
     * @since 2023/2/23 20:13
     */
    void addUser(User user);

    /**
     * @description 删除用户
     * @return
     * @author 钟岩龙
     * @since 2023/2/23 20:15
     */
    void deleteUser(int id);

    /**
     * @description 修改用户
     * @return
     * @author 钟岩龙
     * @since 2023/2/23 20:14
     */
    void updateUser(User user);


    /**
     * @description 分页查询
     * @return
     * @author 钟岩龙
     * @since 2023/2/23 20:15
     */
    List<User>findUsersByPage(User user, PageModel model);

    int getTotalUserCount(User user);


    public User findUserInfo(String loginname);

    User findUserById(int id);
}
