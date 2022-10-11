package com.viniciusvieira.questionsanswers.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.viniciusvieira.questionsanswers.dtos.CourseDto;
import com.viniciusvieira.questionsanswers.models.CourseModel;

@Mapper
public abstract class CourseMapper {
	public static final CourseMapper INSTANCE = Mappers.getMapper(CourseMapper.class);
	
	public abstract CourseModel toCorseModel(CourseDto courseDto);
}
