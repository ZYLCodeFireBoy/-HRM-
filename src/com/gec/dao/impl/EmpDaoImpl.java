package com.gec.dao.impl;

import com.gec.bean.Dept;
import com.gec.bean.Employee;
import com.gec.bean.Job;
import com.gec.bean.PageModel;
import com.gec.dao.EmpDao;
import com.gec.util.JDBCUtils;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

/**
 * @ClassName EmpDaoImpl
 * @Description TODO
 * @Author 钟岩龙
 * @Date 2023/3/3 15:55
 * @Version 1.0
 */

public class EmpDaoImpl implements EmpDao {

    @Override
    public int getTotalEmpCount(Employee employee) {
        // 1=1 恒等式 2=2 3=3 为了方便后续可以动态添加条件
        StringBuffer sql = new StringBuffer("select count(0) from employee_inf where 1=1 and state=0");

        List<Object> objects = new ArrayList<>();
        ResultSet rs = null;
        try{
            // 防止空指针异常
            if(employee != null && !"".equals(employee.getName()) && employee.getName() != null){
                sql.append(" and NAME like ?");
                objects.add("%" + employee.getName() +"%");
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
    public List<Employee> findEmpByPage(Employee employee, PageModel model) {

        List<Employee> employees = new ArrayList<>();
        // 1=1 恒等式 2=2 3=3 为了方便后续可以动态添加条件
        StringBuffer sql = new StringBuffer("SELECT *, job_inf.NAME AS JOB_NAME,  dept_inf.NAME AS DEPT_NAME   FROM employee_inf LEFT JOIN job_inf ON employee_inf.job_id = job_inf.ID LEFT JOIN dept_inf ON employee_inf.dept_id = dept_inf.ID where 1=1 and employee_inf.state=0 ");

        List<Object> objects = new ArrayList<>();
        System.out.println("objects:" + objects);
        ResultSet rs = null;
        try{
            // 防止空指针异常
            if(employee != null && !"".equals(employee.getName()) && employee.getName() != null){
                sql.append(" and employee_inf.NAME LIKE ? ");
                objects.add("%" + employee.getName() +"%");
            }
            //分页查询 limit 0,2
            sql.append(" limit ").append(model.getStartRow()).append(",").append(PageModel.pageSize);
            //动态参数数组 把list转成数组
            Object[] params = objects.toArray(new Object[objects.size()]);
            rs = JDBCUtils.executeQuery(sql.toString(),params);
            while(rs.next()){
                Employee e = new Employee();
                Job j = new Job();
                Dept d = new Dept();
                copyData(rs,e,j,d);
                employees.add(e);

            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils.closeConn(JDBCUtils.getConnection(),JDBCUtils.getPsmt(),rs);
        }
        return employees;
    }

    @Override
    public void deleteEmpById(int id) {
        //软删除，逻辑删除，将状态改为1
        String sql="update  employee_inf  set state=1 where id = ?";
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
    public void addEmp(Employee employee) {
        String sql="insert into employee_inf " +
                "(NAME,CARD_ID,ADDRESS,POST_CODE,TEL,PHONE,QQ_NUM,EMAIL,SEX,PARTY,BIRTHDAY,RACE,EDUCATION,SPECIALITY,HOBBY,REMARK,CREATE_DATE,state,dept_id,job_id)" +
                "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        //参数
        Object[]params={employee.getName(),employee.getCardId(),employee.getAddress(),employee.getPostCode(),
                employee.getTel(),employee.getPhone(),employee.getQqNum(),employee.getEmail(),employee.getSex(),
                employee.getParty(),employee.getBirthday(),
                employee.getRace(),employee.getEducation(),
                employee.getSpeciality(),employee.getHobby(),
                employee.getRemark(),employee.getCreateDate(),
                0,employee.getDept().getId(),employee.getJob().getId()};

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
    public void updateEmp(Employee employee) {
        String sql="update  employee_inf set NAME=?,CARD_ID=?,ADDRESS=?,POST_CODE=?," +
                "TEL=?,PHONE=?,QQ_NUM=?,EMAIL=?,SEX=?,PARTY=?,BIRTHDAY=?," +
                "RACE=?,EDUCATION=?,SPECIALITY=?,HOBBY=?,REMARK=?," +
                "CREATE_DATE=?,state=?,dept_id=?,job_id=? where id =?";
        //参数
        Object[]params={employee.getName(),employee.getCardId()
                ,employee.getAddress(), employee.getPostCode()
                ,employee.getTel(), employee.getPhone(),
                employee.getQqNum(),employee.getEmail(),
                employee.getSex(),  employee.getParty(),
                employee.getBirthday(),employee.getRace(),
                employee.getEducation(), employee.getSpeciality(),
                employee.getHobby(),employee.getRemark(),
                employee.getCreateDate(), 0,
                employee.getDept().getId(),employee.getJob().getId(),
                employee.getId()};
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
    public Employee findEmpById(int id) {

        String sql="SELECT  *, job_inf.NAME AS JOB_NAME,dept_inf.NAME AS DEPT_NAME FROM employee_inf LEFT JOIN job_inf ON employee_inf.job_id = job_inf.ID LEFT JOIN dept_inf ON employee_inf.dept_id = dept_inf.ID WHERE employee_inf.id=? ;";
        //参数
        Object[] params = {id};

        ResultSet rs = null;
        try{
            //有个领导会检查代码（团队空闲时）
            rs = JDBCUtils.executeQuery(sql, params);
            while (rs.next()){
                //封装数据
                Employee employee = new Employee();
                Job job = new Job();
                Dept dept = new Dept();
                employee.setId(rs.getInt("id"));
                employee.setName(rs.getString("NAME"));
                employee.setCardId(rs.getString("CARD_ID"));
                employee.setAddress(rs.getString("ADDRESS"));
                employee.setPostCode(rs.getString("POST_CODE"));
                employee.setTel(rs.getString("TEL"));
                employee.setPhone(rs.getString("PHONE"));
                employee.setQqNum(rs.getString("QQ_NUM"));
                employee.setEmail(rs.getString("EMAIL"));
                employee.setSex(rs.getInt("SEX"));
                employee.setParty(rs.getString("PARTY"));
                employee.setBirthday(rs.getDate("BIRTHDAY"));
                employee.setRace(rs.getString("RACE"));
                employee.setEducation(rs.getString("EDUCATION"));
                employee.setSpeciality(rs.getString("SPECIALITY"));
                employee.setHobby(rs.getString("HOBBY"));
                employee.setRemark(rs.getString("REMARK"));
                employee.setCreateDate(rs.getDate("CREATE_DATE"));
                job.setName(rs.getString("JOB_NAME"));
                employee.setJob(job);
                dept.setName(rs.getString("DEPT_NAME"));
                employee.setDept(dept);
                return employee;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils.closeConn(JDBCUtils.getConnection(),JDBCUtils.getPsmt(),rs);
        }
        return null;
    }

    @Override
    public List<Dept> getdeptsList() {
        List<Dept> depts = new ArrayList<>();
        String sql="select * from dept_inf";
        ResultSet rs = null;

        try{
            //有个领导会检查代码（团队空闲时）
            rs = JDBCUtils.executeQuery(sql);
            while (rs.next()){
                //封装数据
                Dept dept = new Dept();
                dept.setId(rs.getInt("id"));
                dept.setName(rs.getString("name"));
                dept.setRemark(rs.getString("remark"));
                depts.add(dept);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils.closeConn(JDBCUtils.getConnection(),JDBCUtils.getPsmt(),rs);
        }
       return depts;
    }

    @Override
    public List<Job> getjobsList() {
        List<Job> jobs = new ArrayList<>();
        String sql="select * from job_inf";
        ResultSet rs = null;
        try{
            //有个领导会检查代码（团队空闲时）
            rs = JDBCUtils.executeQuery(sql);
            while (rs.next()){
                //封装数据
                Job job = new Job();
                job.setId(rs.getInt("id"));
                job.setName(rs.getString("name"));
                job.setRemark(rs.getString("remark"));
                jobs.add(job);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils.closeConn(JDBCUtils.getConnection(),JDBCUtils.getPsmt(),rs);
        }
        return jobs;
    }


    private void copyData(ResultSet rs, Employee employee, Job job,Dept dept) {
        try{
            employee.setId(rs.getInt("id"));
            employee.setName(rs.getString("NAME"));
            employee.setSex(rs.getInt("SEX"));
            employee.setPhone(rs.getString("PHONE"));
            employee.setEmail(rs.getString("EMAIL"));
            employee.setAddress(rs.getString("ADDRESS"));
            employee.setCreateDate(rs.getDate("CREATE_DATE"));
            employee.setEducation(rs.getString("EDUCATION"));
            employee.setCardId(rs.getString("CARD_ID"));

            job.setName(rs.getString("JOB_NAME"));
            employee.setJob(job);
            dept.setName(rs.getString("DEPT_NAME"));
            employee.setDept(dept);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
