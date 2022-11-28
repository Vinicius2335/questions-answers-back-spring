package com.viniciusvieira.questionsanswers.api.mappers.v2;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StudentMapper {
    private final ModelMapper modelMapper;
}
