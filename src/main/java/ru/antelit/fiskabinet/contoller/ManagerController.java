package ru.antelit.fiskabinet.contoller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.antelit.fiskabinet.domain.Kkm;
import ru.antelit.fiskabinet.domain.Organization;
import ru.antelit.fiskabinet.service.BitrixService;
import ru.antelit.fiskabinet.service.KkmService;
import ru.antelit.fiskabinet.service.OrgService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Controller
public class ManagerController {

    @Autowired
    private OrgService orgService;
    @Autowired
    private KkmService kkmService;
    @Autowired
    private BitrixService bitrixService;

    @GetMapping("manager")
    public String index(Model model) {
        List<Organization> organizations = orgService.list();
        Map<Integer, String> urlMap = organizations.stream()
                .filter(org -> org.getSourceId() != null)
                .collect(Collectors.toMap(Organization::getId, org -> bitrixService.getCompanyUrl(org.getSourceId())));
        model.addAttribute("organizations", organizations);
        model.addAttribute("urlMap", urlMap);
        return "manager";
    }

    @PostMapping("manager")
    public String redirect() {
        return "redirect:/manager";
    }

    @GetMapping("manager/list")
    public String getList(@RequestParam("orgName") String query, Model model) {
        var orgs = orgService.findByName(query);
        var urlMap = orgs.stream()
                .collect(Collectors.toMap(
                        Organization::getId,
                        org -> bitrixService.getCompanyUrl(String.valueOf(org.getId())))
                );
        model.addAttribute("organizations", orgs);
        model.addAttribute("urlMap", urlMap);
        return "manager :: organizations";
    }

}
