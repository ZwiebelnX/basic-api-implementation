package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.exception.CustomException;
import com.thoughtworks.rslist.model.dto.RsEventDto;
import com.thoughtworks.rslist.service.RsEventService;

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

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

@RestController
public class RsController {

    //    private static final List<RsEventDto> rsList = new ArrayList<>();
    private final RsEventService rsEventService;

    public RsController(RsEventService rsEventService) {
        this.rsEventService = rsEventService;
    }
    //
    //    public static void initAll() {
    //        rsList.clear();
    //        UserDto userDto = new UserDto("Sicong", "male", 22, "sicong.chen@163.com", "15800000000");
    //        RsEventDto rsEventDto = new RsEventDto("第一条事件", "无", userDto);
    //        rsList.add(rsEventDto);
    //        rsEventDto = new RsEventDto("第二条事件", "无", userDto);
    //        rsList.add(rsEventDto);
    //        rsEventDto = new RsEventDto("第三条事件", "无", userDto);
    //        rsList.add(rsEventDto);
    //        UserController.registerUser(userDto);
    //    }

    @GetMapping("rs/list")
    public ResponseEntity<List<RsEventDto>> getList(@RequestParam(required = false) Integer start, @RequestParam(required = false) Integer end) {
        return ResponseEntity.ok(rsEventService.getList(start, end));
    }

    @GetMapping("rs/{id}")
    public ResponseEntity<RsEventDto> getOneRsEvent(@PathVariable Integer id) throws CustomException {
        return new ResponseEntity<>(rsEventService.getEvent(id), HttpStatus.OK);
    }

    @PostMapping("rs/event")
    public ResponseEntity<String> addOneRsEvent(@RequestBody @Valid RsEventDto rsEventDto) throws CustomException {
        Integer id = rsEventService.createRsEvent(rsEventDto);
        return ResponseEntity.created(URI.create("")).header("index", String.valueOf(id)).build();
    }

    @PutMapping("rs/{id}")
    public ResponseEntity<String> modifyOneRsEvent(@PathVariable int id, @RequestBody RsEventDto rsEventDto) throws CustomException {
        rsEventService.modifyEvent(id, rsEventDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("rs/{id}")
    public ResponseEntity<String> deleteOneRsEvent(@PathVariable int id) {
        rsEventService.deleteEvent(id);
        return ResponseEntity.ok().build();
    }
}
