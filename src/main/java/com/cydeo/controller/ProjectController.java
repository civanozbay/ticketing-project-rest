package com.cydeo.controller;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.entity.ResponseWrapper;
import com.cydeo.service.ProjectService;
import com.cydeo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/project")
public class ProjectController {
    private final ProjectService projectService;
    private final UserService userService;

    public ProjectController(ProjectService projectService, UserService userService) {
        this.projectService = projectService;
        this.userService = userService;
    }
    @GetMapping("/create")
    public ResponseEntity<ResponseWrapper> getProjects() {
        List<ProjectDTO> projectDTOS = projectService.listAllProjectDetails();
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseWrapper("projects retrieved",projectDTOS,HttpStatus.OK));

    }
    @PostMapping("/create")
    public ResponseEntity<ResponseWrapper> createProject(@RequestBody ProjectDTO projectDTO) {
        projectService.save(projectDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseWrapper("Project is successfully created",HttpStatus.CREATED));

    }

//    @PostMapping("/create")
//    public String insertProject(@ModelAttribute("project") ProjectDTO project, BindingResult bindingResult, Model model) {
//
//    }


    @GetMapping("/delete/{projectcode}")
    public ResponseEntity<ResponseWrapper> deleteProject(@PathVariable("projectcode") String projectcode) {
        projectService.delete(projectcode);
        return ResponseEntity.ok(new ResponseWrapper("Project is successfully deleted",HttpStatus.OK));
    }

    @GetMapping("/complete/{projectcode}")
    public ResponseEntity<ResponseWrapper> completeProject(@PathVariable("projectcode") String projectcode) {
        projectService.complete(projectcode);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseWrapper("Project is successfully completed",HttpStatus.OK));
    }



    @PutMapping("/update/{projectcode}")
    public ResponseEntity<ResponseWrapper> editProject(@PathVariable("projectcode") String projectcode) {
        projectService.update(projectService.getByProjectCode(projectcode));
        return ResponseEntity.ok(new ResponseWrapper("Project updated",HttpStatus.OK));

    }

    @PostMapping("/update")
    public String updateProject(@ModelAttribute("project") ProjectDTO project, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {

            model.addAttribute("projects", projectService.listAllProjectDetails());
            model.addAttribute("managers", userService.listAllByRole("manager"));

            return "/project/update";

        }

        projectService.update(project);
        return "redirect:/project/create";

    }


    @GetMapping("/manager/project-status")
    public String getProjectByManager(@PathVariable String projectStatus) {
        projectService.

        return "/manager/project-status";
    }

    @GetMapping("/manager/complete/{projectCode}")
    public String managerCompleteProject(@PathVariable("projectCode") String projectCode) {
        projectService.complete(projectCode);
        return "redirect:/project/manager/project-status";
    }

}
