package com.arno.boot.oauth2;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.arno.boot.oauth2.pojo.UserInfo;
import com.arno.boot.oauth2.service.GiteeServiceImpl;
import com.arno.boot.oauth2.service.GithubServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: lingquan.liu@quvideo.com
 * @Date: 2021/4/21 13:04
 * @Description:
 */
@Controller
public class UserController {

    @Autowired
    private GiteeServiceImpl giteeService;

    @Autowired
    GithubServiceImpl githubService;


    @GetMapping(value = "/")
    public String index() {
        return "login";
    }

    @GetMapping(value = "/login")
    public String login() {
        return "login";
    }


    @GetMapping(value = "/oauth/login")
    public String login(@RequestParam("type") String type) {
        if (type.toUpperCase().equals(LoginType.GITHUB.toString())) {
            return "redirect:" + githubService.authorizeUri();
        }
        return "redirect:" + giteeService.authorizeUri();
    }

    @GetMapping(value = "/github/callback")
    public String callback(HttpServletRequest request, Model model) {
        String code = request.getParameter("code");
        String token = githubService.getAccessToken(code);
        model.addAttribute("token", token);
        return "index";
    }

    @GetMapping(value = "/gitee/callback")
    public String giteeCallBack(HttpServletRequest request, Model model) {
        String code = request.getParameter("code");
        String token = githubService.getAccessToken(code);
        model.addAttribute("token", token);
        return "index";
    }
}
