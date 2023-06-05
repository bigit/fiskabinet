package ru.antelit.fiskabinet.contoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.antelit.fiskabinet.domain.Organization;
import ru.antelit.fiskabinet.domain.Tradepoint;
import ru.antelit.fiskabinet.service.OrgService;
import ru.antelit.fiskabinet.service.TradepointService;
import ru.antelit.fiskabinet.service.repository.TradepointRepository;

@Controller
public class TradepointController {

    @Autowired
    private TradepointService tradepointService;
    @Autowired
    private OrgService orgService;

    private Integer orgId;

    @GetMapping({"/tradepoint/{id}", "/tradepoint/new"})
    public String index(@PathVariable(value = "id", required = false) Integer id,
                        @RequestParam(value = "org", required = false) Integer org,
                        Model model) {
        Tradepoint tradepoint;
        if (id != null) {
            tradepoint = tradepointService.get(id);
        } else {
            tradepoint = new Tradepoint();
            orgId = org;
        }
        model.addAttribute("tradepoint", tradepoint);
        return "tradepoint";
    }

    @PostMapping("/tradepoint/save")
    public String save(Tradepoint tradepoint) {
        if (orgId != null) {
            tradepoint.setOrganization(orgService.get(orgId));
        }
        Integer id = tradepointService.save(tradepoint);
        return "redirect:/tradepoint/" + id;
    }
}
