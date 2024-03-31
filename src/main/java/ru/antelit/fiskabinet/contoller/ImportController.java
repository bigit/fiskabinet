package ru.antelit.fiskabinet.contoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.antelit.fiskabinet.api.bitrix.model.CompanyDto;
import ru.antelit.fiskabinet.api.bitrix.model.RequisiteDto;
import ru.antelit.fiskabinet.domain.Organization;
import ru.antelit.fiskabinet.service.BitrixService;
import ru.antelit.fiskabinet.service.OrgService;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ImportController {

    private final BitrixService bitrixService;
    private final OrgService orgService;

    private final Map<String, CompanyDto> companyCache = new HashMap<>();
    private RequisiteDto selectedRequisite;

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
    public ResponseEntity<?> importData(Organization orgDto,
                                        HttpServletResponse response) {
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
    public String lookup(String query, Model model) {
        if (query == null || query.isBlank()) {
            model.addAttribute("companies", Collections.emptyList());
        } else {
            List<CompanyDto> companies;
            if (query.matches("\\d{5,}")) {
                companies = bitrixService.findCompaniesByInn(query);
            } else {
                companies = bitrixService.findCompaniesByName(query);
            }
            companies.forEach(companyDto -> companyCache.putIfAbsent(companyDto.getId(), companyDto));
            bitrixService.setImportStatus(companies);
            model.addAttribute("companies", companies);
        }
        return "import::results";
    }

    @GetMapping("import/info")
    public String info(@RequestParam("bid") String bid, Model model) {
        var cmp = companyCache.get(bid);
        selectedRequisite = bitrixService.getRequisites(cmp);

        var org = orgService.findByInn(selectedRequisite.getInn());
        if (org == null) {
            org = new Organization();
            org.setSourceId(cmp.getId());
            org.setName(cmp.getTitle());
            org.setInn(selectedRequisite.getInn());
        } else {
            model.addAttribute("exists", true);
            model.addAttribute("source", cmp);
            model.addAttribute("req", selectedRequisite);
        }
        model.addAttribute("target", org);
        return "import::details";
    }
}