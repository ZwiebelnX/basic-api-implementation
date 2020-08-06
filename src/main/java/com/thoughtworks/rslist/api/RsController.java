package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.exception.CustomException;
import com.thoughtworks.rslist.model.dto.RsEventDto;
import com.thoughtworks.rslist.model.dto.UserDto;

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
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

@RestController
public class RsController {

    private static final List<RsEventDto> rsList = new ArrayList<>();

    public static void initAll() {
        rsList.clear();
        UserDto userDto = new UserDto("Sicong", "male", 22, "sicong.chen@163.com", "15800000000");
        RsEventDto rsEventDto = new RsEventDto("第一条事件", "无", userDto);
        rsList.add(rsEventDto);
        rsEventDto = new RsEventDto("第二条事件", "无", userDto);
        rsList.add(rsEventDto);
        rsEventDto = new RsEventDto("第三条事件", "无", userDto);
        rsList.add(rsEventDto);
        UserController.registerUser(userDto);
    }

    @GetMapping("rs/list")
    public ResponseEntity<List<RsEventDto>> getList(@RequestParam(required = false) Integer start, @RequestParam(required = false) Integer end)
        throws CustomException {
        if (start == null || end == null) {
            return new ResponseEntity<>(rsList, HttpStatus.OK);
        } else {
            checkIndexOutOfBound(start, "invalid request param");
            checkIndexOutOfBound(end - 1, "invalid request param");
            return new ResponseEntity<>(rsList.subList(start - 1, end), HttpStatus.OK);
        }
    }

    @GetMapping("rs/{index}")
    public ResponseEntity<RsEventDto> getOneRsEvent(@PathVariable Integer index) throws CustomException {
        checkIndexOutOfBound(index, "invalid index");
        return new ResponseEntity<>(rsList.get(index - 1), HttpStatus.OK);
    }

    @PostMapping("rs/event")
    public ResponseEntity<String> addOneRsEvent(@RequestBody @Valid RsEventDto rsEventDto) {
        rsList.add(rsEventDto);
        UserController.registerUser(rsEventDto.getUserDto());
        return ResponseEntity.created(URI.create("")).header("index", String.valueOf(rsList.indexOf(rsEventDto))).build();
    }

    @PutMapping("rs/{index}")
    public ResponseEntity<String> modifyOneRsEvent(@PathVariable int index, @RequestBody RsEventDto rsEventDto) {
        if (rsEventDto.getEventName() != null && !rsEventDto.getEventName().isEmpty()) {
            rsList.get(index - 1).setEventName(rsEventDto.getEventName());
        }
        if (rsEventDto.getKeyWord() != null && !rsEventDto.getEventName().isEmpty()) {
            rsList.get(index - 1).setKeyWord(rsEventDto.getKeyWord());
        }
        if (rsEventDto.getUserDto() != null) {
            rsList.get(index - 1).setUserDto(rsEventDto.getUserDto());
        }
        return new ResponseEntity<>(HttpStatus.OK);
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
