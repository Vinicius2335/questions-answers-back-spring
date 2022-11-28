package com.viniciusvieira.questionsanswers.domain.services;

import com.viniciusvieira.questionsanswers.domain.models.AdminModel;
import com.viniciusvieira.questionsanswers.domain.models.StudentModel;
import com.viniciusvieira.questionsanswers.domain.repositories.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final ExtractEntityFromTokenService extractEntityFromTokenService;
/**
    public StudentModel findByIdOrThrowStudentNotFoundException(Long idStudent) {
        Long idAdmin = extractEntityFromTokenService.extractAdminFromToken().getIdAdmin();

        return studentRepository.findOneStudent(idStudent, idAdmin)
                .orElseThrow(() -> new StudentNotFoundException("Student Not Found"));
    }

    public List<StudentModel> findByName(String name){
        Long idAdmin = extractEntityFromTokenService.extractAdminFromToken().getIdAdmin();
        return studentRepository.listStudentsByName(name, idAdmin);
    }

    @Transactional
    public StudentModel save(StudentDto studentDto) {
        AdminModel admin = extractEntityFromTokenService.extractAdminFromToken();
        // dto name/email do student
    }

    @Transactional
    public void replace(Long idStudent, StudentDto studentDto) {
        //CourseModel course = CourseMapper.INSTANCE.toCorseModel(courseDto);
        StudentModel studentFound = findByIdOrThrowStudentNotFoundException(idStudent);

        // dto name/email do student
        studentRepository.save(studentFound);
    }

    @Transactional
    public void delete(Long idStudent) {
        StudentModel studentFound = findByIdOrThrowStudentNotFoundException(idStudent);
        Long idAdmin = extractEntityFromTokenService.extractAdminFromToken().getIdAdmin();

        studentRepository.deleteById(studentFound.getIdStudent(), idAdmin);
    }
    **/
}
