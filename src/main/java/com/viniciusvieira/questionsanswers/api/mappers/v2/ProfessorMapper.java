package com.viniciusvieira.questionsanswers.api.mappers.v2;

import com.viniciusvieira.questionsanswers.api.representation.models.ProfessorDto;
import com.viniciusvieira.questionsanswers.api.representation.requests.ProfessorRequestBody;
import com.viniciusvieira.questionsanswers.domain.models.ProfessorModel;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProfessorMapper {
    private final ModelMapper modelMapper;

    public ProfessorDto toProfessorDto (ProfessorModel professorModel){
        return modelMapper.map(professorModel, ProfessorDto.class);
    }

    public List<ProfessorDto> toProfessorDtoList(List<ProfessorModel> professors){
        return professors.stream().map(this::toProfessorDto).toList();
    }

    public ProfessorModel toProfessorDomain(ProfessorRequestBody professorRequestBody){
        return modelMapper.map(professorRequestBody, ProfessorModel.class);
    }

    public ProfessorModel toProfessorDomain(ProfessorDto professorDto){
        return modelMapper.map(professorDto, ProfessorModel.class);
    }
}
