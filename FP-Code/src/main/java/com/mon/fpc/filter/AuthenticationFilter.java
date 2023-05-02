package com.mon.fpc.filter;

import cn.hutool.core.util.ReUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.mon.fpc.core.BaseController;
import com.mon.fpc.core.Resp;
import com.mon.fpc.utils.JwtUtil;
import com.mon.fpc.utils.LoginUserHolder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

@Component  //开启验证
public class AuthenticationFilter implements Filter {

    @Resource
    JwtUtil jwtUtils;

    private Set<String> whiteUrl = new HashSet<>();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        Map<String,String> map = new HashMap<>();
        String url = ((HttpServletRequest) servletRequest).getRequestURI();
        if (url != null) {

            whiteUrl.add("/resource/**");
            whiteUrl.add("/swagger-ui.html");
            whiteUrl.add("/swagger-resources/**");
            whiteUrl.add("/v2/**");
            whiteUrl.add("/webjars/**");
            whiteUrl.add("/doc.html");
            whiteUrl.add("/swagger-resources");
            whiteUrl.add("/error");
            whiteUrl.add("/favicon.ico");
            whiteUrl.add("/user/AccountLogin");
            whiteUrl.add("/user/Register");

            //登录请求直接放行swagger-ui.html
            for (String s : whiteUrl) {
                if (ReUtil.isMatch(getRegPath(s), url)) {

                    filterChain.doFilter(servletRequest, servletResponse);
                    return;
                }
            }
            //其他请求验证token
            String token = ((HttpServletRequest) servletRequest).getHeader("token");
            Resp res = new Resp();
            int key = 1;//key: 1 ，客户端路径；2，管理端路径携带client；0，管理端路径不携带client，直接拒绝
            if (StringUtils.isNotBlank(token)) {
                //token验证结果
                int verify = jwtUtils.verify(token);
                if (verify != 1) {
                    //验证失败
                    if (verify == 2) {
                        res = BaseController.error(Resp.RespEnum.NO_LOGIN);
                        // map.put("errorMsg","No permission!");//token已过期
                    } else if (verify == 0) {
                        res = BaseController.error(Resp.RespEnum.NO_LOGIN);
                        //map.put("errorMsg","No permission!");//用户信息验证失败
                    } else if (verify == 8) {
                        res = BaseController.error(Resp.RespEnum.NO_LOGIN, "该账户在其他设备登录");
                        //当帐号重复登录时，不允许上一个帐号进行权限操作
                    } else if (verify == 9) {
                        res = BaseController.error(Resp.RespEnum.NO_LOGIN, "登录已过期，请重新登录");
                    }
                } else if (verify == 1) {
                    //验证成功，放行
                    filterChain.doFilter(servletRequest, servletResponse);
                    return;
                }
            } else if (ReUtil.isMatch(getRegPath("/Product/Detail"), url) || ReUtil.isMatch(getRegPath("/Issuer/Detail"), url) || ReUtil.isMatch(getRegPath("/Home/Info"), url)) {
                LoginUserHolder.remove();
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            } else {
                LoginUserHolder.remove();
                //token为空的返回
                res = BaseController.error(Resp.RespEnum.NO_LOGIN);
                //map.put("errorMsg","No permission!");//未携带token信息
            }

            JSONObject jsonObject = new JSONObject(res);
            servletResponse.setContentType("application/json");
            //设置响应的编码
            servletResponse.setCharacterEncoding("utf-8");
            //响应
            PrintWriter pw = servletResponse.getWriter();
            pw.write(jsonObject.toString());
            pw.flush();
            pw.close();
        }
    }


    /**
     * 将通配符表达式转化为正则表达式
     * * 表示匹配0或多个不是/的字符
     * <p>
     * ** 表示匹配0或多个任意字符
     * <p>
     * ? 表示匹配1个任意字符
     *
     * @param path
     * @return
     */
    private static String getRegPath(String path) {
        char[] chars = path.toCharArray();
        int len = chars.length;
        StringBuilder sb = new StringBuilder();
        boolean preX = false;
        for (int i = 0; i < len; i++) {
            if (chars[i] == '*') {//遇到*字符
                if (preX) {//如果是第二次遇到*，则将**替换成.*
                    sb.append(".*");
                    preX = false;
                } else if (i + 1 == len) {//如果是遇到单星，且单星是最后一个字符，则直接将*转成[^/]*
                    sb.append("[^/]*");
                } else {//否则单星后面还有字符，则不做任何动作，下一把再做动作
                    preX = true;
                }
            } else {//遇到非*字符
                if (preX) {//如果上一把是*，则先把上一把的*对应的[^/]*添进来
                    sb.append("[^/]*");
                    preX = false;
                }
                if (chars[i] == '?') {//接着判断当前字符是不是?，是的话替换成.
                    sb.append('.');
                } else {//不是?的话，则就是普通字符，直接添进来
                    sb.append(chars[i]);
                }
            }
        }
        return sb.toString();
    }

}
