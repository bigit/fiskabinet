package ru.antelit.fiskabinet.contoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.antelit.fiskabinet.domain.Kkm;
import ru.antelit.fiskabinet.domain.Organization;
import ru.antelit.fiskabinet.domain.Tradepoint;
import ru.antelit.fiskabinet.domain.UserInfo;
import ru.antelit.fiskabinet.service.KkmService;
import ru.antelit.fiskabinet.service.OrgService;
import ru.antelit.fiskabinet.service.TradepointService;
import ru.antelit.fiskabinet.service.UserInfoService;
import ru.antelit.fiskabinet.service.repository.KkmRepository;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
    private KkmRepository kkmRepository;

    private List<Tradepoint> tradepointList;
    private Map<Organization, List<Tradepoint>> tradepointMap;
    private Map<Tradepoint, List<Kkm>> kkmMap;

    @GetMapping("/home")
    public String home(Model model) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        UserInfo userInfo = userInfoService.findUser(userDetails.getUsername());
        model.addAttribute("user", userInfo);

        List<Organization> organizations = orgService.getUserOrganizations(userInfo);
        model.addAttribute("organizations", organizations);

        tradepointMap = new HashMap<>();
        tradepointList = new LinkedList<>();


        for (Organization org: organizations) {
            List<Tradepoint> tradepoints = tradepointSerivce.listTradepointsByOrganization(org);
            tradepointList.addAll(tradepoints);
            tradepointMap.put(org, tradepoints);
        }
        model.addAttribute("tradepointMap",tradepointMap);

        kkmMap = new HashMap<>();

        for (Tradepoint tradepoint: tradepointList) {
//            List<Kkm> kkms = kkmService.listKkmByTradepoint(tradepoint);
            List<Kkm> kkms = kkmRepository.getKkmByTradepoint(tradepoint);
            kkmMap.put(tradepoint, kkms);
        }

        model.addAttribute("kkmMap", kkmMap);
        return "home";
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
