package com.gyx.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gyx.wiki.domain.Content;
import com.gyx.wiki.domain.Doc;
import com.gyx.wiki.domain.DocExample;
import com.gyx.wiki.mapper.ContentMapper;
import com.gyx.wiki.mapper.DocMapper;
import com.gyx.wiki.req.DocQueryReq;
import com.gyx.wiki.req.DocSaveReq;
import com.gyx.wiki.res.DocQueryResp;
import com.gyx.wiki.res.PageResp;
import com.gyx.wiki.util.CopyUtil;
import com.gyx.wiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DocService {

    private static final Logger LOG = LoggerFactory.getLogger(DocService.class);


    @Resource
    private DocMapper docMapper;

    @Resource
    private ContentMapper contentMapper;


    @Resource
    private SnowFlake snowFlake;
    public PageResp<DocQueryResp> list(DocQueryReq req){

        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();
        PageHelper.startPage( req.getPage(), req.getSize());
        List<Doc> docList = docMapper.selectByExample(docExample);

        PageInfo<Doc> pageInfo = new PageInfo<>(docList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());
//
//        List<DocResp> respList = new ArrayList<>();
//        for (Doc doc : docList) {
////            DocResp docResp = new DocResp();
////            BeanUtils.copyProperties(doc, docResp);
//
//            DocResp docResp = CopyUtil.copy(doc, DocResp.class);
//            respList.add(docResp);
//
//        }



        List<DocQueryResp> list = CopyUtil.copyList(docList, DocQueryResp.class);

        PageResp<DocQueryResp> pageResp = new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);

        return pageResp;
    }

    public List<DocQueryResp> all(Long ebookId) {
        DocExample docExample = new DocExample();
        docExample.createCriteria().andEbookIdEqualTo(ebookId);
        docExample.setOrderByClause("sort asc");
        List<Doc> docList = docMapper.selectByExample(docExample);

        List<DocQueryResp> list = CopyUtil.copyList(docList, DocQueryResp.class);


        return list;
    }

    /**
     * 保存
     */
    public void save(DocSaveReq req) {
        Doc doc = CopyUtil.copy(req, Doc.class);
        Content content = CopyUtil.copy(req, Content.class);
        if (ObjectUtils.isEmpty(req.getId())) {
            // 新增
            doc.setId(snowFlake.nextId());
            docMapper.insert(doc);

            content.setId(doc.getId());
            contentMapper.insert(content);
        } else {
            //更新
            docMapper.updateByPrimaryKey(doc);
            int count = contentMapper.updateByPrimaryKeyWithBLOBs(content);
            if (count == 0) {
                contentMapper.insert(content);
            }
        }

    }

    public void delete(Long id) {
        docMapper.deleteByPrimaryKey(id);
    }
    public void delete(List<String> ids) {
        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();
        criteria.andIdIn(ids);
        docMapper.deleteByExample(docExample);
    }


    public String findContent(Long id) {
        Content content = contentMapper.selectByPrimaryKey(id);
        return content.getContent();
    }
}
