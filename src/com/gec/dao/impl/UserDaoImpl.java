package com.gec.dao.impl;

import com.gec.bean.PageModel;
import com.gec.bean.User;
import com.gec.dao.UserDao;
import com.gec.util.JDBCUtils;

import java.sql.ResultSet;

import java.util.ArrayList;

import java.util.List;

/**
 * @ClassName UserDaoImpl
 * @Description TODO
 * @Author 钟岩龙
 * @Date 2023/2/23 20:30
 * @Version 1.0
 */

public class UserDaoImpl implements UserDao {
    @Override
    public User login(String loginname, String password) {
        String sql= "select * from user_inf where loginname=? and password=? and status=2 ";
        //参数
        Object[] params = {loginname,password};

        ResultSet rs = null;
        try{
            //有个领导会检查代码（团队空闲时）
            rs = JDBCUtils.executeQuery(sql, params);
            while (rs.next()){
                //封装数据
                User user = new User();
                copyData(rs,user);
                return user;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils.closeConn(JDBCUtils.getConnection(),JDBCUtils.getPsmt(),rs);
        }
        return null;
    }



    @Override
    public void addUser(User user) {
        String sql="insert into user_inf (loginname,password,status,createdate,username)values(?,?,?,?,?)";
        //参数
        Object[]params={user.getLoginname(),user.getPassword(),user.getStatus(),user.getCreatedate(),user.getUsername()};

        ResultSet rs=null;
        try{
            JDBCUtils.excuteUpdate(sql,params);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils.closeConn(JDBCUtils.getConnection(), JDBCUtils.getPsmt(),rs);
        }
    }

    @Override
    public void updateUser(User user) {
        String sql="update  user_inf set loginname=?,password=?,username=? where id =?";
        //参数
        Object[]params={user.getLoginname(),user.getPassword(),user.getUsername(),user.getId()};
        ResultSet rs=null;
        try{
            JDBCUtils.excuteUpdate(sql,params);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils.closeConn(JDBCUtils.getConnection(), JDBCUtils.getPsmt(),rs);
        }
    }

    @Override
    public void deleteUser(int id) {
        //软删除，逻辑删除，将状态改为1
        String sql="update  user_inf  set status=1 where id = ?";
        //参数
        Object[]params={id};
        ResultSet rs=null;
        try{
            JDBCUtils.excuteUpdate(sql,params);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils.closeConn(JDBCUtils.getConnection(), JDBCUtils.getPsmt(),rs);
        }
    }

    @Override
    public List<User> findUsersByPage(User user, PageModel model) {

        List<User> users = new ArrayList<>();
        // 1=1 恒等式 2=2 3=3 为了方便后续可以动态添加条件
        StringBuffer sql = new StringBuffer("select * from user_inf where 1=1 and status=2");

        List<Object> objects = new ArrayList<>();
        System.out.println("objects:" + objects);

        ResultSet rs = null;
        try{
            // 防止空指针异常
            if(user != null && !"".equals(user.getLoginname()) && user.getLoginname() != null){
                sql.append(" and loginname like ?");
                objects.add("%" + user.getLoginname() +"%");
            }
            //分页查询 limit 0,2
            sql.append(" limit ").append(model.getStartRow()).append(",").append(PageModel.pageSize);
            //动态参数数组 把list转成数组
            Object[] params = objects.toArray(new Object[objects.size()]);
            rs = JDBCUtils.executeQuery(sql.toString(),params);

            while(rs.next()){
                User u = new User();
                copyData(rs,u);
                users.add(u);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils.closeConn(JDBCUtils.getConnection(),JDBCUtils.getPsmt(),rs);
        }
        return users;
    }

    @Override
    public int getTotalUserCount(User user) {
        // 1=1 恒等式 2=2 3=3 为了方便后续可以动态添加条件
        StringBuffer sql = new StringBuffer("select count(0) from user_inf where 1=1 ");

        List<Object> objects = new ArrayList<>();
        ResultSet rs = null;
        try{
            // 防止空指针异常
            if(user != null && !"".equals(user.getLoginname()) && user.getLoginname() != null){
                sql.append(" and loginname like ?");
                objects.add("%" + user.getLoginname() +"%");
            }

            //分页查询 limit 0,2
            //动态参数数组 把list转成数组
            Object[] params = objects.toArray(new Object[objects.size()]);
            rs = JDBCUtils.executeQuery(sql.toString(),params);

            while(rs.next()){
                //返回  统计数
                return rs.getInt(1);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils.closeConn(JDBCUtils.getConnection(),JDBCUtils.getPsmt(),rs);
        }
        return 0;
    }

    @Override
    public User findUserInfo(String loginname) {
        String sql = "select * from user_inf where loginname=?";
        //参数
        Object[] params = {loginname};

        ResultSet rs = null;
        try {
            //有个领导会检查代码（团队空闲时）
            rs = JDBCUtils.executeQuery(sql, params);
            while (rs.next()) {
                //封装数据
                User user = new User();
                copyData(rs, user);
                return user;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeConn(JDBCUtils.getConnection(), JDBCUtils.getPsmt(), rs);
        }
        return null;
    }

    @Override
    public User findUserById(int id) {
        String sql="select * from user_inf where id=?";
        //参数
        Object[] params = {id};

        ResultSet rs = null;
        try{
            //有个领导会检查代码（团队空闲时）
            rs = JDBCUtils.executeQuery(sql, params);
            while (rs.next()){
                //封装数据
                User user = new User();
                copyData(rs,user);
                return user;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils.closeConn(JDBCUtils.getConnection(),JDBCUtils.getPsmt(),rs);
        }
        return null;
    }


    private void copyData(ResultSet rs, User user) {
        try{
            user.setId(rs.getInt("id"));
            user.setLoginname(rs.getString("loginname"));
            user.setCreatedate(rs.getDate("createdate"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setStatus(rs.getInt("status"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}