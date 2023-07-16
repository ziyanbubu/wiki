package com.gyx.wiki.controller;

import com.gyx.wiki.req.EbookReq;
import com.gyx.wiki.res.CommonResp;
import com.gyx.wiki.res.EbookResp;
import com.gyx.wiki.res.PageResp;
import com.gyx.wiki.service.EbookService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/ebook")
public class EbookController {

    @Resource
    private EbookService ebookService;

    @GetMapping ("/list")
    public CommonResp list(EbookReq req){
        CommonResp<PageResp<EbookResp>> resp = new CommonResp<>();
        PageResp<EbookResp> list = ebookService.list(req);
        resp.setContent(list);
        return resp;
    }
}
