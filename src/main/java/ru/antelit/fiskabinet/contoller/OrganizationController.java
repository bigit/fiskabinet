package ru.antelit.fiskabinet.contoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.antelit.fiskabinet.domain.Organization;
import ru.antelit.fiskabinet.domain.UserInfo;
import ru.antelit.fiskabinet.service.OrgService;
import ru.antelit.fiskabinet.service.UserInfoService;

import javax.validation.Valid;

@Controller
public class OrganizationController {

    @Autowired
    private OrgService organizationService;
    @Autowired
    private UserInfoService userInfoService;

    @GetMapping({"/org/{id}", "/org/new"})
    public String org(@PathVariable(name = "id", required = false) Integer id,
                      @RequestParam(name = "saved", required = false, defaultValue = "false") boolean saved,
                      Model model) {

        if (!model.containsAttribute("org")) {
            Organization org;
            if (id != null) {
                org = organizationService.get(id);
            } else {
                org = new Organization();
            }
            model.addAttribute("org", org);
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
        attrs.addFlashAttribute("saved", true);
        return "redirect:/org/" + id + "?saved=true";
    }


}
