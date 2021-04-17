package zup.postblog.springbootapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ConstraintException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	
	public ConstraintException(String msg) {
		super(msg);
	}
	
	public ConstraintException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
