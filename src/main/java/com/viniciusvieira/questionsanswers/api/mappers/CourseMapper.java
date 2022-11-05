package com.viniciusvieira.questionsanswers.api.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.viniciusvieira.questionsanswers.api.representation.models.CourseDto;
import com.viniciusvieira.questionsanswers.domain.models.CourseModel;

@Mapper
public abstract class CourseMapper {
	public static final CourseMapper INSTANCE = Mappers.getMapper(CourseMapper.class);
	
	public abstract CourseModel toCorseModel(CourseDto courseDto);
}
