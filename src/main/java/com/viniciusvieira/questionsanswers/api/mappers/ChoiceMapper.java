package com.viniciusvieira.questionsanswers.api.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.viniciusvieira.questionsanswers.api.representation.models.ChoiceDto;
import com.viniciusvieira.questionsanswers.domain.models.ChoiceModel;

@Mapper
public abstract class ChoiceMapper {
	public static final ChoiceMapper INSTANCE = Mappers.getMapper(ChoiceMapper.class);
	
	public abstract ChoiceModel toChoiceModel(ChoiceDto choiceDto);
}
