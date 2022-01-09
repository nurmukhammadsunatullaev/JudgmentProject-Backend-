package undp.judgment.list.judgment.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import undp.judgment.list.judgment.models.attributes.AttributeEntity;
import undp.judgment.list.judgment.models.attributes.SearchEntity;
import undp.judgment.list.judgment.models.cases.ClaimEntity;
import undp.judgment.list.judgment.services.ReportService;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.*;


@RestController
@CrossOrigin(origins = "*",allowedHeaders = "*")
@RequestMapping("/api")
public class DataController {

    private final ReportService reportService;

    @Autowired
    public DataController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping(value = "/judgements/{lang}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ClaimEntity>> getAttributes(@RequestBody @Valid SearchEntity searchEntity,@PathVariable(name="lang") String lang){
        System.out.println(searchEntity);
        return ResponseEntity.ok()
                .contentType(APPLICATION_JSON_UTF8)
                .header("Content-Type","application/json")
                .body(reportService.findAll(searchEntity,lang));
    }

    @GetMapping(value = {"/{attribute}/{lang}","/{attribute}/{lang}/{parentId}"})
    public ResponseEntity<List<AttributeEntity>> getAttributes(@PathVariable String attribute,@PathVariable(name = "lang") String lang, @PathVariable(name = "parentId", required = false) Optional<Integer> parentId){
        try{
            Integer parentIdValue=0;
            if(parentId.isPresent()){
                parentIdValue=parentId.get();
            }
            List<AttributeEntity> result=reportService.findAllAttributes(attribute, parentIdValue, lang);
            result.add(new AttributeEntity(0, "Барчаси"));
            return ResponseEntity.ok()
                    .contentType(APPLICATION_JSON_UTF8)
                    .header("Content-Type","application/json; charset=UTF-8")
                    .body(result);
        }catch (NoSuchMessageException ex){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/counts/{type}")
    public  ResponseEntity<?> getCaseCount(@PathVariable(name = "type") Integer type){
        if(type>0 && type< 5){
            return ResponseEntity.ok(reportService.findById(type));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(value = {"/statistics/{attribute}","/statistics/{attribute}/{parentId}"})
    public ResponseEntity<?> getStatistics(@PathVariable("attribute") String attribute,@PathVariable(name = "parentId", required = false) Optional<Integer> parentId){
        try{
            Integer parentIdValue=0;
            if(parentId.isPresent()){
                parentIdValue=parentId.get();
            }

            return ResponseEntity.ok()
                    .contentType(APPLICATION_JSON_UTF8)
                    .header("Content-Type","application/json; charset=UTF-8")
                    .body(reportService.findAllStatistics(attribute,parentIdValue));
        }catch (NoSuchMessageException ex){
            return ResponseEntity.notFound().build();
        }
    }

//    @PostConstruct
//    public void init(){
//        reportService.link();
//    }

}
