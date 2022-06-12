package com.cc.controller;


import com.cc.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @PostMapping("/upload")
    public Result<String> upLoadFile(MultipartFile file){
        //这里的file只是一个临时的文件存储，临时存储到某一个位置，然后待接收完毕后再转存到目标位置上，然后再把这个临时文件删除



        return null;
    }


}
