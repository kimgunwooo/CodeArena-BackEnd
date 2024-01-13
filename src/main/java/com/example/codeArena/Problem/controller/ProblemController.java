package com.example.codeArena.Problem.controller;

import com.example.codeArena.Problem.dto.ProblemCreateDto;
import com.example.codeArena.Problem.dto.ProblemDto;
import com.example.codeArena.Problem.dto.ProblemUpdateDto;
import com.example.codeArena.Problem.service.ProblemService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/problems")
public class ProblemController {

    private static final Logger logger = LoggerFactory.getLogger(ProblemController.class);


    private final ProblemService problemService;

    public ProblemController(ProblemService problemService) {
        this.problemService = problemService;
    }

    // 문제 생성
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ProblemDto> createProblem(@RequestBody ProblemCreateDto createDto) {
        try {
            ProblemDto createdProblem = problemService.createProblem(createDto);
            return ResponseEntity.ok(createdProblem);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "문제 생성 중 오류 발생: " + e.getMessage());
        }
    }


    // 모든 문제 조회
    @GetMapping
    public ResponseEntity<List<ProblemDto>> getAllProblems() {
        List<ProblemDto> problems = problemService.getAllProblems();
        return ResponseEntity.ok(problems);
    }

    // 특정 문제 조회
    @GetMapping("/{id}")
    public ResponseEntity<ProblemDto> getProblemById(@PathVariable Long id) {
        return problemService.getProblemById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 문제 수정
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ProblemDto> updateProblem(@PathVariable Long id, @Valid @RequestBody ProblemUpdateDto updateDto) {
        try {
            return problemService.updateProblem(id, updateDto)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            logger.error("문제 수정 중 예외 발생", e); // 예외 로깅
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "문제 수정 중 오류 발생: " + e.getMessage());
        }
    }

    // 문제 삭제
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProblem(@PathVariable Long id) {
        try {
            problemService.deleteProblem(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "문제 삭제 중 오류 발생: " + e.getMessage());
        }
    }
}