package ru.antelit.fiskabinet.contoller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.antelit.fiskabinet.domain.Kkm;
import ru.antelit.fiskabinet.domain.Organization;
import ru.antelit.fiskabinet.domain.UserInfo;
import ru.antelit.fiskabinet.domain.dto.OrgDto;
import ru.antelit.fiskabinet.service.BitrixService;
import ru.antelit.fiskabinet.service.KkmService;
import ru.antelit.fiskabinet.service.OrgService;
import ru.antelit.fiskabinet.service.UserInfoService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
public class OrganizationController {

    private final OrgService organizationService;
    private final KkmService kkmService;
    private final BitrixService bitrixService;
    private final UserInfoService userInfoService;

    public OrganizationController(OrgService organizationService, KkmService kkmService,
                                  BitrixService bitrixService, UserInfoService userInfoService) {
        this.organizationService = organizationService;
        this.kkmService = kkmService;
        this.bitrixService = bitrixService;
        this.userInfoService = userInfoService;
    }

    @GetMapping({"/org/{id}", "/org/new"})
    public String org(@PathVariable(name = "id", required = false) Integer id, Model model) {
        if (!model.containsAttribute("org")) {
            Organization org = id != null ? organizationService.get(id) : new Organization();

            OrgDto dto = new OrgDto();
            dto.setId(org.getId());
            dto.setName(org.getName());
            dto.setFullyImported(org.isFullyImported());
            if (org.getInn() != null)  {
                dto.setInn(org.getInn());
            }
            if (org.getSourceId() != null) {
                dto.setUrl(bitrixService.getCompanyUrl(org.getSourceId()));
            }
            model.addAttribute("org", dto);

            List<Kkm> kkmList = new ArrayList<>();
            if (org.getId() != null) {
                kkmList = kkmService.getByOrganization(org);
            }
            model.addAttribute("kkmList", kkmList);
        }
        return "org";
    }

    @PostMapping("/org")
    public String save(@Valid Organization organization, BindingResult bindingResult, RedirectAttributes attrs) {
        if (bindingResult.hasErrors()) {
            attrs.addFlashAttribute("org.springframework.validation.BindingResult.org", bindingResult);
            attrs.addFlashAttribute("org", organization);
            return "redirect:/org/" + (organization.getId() != null ? organization.getId() : "new");
        }
        UserInfo user = userInfoService.getCurrentUser();
        organization.setOwner(user);
        Integer id = organizationService.save(organization);
        return "redirect:/org/" + id;
    }
}
