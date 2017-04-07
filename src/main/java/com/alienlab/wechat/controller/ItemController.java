package com.alienlab.wechat.controller;

import com.alienlab.wechat.common.ExecResult;
import com.alienlab.wechat.entity.NamelistItem;
import com.alienlab.wechat.service.NamelistItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Wang on 2017/3/31.
 */
@Api(value="/wechatcore-api/NamelistItem",description = "白名单API")
@RestController
@RequestMapping("/wechatcore-api")
public class ItemController {
    @Autowired
    private NamelistItemService namelistItemService;

    @ApiOperation(value = "验证手机号")
    @PostMapping(value = "onlive/newUser")
    public ResponseEntity validatePhone(HttpServletRequest request){
        String phone = request.getParameter("phone");
        boolean isValidate = namelistItemService.validatePhone(phone);
        if(!isValidate){
            System.out.println("phone number wrong...");
            ExecResult er= new ExecResult(false,"该手机号码不能创建直播间！请重试");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }else{
            NamelistItem name = namelistItemService.findNamelistItemByPhone(phone);
            name.setCity(request.getParameter("city"));
            name.setCountry(request.getParameter("country"));
            name.setOpenId(request.getParameter("openId"));
            name.setPhone(phone);
            name.setProvince(request.getParameter("province"));
            name.setSex(request.getParameter("sex"));
            name.setUnionId(request.getParameter("unionId"));
            name.setHeaderimg(request.getParameter("headerimg"));
            name.setNickName(request.getParameter("nickName"));
            namelistItemService.addNamelistItem(name);
            ExecResult right=  new ExecResult(true,"用户创建成功！");
            return ResponseEntity.ok().body(right);
        }
    }
}
