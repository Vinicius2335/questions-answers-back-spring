package com.viniciusvieira.questionsanswers.api.exceptionhandler;

import java.time.OffsetDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionDetails {
	protected String title;
    protected int status;
    protected OffsetDateTime timestamp;
    protected List<Field> fields;
    
    @Getter
    @AllArgsConstructor
    public static class Field {
    	protected String name;
    	protected String message;
    }
}
