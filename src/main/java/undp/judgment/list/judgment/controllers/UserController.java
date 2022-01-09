package undp.judgment.list.judgment.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import undp.judgment.list.judgment.message.request.LoginForm;
import undp.judgment.list.judgment.message.response.JwtResponse;
import undp.judgment.list.judgment.security.jwt.JwtProvider;
import undp.judgment.list.judgment.security.models.User;
import undp.judgment.list.judgment.services.BlockedService;
import undp.judgment.list.judgment.services.ReportService;
import undp.judgment.list.judgment.services.UserDaoImpl;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;

@RestController
public class UserController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserDaoImpl userRepository;

    @Autowired
    JwtProvider jwtProvider;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginForm loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateJwtToken(authentication);
        User userDetails = (User) authentication.getPrincipal();
        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getFullName(), userDetails.getAuthorities()));
    }


    @RequestMapping("/pdf/{fileName}")
    public void downloadPDFResource(HttpServletRequest request, HttpServletResponse response, @PathVariable("fileName") String fileName)  {
        String dataDirectory = "uploads/";
        Path file = Paths.get(dataDirectory, fileName);
        if (Files.exists(file)) {
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition", "attachment; filename="+fileName);
            try(OutputStream outputStream=response.getOutputStream()) {
                Files.copy(file, outputStream);
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @PostMapping("/fileUpload/{caseId}/{judgementId}")
    public HashMap<String,Object> uploadFile(@PathVariable("caseId") Long caseId,@PathVariable("judgementId") Long judgementId, @RequestParam("file") MultipartFile file) throws IOException {
        HashMap<String,Object> content=new HashMap<>();
        String filePath=new StringBuilder("uploads/")
                    .append(caseId)
                    .append("_")
                    .append(judgementId)
                    .append(".pdf")
                    .toString();
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        User userDetails= (User) authentication.getPrincipal();
        if(userRepository.canUploadFile(caseId,judgementId,userDetails.getUserId())){
            if(!Files.exists(Paths.get("uploads"))){
                Files.createDirectory(Paths.get("uploads"));
            }
            if(Files.exists(Paths.get(filePath))){
                Files.delete(Paths.get(filePath));
            }

            Files.write(Paths.get(filePath),file.getBytes());
            userRepository.updateFilePath(caseId,judgementId,filePath);
            content.put("code",200);
            content.put("message","Successfully uploaded!!!");
        }else {
            content.put("code",401);
            content.put("message","Permission denied!!!");
        }
        return content;
    }

    @GetMapping("/api/block/list")
    public ResponseEntity<?> blockList(){

        return ResponseEntity.ok(BlockedService.newInstance().findAll());

    }

}
