package com.viniciusvieira.questionsanswers.api.mappers.v1;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.viniciusvieira.questionsanswers.api.representation.models.AssignmentDto;
import com.viniciusvieira.questionsanswers.domain.models.AssignmentModel;
import org.mapstruct.Mapping;

@Mapper
public abstract class AssignmentMapper {
	public static final AssignmentMapper INSTANCE = Mappers.getMapper(AssignmentMapper.class);
	
	@Mapping(target = "accessCode", ignore = true)
	@Mapping(target = "enabled", ignore = true)
	@Mapping(target = "idAssignment", ignore = true)
	@Mapping(target = "professor", ignore = true)
	public abstract AssignmentModel toAssignmentModel(AssignmentDto assigmentDto); 
}
