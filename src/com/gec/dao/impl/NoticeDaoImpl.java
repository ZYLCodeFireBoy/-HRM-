package com.gec.dao.impl;

import com.gec.bean.Job;
import com.gec.bean.Notice;
import com.gec.bean.PageModel;
import com.gec.bean.Type;
import com.gec.dao.NoticeDao;
import com.gec.util.JDBCUtils;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName NoticeDaoImpl
 * @Description TODO
 * @Author 钟岩龙
 * @Date 2023/3/7 15:34
 * @Version 1.0
 */

public class NoticeDaoImpl implements NoticeDao {

    @Override
    public int getTotalNoticeCount(Notice notice) {
        // 1=1 恒等式 2=2 3=3 为了方便后续可以动态添加条件
        StringBuffer sql = new StringBuffer("select count(0) from notice_inf  where 1=1 ");

        List<Object> objects = new ArrayList<>();
        ResultSet rs = null;
        try{
            // 防止空指针异常
            if(notice != null && !"".equals(notice.getName()) && notice.getName() != null){
                sql.append(" and name like ?");
                objects.add("%" + notice.getName() +"%");
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
    public List<Notice> findNoticeByPage(Notice notice, PageModel model) {
        List<Notice> notices = new ArrayList<>();
        // 1=1 恒等式 2=2 3=3 为了方便后续可以动态添加条件
        StringBuffer sql = new StringBuffer("SELECT *,type_inf.name AS type_name FROM notice_inf LEFT JOIN type_inf ON notice_inf.type_id=type_inf.id WHERE 1=1 AND type_inf.state=0 ");

        List<Object> objects = new ArrayList<>();
        System.out.println("objects:" + objects);

        ResultSet rs = null;
        try{
            // 防止空指针异常
            if(notice != null && !"".equals(notice.getName()) && notice.getName() != null){
                sql.append(" and notice_inf.name like ?");
                objects.add("%" + notice.getName() +"%");
            }

            //分页查询 limit 0,2
            sql.append(" limit ").append(model.getStartRow()).append(",").append(PageModel.pageSize);


            //动态参数数组 把list转成数组
            Object[] params = objects.toArray(new Object[objects.size()]);
            rs = JDBCUtils.executeQuery(sql.toString(),params);

            while(rs.next()){
                Notice n = new Notice();
                Type type = new Type();
                n.setId(rs.getInt("id"));
                n.setName(rs.getString("name"));
                n.setContent(rs.getString("content"));
                n.setCreateDate(rs.getDate("create_date"));
                n.setModifyDate(rs.getDate("modify_date"));
                type.setName(rs.getString("type_name"));
                n.setType(type);
                notices.add(n);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils.closeConn(JDBCUtils.getConnection(),JDBCUtils.getPsmt(),rs);
        }
        return notices;
    }

    @Override
    public void deleteNoticeById(int id) {
        //软删除，逻辑删除，将状态改为1
        String sql="delete from  notice_inf  where id = ?";
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
    public void addNotice(Notice notice) {
        String sql="insert into notice_inf (name,create_date,type_id,content,user_id)values(?,?,?,?,?)";
        //参数
        Object[]params={notice.getName(),notice.getCreateDate(),notice.getType().getId(),notice.getContent(),notice.getUser().getId()};

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
    public void updateNotice(Notice notice) {
        String sql="update notice_inf set name=?,type_id=?,content=? where id =?";
        //参数
        Object[]params={notice.getName(),notice.getType().getId(),notice.getContent(),notice.getId()};
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
    public Notice findNoticeById(int id) {
        String sql="SELECT *,type_inf.name AS type_name FROM notice_inf LEFT JOIN type_inf ON notice_inf.type_id=type_inf.id WHERE 1=1 AND type_inf.state=0 AND notice_inf.id=?";
        //参数
        Object[] params = {id};

        ResultSet rs = null;
        try{
            //有个领导会检查代码（团队空闲时）
            rs = JDBCUtils.executeQuery(sql, params);
            while (rs.next()){
                //封装数据
                Notice n = new Notice();
                Type type = new Type();
                n.setId(rs.getInt("id"));
                n.setName(rs.getString("name"));
                n.setContent(rs.getString("content"));
                n.setCreateDate(rs.getDate("create_date"));
                n.setModifyDate(rs.getDate("modify_date"));
                type.setName(rs.getString("type_name"));
                n.setType(type);
                return n;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils.closeConn(JDBCUtils.getConnection(),JDBCUtils.getPsmt(),rs);
        }
        return null;
    }
}
