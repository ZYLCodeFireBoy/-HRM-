package com.gec.service.impl;


import com.gec.bean.PageModel;
import com.gec.bean.User;
import com.gec.dao.UserDao;
import com.gec.dao.impl.UserDaoImpl;
import com.gec.service.UserService;

import java.util.List;

/**
 * @ClassName UserServiceImpl
 * @Description TODO
 * @Author 钟岩龙
 * @Date 2023/2/23 20:31
 * @Version 1.0
 */

public class UserServiceImpl implements UserService {

    UserDao dao =new UserDaoImpl();



    @Override
    public User login(String loginname, String password) {
        if(dao.login(loginname,password)!=null){
            return dao.login(loginname,password);
        }
        return null;
    }

    @Override
    public void addUser(User user) {
         dao.addUser(user);
    }

    @Override
    public void deleteUser(int id) {
        dao.deleteUser(id);
    }

    @Override
    public void updateUser(User user) {
        dao.updateUser(user);
    }

    @Override
    public List<User> findUsersByPage(User user, PageModel model) {

        return dao.findUsersByPage(user,model);
    }

    @Override
    public int getTotalUserCount(User user) {
        return dao.getTotalUserCount(user);
    }

    @Override
    public User findUserInfo(String loginname) {
            return dao.findUserInfo(loginname) ;
    }

    @Override
    public User findUserById(int id) {
        return dao.findUserById(id);
    }
}
