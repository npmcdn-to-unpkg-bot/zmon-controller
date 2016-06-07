package org.zalando.zmon.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.zalando.zmon.domain.*;
import org.zalando.zmon.service.AlertService;
import org.zalando.zmon.service.ZMonService;

/**
 * Created by jmussler on 3/10/15.
 */
@Controller
@RequestMapping("/api/v1/checks")
public class CheckAPI {
    @Autowired
    ZMonService service;

    @Autowired
    AlertService alertService;

    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @RequestMapping(value = "/all-check-definitions")
    public CheckDefinitions getAllCheckDefinitions() {
        return service.getCheckDefinitions(null);
    }

    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @RequestMapping(value = "/all-active-check-definitions")
    public CheckDefinitions getAllActiveCheckDefinitions() {
        return service.getCheckDefinitions(DefinitionStatus.ACTIVE);
    }

    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @RequestMapping(value = "/check-definitions-diff")
    public CheckDefinitionsDiff getCheckDefinitionsDiff(@RequestParam(value = "lastSnapshotId") Long snapshotId) {
        return service.getCheckDefinitionsDiff(snapshotId);
    }

    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @RequestMapping(value = "/all-active-alert-definitions")
    public AlertDefinitions getAllActiveAlertDefinitions() {
        return alertService.getActiveAlertDefinitionsDiff();
    }


    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @RequestMapping(value = "/alert-definitions-diff")
    public AlertDefinitionsDiff getAlertDefinitionsDiff(@RequestParam(value = "lastSnapshotId") Long snapshotId) {
        return alertService.getAlertDefinitionsDiff(snapshotId);
    }
}
