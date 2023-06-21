package ru.antelit.fiskabinet.contoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.antelit.fiskabinet.domain.Kkm;
import ru.antelit.fiskabinet.domain.Organization;
import ru.antelit.fiskabinet.domain.Tradepoint;
import ru.antelit.fiskabinet.domain.UserInfo;
import ru.antelit.fiskabinet.service.BitrixService;
import ru.antelit.fiskabinet.service.KkmService;
import ru.antelit.fiskabinet.service.OrgService;
import ru.antelit.fiskabinet.service.TradepointService;
import ru.antelit.fiskabinet.service.UserInfoService;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Controller
public class KabinetController {

    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private OrgService orgService;
    @Autowired
    private TradepointService tradepointSerivce;
    @Autowired
    private KkmService kkmService;
    @Autowired
    private BitrixService bitrixService;

    private List<Tradepoint> tradepointList;
    private Map<Organization, List<Tradepoint>> tradepointMap;
    private Map<Tradepoint, List<Kkm>> kkmMap;

    @GetMapping("/home")
    public String home(Model model) {

        UserInfo userInfo = getCurrentUserInfo();
        model.addAttribute("user", userInfo);

        List<Organization> organizations = orgService.getUserOrganizations(userInfo);
        model.addAttribute("organizations", organizations);

        tradepointMap = new HashMap<>();
        tradepointList = new LinkedList<>();

        for (Organization org : organizations) {
            List<Tradepoint> tradepoints = tradepointSerivce.listTradepointsByOrganization(org);
            tradepointList.addAll(tradepoints);
            tradepointMap.put(org, tradepoints);
        }
        model.addAttribute("tradepointMap", tradepointMap);

        kkmMap = new HashMap<>();

        for (Tradepoint tradepoint : tradepointList) {
//            List<Kkm> kkms = kkmService.listKkmByTradepoint(tradepoint);
            List<Kkm> kkms = kkmService.getKkmByTradepoint(tradepoint);
            kkmMap.put(tradepoint, kkms);
        }

        model.addAttribute("kkmMap", kkmMap);
        return "home";
    }

    private UserInfo getCurrentUserInfo() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userInfoService.findUser(userDetails.getUsername());
    }

    @PostMapping("/kkm/task")
    @ResponseBody
    public ResponseEntity<?> serviceRequest(@RequestParam("id") Integer kkmId) throws ExecutionException, InterruptedException, TimeoutException {
        Kkm kkm = kkmService.get(kkmId);
        bitrixService.createTask(getCurrentUserInfo(), kkm);
        return ResponseEntity.ok(kkmId);
    }

    @PostMapping("/home")
    public String postHome() {
        return "redirect:home";
    }

    @GetMapping("/")
    public String redirectToHome() {
        return "redirect:home";
    }
}
