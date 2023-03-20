package com.gec.web;



import com.gec.bean.PageModel;
import com.gec.bean.User;
import com.gec.service.UserService;
import com.gec.service.impl.UserServiceImpl;
import com.gec.util.CrossRegion;
import com.gec.util.TokenUtils;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName Login
 * @Description TODO
 * @Author 钟岩龙
 * @Date 2023/2/24 10:24
 * @Version 1.0
 */
@WebServlet(urlPatterns = {"/login","/findUserByPage","/userinfo"
            ,"/saveUser","/deleteUser","/updateUser","/findUserById"})
public class UserController extends HttpServlet {

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
        UserService service = new UserServiceImpl();
        //判断
        if(servletPath.equals("/login")){
            //获得用户名密码
            String loginname = req.getParameter("loginname");
            String password = req.getParameter("password");
            User user = service.login(loginname,password);

            //判断
            if(user != null){
                //传入用户名返回一个token
                String token = TokenUtils.getToken(loginname);//错误的 user.getLoginname()
                System.out.println("生成的token:" + token);

                user.setToken(token);
                //设置到响应头
                resp.setHeader("token",user.getToken());
                //设置状态
                resp.setStatus(200);


                Map<String,Object> map = new HashMap<>();
                map.put("token",user.getToken());
                map.put("code",200);
                map.put("userId",user.getId());
                map.put("message","success");
                //组装json
                Gson gson = new Gson();
                String json = gson.toJson(map);
                //响应json到前台
                resp.getWriter().write(json);
            }else{
                resp.getWriter().write("用户名密码错误");
            }
        }else if(servletPath.equals("/userinfo")){
            System.out.println(".....进来userinfo.....");
            String loginname = req.getParameter("loginname");

            //接收token
            String token1 = req.getParameter("token");
            //检查（较验）
            boolean b = TokenUtils.checkToken(token1);
            //判断
            if(b){
                System.out.println("....有这个token.....");
                User user = service.findUserInfo(loginname);
                //组装Map
                Map<String,Object> map = new HashMap<>();
                map.put("usr",user);
                map.put("token",token1);
                map.put("code",200);
                map.put("roles","[admin]");
                map.put("name","admin");
                map.put("avatar","https://img.zcool.cn/community/01639c586c91bba801219c77f6efc8.gif");
                map.put("message","success");
                //组装json
                Gson gson = new Gson();
                String json = gson.toJson(map);
                //响应json到前台
                resp.getWriter().write(json);
            }
        }else if(servletPath.equals("/findUserByPage")){
            //获得当前页码
            String pageIndex = req.getParameter("pageIndex");
            int page = !"".equals(pageIndex) && pageIndex != null ? Integer.parseInt(pageIndex) : 1;
            //设置到PageModel
            PageModel model = new PageModel();
            model.setPageIndex(page);

            //查询用户列表
            List<User> users = null;

            //查询用户名
            String lgname = req.getParameter("lgname");
            User user = new User();
            user.setLoginname(lgname);

            //总记录数
            int count = service.getTotalUserCount(user);
            model.setTotalRecoreSum(count);
            users = service.findUsersByPage(user,model);

            Map<String,Object> map = new HashMap<>();
            map.put("model",model);
            map.put("users",users);

            //使用json返回
            Gson gson =new Gson();
            String s = gson.toJson(map);
            //响应前台
            resp.getWriter().write(s);
        }else if(servletPath.equals("/saveUser") || servletPath.equals("/updateUser")){
            String id = req.getParameter("id");
            System.out.println(id);
            String loginname = req.getParameter("loginname");
            String username = req.getParameter("username");
            String password = req.getParameter("password");

            //组装用户对象
            User user = new User();
            user.setLoginname(loginname);
            user.setCreatedate(new Date());
            user.setPassword(password);
            user.setUsername(username);
            user.setStatus(2);

            //调用保存方法
            if(id.equals("0")){
                service.addUser(user);
            }else{
                //修改
                int uid= Integer.parseInt(id);
                user.setId(uid);
                service.updateUser(user);
            }
            Map<Object, Object> map = new HashMap<>();
            map.put("code",200);
            map.put("message","success");

            //Gson包装
            Gson gson=new Gson();
            String json=gson.toJson(map);
            resp.getWriter().write(json);

        }else if(servletPath.equals("/findUserById")){
            int id = Integer.parseInt(req.getParameter("id"));
            //service
            User user = service.findUserById(id);
            Map<String,Object> map = new HashMap<>();
            map.put("code",200);
            map.put("users",user);

            //Gson
            Gson gson = new Gson();
            String s = gson.toJson(map);
            resp.getWriter().write(s);
        }else if(servletPath.equals("/deleteUser")){
            int id = Integer.parseInt(req.getParameter("id"));
            //service
            service.deleteUser(id);
            Map<String,Object> map = new HashMap<>();
            map.put("code",200);
            map.put("message","delete success");
            //Gson
            Gson gson = new Gson();
            String s = gson.toJson(map);
            resp.getWriter().write(s);
        }
    }
}


