package ru.antelit.fiskabinet.contoller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.antelit.fiskabinet.domain.Kkm;
import ru.antelit.fiskabinet.domain.KkmModel;
import ru.antelit.fiskabinet.domain.Tradepoint;
import ru.antelit.fiskabinet.domain.Vendor;
import ru.antelit.fiskabinet.domain.dto.KkmDto;
import ru.antelit.fiskabinet.service.KkmService;
import ru.antelit.fiskabinet.service.ModelService;
import ru.antelit.fiskabinet.service.OfdService;
import ru.antelit.fiskabinet.service.OrgService;
import ru.antelit.fiskabinet.service.TradepointService;
import ru.antelit.fiskabinet.service.VendorService;

import jakarta.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class KkmController {

    private final KkmService kkmService;
    private final VendorService vendorService;
    private final ModelService modelService;
    private final TradepointService tradepointService;
    private final OrgService orgService;
    private final OfdService ofdService;

    public KkmController(KkmService kkmService, VendorService vendorService, ModelService modelService,
                         TradepointService tradepointService, OrgService orgService, OfdService ofdService) {
        this.kkmService = kkmService;
        this.vendorService = vendorService;
        this.modelService = modelService;
        this.tradepointService = tradepointService;
        this.orgService = orgService;
        this.ofdService = ofdService;
    }

    @GetMapping({"/kkm/{id}", "/kkm/new"})
    public String index(@PathVariable(value = "id", required = false) Integer id,
                        @RequestParam(value = "org", required = false) Integer orgId,
                        Model model) {
        KkmDto kkmDto;

        if (!model.containsAttribute("kkmDto")) {
            if (id != null) {
                kkmDto = KkmDto.create(kkmService.get(id));
            } else {
                kkmDto = new KkmDto();
            }
            model.addAttribute("kkmDto", kkmDto);
        } else {
            kkmDto = (KkmDto) model.getAttribute("kkmDto");
        }
        assert kkmDto != null;

        if (kkmDto.getOrgId() == null) {
            kkmDto.setOrgId(orgId);
        } else {
            orgId = kkmDto.getOrgId();
        }

        var models = modelService.getModelsByVendorId(kkmDto.getVendorId())
                .stream()
                .collect(Collectors.toMap(KkmModel::getId, KkmModel::getName));
        model.addAttribute("models", models);

        Map<Integer, String> vendors = vendorService.list().stream()
                .collect(Collectors.toMap(Vendor::getId, Vendor::getName));
        model.addAttribute("vendors", vendors);

        List<Tradepoint> tpList;
        if (orgId != null) {
            tpList = tradepointService.listTradepointsByOrganization(orgId);
        } else if (kkmDto.getTradepointId() != null) {
            var tp = kkmDto.getTradepointId();
            tpList = tradepointService.listSiblings(tp);
        } else {
            var org = orgService.get(kkmDto.getOrgId());
            tpList = Collections.singletonList(tradepointService.createDefaultTradepoint(org));
        }
        var tradepoints = tpList.stream().collect(Collectors.toMap(Tradepoint::getId, Tradepoint::getName));
        model.addAttribute("tradepoints", tradepoints);

        var ofdList = ofdService.list();
        model.addAttribute("ofdList", ofdList);

        return "kkm";
    }

    @PostMapping("/kkm/save")
    @Secured("hasAnyRole()")
    public String save(@Valid KkmDto kkmDto, BindingResult bindingResult, RedirectAttributes attrs) {

        if (bindingResult.hasErrors()) {
            attrs.addFlashAttribute("org.springframework.validation.BindingResult.kkmDto", bindingResult);
            attrs.addFlashAttribute("kkmDto", kkmDto);
            return "redirect:/kkm/" + (kkmDto.getId() != null ?
                    kkmDto.getId() : "new" + "?org=" + kkmDto.getOrgId());
        }

        var kkm = kkmService.fromDto(kkmDto);
        if (kkm.getInnerName().isBlank()) {
            kkm.setInnerName(kkm.getKkmModel().getVendor().getName() + " "
                    + kkm.getKkmModel().getName() + " "
                    + StringUtils.right(kkm.getSerialNumber(), 4));
        }
        Kkm saved = kkmService.save(kkm);
        return "redirect:/kkm/" + saved.getId();
    }

    @GetMapping("/models")
    @Secured("hasAnyRole()")
    public String getModels(@RequestParam("vid") Integer vendorId, Model model) {
        Map<Integer, String> models = modelService.getModelsByVendorId(vendorId).stream()
                .collect(Collectors.toMap(KkmModel::getId, KkmModel::getName));
        model.addAttribute("models", models);
        return "kkm::m_list";
    }
}