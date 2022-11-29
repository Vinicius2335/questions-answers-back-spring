package com.viniciusvieira.questionsanswers.api.mappers.v2;

import com.viniciusvieira.questionsanswers.api.representation.models.StudentDto;
import com.viniciusvieira.questionsanswers.api.representation.requests.StudentRequestBody;
import com.viniciusvieira.questionsanswers.domain.models.StudentModel;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StudentMapper {
    private final ModelMapper modelMapper;

    public StudentDto toStudentDto (StudentModel studentModel){
        return modelMapper.map(studentModel, StudentDto.class);
    }

    public List<StudentDto> toListOfStudentDto (List<StudentModel> students){
        return students.stream().map(this::toStudentDto).toList();
    }

    public StudentModel toStudentDomain (StudentRequestBody studentRequestBody){
        return modelMapper.map(studentRequestBody, StudentModel.class);
    }
    public StudentModel toStudentDomain (StudentDto studentDto){
        return modelMapper.map(studentDto, StudentModel.class);
    }
}
