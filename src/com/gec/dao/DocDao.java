package com.gec.dao;

import com.gec.bean.Document;
import com.gec.bean.PageModel;

import java.util.List;

/**
 * @ClassName DocDao
 * @Description TODO
 * @Author 钟岩龙
 * @Date 2023/3/4 17:10
 * @Version 1.0
 */

public interface DocDao {

    int getTotalDocCount(Document document);

    List<Document> findDocsByPage(Document document, PageModel model);


    void deleteDocById(int id);

    void addDoc(Document document);

    void updateDoc(Document document);

    Document findDocById(int id);
}
