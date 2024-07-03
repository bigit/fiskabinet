package ru.antelit.fiskabinet.contoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.antelit.fiskabinet.api.bitrix.model.CompanyInfo;
import ru.antelit.fiskabinet.domain.Organization;
import ru.antelit.fiskabinet.service.BitrixService;
import ru.antelit.fiskabinet.service.OrgService;

import jakarta.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ImportController {

    private final BitrixService bitrixService;
    private final OrgService orgService;

    private final Map<String, CompanyInfo> companyCache = new HashMap<>();

    @Autowired
    public ImportController(BitrixService bitrixService, OrgService orgService) {
        this.bitrixService = bitrixService;
        this.orgService = orgService;
    }

    @GetMapping("import")
    public String show() {
        return "import";
    }

    @PostMapping("import/save")
    public ResponseEntity<?> importData(Organization orgDto, HttpServletResponse response) {
        var org = new Organization();
        org.setId(orgDto.getId());
        org.setName(orgDto.getName());
        org.setInn(orgDto.getInn());
        org.setSourceId(orgDto.getSourceId());
        var savedId = orgService.save(org);
        response.setHeader("HX-Trigger", "saved");
        return ResponseEntity.ok(savedId);
    }

    @GetMapping("import/lookup")
    public String lookup(@RequestParam("query") String query, Model model) {
        if (query == null || query.isBlank()) {
            model.addAttribute("companies", Collections.emptyList());
        } else {
            List<CompanyInfo> companies;
            if (query.matches("\\d{5,}")) {
                companies = bitrixService.findCompaniesByRequisiteInn(query);
                companies.forEach(company -> companyCache.putIfAbsent(company.getSourceId(), company));
            } else {
                companies = bitrixService.findCompaniesByName(query);
                companies.forEach(company -> companyCache.putIfAbsent(company.getSourceId(), company));

                var reqCompanies = bitrixService.findCompaniesByRequisiteName(query);
                for (var req: reqCompanies) {
                    var key = req.getSourceId();
                    if (companyCache.containsKey(key)) {
                        var cmp = companyCache.get(key);
                        if (!cmp.getMessages().isEmpty()) {
                            companies.remove(cmp);
                            companyCache.remove(key);
                        }
                    }
                    companies.add(req);
                    companyCache.put(key, req);
                }
            }
            model.addAttribute("companies", companies);
        }
        return "import::results";
    }

    @GetMapping("import/info")
    public String info(@RequestParam("source_id") String sourceId, Model model) {
        var cmp = companyCache.get(sourceId);
        CompanyInfo target = new CompanyInfo();
        Organization org = null;
        if (cmp.getInn() != null) {
            org = orgService.findByInn(cmp.getInn());
            if (org != null) {
                target.addMessage("Организация с таким ИНН уже существует", false);
            }
        } if (org == null && !cmp.getName().isBlank()) {
            var list = orgService.findByName(cmp.getName());
            if (!list.isEmpty()) {
                org = list.get(0);
                target.addMessage("Организация с таким именем уже существует", false);
            }
        }
        if (org == null) {
            target.setSourceId(cmp.getSourceId());
            target.setName(cmp.getName());
            target.setInn(cmp.getInn());
            target.setMessages(cmp.getMessages());
        } else {
            target.setId(org.getId());
            target.setSourceId(org.getSourceId());
            target.setName(org.getName());
            target.setInn(org.getInn());
            model.addAttribute("source", cmp);
        }
        model.addAttribute("target", target);
        return "import::details";
    }
}