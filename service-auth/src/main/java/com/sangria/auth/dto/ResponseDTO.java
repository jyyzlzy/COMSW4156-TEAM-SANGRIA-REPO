package com.sangria.auth.dto;
import lombok.*;

@Data
@AllArgsConstructor
public class ResponseDTO<T> {
	private int code;
	
	private String meg;
	
	private T data;
}
