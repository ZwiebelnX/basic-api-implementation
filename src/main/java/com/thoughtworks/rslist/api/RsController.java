package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.exception.CustomException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

@RestController
public class RsController {

    private static final List<RsEvent> rsList = new ArrayList<>();

    public static void initRsList() {
        rsList.clear();
        User user = new User("Sicong", "male", 22, "sicong.chen@163.com", "15800000000");
        RsEvent rsEvent = new RsEvent("第一条事件", "无", user);
        rsList.add(rsEvent);
        rsEvent = new RsEvent("第二条事件", "无", user);
        rsList.add(rsEvent);
        rsEvent = new RsEvent("第三条事件", "无", user);
        rsList.add(rsEvent);
    }

    @GetMapping("rs/list")
    public List<RsEvent> getList(@RequestParam(required = false) Integer start, @RequestParam(required = false) Integer end) throws CustomException {
        if (start == null || end == null) {
            return rsList;
        } else {
            checkIndexOutOfBound(start, "invalid request param");
            checkIndexOutOfBound(end - 1, "invalid request param");
            return rsList.subList(start - 1, end);
        }
    }

    @GetMapping("rs/{index}")
    public RsEvent getOneRsEvent(@PathVariable Integer index) throws CustomException {
        checkIndexOutOfBound(index, "invalid index");
        return rsList.get(index - 1);
    }

    @PostMapping("rs/event")
    public ResponseEntity<String> addOneRsEvent(@RequestBody @Valid RsEvent rsEvent) {
        rsList.add(rsEvent);
        UserController.registerUser(rsEvent.getUser());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("rs/{index}")
    public void modifyOneRsEvent(@PathVariable int index, @RequestBody RsEvent rsEvent) {
        if (rsEvent.getEventName() != null && !rsEvent.getEventName().isEmpty()) {
            rsList.get(index - 1).setEventName(rsEvent.getEventName());
        }
        if (rsEvent.getKeyWord() != null && !rsEvent.getEventName().isEmpty()) {
            rsList.get(index - 1).setKeyWord(rsEvent.getKeyWord());
        }
    }

    @DeleteMapping("rs/{index}")
    public void deleteOneRsEvent(@PathVariable int index) {
        rsList.remove(index - 1);
    }


    private void checkIndexOutOfBound(int index, String message) throws CustomException {
        if (index - 1 < 0 || index - 1 >= rsList.size()) {
            throw new CustomException(message);
        }
    }
}
