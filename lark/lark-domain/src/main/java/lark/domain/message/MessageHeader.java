package lark.domain.message;
import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class MessageHeader<T extends Serializable> implements Serializable{
	private static final long serialVersionUID = 1L;
	@Getter @Setter private String transactionId;
	@Getter @Setter private String type;
	@Getter @Setter private Integer version;
	@Getter @Setter private Long time;
	@Getter @Setter private T body;
}
