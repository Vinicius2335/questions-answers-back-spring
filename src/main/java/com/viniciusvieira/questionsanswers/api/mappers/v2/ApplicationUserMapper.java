package com.viniciusvieira.questionsanswers.api.mappers.v2;

import com.viniciusvieira.questionsanswers.api.representation.models.ApplicationUserDto;
import com.viniciusvieira.questionsanswers.domain.models.ApplicationUserModel;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApplicationUserMapper {
    private final ModelMapper modelMapper;

    public ApplicationUserDto toApplicationUserDto(ApplicationUserModel applicationUserModel){
        return modelMapper.map(applicationUserModel, ApplicationUserDto.class);
    }
}
