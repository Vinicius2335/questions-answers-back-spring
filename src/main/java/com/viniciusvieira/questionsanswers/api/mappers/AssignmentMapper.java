package com.viniciusvieira.questionsanswers.api.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.viniciusvieira.questionsanswers.api.representation.models.AssignmentDto;
import com.viniciusvieira.questionsanswers.domain.models.AssignmentModel;

@Mapper
public abstract class AssignmentMapper {
	public static final AssignmentMapper INSTANCE = Mappers.getMapper(AssignmentMapper.class);
	
	public abstract AssignmentModel toAssignmentModel(AssignmentDto assigmentDto); 
}
