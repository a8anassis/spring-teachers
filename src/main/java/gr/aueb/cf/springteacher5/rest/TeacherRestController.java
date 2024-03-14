package gr.aueb.cf.springteacher5.rest;

import gr.aueb.cf.springteacher5.dto.TeacherInsertDTO;
import gr.aueb.cf.springteacher5.dto.TeacherReadOnlyDTO;
import gr.aueb.cf.springteacher5.dto.TeacherUpdateDTO;
import gr.aueb.cf.springteacher5.mapper.Mapper;
import gr.aueb.cf.springteacher5.model.Teacher;
import gr.aueb.cf.springteacher5.service.ITeacherService;
import gr.aueb.cf.springteacher5.service.exceptions.EntityNotFoundException;
import gr.aueb.cf.springteacher5.validator.TeacherInsertValidator;
import gr.aueb.cf.springteacher5.validator.TeacherUpdateValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TeacherRestController {

    private final ITeacherService teacherService;
    private final TeacherInsertValidator insertValidator;
    private final TeacherUpdateValidator updateValidator;

//    @Autowired
//    public TeacherRestController(ITeacherService teacherService, TeacherInsertValidator insertValidator,
//                                 TeacherUpdateValidator updateValidator) {
//        this.teacherService = teacherService;
//        this.insertValidator = insertValidator;
//        this.updateValidator = updateValidator;
//    }

    @Operation(summary = "Get teachers by their lastname starting with initials")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Teachers Found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TeacherReadOnlyDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid lastname supplied",
                    content = @Content)})
    @GetMapping("/teachers")
    public ResponseEntity<List<TeacherReadOnlyDTO>> getTeachersByLastname(@RequestParam("lastname") String lastname) {
        List<Teacher> teachers;
        try {
            teachers = teacherService.getTeachersByLastname(lastname);
            List<TeacherReadOnlyDTO> readOnlyDTOS = new ArrayList<>();
            for (Teacher teacher : teachers) {
                readOnlyDTOS.add(Mapper.mapToReadOnlyDTO(teacher));
            }
            return new ResponseEntity<>(readOnlyDTOS, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            //throw e;
        }
    }

    @Operation(summary = "Get a Teacher by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Teacher Found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TeacherReadOnlyDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Teacher not found",
                    content = @Content)})
    @GetMapping("/teachers/{id}")
    public ResponseEntity<TeacherReadOnlyDTO> getTeacher(@PathVariable("id") Long id) {
        Teacher teacher;

        try {
            teacher = teacherService.getTeacherById(id);
            TeacherReadOnlyDTO dto = Mapper.mapToReadOnlyDTO(teacher);
            return ResponseEntity.ok(dto);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            //throw e;
        }
    }


    @Operation(summary = "Add a teacher")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Teacher created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TeacherReadOnlyDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input was supplied",
                    content = @Content),
            @ApiResponse(responseCode = "503", description = "Service Unavailable",
                    content = @Content)})
    @PostMapping("/teachers")
    public ResponseEntity<TeacherReadOnlyDTO> addTeacher(@Valid @RequestBody TeacherInsertDTO dto, BindingResult bindingResult) {
        insertValidator.validate(dto, bindingResult);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
             Teacher teacher = teacherService.insertTeacher(dto);
             TeacherReadOnlyDTO teacherReadOnlyDTO = Mapper.mapToReadOnlyDTO(teacher);
             URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                     .path("/{id}")
                     .buildAndExpand(teacherReadOnlyDTO.getId())
                     .toUri();

             return ResponseEntity.created(location).body(teacherReadOnlyDTO);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
            //throw e;
        }
    }

    @Operation(summary = "Update a teacher")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Teacher updated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TeacherReadOnlyDTO.class)) }),
            @ApiResponse(responseCode = "401", description = "Unauthorized user",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input was supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Teacher not found",
                    content = @Content) })
    @PutMapping("/teachers/{id}")
    public ResponseEntity<TeacherReadOnlyDTO> updateTeacher(@PathVariable("id") Long id, @Valid @RequestBody TeacherUpdateDTO dto,
                                                            BindingResult bindingResult) {
        if (!Objects.equals(id, dto.getId())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        updateValidator.validate(dto, bindingResult);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            Teacher teacher = teacherService.updateTeacher(dto);
            TeacherReadOnlyDTO readOnlyDTO = Mapper.mapToReadOnlyDTO(teacher);
            return new ResponseEntity<>(readOnlyDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Delete a Teacher by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Teacher Deleted",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TeacherReadOnlyDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Teacher not found",
                    content = @Content)})
    @DeleteMapping("/teachers/{id}")
    public ResponseEntity<TeacherReadOnlyDTO> deleteTeacher(@PathVariable("id") Long id) {
        try {
            Teacher teacher = teacherService.deleteTeacher(id);
            TeacherReadOnlyDTO readOnlyDTO = Mapper.mapToReadOnlyDTO(teacher);
            return ResponseEntity.ok(readOnlyDTO);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
