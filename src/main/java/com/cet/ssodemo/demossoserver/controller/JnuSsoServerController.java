package com.cet.ssodemo.demossoserver.controller;

import com.cet.ssodemo.demossoserver.entity.TicketStatusVo;
import com.cet.ssodemo.demossoserver.util.RandomUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Jinhua
 */
@Slf4j
@RestController
@RequestMapping(value = "/jnu-sso-server")
public class JnuSsoServerController {

    private final Map<String, TicketStatusVo> loginStateMap = new ConcurrentHashMap<>();

    @SneakyThrows
    @GetMapping(value = "/login")
    public TicketStatusVo login(@RequestParam(value = "service") String service, HttpServletResponse response) {
        log.info("service = {}", service);

        // 如果登录过，则返回之前的登录信息即可
        TicketStatusVo compute = this.loginStateMap.compute(service, (srv, tkSt) -> {
            if (tkSt == null) {
                // 模拟账户的票据
                String ticket = RandomUtil.randomString(30);
                // 保证服务端持有票据
                TicketStatusVo generated = new TicketStatusVo(ticket);
                log.info("服务端无票据，生成票据为：{}", generated);
                return generated;
            }
            log.info("服务端已有票据，且登录状态为：{}", tkSt);
            return tkSt;
        });
        String url = "http://localhost:24257/NGPFramework/login.aspx?ticket=";
        response.sendRedirect(url + compute.getTicket());
        return compute;
    }

    @GetMapping(value = "/logout")
    public String logout(@RequestParam(value = "service") String service) {
        log.info("service = {}", service);
        // 保证登出
        TicketStatusVo removed = this.loginStateMap.remove(service);
        if (removed != null) {
            return removed.getTicket();
        }
        return "";
    }

    @GetMapping(value = "/serviceValidate")
    public TicketStatusVo validate(@RequestParam(value = "ticket") String ticket,
                                   @RequestParam(value = "service") String service) {
        TicketStatusVo validated = this.loginStateMap.computeIfPresent(service, (tk, tkSt) -> {
            // 完成校验
            tkSt.validatePass();
            return tkSt;
        });
        return validated == null ? new TicketStatusVo(ticket) : validated;
    }
}
