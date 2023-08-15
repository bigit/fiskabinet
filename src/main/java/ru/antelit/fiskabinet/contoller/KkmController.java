package ru.antelit.fiskabinet.contoller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
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

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class KkmController {

    @Autowired
    private KkmService kkmService;
    @Autowired
    private VendorService vendorService;
    @Autowired
    private ModelService modelService;
    @Autowired
    private TradepointService tradepointService;
    @Autowired
    private OrgService orgService;
    @Autowired
    private OfdService ofdService;

    @GetMapping({"/kkm/{id}", "/kkm/new"})
    public String index(@PathVariable(value = "id", required = false) Integer id,
                        @RequestParam(value = "tp", required = false) Integer tp,
                        @RequestParam(value = "org", required = false) Integer orgId,
                        @RequestHeader(value = "referer", required = false) String referer,
                        Model model) {
        KkmDto kkmDto = new KkmDto();

        if (!model.containsAttribute("kkmDto")) {
            if (id != null) {
                kkmDto = KkmDto.create(kkmService.get(id));
                var models = modelService.getModelsByVendorId(kkmDto.getVendorId())
                        .stream()
                        .collect(Collectors.toMap(KkmModel::getId, KkmModel::getName));
                model.addAttribute("models", models);
            } else {
                kkmDto = new KkmDto();
            }
            if (kkmDto.getOrgId() == null) {
                kkmDto.setOrgId(orgId);
            } else {
                orgId = kkmDto.getOrgId();
            }
            model.addAttribute("kkmDto", kkmDto);
        }

        Map<Integer, String> vendors = vendorService.list().stream()
                .collect(Collectors.toMap(Vendor::getId, Vendor::getName));
        model.addAttribute("vendors", vendors);

        List<Tradepoint> tpList = null;
        if (orgId != null) {
            tpList = tradepointService.listTradepointsByOrganization(orgId);
        } else if (kkmDto.getTradepointId() != null) {
            tp = kkmDto.getTradepointId();
            tpList = tradepointService.listSiblings(tp);
        } else {
            var org = orgService.get(orgId);
            tpList = Collections.singletonList(tradepointService.createDefaultTradepoint(org));
        }
        var tradepoints = tpList.stream().collect(Collectors.toMap(Tradepoint::getId, Tradepoint::getName));
        model.addAttribute("tradepoints", tradepoints);

        var ofdList = ofdService.list();
        model.addAttribute("ofdList", ofdList);

        model.addAttribute("referer", referer);
        return "kkm";
    }

    @PostMapping("/kkm/save")
    @Secured("hasAnyRole()")
    public String save(@Valid KkmDto kkmDto, BindingResult bindingResult, RedirectAttributes attrs) {

        if (bindingResult.hasErrors()) {
            attrs.addFlashAttribute("org.springframework.validation.BindingResult.kkmDto", bindingResult);
            attrs.addFlashAttribute("kkmDto", kkmDto);
            return "redirect:/kkm/" + (kkmDto.getId() != null ? kkmDto.getId() : "new" +
                    "?org=" + kkmDto.getOrgId());
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