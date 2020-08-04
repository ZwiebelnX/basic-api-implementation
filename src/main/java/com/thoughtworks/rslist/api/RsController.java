package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class RsController {

    private final List<RsEvent> rsList = new ArrayList<>();

    public RsController() {
        RsEvent rsEvent = new RsEvent("第一条事件", "无");
        rsList.add(rsEvent);
        rsEvent = new RsEvent("第二条事件", "无");
        rsList.add(rsEvent);
        rsEvent = new RsEvent("第三条事件", "无");
        rsList.add(rsEvent);
    }

    @GetMapping("rs/list")
    public List<RsEvent> getList(@RequestParam(required = false) Integer start, @RequestParam(required = false) Integer end) {
        if (start == null || end == null) {
            return rsList;
        } else {
            return rsList.subList(start - 1, end);
        }
    }

    @GetMapping("rs/list/{index}")
    public RsEvent getOneRsEvent(@PathVariable Integer index) {
        return rsList.get(index - 1);
    }

    @PostMapping("rs/list")
    public void addOneRsEvent(@RequestBody RsEvent rsEvent) {
        rsList.add(rsEvent);
    }

    @DeleteMapping("rs/list/{index}")
    public void deleteOneRsEvent(@PathVariable int index) {
        rsList.remove(index - 1);
    }
}
