package com.nikki.jwt.security.controller;

import com.nikki.jwt.security.entity.Manager;
import com.nikki.jwt.security.service.ManagerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/manager")
public class ManagerController {

    private final ManagerService managerService;

    @GetMapping("/get")
    public List<Manager> getManagers(@RequestParam(defaultValue = "20") Integer count) {
        log.info("START endpoint manager/get, request sent by principal: {}, with request param: {}",
                SecurityContextHolder.getContext().getAuthentication().getPrincipal(), count);

        List<Manager> managers = managerService.getManagers(count);

        log.info("END endpoint manager/get, success response containing {} managers.", managers.size());
        return managers;
    }
}
