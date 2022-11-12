package com.viniciusvieira.questionsanswers.api.exceptionhandler;

import java.time.OffsetDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonInclude(Include.NON_NULL)
@Schema(name = "ExceptionDetails")
public class ExceptionDetails {
	
	@Schema(example = "Exception Name, check documentation")
	private String title;
	
	@Schema(example = "404")
	private int status;
	
	@Schema(example = "2022-07-15T11:21:50.902245498Z")
	private OffsetDateTime timestamp;
	
	@Schema(example = "List of fields that generated error")
	private List<Field> fields;
    
    @Getter
    @AllArgsConstructor
    @Schema(name = "FieldError")
    public static class Field {
    	@Schema(example = "Name")
    	private String name;
    	
    	@Schema(example = "Field Name cannot be null")
    	private String message;
    }
}
