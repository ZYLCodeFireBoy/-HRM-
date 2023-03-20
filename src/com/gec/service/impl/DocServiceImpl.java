package com.gec.service.impl;

import com.gec.bean.Document;
import com.gec.bean.PageModel;
import com.gec.dao.DocDao;
import com.gec.dao.impl.DocDaoImpl;
import com.gec.service.DocService;

import java.util.List;

/**
 * @ClassName DocServiceImpl
 * @Description TODO
 * @Author 钟岩龙
 * @Date 2023/3/4 17:09
 * @Version 1.0
 */

public class DocServiceImpl implements DocService {

    DocDao dao=new DocDaoImpl();

    @Override
    public int getTotalDocCount(Document document) {
        return dao.getTotalDocCount(document);
    }

    @Override
    public List<Document> findDocsByPage(Document document, PageModel model) {
        return dao.findDocsByPage(document,model);
    }

    @Override
    public void deleteDocById(int id) {
        dao.deleteDocById(id);
    }

    @Override
    public void addDoc(Document document) {
        dao.addDoc(document);
    }

    @Override
    public void updateDoc(Document document) {
        dao.updateDoc(document);
    }

    @Override
    public Document findDocById(int id) {
        return dao.findDocById(id);
    }
}
