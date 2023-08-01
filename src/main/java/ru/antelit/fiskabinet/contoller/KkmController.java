package ru.antelit.fiskabinet.contoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import ru.antelit.fiskabinet.domain.Kkm;
import ru.antelit.fiskabinet.domain.KkmModel;
import ru.antelit.fiskabinet.domain.Tradepoint;
import ru.antelit.fiskabinet.domain.Vendor;
import ru.antelit.fiskabinet.domain.dto.KkmDto;
import ru.antelit.fiskabinet.service.BitrixService;
import ru.antelit.fiskabinet.service.KkmService;
import ru.antelit.fiskabinet.service.ModelService;
import ru.antelit.fiskabinet.service.TradepointService;
import ru.antelit.fiskabinet.service.VendorService;

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
    private BitrixService bitrixService;

    @GetMapping({"/kkm/{id}", "/kkm/new"})
    public String index(@PathVariable(value = "id", required = false) Integer id,
                        @RequestParam(value = "tp", required = false) Integer tp,
                        @RequestParam(value = "org", required = false) Integer org,
                        @RequestHeader(value = "referer", required = false) String referer,
                        Model model) {
        KkmDto kkmDto;

        if (id != null) {
            kkmDto = KkmDto.create(kkmService.get(id));

            var models = modelService.getModelsByVendorId(kkmDto.getVendorId())
                    .stream()
                    .collect(Collectors.toMap(KkmModel::getId, KkmModel::getName));
            model.addAttribute("models", models);

        } else {
            kkmDto = new KkmDto();
        }

        model.addAttribute("kkm", kkmDto);

        Map<Integer, String> vendors = vendorService.list().stream()
                .collect(Collectors.toMap(Vendor::getId, Vendor::getName));
        model.addAttribute("vendors", vendors);
        if (kkmDto.getTradepointId() != null) {
            tp = kkmDto.getTradepointId();
        }
        var tradepoints = tradepointService.listSiblings(tp).stream()
                    .collect(Collectors.toMap(Tradepoint::getId, Tradepoint::getName));

        model.addAttribute("tradepoints", tradepoints);
        model.addAttribute("referer", referer);
        return "kkm";
    }

    @PostMapping("/kkm/save")
    public String save(KkmDto kkmDto) {

        var kkm = kkmService.fromDto(kkmDto);
        Kkm saved = kkmService.save(kkm);;
        return "redirect:/kkm/" + saved.getId() +"?tp=" + kkmDto.getTradepointId();
    }

    @GetMapping("/models")
    public String getModels(@RequestParam("vid") Integer vendorId, Model model) {
        Map<Integer, String> models = modelService.getModelsByVendorId(vendorId).stream()
                .collect(Collectors.toMap(KkmModel::getId, KkmModel::getName));

        model.addAttribute("models", models);
        return "kkm::m_list";
    }



}
