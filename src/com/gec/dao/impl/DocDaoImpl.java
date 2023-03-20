package com.gec.dao.impl;

import com.gec.bean.Dept;
import com.gec.bean.Document;
import com.gec.bean.PageModel;
import com.gec.bean.User;
import com.gec.dao.DocDao;
import com.gec.service.DocService;
import com.gec.util.JDBCUtils;

import javax.print.Doc;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName DocDaoImpl
 * @Description TODO
 * @Author 钟岩龙
 * @Date 2023/3/4 17:11
 * @Version 1.0
 */

public class DocDaoImpl implements DocDao {
    @Override
    public int getTotalDocCount(Document document) {
        // 1=1 恒等式 2=2 3=3 为了方便后续可以动态添加条件
        StringBuffer sql = new StringBuffer("select count(0) from document_inf where 1=1   ");

        List<Object> objects = new ArrayList<>();
        ResultSet rs = null;
        try{
            // 防止空指针异常
            if(document != null && !"".equals(document.getFileName()) && document.getFileName() != null){
                sql.append(" and filename like ?");
                objects.add("%" + document.getFileName() +"%");
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
    public List<Document> findDocsByPage(Document document, PageModel model) {
        List<Document> documents = new ArrayList<>();
        // 1=1 恒等式 2=2 3=3 为了方便后续可以动态添加条件
        StringBuffer sql = new StringBuffer("SELECT *,user_inf.loginname AS loginname FROM document_inf LEFT JOIN user_inf ON document_inf.USER_ID=user_inf.ID where 1=1 ");
        List<Object> objects = new ArrayList<>();
        System.out.println("objects:" + objects);
        ResultSet rs = null;
        try{
            // 防止空指针异常
            if(document != null && !"".equals(document.getTitle()) && document.getTitle() != null){
                sql.append(" and title like ?");
                objects.add("%" + document.getTitle() +"%");
            }
            //分页查询 limit 0,2
            sql.append(" limit ").append(model.getStartRow()).append(",").append(PageModel.pageSize);
            //动态参数数组 把list转成数组
            Object[] params = objects.toArray(new Object[objects.size()]);
            rs = JDBCUtils.executeQuery(sql.toString(),params);
            while(rs.next()){
                Document d = new Document();
                User user = new User();
                copyData(rs,d,user);
                documents.add(d);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils.closeConn(JDBCUtils.getConnection(),JDBCUtils.getPsmt(),rs);
        }
        return documents;
    }

    private void copyData(ResultSet rs, Document document,User user) {
        try{
            document.setId(rs.getInt("id"));
            document.setTitle(rs.getString("TITLE"));
            document.setFileName(rs.getString("filename"));
            document.setRemark(rs.getString("filetype"));
            document.setFileBytes(rs.getBytes("filebytes"));
            document.setRemark(rs.getString("REMARK"));
            document.setCreateDate(rs.getDate("CREATE_DATE"));
            user.setLoginname(rs.getString("loginname"));
            document.setUser(user);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void deleteDocById(int id) {
        //软删除，逻辑删除，将状态改为1
        String sql="delete from document_inf  where id = ?";
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
    public void addDoc(Document document) {
        String sql="insert into document_inf (TITLE,filename,filetype,filebytes,REMARK,CREATE_DATE,USER_ID)values(?,?,?,?,?,?,?)";
        //参数
        Object[]params={document.getTitle(),document.getFileName(),
                document.getFiletype(),document.getFileBytes(),document.getRemark(),
                document.getCreateDate(),document.getUser().getId()};
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
    public void updateDoc(Document document) {
        String sql="update  document_inf set TITLE=?,REMARK=? where id =?";
        //参数
        Object[]params={document.getTitle(),document.getRemark(),document.getId()};
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
    public Document findDocById(int id) {
        String sql="select * from document_inf where id=?";
        //参数
        Object[] params = {id};

        ResultSet rs = null;
        try{
            //有个领导会检查代码（团队空闲时）
            rs = JDBCUtils.executeQuery(sql, params);
            while (rs.next()){
                //封装数据
                Document document = new Document();
                User user = new User();
                document.setTitle(rs.getString("TITLE"));
                document.setFileName(rs.getString("filename"));
                document.setRemark(rs.getString("filetype"));
                document.setFileBytes(rs.getBytes("filebytes"));
                document.setRemark(rs.getString("REMARK"));
                document.setCreateDate(rs.getDate("CREATE_DATE"));
                return document;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils.closeConn(JDBCUtils.getConnection(),JDBCUtils.getPsmt(),rs);
        }
        return null;
    }
}
