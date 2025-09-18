package com.controller;


import com.alibaba.fastjson.JSONObject;
import com.pojo.Person;
import com.pojo.ReqTest;
import com.pojo.Res;
import com.service.TestService;
import com.utils.HttpClientUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;


@RestController
@Log4j2
public class WxController {

    @Value("${wx.appId}")
    String appId;
    @Value("${wx.secret}")
    String secret;
    @Value("${wx.redirect_uri}")
    String redirect_uri;
    @Autowired
    private TestService testService;
    @GetMapping("/getQRCode")
    public void getQRCode(HttpServletResponse response) throws IOException {
        //  直接请求微信地址就行,%s相当于占位符
        String url = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";
        try {
            URLEncoder.encode(redirect_uri,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.sendRedirect(String.format(url,appId,redirect_uri,"uds"));
    }

    @RequestMapping(value = "/reg-user",method = {RequestMethod.POST,RequestMethod.GET})
    public void login(@RequestParam String code, @RequestParam String state, HttpServletResponse response){

        /**
         * 通过code获取access_token
         */

        try {
            String baseAccessTokenUrl =
                    "https://api.weixin.qq.com/sns/oauth2/access_token" +
                            "?appid=%s" +
                            "&secret=%s" +
                            "&code=%s" +
                            "&grant_type=authorization_code";
            String accessTokenUrl = String.format(baseAccessTokenUrl, appId, secret, code);
            String accessTokenInfo = HttpClientUtils.get(accessTokenUrl);
            JSONObject jsonObject = JSONObject.parseObject(accessTokenInfo);
            //json对象转Map
            Map<String,Object> map = (Map<String,Object>)jsonObject;
            String accessToken = (String) map.get("access_token");
            log.info("accessToken:"+accessToken);
            String openid = (String) map.get("openid");
            log.info("openid:"+openid);
            String unionid = (String) map.get("unionid");
            log.info("unionid:"+unionid);
            /**
             *  通过accessToken、openid获取用户信息
             */
            String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                    "?access_token=%s" +
                    "&openid=%s";
            String userInfoUrl = String.format(baseUserInfoUrl, accessToken, openid);
            String userInfo = HttpClientUtils.get(userInfoUrl);
            JSONObject userInfoObject = JSONObject.parseObject(userInfo);
            //昵称
            String nickname = (String) userInfoObject.get("nickname");
            log.info("nickname:"+nickname);
            //头像
            String headimgurl = (String) userInfoObject.get("headimgurl");
            log.info("headimgurl:"+headimgurl);
            response.sendRedirect(headimgurl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/test",method = RequestMethod.POST)
    @ResponseBody
    public Res<Person> test(@RequestBody Person person){
        Res<Person> res = new Res<>();
        res.setData(person);
        return res;
    }


    @RequestMapping(value = "/test1",method = RequestMethod.GET)
    public void test(){
        testService.demo();
    }
    @RequestMapping(value = "/test2",method = RequestMethod.GET)
    public void test2(){
        testService.resttemplateDemo();
    }
}
