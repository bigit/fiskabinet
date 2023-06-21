package ru.antelit.fiskabinet.contoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.antelit.fiskabinet.domain.Kkm;
import ru.antelit.fiskabinet.domain.Organization;
import ru.antelit.fiskabinet.service.BitrixService;
import ru.antelit.fiskabinet.service.KkmService;
import ru.antelit.fiskabinet.service.OrgService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        model.addAttribute("organizations", organizations);
        Map<Integer, List<Kkm>> kkmMap = new HashMap<>();
        for (var org : organizations) {
            List<Kkm> kkms = kkmService.getByOrganization(org);
            kkmMap.put(org.getId(), kkms);
        }
        model.addAttribute("kkmMap", kkmMap);
        return "manager";
    }

    @PostMapping("manager")
    public String redirect() {
        return "redirect:/manager";
    }

    @PostMapping("manager/import")
    public void importData() {
        bitrixService.importOrganizationsData();
    }
}
