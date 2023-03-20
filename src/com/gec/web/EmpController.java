package com.gec.web;



import com.gec.bean.Dept;
import com.gec.bean.Employee;
import com.gec.bean.Job;
import com.gec.bean.PageModel;

import com.gec.service.EmpService;

import com.gec.service.impl.EmpServiceImpl;
import com.gec.util.CrossRegion;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName EmployeeController
 * @Description TODO
 * @Author 钟岩龙
 * @Date 2023/3/3 15:50
 * @Version 1.0
 */
@WebServlet(urlPatterns = {"/findEmpByPage", "/saveEmp",
        "/deleteEmp","/updateEmp","/findEmpById","/getDeptsAndJobs"})
public class EmpController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //设置请求编码
        req.setCharacterEncoding("utf-8");
        resp.setCharacterEncoding("utf-8");
        //跨域设置
        CrossRegion.setCross(resp);
        //如果是get,设置200ok
        
        if ("OPTIONS".equals(req.getMethod())) {
            resp.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        //请求的servlet 路径
        String servletPath = req.getServletPath();
        //service对象
        EmpService service=new EmpServiceImpl();
        if(servletPath.equals("/findEmpByPage")){
            List<Dept> depts = service.getdeptsList();
            List<Job> jobs = service.getjobsList();
            //获得当前页码
            String pageIndex = req.getParameter("pageIndex");
            int page = !"".equals(pageIndex) && pageIndex != null ? Integer.parseInt(pageIndex) : 1;
            //设置到PageModel
            PageModel model = new PageModel();
            model.setPageIndex(page);
            //查询用户列表
            List<Employee> employees = null;
            //提供部门名称查询
            String Ename = req.getParameter("Ename");
            Employee employee = new Employee();
            employee.setName(Ename);
            //总记录数
            int count=service.getTotalEmpCount(employee);
            model.setTotalRecoreSum(count);
            employees=service.findEmpByPage(employee,model);

            Map<String,Object> map=new HashMap<>();
            map.put("model",model);
            map.put("emps",employees);
            map.put("depts",depts);
            map.put("jobs",jobs);

            //使用json返回
            Gson gson =new Gson();
            String s = gson.toJson(map);
            //响应前台
            resp.getWriter().write(s);
        }else if(servletPath.equals("/deleteEmp")){
            int id = Integer.parseInt(req.getParameter("id"));
            //service
            service.deleteEmpById(id);
            Map<String,Object> map = new HashMap<>();
            map.put("code",200);
            map.put("message","delete success");
            //Gson
            Gson gson = new Gson();
            String s = gson.toJson(map);
            resp.getWriter().write(s);
        }else if(servletPath.equals("/saveEmp")|| servletPath.equals("/updateEmp")) {
            int i=0;
            String id = req.getParameter("id");
            String name = req.getParameter("name");
            String cardId = req.getParameter("cardId");
            String address = req.getParameter("address");
            String postCode = req.getParameter("postCode");
            String tel = req.getParameter("tel");
            String phone = req.getParameter("phone");
            String qqNum = req.getParameter("qqNum");
            String email = req.getParameter("email");
            String checkedSexValue = req.getParameter("checkedSexValue");
            if (checkedSexValue.equals("男")){
                 i=1;
            }else if(checkedSexValue.equals("女")){
                i=2;
            }
            String party = req.getParameter("party");

            String valueDate = req.getParameter("valueDate");
            String[] ts = valueDate.split("T");
            String ts_zhuzhuang=ts[0]+" "+ts[1];
            String birthdays = ts_zhuzhuang.substring(0, ts_zhuzhuang.length() - 5);
            System.out.println(birthdays);

            String race = req.getParameter("race");
            String education = req.getParameter("education");
            String speciality = req.getParameter("speciality");
            String hobby = req.getParameter("hobby");
            String remark = req.getParameter("remark");
            String department = req.getParameter("depts");
            String job = req.getParameter("jobs");
            //组装用户对象
            Employee employee = new Employee();
            Dept d = new Dept();
            Job j = new Job();
            employee.setName(name);
            employee.setCardId(cardId);
            employee.setAddress(address);
            employee.setPostCode(postCode);
            employee.setTel(tel);
            employee.setPhone(phone);
            employee.setQqNum(qqNum);
            employee.setEmail(email);
            employee.setSex(i);
            System.out.println(employee.getSex());
            employee.setParty(party);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date newTime = format.parse(birthdays);
                employee.setBirthday(newTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            employee.setRace(race);
            employee.setEducation(education);
            employee.setSpeciality(speciality);
            employee.setHobby(hobby);
            employee.setRemark(remark);
            employee.setCreateDate(new Date());
            d.setId(Integer.parseInt(department));
            j.setId(Integer.parseInt(job));
            employee.setDept(d);
            employee.setJob(j);
            //调用保存方法
            if (id.equals("0")) {
                service.addEmp(employee);
            } else {
                //修改
                int uid = Integer.parseInt(id);
                employee.setId(uid);
                service.updateEmp(employee);
            }
            Map<Object, Object> map = new HashMap<>();
            map.put("code", 200);
            map.put("message", "add success");
            //Gson包装
            Gson gson = new Gson();
            String json = gson.toJson(map);
            resp.getWriter().write(json);
        }else if(servletPath.equals("/findEmpById")){
            List<Dept> depts = service.getdeptsList();
            List<Job> jobs = service.getjobsList();
            int id = Integer.parseInt(req.getParameter("id"));
            //service
            Employee emp = service.findEmpById(id);
            Map<String,Object> map = new HashMap<>();
            map.put("code",200);
            map.put("depts",depts);
            map.put("jobs",jobs);
            map.put("emp",emp);
            //Gson
            Gson gson = new Gson();
            String s = gson.toJson(map);
            resp.getWriter().write(s);
        }else if(servletPath.equals("/getDeptsAndJobs")){
            List<Dept> depts = service.getdeptsList();
            List<Job> jobs = service.getjobsList();
            Map<String,Object> map = new HashMap<>();
            map.put("code",200);
            map.put("depts",depts);
            map.put("jobs",jobs);
            //Gson
            Gson gson = new Gson();
            String s = gson.toJson(map);
            resp.getWriter().write(s);
        }
    }
}
