package com.gec.dao.impl;

import com.gec.bean.Dept;
import com.gec.bean.PageModel;
import com.gec.bean.User;
import com.gec.dao.DeptDao;
import com.gec.util.JDBCUtils;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName DeptDapImpl
 * @Description TODO
 * @Author 钟岩龙
 * @Date 2023/3/2 10:17
 * @Version 1.0
 */

public class DeptDaoImpl implements DeptDao {

    @Override
    public int getTotalDeptCount(Dept dept) {
        // 1=1 恒等式 2=2 3=3 为了方便后续可以动态添加条件
        StringBuffer sql = new StringBuffer("select count(0) from dept_inf where 1=1 and state=0 ");

        List<Object> objects = new ArrayList<>();
        ResultSet rs = null;
        try{
            // 防止空指针异常
            if(dept != null && !"".equals(dept.getName()) && dept.getName() != null){
                sql.append(" and Name like ?");
                objects.add("%" + dept.getName() +"%");
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
    public List<Dept> findDeptsByPage(Dept dept, PageModel model) {
        List<Dept> depts = new ArrayList<>();
        // 1=1 恒等式 2=2 3=3 为了方便后续可以动态添加条件
        StringBuffer sql = new StringBuffer("select * from dept_inf where 1=1 and state=0");

        List<Object> objects = new ArrayList<>();
        System.out.println("objects:" + objects);
        ResultSet rs = null;
        try{
            // 防止空指针异常
            if(dept != null && !"".equals(dept.getName()) && dept.getName() != null){
                sql.append(" and name like ?");
                objects.add("%" + dept.getName() +"%");
            }
            //分页查询 limit 0,2
            sql.append(" limit ").append(model.getStartRow()).append(",").append(PageModel.pageSize);
            //动态参数数组 把list转成数组
            Object[] params = objects.toArray(new Object[objects.size()]);
            rs = JDBCUtils.executeQuery(sql.toString(),params);
            while(rs.next()){
                Dept d = new Dept();
                copyData(rs,d);
                depts.add(d);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils.closeConn(JDBCUtils.getConnection(),JDBCUtils.getPsmt(),rs);
        }
        return depts;
    }

    @Override
    public void deleteDeptById(int id) {
        //软删除，逻辑删除，将状态改为1
        String sql="update  dept_inf  set state=1 where id = ?";
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
    public void addDept(Dept dept) {
        String sql="insert into dept_inf (NAME,REMARK,state)values(?,?,?)";
        //参数
        Object[]params={dept.getName(),dept.getRemark(),dept.getStatus()};

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
    public void updateDept(Dept dept) {
        String sql="update  dept_inf set NAME=?,REMARK=?,state=? where id =?";
        //参数
        Object[]params={dept.getName(),dept.getRemark(),dept.getStatus(),dept.getId()};
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
    public Dept findDeptById(int id) {
        String sql="select * from dept_inf where id=?";
        //参数
        Object[] params = {id};
        ResultSet rs = null;
        try{
            //有个领导会检查代码（团队空闲时）
            rs = JDBCUtils.executeQuery(sql, params);
            while (rs.next()){
                //封装数据
                Dept dept = new Dept();
                copyData(rs,dept);
                return dept;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils.closeConn(JDBCUtils.getConnection(),JDBCUtils.getPsmt(),rs);
        }
        return null;
    }


    private void copyData(ResultSet rs, Dept dept) {
        try{
            dept.setId(rs.getInt("id"));
            dept.setName(rs.getString("name"));
            dept.setRemark(rs.getString("remark"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
