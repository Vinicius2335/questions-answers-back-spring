package com.viniciusvieira.questionsanswers.domain.services;

import com.viniciusvieira.questionsanswers.api.mappers.v2.ApplicationUserMapper;
import com.viniciusvieira.questionsanswers.api.mappers.v2.StudentMapper;
import com.viniciusvieira.questionsanswers.api.representation.models.ApplicationUserDto;
import com.viniciusvieira.questionsanswers.api.representation.models.StudentDto;
import com.viniciusvieira.questionsanswers.api.representation.requests.ApplicationUserRequestBody;
import com.viniciusvieira.questionsanswers.api.representation.requests.StudentRequestBody;
import com.viniciusvieira.questionsanswers.domain.exception.StudentNotFoundException;
import com.viniciusvieira.questionsanswers.domain.models.ApplicationUserModel;
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
    private final StudentMapper studentMapper;
    private final ApplicationUserMapper applicationUserMapper;
    private final InsertUserService insertUserService;

    private StudentModel findByIdOrThrowStudentNotFoundException(Long idStudent) {
        return studentRepository.findById(idStudent)
                .orElseThrow(() -> new StudentNotFoundException("Student Not Found"));
    }

    public List<StudentDto> findByName(String name){
        List<StudentModel> students = studentRepository.findByNameContaining(name);
        return studentMapper.toListOfStudentDto(students);
    }

    @Transactional
    public StudentDto saveStudent(StudentRequestBody studentRequestBody) {
        StudentModel student = studentMapper.toStudentDomain(studentRequestBody);
        student.setEnabled(true);

        StudentModel studentSaved = studentRepository.save(student);
        return studentMapper.toStudentDto(studentSaved);
    }

    @Transactional
    public ApplicationUserDto saveApplicationUserStudent(Long idStudent, ApplicationUserRequestBody
            applicationUserRequestBody) {
        StudentModel studentFound = findByIdOrThrowStudentNotFoundException(idStudent);

        ApplicationUserModel applicationUserSaved = insertUserService.insertStudent(studentFound,
                applicationUserRequestBody);

        return applicationUserMapper.toApplicationUserDto(applicationUserSaved);
    }

    @Transactional
    public StudentDto replace(Long idStudent, StudentRequestBody studentRequestBody) {
        StudentModel student = studentMapper.toStudentDomain(studentRequestBody);
        StudentModel studentFound = findByIdOrThrowStudentNotFoundException(idStudent);

        studentFound.setName(student.getName());
        studentFound.setEmail(student.getEmail());
        StudentModel studentReplaced = studentRepository.save(studentFound);

        return studentMapper.toStudentDto(studentReplaced);
    }

    @Transactional
    public void delete(Long idStudent) {
        StudentModel studentFound = findByIdOrThrowStudentNotFoundException(idStudent);

        studentRepository.deleteById(studentFound.getIdStudent());
    }

}

//TEST