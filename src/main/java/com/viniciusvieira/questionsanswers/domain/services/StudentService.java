package com.viniciusvieira.questionsanswers.domain.services;

import com.viniciusvieira.questionsanswers.api.mappers.v2.StudentMapper;
import com.viniciusvieira.questionsanswers.api.representation.models.StudentDto;
import com.viniciusvieira.questionsanswers.api.representation.requests.StudentRequestBody;
import com.viniciusvieira.questionsanswers.domain.exception.StudentNotFoundException;
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

    public StudentDto findByIdOrThrowStudentNotFoundException(Long idStudent) {
        StudentModel studentFound = studentRepository.findById(idStudent)
                .orElseThrow(() -> new StudentNotFoundException("Student Not Found"));

        return studentMapper.toStudentDto(studentFound);
    }

    public List<StudentDto> findByName(String name){
        List<StudentModel> students = studentRepository.findByNameContaining(name);
        return studentMapper.toListOfStudentDto(students);
    }

    @Transactional
    public StudentDto save(StudentRequestBody studentRequestBody) {
        StudentModel student = studentMapper.toStudentDomain(studentRequestBody);
        student.setEnabled(true);

        StudentModel studentSaved = studentRepository.save(student);
        return studentMapper.toStudentDto(studentSaved);
    }

    @Transactional
    public StudentDto replace(Long idStudent, StudentRequestBody studentRequestBody) {
        StudentModel student = studentMapper.toStudentDomain(studentRequestBody);
        StudentDto studentFound = findByIdOrThrowStudentNotFoundException(idStudent);

        studentFound.setName(student.getName());
        studentFound.setEmail(student.getEmail());
        StudentModel studentReplaced = studentRepository.save(studentMapper.toStudentDomain(studentFound));

        return studentMapper.toStudentDto(studentReplaced);
    }

    @Transactional
    public void delete(Long idStudent) {
        StudentDto studentFound = findByIdOrThrowStudentNotFoundException(idStudent);

        studentRepository.deleteById(studentFound.getIdStudent());
    }

}
