package com.viniciusvieira.questionsanswers.api.mappers.v1;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.viniciusvieira.questionsanswers.api.representation.models.ChoiceDto;
import com.viniciusvieira.questionsanswers.domain.models.ChoiceModel;
import org.mapstruct.Mapping;

@Mapper
public abstract class ChoiceMapper {
	public static final ChoiceMapper INSTANCE = Mappers.getMapper(ChoiceMapper.class);
	
	@Mapping(target = "enabled", ignore = true)
	@Mapping(target = "idChoice", ignore = true)
	@Mapping(target = "professor", ignore = true)
	public abstract ChoiceModel toChoiceModel(ChoiceDto choiceDto);
}
