package com.ratwareid.danspro.controller;

import com.ratwareid.danspro.tool.RestBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping(value = "/job")
public class JobController extends BaseController{

    @RequestMapping(value = "/list",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> jobList() throws Exception {
        RestBuilder restBuilder = new RestBuilder("http://dev3.dansmultipro.co.id/api/recruitment/positions.json", MediaType.APPLICATION_JSON_VALUE);
        String responseJSON = restBuilder.sendGet();
        return ResponseEntity.ok().body(responseJSON);
    }

    @RequestMapping(value = "/detail/{id}",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> jobDetail(@PathVariable String id) throws Exception {
        RestBuilder restBuilder = new RestBuilder("http://dev3.dansmultipro.co.id/api/recruitment/positions/"+id, MediaType.APPLICATION_JSON_VALUE);
        String responseJSON = restBuilder.sendGet();
        return ResponseEntity.ok().body(responseJSON);
    }
}
