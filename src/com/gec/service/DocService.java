package com.gec.service;


import com.gec.bean.Document;
import com.gec.bean.PageModel;

import java.util.List;

/**
 * @ClassName DocService
 * @Description TODO
 * @Author 钟岩龙
 * @Date 2023/3/4 17:08
 * @Version 1.0
 */

public interface DocService {

    int getTotalDocCount(Document document);

    List<Document> findDocsByPage(Document document, PageModel model);


    void deleteDocById(int id);

    void addDoc(Document document);

    void updateDoc(Document document);

    Document findDocById(int id);
}
