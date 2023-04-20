package com.nikki.jwt.security.controller;

import com.nikki.jwt.security.dto.manager.CreateManagerRequest;
import com.nikki.jwt.security.dto.manager.ManagerResponse;
import com.nikki.jwt.security.service.ManagerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/manager")
public class ManagerController {

    private final ManagerService managerService;

    @GetMapping
    public ResponseEntity<List<ManagerResponse>> getManagers(@RequestParam(defaultValue = "20") Integer count) {
        log.info("START endpoint manager (Get), request sent by principal: {}, with request param: {}",
                SecurityContextHolder.getContext().getAuthentication().getPrincipal(), count);

        List<ManagerResponse> managers = managerService.getManagers(count);

        log.info("END endpoint manager (Get), success response containing {} managers.", managers.size());
        return new ResponseEntity<>(managers, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ManagerResponse> createManager(@RequestBody CreateManagerRequest request) {
        log.info("START endpoint manager (Post), request sent by principal: {}, request: {}",
                SecurityContextHolder.getContext().getAuthentication().getPrincipal(), request);

        ManagerResponse managerResponse = managerService.createManager(request);

        log.info("END endpoint manager (Post), created: {}.", managerResponse);
        return new ResponseEntity<>(managerResponse, HttpStatus.CREATED);
    }
}
