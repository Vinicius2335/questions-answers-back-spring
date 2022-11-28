package com.viniciusvieira.questionsanswers.api.mappers.v1;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.viniciusvieira.questionsanswers.api.representation.models.CourseDto;
import com.viniciusvieira.questionsanswers.domain.models.CourseModel;
import org.mapstruct.Mapping;

@Mapper
public abstract class CourseMapper {
	public static final CourseMapper INSTANCE = Mappers.getMapper(CourseMapper.class);
	
	@Mapping(target = "enabled", ignore = true)
	@Mapping(target = "idCourse", ignore = true)
	@Mapping(target = "professor", ignore = true)
	public abstract CourseModel toCorseModel(CourseDto courseDto);
}
